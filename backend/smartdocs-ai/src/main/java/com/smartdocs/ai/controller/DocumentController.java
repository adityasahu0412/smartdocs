package com.smartdocs.ai.controller;

import com.smartdocs.ai.entity.Document;
import com.smartdocs.ai.service.DocumentService;
import com.smartdocs.ai.service.GeminiService;
import com.smartdocs.ai.util.EmbeddingUtil;
import com.smartdocs.ai.util.PdfUtil;
import com.smartdocs.ai.util.TextChunker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// handles PDF upload and question answering

@RestController
@RequestMapping("/api/docs")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    // storing chunks and embeddings in memory for now
    // these get replaced every time a new file is uploaded
    private List<String> storedChunks = new ArrayList<>();
    private List<Map<String, Integer>> storedEmbeddings = new ArrayList<>();

    @Autowired
    private DocumentService documentService;

    @Autowired
    private GeminiService geminiService;

    // upload PDF, extract text, create embeddings and store in memory
    @PostMapping("/upload")
    public Document upload(@RequestParam("file") MultipartFile file) throws IOException {

        // create uploads folder if it doesn't exist
        String uploadDir = System.getProperty("user.dir") + "\\uploads\\";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // save the file to disk
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + fileName;
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());
        System.out.println("File saved at: " + filePath);

        // extract text from PDF using PDFBox
        String text = PdfUtil.extractText(filePath);
        System.out.println("Extracted text preview: " + text.substring(0, Math.min(500, text.length())));

        // split text into chunks of 500 characters
        List<String> chunks = TextChunker.chunkText(text, 500);
        System.out.println("Total chunks created: " + chunks.size());

        // print first 3 chunks for debugging
        for (int i = 0; i < Math.min(3, chunks.size()); i++) {
            System.out.println("Chunk " + i + ": " + chunks.get(i));
        }

        // create word frequency embeddings for each chunk
        List<Map<String, Integer>> embeddings = new ArrayList<>();
        for (String chunk : chunks) {
            embeddings.add(EmbeddingUtil.getWordFrequency(chunk));
        }
        System.out.println("Embeddings created: " + embeddings.size());

        // clear old data and store new chunks and embeddings
        storedChunks.clear();
        storedEmbeddings.clear();
        storedChunks.addAll(chunks);
        storedEmbeddings.addAll(embeddings);

        // save file info to database
        Document doc = new Document();
        doc.setFileName(fileName);
        doc.setFilePath(filePath);
        return documentService.saveDocument(doc);
    }

    // find relevant chunks using cosine similarity and ask Gemini
    @PostMapping("/ask")
    public String askQuestion(@RequestParam("question") String question) {

        // check if any document is uploaded
        if (storedChunks.isEmpty() || storedEmbeddings.isEmpty()) {
            return "Please upload a document first!";
        }

        // convert question to word frequency vector
        Map<String, Integer> queryVector = EmbeddingUtil.getWordFrequency(question);

        // find chunks with cosine similarity score above threshold
        List<String> topChunks = new ArrayList<>();
        for (int i = 0; i < storedEmbeddings.size(); i++) {
            double score = EmbeddingUtil.cosineSimilarity(queryVector, storedEmbeddings.get(i));
            if (score > 0.1) {
                topChunks.add(storedChunks.get(i));
            }
        }

        if (topChunks.isEmpty()) {
            return "No relevant information found in the document for this question.";
        }

        // take top 3 most relevant chunks as context
        String context = String.join("\n", topChunks.stream().limit(3).toList());

        // send question + context to Gemini and return answer
        String aiResponse = geminiService.generateAnswer(question, context);
        System.out.println("Gemini response received for question: " + question);

        return aiResponse;
    }
}
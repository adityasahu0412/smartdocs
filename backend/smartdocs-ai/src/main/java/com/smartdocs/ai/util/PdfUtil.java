package com.smartdocs.ai.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

// utility class to extract text from PDF files using Apache PDFBox

public class PdfUtil {

    public static String extractText(String filePath) throws IOException {

        File file = new File(filePath);

        // load the PDF file
        PDDocument document = PDDocument.load(file);

        // PDFTextStripper extracts all text from the PDF
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);

        // always close document to avoid memory leak
        document.close();

        return text;
    }
}
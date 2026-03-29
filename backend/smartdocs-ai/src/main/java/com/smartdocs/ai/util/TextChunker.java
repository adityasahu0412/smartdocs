package com.smartdocs.ai.util;

import java.util.ArrayList;
import java.util.List;

// splits large text into smaller chunks
// needed because we can't send the entire document to Gemini at once

public class TextChunker {

    // splits text into chunks of given size (in characters)
    // example: chunkText("hello world", 5) -> ["hello", " worl", "d"]
    public static List<String> chunkText(String text, int chunkSize) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {

            // take chunkSize characters or remaining text if less than chunkSize
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));

            start = end;
        }

        return chunks;
    }
}
package com.smartdocs.ai.util;

import java.util.HashMap;
import java.util.Map;

// utility class for creating word frequency vectors and calculating cosine similarity
// this is used to find which chunks of the document are most relevant to the question

public class EmbeddingUtil {

    // converts text into a word frequency map
    // example: "hello world hello" -> {hello=2, world=1}
    public static Map<String, Integer> getWordFrequency(String text) {

        Map<String, Integer> freq = new HashMap<>();

        // split on non-word characters (spaces, punctuation etc.)
        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            if (word.isEmpty()) continue;
            freq.put(word, freq.getOrDefault(word, 0) + 1);
        }

        return freq;
    }

    // calculates cosine similarity between two word frequency vectors
    // returns a value between 0 and 1 - higher means more similar
    // added 1e-10 to avoid division by zero
    public static double cosineSimilarity(Map<String, Integer> a, Map<String, Integer> b) {

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        // calculate dot product - only for words that appear in both maps
        for (String key : a.keySet()) {
            if (b.containsKey(key)) {
                dot += a.get(key) * b.get(key);
            }
        }

        // calculate magnitude of vector a
        for (int val : a.values()) {
            normA += val * val;
        }

        // calculate magnitude of vector b
        for (int val : b.values()) {
            normB += val * val;
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }
}
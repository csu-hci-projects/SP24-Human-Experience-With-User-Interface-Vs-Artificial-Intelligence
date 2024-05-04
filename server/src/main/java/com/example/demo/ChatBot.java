package com.example.demo;

import java.io.*;
import java.util.*;
import org.apache.commons.text.similarity.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ChatBot {
    public static boolean learn = false; //testing/beta feature in order to teach the chatbot while running the program
    public static String lastAnswer;

    private static final String KNOWLEDGE_BASE_FILE_PATH = "src/main/java/com/example/demo/Knowledge_Base.json";
    private static final double SIMILARITY_THRESHOLD = 0.6;
    private static final int MAX_RESULTS = 5;

    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadKnowledgeBase() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader(KNOWLEDGE_BASE_FILE_PATH)) {
            Object obj = parser.parse(fileReader);
            return (JSONObject) obj;
        }
    }

    public static void saveKnowledgeBase(String newQuestion, String newAnswer) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(KNOWLEDGE_BASE_FILE_PATH);
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray questions = (JSONArray) jsonObject.get("questions");

            JSONObject newQuestionObj = new JSONObject();
            newQuestionObj.put("question", newQuestion);
            newQuestionObj.put("answer", newAnswer);

            questions.add(newQuestionObj);

            FileWriter writer = new FileWriter(KNOWLEDGE_BASE_FILE_PATH);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();

            System.out.println("New question added successfully!");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getChatbotResponse(String userInput) throws IOException, ParseException {
        Map<String, Object> knowledgeBase = loadKnowledgeBase();

        if (learn) { //set below, training the chatbot
            saveKnowledgeBase(lastAnswer, userInput);
            learn = false;
            return "Thank you!";
        }

        lastAnswer = userInput;

        String normalizedInput = userInput.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");

        List<String> questionsList = getQuestionsList(knowledgeBase);

        Map<String, String> preprocessedQuestions = preprocessQuestions(questionsList); //also normalizing

        Map<String, Double> similarityScores = new HashMap<>();
        for (Map.Entry<String, String> entry : preprocessedQuestions.entrySet()) {
            double similarity = calculateSimilarity(normalizedInput, entry.getValue());
            if (similarity >= SIMILARITY_THRESHOLD) {  // <-- Creating a list of all similary thresholds for the questions
                similarityScores.put(entry.getKey(), similarity);
            }
        }

        List<Map.Entry<String, Double>> sortedSimilarities = new ArrayList<>(similarityScores.entrySet());
        sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<String> bestMatches = new ArrayList<>(); //getting the answers for the best matches
        for (int i = 0; i < Math.min(MAX_RESULTS, sortedSimilarities.size()); i++) {
            bestMatches.add(sortedSimilarities.get(i).getKey());
        }

        if (!bestMatches.isEmpty()) { //returning that answer
            return getAnswerForQuestion(bestMatches.get(0), knowledgeBase);
        } else {
            //learn = true; <-- this is where learn would be set
            //return "I don't know the answer. What do you mean to learn from it?"; <-- old response
            return "I'm not too sure what you said. Please try rephrasing it!";
        }
    }

    private static Map<String, String> preprocessQuestions(List<String> questions) {
        Map<String, String> preprocessedQuestions = new HashMap<>();
        for (String question : questions) {
            preprocessedQuestions.put(question, question.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", ""));
        }
        return preprocessedQuestions;
    }

    private static double calculateSimilarity(String s1, String s2) {
        Map<CharSequence, Integer> freq1 = getWordFrequencies(s1); //finding frequencies of words in each
        Map<CharSequence, Integer> freq2 = getWordFrequencies(s2);
    
        CosineSimilarity cosineSimilarity = new CosineSimilarity(); //getting cosine similarity
        return cosineSimilarity.cosineSimilarity(freq1, freq2);
    }
    
    private static Map<CharSequence, Integer> getWordFrequencies(String s) {
        Map<CharSequence, Integer> frequencies = new HashMap<>();
        String[] words = s.split(" ");
        for (String word : words) {
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }
        return frequencies;
    }
    

    public static String getAnswerForQuestion(String question, Map<String, Object> knowledgeBase) {
        JSONArray questionsArray = (JSONArray) knowledgeBase.get("questions");
        for (Object obj : questionsArray) {
            JSONObject q = (JSONObject) obj;
            String qStr = (String) q.get("question");
            if (qStr.equals(question)) {
                return (String) q.get("answer");
            }
        }
        return null;
    }

    public static List<String> getQuestionsList(Map<String, Object> knowledgeBase) {
        List<String> questionsList = new ArrayList<>();
        JSONArray questionsArray = (JSONArray) knowledgeBase.get("questions");
        for (Object obj : questionsArray) {
            JSONObject q = (JSONObject) obj;
            String qStr = (String) q.get("question");
            questionsList.add(qStr);
        }
        return questionsList;
    }
}

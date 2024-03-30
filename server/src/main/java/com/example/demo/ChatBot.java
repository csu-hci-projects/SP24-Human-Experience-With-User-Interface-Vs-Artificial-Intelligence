package com.example.demo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChatBot {

    public static boolean learn = false;
    public static String lastAnswer;

    private static final String KNOWLEDGE_BASE_FILE_PATH = "src/main/java/com/example/demo/Knowledge_Base.json";

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
            // Read existing JSON file
            System.out.println("Before: " + newQuestion);
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(KNOWLEDGE_BASE_FILE_PATH);
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            // Get the "questions" array from the JSON object
            JSONArray questions = (JSONArray) jsonObject.get("questions");

            // Create a new JSON object for the new question
            JSONObject newQuestionObj = new JSONObject();
            newQuestionObj.put("question", newQuestion);
            newQuestionObj.put("answer", newAnswer);

            // Add the new question object to the "questions" array
            questions.add(newQuestionObj);

            // Write the updated JSON object back to the file
            FileWriter writer = new FileWriter(KNOWLEDGE_BASE_FILE_PATH);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();

            System.out.println("New question added successfully!");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    

    public static String findBestMatch(String userQuestion, List<String> questions) {
        return getClosestMatch(userQuestion, questions);
    }

    public static String getChatbotResponse(String userInput) throws IOException, ParseException {

        Map<String, Object> knowledgeBase = loadKnowledgeBase();

        if (learn) {
            saveKnowledgeBase(lastAnswer, userInput);
            learn = false;
            return "Thank you!";
        }

        lastAnswer = userInput;

            String bestMatch = findBestMatch(userInput, getQuestionsList(knowledgeBase));
            if (bestMatch != null) {
                learn = false;
                return getAnswerForQuestion(bestMatch, knowledgeBase);
            } else {
                // Ask the user what they mean to learn from the question
                /*
                JSONObject newQuestion = new JSONObject();
                newQuestion.put("question", userInput);
                newQuestion.put("answer", newAnswer);
                JSONArray questionsArray = (JSONArray) knowledgeBase.get("questions");
                questionsArray.add(newQuestion);
                saveKnowledgeBase("Knowledge_Base.json", knowledgeBase);
                */
                learn = true;
                return "I don't know the answer. What do you mean to learn from it?";
            }
    }

    public static String getClosestMatch(String userQuestion, List<String> questions) {
        double similarityThreshold = 0.7;
        List<String> matches = new ArrayList<>();
        for (String question : questions) {
            double similarity = similarity(userQuestion, question);
            if (similarity >= similarityThreshold) {
                matches.add(question);
            }
        }
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        return null;
    }

    public static double similarity(String s1, String s2) {
        String longer = s1.toLowerCase();
        String shorter = s2.toLowerCase();
        int longerLength = longer.length();
        int shorterLength = shorter.length();
        if (longerLength < shorterLength) {
            String temp = longer;
            longer = shorter;
            shorter = temp;
        }
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
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

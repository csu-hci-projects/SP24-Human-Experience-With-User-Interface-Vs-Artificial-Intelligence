package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ApiController {

    MyRequestData data = new MyRequestData();
    public int callNum = 32;

    /*
    @PostMapping("/api")
    public ResponseEntity<String> handlePostRequest(@RequestBody MyRequestData requestData) {

        Caller caller = new Caller();
        Calculations calculation = new Calculations(requestData.age, requestData.height, requestData.weight, requestData.desiredWeight, requestData.sex, requestData.activity, requestData.body);
        String data = caller.useData(calculation);

        return ResponseEntity.ok(data);
    }
    */

    @CrossOrigin(origins = "http://localhost:3001")
    @PostMapping("/chatbot")
    public ResponseEntity<String> handleChatbotRequest(@RequestBody String userInput) throws IOException, ParseException {
        String input = userInput.replace("+", " ");

        System.out.println("RECEIVED: " + input);

        if (input.toLowerCase().contains("input mode")) {
            callNum = 1;
            return ResponseEntity.ok("Input mode entered! Let's get started generated you a meal plan! To start off, what is your age?");
        }

        if (callNum == 1) {
            data.age = extractNum(input);
            if (data.age < 1 || data.age > 100) {
                return ResponseEntity.ok("I'm sorry, that's not a valid age. Please try again.");
            }
            //System.out.println("I GOT: " + data.age);
            callNum++;
            return ResponseEntity.ok("Thank you! Next, I'll need your height. Please input in inches.");
        } else if (callNum == 2) {
            data.height = extractNum(input);
            if (data.height < 20 || data.height > 100) {
                return ResponseEntity.ok("I'm sorry, that's not a valid height. Please try again.");
            }
            //System.out.println("I GOT: " + data.age);
            callNum++;
            return ResponseEntity.ok("Perfect! What's your current weight?");
        } else if (callNum == 3) {
            data.weight = extractNum(input);
            if (data.weight < 60 || data.weight > 300) {
                return ResponseEntity.ok("I'm sorry, that's not a valid weight. Please try again.");
            }
            //System.out.println("I GOT: " + data.age);
            callNum++;
            return ResponseEntity.ok("Alright, and what is your goal weight?");
        } else if (callNum == 4) {
            data.desiredWeight = extractNum(input);
            if (data.desiredWeight < 60 || data.desiredWeight > 300) {
                return ResponseEntity.ok("I'm sorry, that's not a valid desired weight. Please try again.");
            }
            //System.out.println("I GOT: " + data.age);
            callNum++;
            return ResponseEntity.ok("Just a few more questions! What is your sex?");
        } else if (callNum == 5) {
            data.sex = extractSex(input);
            if (data.sex != "Male" && data.sex != "Female") {
                return ResponseEntity.ok("I'm sorry, I didn't get that. Are you male or female?");
            }
            //System.out.println("I GOT: " + data.sex);
            callNum++;
            return ResponseEntity.ok("Pefect. How many days per week do you exercise?");
        } else if (callNum == 6) {
            data.activity = extractActiveDays(input);
            if (data.activity != "1-2" && data.activity != "3-4" && data.activity != "5-7") {
                return ResponseEntity.ok("Apologies, I couldn't determine your answer. Please try again.");
            }
            System.out.println("I GOT: " + data.activity);
            callNum++;
            return ResponseEntity.ok("Final question! Body type");
        } else {
            String response = ChatBot.getChatbotResponse(input);
            System.out.println("Input: " + input);
            System.out.println("Output: " + response);
            return ResponseEntity.ok(response);
        }

    }

    public int extractNum(String input) {

        Pattern pattern = Pattern.compile("\\d+");

        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }

    public String extractSex(String input) {
        // Define regular expressions for male and female
        Pattern malePattern = Pattern.compile("\\b(?:male|man|guy|dude)\\b", Pattern.CASE_INSENSITIVE);
        Pattern femalePattern = Pattern.compile("\\b(?:female|woman|lady|girl)\\b", Pattern.CASE_INSENSITIVE);
    
        Matcher maleMatcher = malePattern.matcher(input);
        Matcher femaleMatcher = femalePattern.matcher(input);
    
        if (maleMatcher.find()) {
            return "Male";
        } else if (femaleMatcher.find()) {
            return "Female";
        }
    
        return "NOT VALID";
    }

    public static String extractActiveDays(String input) {
        // Define regular expressions to match numbers in various formats
        Pattern numberPattern = Pattern.compile("\\b(?:one|two|three|four|five|six|seven|twice|\\d+)\\b", Pattern.CASE_INSENSITIVE);

        Matcher numberMatcher = numberPattern.matcher(input);

        int minDays = -1;
        int maxDays = -1;

        while (numberMatcher.find()) {
            String numberString = numberMatcher.group().toLowerCase(); // Convert to lowercase for case insensitivity
            int days = convertToNumber(numberString);
            if (days > 0 && days <= 7) {
                if (minDays == -1 || days < minDays) {
                    minDays = days;
                }
                if (maxDays == -1 || days > maxDays) {
                    maxDays = days;
                }
            }
        }

        if (minDays != -1 && maxDays != -1) {
            if (minDays <= 2 && maxDays <= 2) {
                return "1-2";
            } else if (minDays <= 4 && maxDays <= 4) {
                return "3-4";
            } else {
                return "5-7";
            }
        }

        return "Unknown"; // Or handle this case differently based on your requirements
    }

    public static int convertToNumber(String numberString) {
        switch (numberString) {
            case "one":
                return 1;
            case "once":
                return 1;
            case "two":
                return 2;
            case "twice":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            default:
                try {
                    return Integer.parseInt(numberString);
                } catch (NumberFormatException e) {
                    return -1; // Invalid number format
                }
            }
        }
    
}
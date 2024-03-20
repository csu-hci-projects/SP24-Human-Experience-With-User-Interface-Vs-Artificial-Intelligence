package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import com.ChatbotRequest;
import com.example.DialogflowService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ApiController {

    @PostMapping("/api")
    public ResponseEntity<String> handlePostRequest(@RequestBody MyRequestData requestData) {
      
        Caller caller = new Caller();
        Calculations calculation = new Calculations(requestData.age, requestData.height, requestData.weight, requestData.desiredWeight, requestData.sex, requestData.activity, requestData.body);
        String data = caller.useData(calculation);

        return ResponseEntity.ok(data);
    }

    @PostMapping("/chatbot")
    public ResponseEntity<String> handleChatbotRequest(@RequestBody ChatbotRequest chatbotRequest) {
        String chatbotResponse = DialogflowService.sendMessageToDialogflow(chatbotRequest.getMessage());
        return ResponseEntity.ok(chatbotResponse);
    }
    
}
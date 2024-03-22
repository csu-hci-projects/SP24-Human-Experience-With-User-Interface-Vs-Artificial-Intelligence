package com.example;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class DialogflowService {

    private static SessionsClient sessionsClient;
    private static SessionName sessionName;

    @PostConstruct
    public void init() {
        try {
            String projectId = "mealplanner-464";
            FileInputStream keyFileInputStream = new FileInputStream("../API/src/main/resources/keyfile.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(keyFileInputStream);
            SessionsSettings sessionsSettings = SessionsSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, "unique-session-id");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendMessageToDialogflow(String message) {
        if (sessionsClient == null || sessionName == null) {
            return "Error: SessionsClient or SessionName not initialized.";
        }
        
        TextInput.Builder textInput = TextInput.newBuilder().setText(message).setLanguageCode("en-US");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        try {
            DetectIntentResponse response = sessionsClient.detectIntent(sessionName, queryInput);
            QueryResult queryResult = response.getQueryResult();
            return queryResult.getFulfillmentText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to send message to Dialogflow.";
        }
    }
}

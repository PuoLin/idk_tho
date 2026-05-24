package com.example.umpsa;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class CommunityActivity extends Activity {

    LinearLayout cardMoodCheck;
    TextView txtMoodEmoji;
    EditText inputAIQuestion;
    Button btnSendAI, btnSpeakAI;
    TextView txtAIResponse;
    TextView txtMoodReport;

    private String selectedMood = "";
    private static final int REQUEST_CODE_SPEECH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        NavigationHelper.setup(this, "community");

        // Mood check
        cardMoodCheck = findViewById(R.id.cardMoodCheck);
        txtMoodEmoji = findViewById(R.id.txtMoodEmoji);
        cardMoodCheck.setOnClickListener(v -> showMoodDialog());

        // AI chat
        inputAIQuestion = findViewById(R.id.inputAIQuestion);
        btnSendAI = findViewById(R.id.btnSendAI);
        btnSpeakAI = findViewById(R.id.btnSpeakAI);
        txtAIResponse = findViewById(R.id.txtAIResponse);

        btnSendAI.setOnClickListener(v -> sendAIQuestion(inputAIQuestion.getText().toString()));
        btnSpeakAI.setOnClickListener(v -> startVoiceInput());

        // Mood report
        txtMoodReport = findViewById(R.id.txtMoodReport);
    }

    private void showMoodDialog() {
        String[] moods = {"😀 Great", "😊 Good", "😐 Okay", "😟 Stressed"};

        new AlertDialog.Builder(this)
                .setTitle("Select Your Mood")
                .setItems(moods, (dialog, which) -> {
                    selectedMood = moods[which];
                    txtMoodEmoji.setText(selectedMood.split(" ")[0]);
                    txtMoodReport.setText("Weekly report generated based on mood: " + selectedMood);
                }).show();
    }

    private void sendAIQuestion(String question) {
        if (question.trim().isEmpty()) {
            Toast.makeText(this, "Please type a question", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Connect to AI API
        txtAIResponse.setText("AI response for: " + question);
        inputAIQuestion.setText("");
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your question");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        } catch (Exception e) {
            Toast.makeText(this, "Voice input not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String spokenText = result.get(0);
                sendAIQuestion(spokenText);
            }
        }
    }
}
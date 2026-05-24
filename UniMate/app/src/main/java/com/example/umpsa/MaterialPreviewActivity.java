package com.example.umpsa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MaterialPreviewActivity extends Activity {

    TextView btnBack;
    TextView txtExtractedPreview;
    TextView btnSummarize;
    TextView btnTranslate;

    // This will hold the extracted text from scan or upload
    String extractedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_preview);

        btnBack = findViewById(R.id.btnBack);
        txtExtractedPreview = findViewById(R.id.txtExtractedPreview);
        btnSummarize = findViewById(R.id.btnSummarize);
        btnTranslate = findViewById(R.id.btnTranslate);

        btnBack.setOnClickListener(v -> finish());

        // Display text passed from Scan/Upload
        extractedText = getIntent().getStringExtra("extractedText");
        if (extractedText != null && !extractedText.isEmpty()) {
            txtExtractedPreview.setText(extractedText);
        }

        btnSummarize.setOnClickListener(v -> {
            if (extractedText.isEmpty()) {
                Toast.makeText(this, "No text to summarize", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Call AI API to summarize
            Toast.makeText(this, "Summarize clicked (AI processing placeholder)", Toast.LENGTH_SHORT).show();
        });

        btnTranslate.setOnClickListener(v -> {
            if (extractedText.isEmpty()) {
                Toast.makeText(this, "No text to translate", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Call AI API to translate
            Toast.makeText(this, "Translate clicked (AI processing placeholder)", Toast.LENGTH_SHORT).show();
        });
    }
}
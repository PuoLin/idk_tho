package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AiLearningActivity extends Activity {

    View cardScanMaterial;
    View cardUploadMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_learning);

        NavigationHelper.setup(this, "ai");

        cardScanMaterial = findViewById(R.id.cardScanMaterial);
        cardUploadMaterial = findViewById(R.id.cardUploadMaterial);

        cardScanMaterial.setOnClickListener(v ->
                startActivity(new Intent(AiLearningActivity.this, ScanNotesActivity.class))
        );

        cardUploadMaterial.setOnClickListener(v ->
                startActivity(new Intent(AiLearningActivity.this, UploadMaterialActivity.class))
        );
    }
}
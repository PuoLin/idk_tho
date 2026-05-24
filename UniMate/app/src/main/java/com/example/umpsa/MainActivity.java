package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    View cardAcademic;
    View cardAiLearning;
    View cardCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationHelper.setup(this, "home");

        cardAcademic = findViewById(R.id.cardAcademic);
        cardAiLearning = findViewById(R.id.cardAiLearning);
        cardCommunity = findViewById(R.id.cardCommunity);

        cardAcademic.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ScheduleActivity.class))
        );

        cardAiLearning.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AiLearningActivity.class))
        );

        cardCommunity.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CommunityActivity.class))
        );
    }
}
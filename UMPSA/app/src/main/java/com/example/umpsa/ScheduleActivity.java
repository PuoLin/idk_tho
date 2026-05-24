package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScheduleActivity extends Activity {

    View cardTodaySchedule;
    View cardAssignmentPlanner;

    TextView txtClassCount;
    TextView txtTaskCount;
    TextView txtProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        NavigationHelper.setup(this, "academic");

        cardTodaySchedule = findViewById(R.id.cardTodaySchedule);
        cardAssignmentPlanner = findViewById(R.id.cardAssignmentPlanner);

        txtClassCount = findViewById(R.id.txtClassCount);
        txtTaskCount = findViewById(R.id.txtTaskCount);
        txtProgress = findViewById(R.id.txtProgress);

        cardTodaySchedule.setOnClickListener(v ->
                startActivity(new Intent(ScheduleActivity.this, TodayScheduleActivity.class))
        );

        cardAssignmentPlanner.setOnClickListener(v ->
                startActivity(new Intent(ScheduleActivity.this, AssignmentReminderActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        SharedPreferences timetablePrefs =
                getSharedPreferences("UniMateTimetable", MODE_PRIVATE);

        SharedPreferences assignmentPrefs =
                getSharedPreferences("UniMateAssignments", MODE_PRIVATE);

        String timetableData = timetablePrefs.getString("weekly_timetable", "");
        String assignmentData = assignmentPrefs.getString("assignment_reminders", "");

        int classCount = countSavedItems(timetableData);
        int taskCount = countSavedItems(assignmentData);

        txtClassCount.setText(String.valueOf(classCount));
        txtTaskCount.setText(String.valueOf(taskCount));

        int progress = calculateProgress(classCount, taskCount);
        txtProgress.setText(progress + "%");
    }

    private int countSavedItems(String data) {
        if (data == null || data.trim().isEmpty()) {
            return 0;
        }

        return data.split("##").length;
    }

    private int calculateProgress(int classCount, int taskCount) {
        int totalActivity = classCount + taskCount;

        if (totalActivity == 0) {
            return 0;
        }

        int progress = totalActivity * 10;

        if (progress > 100) {
            progress = 100;
        }

        return progress;
    }
}
package com.example.umpsa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class TodayScheduleActivity extends Activity {

    TextView btnAddClass, btnBack;
    LinearLayout timetableList;

    SharedPreferences prefs;
    ArrayList<String[]> timetable = new ArrayList<>();

    String[] days = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_schedule);

        btnAddClass = findViewById(R.id.btnAddClass);
        btnBack = findViewById(R.id.btnBack);
        timetableList = findViewById(R.id.timetableList);

        prefs = getSharedPreferences("UniMateTimetable", MODE_PRIVATE);

        loadTimetable();

        btnBack.setOnClickListener(v -> finish());
        btnAddClass.setOnClickListener(v -> showClassDialog(-1));
    }

    private void showClassOptions(int index) {
        String[] options = {"Edit Class", "Delete Class"};

        new AlertDialog.Builder(this)
                .setTitle("Class Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showClassDialog(index);
                    } else {
                        confirmDeleteClass(index);
                    }
                })
                .show();
    }

    private void showClassDialog(int editIndex) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        Spinner spinnerDay = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                days
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);

        TextView btnPickTime = new TextView(this);
        btnPickTime.setText("Choose Time");
        btnPickTime.setTextSize(16);
        btnPickTime.setTextColor(0xFF4F46E5);
        btnPickTime.setPadding(0, 30, 0, 30);

        EditText editCourse = new EditText(this);
        editCourse.setHint("Course name");

        EditText editVenue = new EditText(this);
        editVenue.setHint("Venue");

        final String[] selectedTime = {""};

        if (editIndex != -1) {
            String[] item = timetable.get(editIndex);
            spinnerDay.setSelection(getDayIndex(item[0]));
            selectedTime[0] = item[1];
            btnPickTime.setText(item[1]);
            editCourse.setText(item[2]);
            editVenue.setText(item[3]);
        }

        btnPickTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int hour12 = hourOfDay % 12;

                        if (hour12 == 0) {
                            hour12 = 12;
                        }

                        selectedTime[0] = String.format("%02d:%02d %s", hour12, minute, amPm);
                        btnPickTime.setText(selectedTime[0]);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
            );

            dialog.show();
        });

        layout.addView(spinnerDay);
        layout.addView(btnPickTime);
        layout.addView(editCourse);
        layout.addView(editVenue);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(editIndex == -1 ? "Add Class" : "Edit Class")
                .setView(layout)
                .setPositiveButton(editIndex == -1 ? "Add" : "Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String day = spinnerDay.getSelectedItem().toString();
                String course = editCourse.getText().toString().trim();
                String venue = editVenue.getText().toString().trim();

                if (course.isEmpty()) {
                    Toast.makeText(this, "Please type course name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedTime[0].isEmpty()) {
                    Toast.makeText(this, "Please choose time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (venue.isEmpty()) {
                    venue = "No venue";
                }

                String[] classData = {
                        cleanText(day),
                        cleanText(selectedTime[0]),
                        cleanText(course),
                        cleanText(venue)
                };

                if (editIndex == -1) {
                    timetable.add(classData);
                    Toast.makeText(this, "Class added", Toast.LENGTH_SHORT).show();
                } else {
                    timetable.set(editIndex, classData);
                    Toast.makeText(this, "Class updated", Toast.LENGTH_SHORT).show();
                }

                saveTimetable();
                renderTimetable();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void loadTimetable() {
        timetable.clear();

        String saved = prefs.getString("weekly_timetable", "");

        if (!saved.isEmpty()) {
            String[] items = saved.split("##");

            for (String item : items) {
                String[] details = item.split("\\|\\|", -1);

                if (details.length == 4) {
                    timetable.add(details);
                }
            }
        }

        renderTimetable();
    }

    private void saveTimetable() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < timetable.size(); i++) {
            String[] item = timetable.get(i);

            builder.append(item[0])
                    .append("||")
                    .append(item[1])
                    .append("||")
                    .append(item[2])
                    .append("||")
                    .append(item[3]);

            if (i < timetable.size() - 1) {
                builder.append("##");
            }
        }

        prefs.edit().putString("weekly_timetable", builder.toString()).apply();
    }

    private void renderTimetable() {
        timetableList.removeAllViews();

        if (timetable.isEmpty()) {
            addEmptyMessage();
            return;
        }

        for (int i = 0; i < timetable.size(); i++) {
            String[] item = timetable.get(i);
            addClassCard(i, item[0], item[1], item[2], item[3]);
        }
    }

    private void addEmptyMessage() {
        TextView empty = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);

        empty.setLayoutParams(params);
        empty.setText("No timetable yet.\nPress + Add Class to create your timetable.");
        empty.setTextSize(15);
        empty.setTextColor(0xFF64748B);
        empty.setPadding(24, 24, 24, 24);
        empty.setBackgroundResource(R.drawable.card_white_big);
        empty.setElevation(4);

        timetableList.addView(empty);
    }

    private void addClassCard(int index, String day, String time, String course, String venue) {
        TextView card = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);

        card.setLayoutParams(params);
        card.setText("📅 " + day + "\n📚 " + course + "\n🕒 " + time + "\n📍 " + venue);
        card.setTextSize(16);
        card.setTextColor(0xFF111827);
        card.setPadding(24, 22, 24, 22);
        card.setBackgroundResource(R.drawable.card_white_big);
        card.setElevation(4);

        card.setOnClickListener(v -> showClassOptions(index));

        timetableList.addView(card);
    }

    private void confirmDeleteClass(int index) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Class?")
                .setMessage("Delete this class only?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    timetable.remove(index);
                    saveTimetable();
                    renderTimetable();
                    Toast.makeText(this, "Class deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int getDayIndex(String day) {
        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(day)) {
                return i;
            }
        }
        return 0;
    }

    private String cleanText(String text) {
        return text.replace("||", " ").replace("##", " ");
    }
}
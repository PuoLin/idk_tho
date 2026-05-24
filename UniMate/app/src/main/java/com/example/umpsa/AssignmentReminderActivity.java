package com.example.umpsa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

public class AssignmentReminderActivity extends Activity {

    TextView btnAddAssignment, btnBack;
    LinearLayout assignmentList;

    SharedPreferences prefs;
    ArrayList<String[]> assignments = new ArrayList<>();

    String[] priorities = {
            "High", "Medium", "Low"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_reminder);

        btnAddAssignment = findViewById(R.id.btnAddAssignment);
        btnBack = findViewById(R.id.btnBack);
        assignmentList = findViewById(R.id.assignmentList);

        prefs = getSharedPreferences("UniMateAssignments", MODE_PRIVATE);

        loadAssignments();

        btnBack.setOnClickListener(v -> finish());
        btnAddAssignment.setOnClickListener(v -> showAssignmentDialog(-1));
    }

    private void showAssignmentOptions(int index) {
        String[] options = {"Edit Assignment", "Delete Assignment"};

        new AlertDialog.Builder(this)
                .setTitle("Assignment Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showAssignmentDialog(index);
                    } else {
                        confirmDeleteAssignment(index);
                    }
                })
                .show();
    }

    private void showAssignmentDialog(int editIndex) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        EditText editTitle = new EditText(this);
        editTitle.setHint("Assignment title");

        EditText editCourse = new EditText(this);
        editCourse.setHint("Course name");

        TextView btnPickDueDate = new TextView(this);
        btnPickDueDate.setText("Choose Due Date & Time");
        btnPickDueDate.setTextSize(16);
        btnPickDueDate.setTextColor(0xFF4F46E5);
        btnPickDueDate.setPadding(0, 30, 0, 30);

        Spinner spinnerPriority = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorities
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        final String[] selectedDueDate = {""};

        if (editIndex != -1) {
            String[] item = assignments.get(editIndex);

            editTitle.setText(item[0]);
            editCourse.setText(item[1]);
            selectedDueDate[0] = item[2];
            btnPickDueDate.setText(item[2]);
            spinnerPriority.setSelection(getPriorityIndex(item[3]));
        }

        btnPickDueDate.setOnClickListener(v -> openDateTimePicker(btnPickDueDate, selectedDueDate));

        layout.addView(editTitle);
        layout.addView(editCourse);
        layout.addView(btnPickDueDate);
        layout.addView(spinnerPriority);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(editIndex == -1 ? "Add Assignment" : "Edit Assignment")
                .setView(layout)
                .setPositiveButton(editIndex == -1 ? "Add" : "Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String title = editTitle.getText().toString().trim();
                String course = editCourse.getText().toString().trim();
                String dueDate = selectedDueDate[0];
                String priority = spinnerPriority.getSelectedItem().toString();

                if (title.isEmpty()) {
                    Toast.makeText(this, "Please type assignment title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (course.isEmpty()) {
                    Toast.makeText(this, "Please type course name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dueDate.isEmpty()) {
                    Toast.makeText(this, "Please choose due date and time", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] assignmentData = {
                        cleanText(title),
                        cleanText(course),
                        cleanText(dueDate),
                        cleanText(priority)
                };

                if (editIndex == -1) {
                    assignments.add(assignmentData);
                    Toast.makeText(this, "Assignment added", Toast.LENGTH_SHORT).show();
                } else {
                    assignments.set(editIndex, assignmentData);
                    Toast.makeText(this, "Assignment updated", Toast.LENGTH_SHORT).show();
                }

                saveAssignments();
                renderAssignments();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void openDateTimePicker(TextView btnPickDueDate, String[] selectedDueDate) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    int displayMonth = month + 1;

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                String amPm = hourOfDay >= 12 ? "PM" : "AM";
                                int hour12 = hourOfDay % 12;

                                if (hour12 == 0) {
                                    hour12 = 12;
                                }

                                selectedDueDate[0] =
                                        String.format("%02d/%02d/%04d %02d:%02d %s",
                                                dayOfMonth,
                                                displayMonth,
                                                year,
                                                hour12,
                                                minute,
                                                amPm);

                                btnPickDueDate.setText(selectedDueDate[0]);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );

                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void loadAssignments() {
        assignments.clear();

        String saved = prefs.getString("assignment_reminders", "");

        if (!saved.isEmpty()) {
            String[] items = saved.split("##");

            for (String item : items) {
                String[] details = item.split("\\|\\|", -1);

                if (details.length == 4) {
                    assignments.add(details);
                }
            }
        }

        renderAssignments();
    }

    private void saveAssignments() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < assignments.size(); i++) {
            String[] item = assignments.get(i);

            builder.append(item[0])
                    .append("||")
                    .append(item[1])
                    .append("||")
                    .append(item[2])
                    .append("||")
                    .append(item[3]);

            if (i < assignments.size() - 1) {
                builder.append("##");
            }
        }

        prefs.edit().putString("assignment_reminders", builder.toString()).apply();
    }

    private void renderAssignments() {
        assignmentList.removeAllViews();

        if (assignments.isEmpty()) {
            addEmptyMessage();
            return;
        }

        for (int i = 0; i < assignments.size(); i++) {
            String[] item = assignments.get(i);
            addAssignmentCard(i, item[0], item[1], item[2], item[3]);
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
        empty.setText("No assignment reminders yet.\nPress + Add Assignment to create one.");
        empty.setTextSize(15);
        empty.setTextColor(0xFF64748B);
        empty.setPadding(24, 24, 24, 24);
        empty.setBackgroundResource(R.drawable.card_white_big);
        empty.setElevation(4);

        assignmentList.addView(empty);
    }

    private void addAssignmentCard(int index, String title, String course, String dueDate, String priority) {
        TextView card = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);

        card.setLayoutParams(params);
        card.setText("📝 " + title + "\n📚 " + course + "\n⏰ Due: " + dueDate + "\n⭐ Priority: " + priority);
        card.setTextSize(16);
        card.setTextColor(0xFF111827);
        card.setPadding(24, 22, 24, 22);
        card.setBackgroundResource(R.drawable.card_white_big);
        card.setElevation(4);

        card.setOnClickListener(v -> showAssignmentOptions(index));

        assignmentList.addView(card);
    }

    private void confirmDeleteAssignment(int index) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Assignment?")
                .setMessage("Delete this assignment only?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    assignments.remove(index);
                    saveAssignments();
                    renderAssignments();
                    Toast.makeText(this, "Assignment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int getPriorityIndex(String priority) {
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].equals(priority)) {
                return i;
            }
        }
        return 0;
    }

    private String cleanText(String text) {
        return text.replace("||", " ").replace("##", " ");
    }
}
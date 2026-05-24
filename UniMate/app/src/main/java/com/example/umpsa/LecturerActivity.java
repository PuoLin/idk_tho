package com.example.umpsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LecturerActivity extends Activity {

    private ViewFlipper viewFlipper;
    private Button btnNavReport;
    private Button btnNavManage;
    private Button btnViewDetails;
    private TextView btnBackFromStudent;
    private Button btnNavProfile;
    private TextView btnModifyBack;
    private EditText etModifyTitle;
    private EditText etModifyDueDate;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchModifyHidden;
    private Button btnModifySave;

    // Modify Assignment Page Elements
    private TextView btnModifyAssignBack;
    private EditText etModifyAssignTitle;
    private EditText etModifyAssignDueDate;
    private Button btnModifyAssignUpload;
    private TextView tvModifyAssignFileName;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchModifyAssignHidden;
    private Button btnModifyAssignSave;

    // Quiz Elements
    private Button btnCreateQuiz;
    private TextView btnBackFromQuiz;
    private Button btnPublishQuiz;
    private EditText etQuizTitle;
    private EditText etQuizDueDate;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchHidden;
    private LinearLayout containerActivities;

    // Assignment Elements
    private Button btnCreateAssignment;
    private TextView btnBackFromAssignment;
    private Button btnPublishAssignment;
    private EditText etAssignmentTitle;
    private EditText etAssignmentDueDate;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchAssignmentHidden;
    private Button btnUploadFile;
    private TextView tvFileName;

    // ChatBot Elements
    private TextView btnFloatingAi;
    private TextView btnBackFromChat;
    private Button btnSendChat;
    private EditText etChatMessage;
    private LinearLayout chatMessageContainer;
    private ScrollView chatScrollView;
    // Student Profile Elements
    private TextView btnBackFromProfile;

    // Variables for drag-and-drop floating button
    private float dX = 0f;
    private float dY = 0f;
    private float initialX = 0f;
    private float initialY = 0f;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_lecturer);

        // Map UI Elements
        viewFlipper = findViewById(R.id.viewFlipper);
        btnNavReport = findViewById(R.id.btnNavReport);
        btnNavManage = findViewById(R.id.btnNavManage);
        containerActivities = findViewById(R.id.containerActivities);
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnViewDetails = findViewById(R.id.btnViewDetails);
        btnBackFromStudent = findViewById(R.id.btnBackFromStudent);

        // Map Student Profile Elements
        btnBackFromProfile = findViewById(R.id.btnBackFromProfile);

        // Map Quiz Elements
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        btnBackFromQuiz = findViewById(R.id.btnBackFromQuiz);
        btnPublishQuiz = findViewById(R.id.btnPublishQuiz);
        etQuizTitle = findViewById(R.id.etQuizTitle);
        etQuizDueDate = findViewById(R.id.etQuizDueDate);
        switchHidden = findViewById(R.id.switchHidden);

        // Map Assignment Elements
        btnCreateAssignment = findViewById(R.id.btnCreateAssignment);
        btnBackFromAssignment = findViewById(R.id.btnBackFromAssignment);
        btnPublishAssignment = findViewById(R.id.btnPublishAssignment);
        etAssignmentTitle = findViewById(R.id.etAssignmentTitle);
        etAssignmentDueDate = findViewById(R.id.etAssignmentDueDate);
        switchAssignmentHidden = findViewById(R.id.switchAssignmentHidden);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        tvFileName = findViewById(R.id.tvFileName);

        // Map Chatbot Elements
        btnFloatingAi = findViewById(R.id.btnFloatingAi);
        btnBackFromChat = findViewById(R.id.btnBackFromChat);
        btnSendChat = findViewById(R.id.btnSendChat);
        etChatMessage = findViewById(R.id.etChatMessage);
        chatMessageContainer = findViewById(R.id.chatMessageContainer);
        chatScrollView = findViewById(R.id.chatScrollView);

        //Map Modify Quiz
        btnModifyBack = findViewById(R.id.btnBack);
        etModifyTitle = findViewById(R.id.editTextText5);
        etModifyDueDate = findViewById(R.id.editTextText11);
        switchModifyHidden = findViewById(R.id.switchModifyHidden);
        btnModifySave = findViewById(R.id.button3);

        // Map Modify Assignment Elements
        btnModifyAssignBack = findViewById(R.id.btnModifyAssignBack);
        etModifyAssignTitle = findViewById(R.id.etModifyAssignTitle);
        etModifyAssignDueDate = findViewById(R.id.etModifyAssignDueDate);
        btnModifyAssignUpload = findViewById(R.id.btnModifyAssignUpload);
        tvModifyAssignFileName = findViewById(R.id.tvModifyAssignFileName);
        switchModifyAssignHidden = findViewById(R.id.switchModifyAssignHidden);
        btnModifyAssignSave = findViewById(R.id.btnModifyAssignSave);

        // NAVIGATION LISTENERS
        btnNavReport.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(0);
            updateNavState(0);
            btnFloatingAi.setVisibility(View.VISIBLE);
        });

        btnNavManage.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
            updateNavState(1);
            btnFloatingAi.setVisibility(View.VISIBLE);
        });

        btnCreateQuiz.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(2);
            etQuizTitle.getText().clear();
            etQuizDueDate.getText().clear();
            deselectBottomNav();
        });

        btnBackFromQuiz.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
            updateNavState(1);
        });

        btnNavProfile.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(5);
            updateNavState(2);
            btnFloatingAi.setVisibility(View.VISIBLE);
        });

        btnViewDetails.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(6);
            btnFloatingAi.setVisibility(View.GONE);
        });

        btnBackFromStudent.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(0);
            btnFloatingAi.setVisibility(View.VISIBLE);
        });

        btnModifyBack.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
        });

        btnBackFromProfile.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(6);
        });

        View btnStudentAhmad = findViewById(R.id.btnStudentAhmad);

        btnStudentAhmad.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(9);
        });


        // Create Assignment Logic
        btnCreateAssignment.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(3);
            etAssignmentTitle.getText().clear();
            etAssignmentDueDate.getText().clear();
            tvFileName.setText("No file selected");
            tvFileName.setTextColor(Color.parseColor("#6B7280"));
            switchAssignmentHidden.setChecked(false);
            deselectBottomNav();
        });

        btnBackFromAssignment.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
            updateNavState(1);
        });

        btnUploadFile.setOnClickListener(v -> {
            tvFileName.setText("rubric_BCS2173_final.pdf");
            tvFileName.setTextColor(Color.parseColor("#10B981"));
            Toast.makeText(this, "File Attached Successfully", Toast.LENGTH_SHORT).show();
        });

        btnPublishAssignment.setOnClickListener(v -> {
            String titleText = etAssignmentTitle.getText().toString().trim();
            String finalTitle = titleText.isEmpty() ? "Untitled Assignment" : titleText;
            String dueText = etAssignmentDueDate.getText().toString().trim();
            String finalDueDate = dueText.isEmpty() ? "No Due Date" : "Closes " + dueText;
            boolean isHidden = switchAssignmentHidden.isChecked();

            LinearLayout newItemCard = new LinearLayout(this);
            newItemCard.setOrientation(LinearLayout.HORIZONTAL);
            newItemCard.setGravity(Gravity.CENTER_VERTICAL);
            newItemCard.setBackgroundColor(Color.WHITE);
            newItemCard.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dpToPx(4));
            newItemCard.setLayoutParams(cardParams);
            newItemCard.setElevation(dpToPx(1));

            TextView iconLabel = new TextView(this);
            iconLabel.setText(isHidden ? "🔒" : "📁");
            iconLabel.setTextSize(24f);
            iconLabel.setGravity(Gravity.CENTER);
            iconLabel.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(40), dpToPx(40)));

            LinearLayout textColumn = new LinearLayout(this);
            textColumn.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textColParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textColParams.setMargins(dpToPx(12), 0, 0, 0);
            textColumn.setLayoutParams(textColParams);

            TextView titleView = new TextView(this);
            titleView.setText(finalTitle);
            titleView.setTextSize(16f);
            titleView.setTextColor(Color.parseColor("#111827"));
            titleView.setTypeface(null, Typeface.BOLD);
            textColumn.addView(titleView);

            TextView subtitleView = new TextView(this);
            subtitleView.setText(isHidden ? "Draft (Hidden from students)" : "Assignment · " + finalDueDate);
            subtitleView.setTextSize(12f);
            subtitleView.setTextColor(Color.parseColor("#6B7280"));
            textColumn.addView(subtitleView);

            newItemCard.addView(iconLabel);
            newItemCard.addView(textColumn);

            TextView arrowView = new TextView(this);
            arrowView.setText("›");
            arrowView.setTextSize(32f);
            arrowView.setTextColor(Color.parseColor("#9CA3AF"));
            newItemCard.addView(arrowView);

            //modify assignment
            newItemCard.setOnClickListener(click -> {
                viewFlipper.setDisplayedChild(8);

                // Pre-fill fields
                etModifyAssignTitle.setText(titleView.getText().toString());
                switchModifyAssignHidden.setChecked(isHidden);

                // Mock the file upload button on this page
                btnModifyAssignUpload.setOnClickListener(uploadClick -> {
                    tvModifyAssignFileName.setText("updated_rubric.pdf");
                    tvModifyAssignFileName.setTextColor(Color.parseColor("#10B981"));
                });

                // Setup the BACK button
                btnModifyAssignBack.setOnClickListener(backClick -> {
                    viewFlipper.setDisplayedChild(1);
                });

                // Program the SAVE button to update THIS specific assignment card
                btnModifyAssignSave.setOnClickListener(saveClick -> {
                    String updatedTitle = etModifyAssignTitle.getText().toString().trim();
                    String updatedDue = etModifyAssignDueDate.getText().toString().trim();
                    boolean updatedHidden = switchModifyAssignHidden.isChecked();

                    titleView.setText(updatedTitle.isEmpty() ? "Untitled Assignment" : updatedTitle);

                    if (updatedHidden) {
                        iconLabel.setText("🔒");
                        subtitleView.setText("Draft (Hidden from students)");
                    } else {
                        iconLabel.setText("📁");
                        String finalDue = updatedDue.isEmpty() ? "No Due Date" : "Closes " + updatedDue;
                        subtitleView.setText("Assignment · " + finalDue);
                    }

                    Toast.makeText(this, "Assignment Updated!", Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1); // Go back to Manage page
                });
            });

            containerActivities.addView(newItemCard, 0);
            Toast.makeText(this, "Assignment Published Successfully!", Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(1);
            updateNavState(1);
        });

        // Quiz Publish Logic
        btnPublishQuiz.setOnClickListener(v -> {
            String titleText = etQuizTitle.getText().toString().trim();
            String finalTitle = titleText.isEmpty() ? "Untitled Quiz" : titleText;
            String dueText = etQuizDueDate.getText().toString().trim();
            String finalDueDate = dueText.isEmpty() ? "No Due Date" : "Closes " + dueText;
            boolean isHidden = switchHidden.isChecked();

            LinearLayout newItemCard = new LinearLayout(this);
            newItemCard.setOrientation(LinearLayout.HORIZONTAL);
            newItemCard.setGravity(Gravity.CENTER_VERTICAL);
            newItemCard.setBackgroundColor(Color.WHITE);
            newItemCard.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dpToPx(4));
            newItemCard.setLayoutParams(cardParams);
            newItemCard.setElevation(dpToPx(1));

            TextView iconLabel = new TextView(this);
            iconLabel.setText(isHidden ? "🔒" : "\uD83D\uDCC4"); // Document Emoji
            iconLabel.setTextSize(24f);
            iconLabel.setGravity(Gravity.CENTER);
            iconLabel.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(40), dpToPx(40)));

            LinearLayout textColumn = new LinearLayout(this);
            textColumn.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textColParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textColParams.setMargins(dpToPx(12), 0, 0, 0);
            textColumn.setLayoutParams(textColParams);

            TextView titleView = new TextView(this);
            titleView.setText(finalTitle);
            titleView.setTextSize(16f);
            titleView.setTextColor(Color.parseColor("#111827"));
            titleView.setTypeface(null, Typeface.BOLD);
            textColumn.addView(titleView);

            TextView subtitleView = new TextView(this);
            subtitleView.setText(isHidden ? "Draft (Hidden from students)" : "Published · " + finalDueDate);
            subtitleView.setTextSize(12f);
            subtitleView.setTextColor(Color.parseColor("#6B7280"));
            textColumn.addView(subtitleView);

            newItemCard.addView(iconLabel);
            newItemCard.addView(textColumn);

            TextView arrowView = new TextView(this);
            arrowView.setText("›");
            arrowView.setTextSize(32f);
            arrowView.setTextColor(Color.parseColor("#9CA3AF"));
            newItemCard.addView(arrowView);

            // 1. Create a tiny "memory" for this specific card's due date and hidden status
            final String[] currentDueState = {dueText};
            final boolean[] currentHiddenState = {isHidden};

            // Route Quiz card to Page 7
            newItemCard.setOnClickListener(click -> {
                viewFlipper.setDisplayedChild(7);

                // 2. Grab the live title directly from the card, and the rest from our memory!
                etModifyTitle.setText(titleView.getText().toString());
                etModifyDueDate.setText(currentDueState[0]);
                switchModifyHidden.setChecked(currentHiddenState[0]);

                // Setup the BACK button for the quiz page
                btnModifyBack.setOnClickListener(backClick -> {
                    viewFlipper.setDisplayedChild(1);
                });

                // Program the SAVE button to update THIS specific quiz card
                btnModifySave.setOnClickListener(saveClick -> {
                    String updatedTitle = etModifyTitle.getText().toString().trim();
                    String updatedDue = etModifyDueDate.getText().toString().trim();
                    boolean updatedHidden = switchModifyHidden.isChecked();

                    // 3. Update our memory arrays so it remembers for the NEXT time you click!
                    currentDueState[0] = updatedDue;
                    currentHiddenState[0] = updatedHidden;

                    // Update the active card UI on the Manage page
                    titleView.setText(updatedTitle.isEmpty() ? "Untitled Quiz" : updatedTitle);

                    if (updatedHidden) {
                        iconLabel.setText("🔒");
                        subtitleView.setText("Draft (Hidden from students)");
                    } else {
                        iconLabel.setText("\uD83D\uDCC4"); // Document emoji
                        String finalDue = updatedDue.isEmpty() ? "No Due Date" : "Closes " + updatedDue;
                        subtitleView.setText("Published · " + finalDue);
                    }

                    Toast.makeText(this, "Quiz Updated!", Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1); // Go back to Manage page
                });
            });

            containerActivities.addView(newItemCard, 0);

            etQuizTitle.getText().clear();
            etQuizDueDate.getText().clear();
            switchHidden.setChecked(false);
            Toast.makeText(this, "Quiz Published Successfully!", Toast.LENGTH_SHORT).show();
            viewFlipper.setDisplayedChild(1);
            updateNavState(1);
        });

        // Chatbot Logic
        // To make the icon moveable (drag to any place we like)
        btnFloatingAi.setOnTouchListener((view, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    // Record the exact starting point
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    initialX = event.getRawX();
                    initialY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // Move the button instantly
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    return true;
                case MotionEvent.ACTION_UP:
                    // Calculate how far the finger moved
                    float diffX = Math.abs(event.getRawX() - initialX);
                    float diffY = Math.abs(event.getRawY() - initialY);

                    // If it moved less than 10 pixels, treat as a click
                    if (diffX < 10 && diffY < 10) {
                        viewFlipper.setDisplayedChild(4); // Go to Chat Page
                        btnFloatingAi.setVisibility(View.GONE); // Hide button
                        deselectBottomNav();
                    }
                    return true;
                default:
                    return false;
            }
        });

        btnBackFromChat.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(0); // Go back to manage
            btnFloatingAi.setVisibility(View.VISIBLE); // Bring button back
            updateNavState(1);
        });

        btnSendChat.setOnClickListener(v -> {
            String message = etChatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // 1. Add the user's message bubble
                addUserMessage(message);
                etChatMessage.getText().clear();

                // 2. Scroll to the bottom
                chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));

                // 3. Simulate AI thinking delay
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    addAiMessage("That is a great insight! I have noted that down for your next class report. Is there anything else you need help with?");
                    chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
                }, 1000);
            }
        });
    }

    // HELPER FUNCTIONS FOR CHAT BUBBLES
    private void addUserMessage(String message) {
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setBackgroundColor(Color.parseColor("#F3F4F6")); // Gray bubble
        bubble.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END; // Push to the right
        params.setMargins(dpToPx(40), 0, 0, dpToPx(16));
        bubble.setLayoutParams(params);

        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setTextColor(Color.parseColor("#111827"));
        tv.setTextSize(14f);
        bubble.addView(tv);

        chatMessageContainer.addView(bubble);
    }

    private void addAiMessage(String message) {
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setBackgroundColor(Color.parseColor("#E0E7FF")); // Blue bubble
        bubble.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.START; // Push to the left
        params.setMargins(0, 0, dpToPx(40), dpToPx(16));
        bubble.setLayoutParams(params);

        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setTextColor(Color.parseColor("#3730A3"));
        tv.setTextSize(14f);
        bubble.addView(tv);

        chatMessageContainer.addView(bubble);
    }

    private void updateNavState(int tabIndex) {
        btnNavReport.setTextColor(tabIndex == 0 ? Color.parseColor("#7C3AED") : Color.parseColor("#6B7280"));
        btnNavManage.setTextColor(tabIndex == 1 ? Color.parseColor("#7C3AED") : Color.parseColor("#6B7280"));
        btnNavProfile.setTextColor(tabIndex == 2 ? Color.parseColor("#7C3AED") : Color.parseColor("#6B7280"));
    }

    private void deselectBottomNav() {
        btnNavReport.setTextColor(Color.parseColor("#6B7280"));
        btnNavManage.setTextColor(Color.parseColor("#6B7280"));
        btnNavProfile.setTextColor(Color.parseColor("#6B7280"));
    }

    // Helper for DP to Pixel Conversion
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
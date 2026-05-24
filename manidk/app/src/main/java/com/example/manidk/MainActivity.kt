package com.example.manidk

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.core.graphics.toColorInt
import android.view.MotionEvent

class MainActivity : Activity() {

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var btnNavReport: Button
    private lateinit var btnNavManage: Button
    private lateinit var btnViewDetails: Button
    private lateinit var btnBackFromStudent: TextView
    private lateinit var btnNavProfile: Button

    // Quiz Elements
    private lateinit var btnCreateQuiz: Button
    private lateinit var btnBackFromQuiz: TextView
    private lateinit var btnPublishQuiz: Button
    private lateinit var etQuizTitle: EditText
    private lateinit var etQuizDueDate: EditText
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchHidden: Switch
    private lateinit var containerActivities: LinearLayout

    // Assignment Elements
    private lateinit var btnCreateAssignment: Button
    private lateinit var btnBackFromAssignment: TextView
    private lateinit var btnPublishAssignment: Button
    private lateinit var etAssignmentTitle: EditText
    private lateinit var etAssignmentDueDate: EditText
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchAssignmentHidden: Switch
    private lateinit var btnUploadFile: Button
    private lateinit var tvFileName: TextView

    //ChatBot Element
    private lateinit var btnFloatingAi: TextView
    private lateinit var btnBackFromChat: TextView
    private lateinit var btnSendChat: Button
    private lateinit var etChatMessage: EditText
    private lateinit var chatMessageContainer: LinearLayout
    private lateinit var chatScrollView: ScrollView

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_lecturer)

        // Map UI Elements
        viewFlipper = findViewById(R.id.viewFlipper)
        btnNavReport = findViewById(R.id.btnNavReport)
        btnNavManage = findViewById(R.id.btnNavManage)
        containerActivities = findViewById(R.id.containerActivities)
        btnNavProfile = findViewById(R.id.btnNavProfile)
        btnViewDetails = findViewById(R.id.btnViewDetails)
        btnBackFromStudent = findViewById(R.id.btnBackFromStudent)

        // Map Quiz Elements
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz)
        btnBackFromQuiz = findViewById(R.id.btnBackFromQuiz)
        btnPublishQuiz = findViewById(R.id.btnPublishQuiz)
        etQuizTitle = findViewById(R.id.etQuizTitle)
        etQuizDueDate = findViewById(R.id.etQuizDueDate)
        switchHidden = findViewById(R.id.switchHidden)

        // Map Assignment Elements
        btnCreateAssignment = findViewById(R.id.btnCreateAssignment)
        btnBackFromAssignment = findViewById(R.id.btnBackFromAssignment)
        btnPublishAssignment = findViewById(R.id.btnPublishAssignment)
        etAssignmentTitle = findViewById(R.id.etAssignmentTitle)
        etAssignmentDueDate = findViewById(R.id.etAssignmentDueDate)
        switchAssignmentHidden = findViewById(R.id.switchAssignmentHidden)
        btnUploadFile = findViewById(R.id.btnUploadFile)
        tvFileName = findViewById(R.id.tvFileName)

        // Map Chatboy Elements
        btnFloatingAi = findViewById(R.id.btnFloatingAi)
        btnBackFromChat = findViewById(R.id.btnBackFromChat)
        btnSendChat = findViewById(R.id.btnSendChat)
        etChatMessage = findViewById(R.id.etChatMessage)
        chatMessageContainer = findViewById(R.id.chatMessageContainer)
        chatScrollView = findViewById(R.id.chatScrollView)

        // NAVIGATION LISTENERS
        btnNavReport.setOnClickListener {
            viewFlipper.displayedChild = 0
            updateNavState(0)
        }

        btnNavManage.setOnClickListener {
            viewFlipper.displayedChild = 1
            updateNavState(1)
        }

        btnCreateQuiz.setOnClickListener {
            viewFlipper.displayedChild = 2
            etQuizTitle.text.clear()
            etQuizDueDate.text.clear()
            deselectBottomNav()
        }

        btnBackFromQuiz.setOnClickListener {
            viewFlipper.displayedChild = 1
            updateNavState(1)
        }

        btnNavProfile.setOnClickListener {
            viewFlipper.displayedChild = 5
            updateNavState(2)
            btnFloatingAi.visibility = View.VISIBLE
        }

        btnViewDetails.setOnClickListener {
            viewFlipper.displayedChild = 6
            btnFloatingAi.visibility = View.GONE
        }

        btnBackFromStudent.setOnClickListener {
            viewFlipper.displayedChild = 0
            btnFloatingAi.visibility = View.VISIBLE
        }

        // create assignemnt logic
        btnCreateAssignment.setOnClickListener {
            viewFlipper.displayedChild = 3
            etAssignmentTitle.text.clear()
            etAssignmentDueDate.text.clear()
            tvFileName.text = "No file selected"
            tvFileName.setTextColor("#6B7280".toColorInt())
            switchAssignmentHidden.isChecked = false
            deselectBottomNav()
        }

        btnBackFromAssignment.setOnClickListener {
            viewFlipper.displayedChild = 1
            updateNavState(1)
        }

        btnUploadFile.setOnClickListener {
            tvFileName.text = "rubric_BCS2173_final.pdf"
            tvFileName.setTextColor("#10B981".toColorInt())
            Toast.makeText(this, "File Attached Successfully", Toast.LENGTH_SHORT).show()
        }

        btnPublishAssignment.setOnClickListener {
            val titleText = etAssignmentTitle.text.toString().trim()
            val finalTitle = titleText.ifEmpty { "Untitled Assignment" }
            val dueText = etAssignmentDueDate.text.toString().trim()
            val finalDueDate = if (dueText.isEmpty()) "No Due Date" else "Closes $dueText"
            val isHidden = switchAssignmentHidden.isChecked
            val dpToPx = { dp: Int -> (dp * resources.displayMetrics.density).toInt() }

            val newItemCard = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setBackgroundColor(Color.WHITE)
                setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, dpToPx(4)) }
                elevation = dpToPx(1).toFloat()
            }

            val iconLabel = TextView(this).apply {
                text = if (isHidden) "🔒" else "📁"
                textSize = 24f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(dpToPx(40), dpToPx(40))
            }

            val textColumn = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(dpToPx(12), 0, 0, 0)
                }
            }

            textColumn.addView(TextView(this).apply {
                text = finalTitle
                textSize = 16f
                setTextColor("#111827".toColorInt())
                setTypeface(null, Typeface.BOLD)
            })

            textColumn.addView(TextView(this).apply {
                text = if (isHidden) "Draft (Hidden from students)" else "Assignment · $finalDueDate"
                textSize = 12f
                setTextColor("#6B7280".toColorInt())
            })

            newItemCard.addView(iconLabel)
            newItemCard.addView(textColumn)
            newItemCard.addView(TextView(this).apply {
                text = "›"
                textSize = 32f
                setTextColor("#9CA3AF".toColorInt())
            })

            containerActivities.addView(newItemCard, 0)
            Toast.makeText(this, "Assignment Published Successfully!", Toast.LENGTH_SHORT).show()
            viewFlipper.displayedChild = 1
            updateNavState(1)
        }

        // quiz publish logic
        btnPublishQuiz.setOnClickListener {
            val titleText = etQuizTitle.text.toString().trim()
            val finalTitle = titleText.ifEmpty { "Untitled Quiz" }
            val dueText = etQuizDueDate.text.toString().trim()
            val finalDueDate = if (dueText.isEmpty()) "No Due Date" else "Closes $dueText"
            val isHidden = switchHidden.isChecked
            val dpToPx = { dp: Int -> (dp * resources.displayMetrics.density).toInt() }

            val newItemCard = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setBackgroundColor(Color.WHITE)
                setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, dpToPx(4)) }
                elevation = dpToPx(1).toFloat()
            }

            val iconLabel = TextView(this).apply {
                text = if (isHidden) "🔒" else "\uD83D\uDCC4"
                textSize = 24f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(dpToPx(40), dpToPx(40))
            }

            val textColumn = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(dpToPx(12), 0, 0, 0)
                }
            }

            textColumn.addView(TextView(this).apply {
                text = finalTitle
                textSize = 16f
                setTextColor("#111827".toColorInt())
                setTypeface(null, Typeface.BOLD)
            })

            textColumn.addView(TextView(this).apply {
                text = if (isHidden) "Draft (Hidden from students)" else "Published · $finalDueDate"
                textSize = 12f
                setTextColor("#6B7280".toColorInt())
            })

            newItemCard.addView(iconLabel)
            newItemCard.addView(textColumn)
            newItemCard.addView(TextView(this).apply {
                text = "›"
                textSize = 32f
                setTextColor("#9CA3AF".toColorInt())
            })

            containerActivities.addView(newItemCard, 0)
            etQuizTitle.text.clear()
            etQuizDueDate.text.clear()
            switchHidden.isChecked = false
            Toast.makeText(this, "Quiz Published Successfully!", Toast.LENGTH_SHORT).show()
            viewFlipper.displayedChild = 1
            updateNavState(1)
        }

        // Chatbot Logic

        ////To make the icon moveable (drag to any place we like) - referred gemini AI
        var dX = 0f
        var dY = 0f
        var initialX = 0f
        var initialY = 0f

        @SuppressLint("ClickableViewAccessibility")
        btnFloatingAi.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    // Record the exact starting point when the finger touches the screen
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                    initialX = event.rawX
                    initialY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Move the button exactly where the finger drags it instantly
                    view.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Calculate how far the finger moved before lifting up
                    val diffX = kotlin.math.abs(event.rawX - initialX)
                    val diffY = kotlin.math.abs(event.rawY - initialY)

                    // If it moved less than 10 pixels, they meant to click it, not drag it!
                    if (diffX < 10 && diffY < 10) {
                        viewFlipper.displayedChild = 4 // Go to Chat Page
                        btnFloatingAi.visibility = View.GONE // Hide the floating button
                        deselectBottomNav()
                    }
                    true
                }
                else -> false
            }
        }
        ////

        btnBackFromChat.setOnClickListener {
            viewFlipper.displayedChild = 1 // Go back to manage
            btnFloatingAi.visibility = View.VISIBLE // Bring the floating button back
            updateNavState(1)
        }

        btnSendChat.setOnClickListener {
            val message = etChatMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // 1. Add the user's message bubble
                addUserMessage(message)
                etChatMessage.text.clear()

                // 2. Scroll to the bottom
                chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }

                // 3. Simulate AI thinking delay for the demo
                Handler(Looper.getMainLooper()).postDelayed({
                    addAiMessage("That is a great insight! I have noted that down for your next class report. Is there anything else you need help with?")
                    chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }
                }, 1000)
            }
        }
    }

    //HELPER FUNCTIONS FOR CHAT BUBBLES

    private fun addUserMessage(message: String) {
        val dpToPx = { dp: Int -> (dp * resources.displayMetrics.density).toInt() }

        val bubble = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor("#F3F4F6".toColorInt()) // Gray bubble
            setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END // Push to the right
                setMargins(dpToPx(40), 0, 0, dpToPx(16))
            }
        }

        bubble.addView(TextView(this).apply {
            text = message
            setTextColor("#111827".toColorInt()) // <-- FIXED HERE
            textSize = 14f
        })

        chatMessageContainer.addView(bubble)
    }

    private fun addAiMessage(message: String) {
        val dpToPx = { dp: Int -> (dp * resources.displayMetrics.density).toInt() }

        val bubble = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor("#E0E7FF".toColorInt()) // Blue bubble
            setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START // Push to the left
                setMargins(0, 0, dpToPx(40), dpToPx(16))
            }
        }

        bubble.addView(TextView(this).apply {
            text = message
            setTextColor("#3730A3".toColorInt()) // <-- FIXED HERE
            textSize = 14f
        })

        chatMessageContainer.addView(bubble)
    }

    private fun updateNavState(tabIndex: Int) {
        btnNavReport.setTextColor(if (tabIndex == 0) "#7C3AED".toColorInt() else "#6B7280".toColorInt())
        btnNavManage.setTextColor(if (tabIndex == 1) "#7C3AED".toColorInt() else "#6B7280".toColorInt())
        btnNavProfile.setTextColor(if (tabIndex == 2) "#7C3AED".toColorInt() else "#6B7280".toColorInt())
    }

    private fun deselectBottomNav() {
        btnNavReport.setTextColor("#6B7280".toColorInt())
        btnNavManage.setTextColor("#6B7280".toColorInt())
        btnNavProfile.setTextColor("#6B7280".toColorInt())
    }
}
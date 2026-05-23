package com.example.manidk

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ViewFlipper
import androidx.core.graphics.toColorInt

class MainActivity : Activity() { // <-- We changed this to standard Activity

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var btnNavReport: Button
    private lateinit var btnNavManage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide standard Action Bar for a clean, modern look
        actionBar?.hide() // <-- We changed this to match the new Activity type

        setContentView(R.layout.activity_home)

        // Map UI Elements
        viewFlipper = findViewById(R.id.viewFlipper)
        btnNavReport = findViewById(R.id.btnNavReport)
        btnNavManage = findViewById(R.id.btnNavManage)

        // Bottom Navigation Listeners
        btnNavReport.setOnClickListener {
            viewFlipper.displayedChild = 0 // Show Reports Page
            updateNavState(true)
        }

        btnNavManage.setOnClickListener {
            viewFlipper.displayedChild = 1 // Show Manage Page
            updateNavState(false)
        }
    }

    // Handles the color switching of the Bottom Navigation to show active states
    private fun updateNavState(isReportActive: Boolean) {
        if (isReportActive) {
            btnNavReport.setTextColor("#7C3AED".toColorInt()) // Active Purple
            btnNavManage.setTextColor("#6B7280".toColorInt()) // Inactive Gray
        } else {
            btnNavReport.setTextColor("#6B7280".toColorInt()) // Inactive Gray
            btnNavManage.setTextColor("#7C3AED".toColorInt()) // Active Purple
        }
    }
}
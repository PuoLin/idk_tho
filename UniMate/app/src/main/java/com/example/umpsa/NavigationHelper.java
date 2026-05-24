package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class NavigationHelper {

    public static void setup(Activity activity, String currentPage) {
        View navHome = activity.findViewById(R.id.navHome);
        View navAcademic = activity.findViewById(R.id.navAcademic);
        View navAiLearning = activity.findViewById(R.id.navAiLearning);
        View navCommunity = activity.findViewById(R.id.navCommunity);

        if (navHome != null) {
            navHome.setOnClickListener(v -> openPage(activity, MainActivity.class));
        }

        if (navAcademic != null) {
            navAcademic.setOnClickListener(v -> openPage(activity, ScheduleActivity.class));
        }

        if (navAiLearning != null) {
            navAiLearning.setOnClickListener(v -> openPage(activity, AiLearningActivity.class));
        }

        if (navCommunity != null) {
            navCommunity.setOnClickListener(v -> openPage(activity, CommunityActivity.class));
        }

        highlightCurrentPage(activity, currentPage);
    }

    private static void openPage(Activity activity, Class<?> targetActivity) {
        if (activity.getClass().equals(targetActivity)) {
            return;
        }

        Intent intent = new Intent(activity, targetActivity);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void highlightCurrentPage(Activity activity, String currentPage) {
        resetNav(activity, R.id.navHome);
        resetNav(activity, R.id.navAcademic);
        resetNav(activity, R.id.navAiLearning);
        resetNav(activity, R.id.navCommunity);

        if (currentPage.equals("home")) {
            selectNav(activity, R.id.navHome);
        } else if (currentPage.equals("academic")) {
            selectNav(activity, R.id.navAcademic);
        } else if (currentPage.equals("ai")) {
            selectNav(activity, R.id.navAiLearning);
        } else if (currentPage.equals("community")) {
            selectNav(activity, R.id.navCommunity);
        }
    }

    private static void selectNav(Activity activity, int id) {
        TextView nav = activity.findViewById(id);

        if (nav != null) {
            nav.setBackgroundResource(R.drawable.nav_selected);
            nav.setTextColor(0xFFFFFFFF);
        }
    }

    private static void resetNav(Activity activity, int id) {
        TextView nav = activity.findViewById(id);

        if (nav != null) {
            nav.setBackgroundColor(0x00000000);
            nav.setTextColor(0xFF94A3B8);
        }
    }
}
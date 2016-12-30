package com.example.android.habittracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.example.android.habittracker.data.HabitContract.HabitEntry;

public class HabitActivity extends AppCompatActivity {

    /**
     * Variable to save the current date in milliseconds
     */
    private long mDateToday;

    /**
     * Spinner field to select the habit name
     */
    private Spinner mGenderSpinner;

    /**
     * Name of the habit. The possible valid values are in the HabitContract.java file:
     * 0 for "None tracked today", 1 for "Got to bed early", 2 for "Performed daily exercise",
     * 3 for "Ate healthy food".
     * {@link HabitEntry#HABIT_NONE}, {@link HabitEntry#HABIT_SLEEP},
     * {@link HabitEntry#HABIT_EXERCISE}, or {@link HabitEntry#HABIT_EAT_HEALTHY}.
     */
    private int mHabit = HabitEntry.HABIT_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
    }


}

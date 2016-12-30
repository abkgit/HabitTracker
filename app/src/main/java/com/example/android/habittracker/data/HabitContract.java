package com.example.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Habit Tracker app.
 */
public class HabitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HabitContract() {}

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit entry for the current date.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of database table for habits */
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the habit.
         *
         * The only possible values are {@link #HABIT_NONE}, {@link #HABIT_SLEEP},
         * {@link #HABIT_EXERCISE}, or {@link #HABIT_EAT_HEALTHY}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT = "habit";

        /**
         * The date(in milliseconds) on which the habit was tracked.
         *
         * Type: BIGINT
         */
        public final static String COLUMN_HABIT_DATE = "date";

        /**
         * Possible values for the habits of the user.
         */
        public static final int HABIT_NONE = 0;
        public static final int HABIT_SLEEP = 1;
        public static final int HABIT_EXERCISE = 2;
        public static final int HABIT_EAT_HEALTHY = 3;
    }
}

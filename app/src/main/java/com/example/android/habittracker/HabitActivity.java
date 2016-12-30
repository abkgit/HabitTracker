package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

import java.util.Calendar;

public class HabitActivity extends AppCompatActivity {

    /**
     * Variable to save the current date and time in milliseconds
     */
    private long mDateToday;

    /**
     * Spinner field to select the habit name
     */
    private Spinner mHabitSpinner;

    /**
     * Name of the habit. The possible valid values are in the HabitContract.java file:
     * 0 for "None tracked today", 1 for "Got to bed early", 2 for "Performed daily exercise",
     * 3 for "Ate healthy food".
     * {@link HabitEntry#HABIT_NONE}, {@link HabitEntry#HABIT_SLEEP},
     * {@link HabitEntry#HABIT_EXERCISE}, or {@link HabitEntry#HABIT_EAT_HEALTHY}.
     */
    private int mHabit = HabitEntry.HABIT_NONE;

    /**
     * Database helper that will provide us access to the database
     */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(this);

        Calendar cal = Calendar.getInstance();
        mDateToday = cal.getTimeInMillis();

        mHabitSpinner = (Spinner) findViewById(R.id.spinner_habit);

        setupSpinner();

        displayDatabaseInfo();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the habit name.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter habitSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_habit_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        habitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mHabitSpinner.setAdapter(habitSpinnerAdapter);

        // Set the integer mHabit to the constant values
        mHabitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.habit_sleep))) {
                        mHabit = HabitEntry.HABIT_SLEEP;
                    } else if (selection.equals(getString(R.string.habit_exercise))) {
                        mHabit = HabitEntry.HABIT_EXERCISE;
                    } else if (selection.equals(getString(R.string.habit_eat_healthy))) {
                        mHabit = HabitEntry.HABIT_EAT_HEALTHY;
                    } else {
                        mHabit = HabitEntry.HABIT_NONE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mHabit = HabitEntry.HABIT_NONE;
            }
        });
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the habits database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT,
                HabitEntry.COLUMN_HABIT_DATE};

        // Perform a query on the habits table
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_data);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The habits table contains <number of rows in Cursor> habit entries.
            // _id - habit - date
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText(getString(R.string.habits_table_contains) + cursor.getCount()
                    + getString(R.string.habit_entries) + "\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT + " - " +
                    HabitEntry.COLUMN_HABIT_DATE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int habitColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT);
            int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DATE);


            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                int currentHabit = cursor.getInt(habitColumnIndex);
                long currentDate = cursor.getLong(dateColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentHabit + " - " +
                        currentDate));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_habit.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_habit, menu);
        return true;
    }

    /**
     * Helper method to insert hardcoded habit data into the database.
     */
    private void insertHabit() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit name from the spinner and system date are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT, mHabit);
        values.put(HabitEntry.COLUMN_HABIT_DATE, mDateToday);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.saving_error), Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, getString(R.string.habit_saved_with_rowid) + newRowId,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to delete all habit data from the database.
     */
    private void purgeTableData() {
        // Closes the database connection
        mDbHelper.close();

        // Deletes the existing database
        this.deleteDatabase(mDbHelper.getDatabaseName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Display the table contents
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Purge Data" menu option
            case R.id.action_delete_all_entries:
                // Delete all habit data from the database
                purgeTableData();
                // Display the table contents
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

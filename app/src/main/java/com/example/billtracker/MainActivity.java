package com.example.billtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = null;
    private TextView chooseTxt;
    private EditText billInput;
    private EditText newCategoryNameInput;
    private Button billButton;
    private Button monthYearButton;
    private Button addCategoryButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button clearData;
    public static String categoryToRemove;
    public static String chosenCategory;
    public static String myYear;
    public static String myMonth;
    public static String myDay;

    // Store all bills in main object
    public static AllBills allBills = new AllBills();

    // Store all categories in array list
    ArrayList<String> categories = allBills.getAllCategories();

    // Show date dialog
    public void showDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                MainActivity.this,
                mDateSetListener,
                year, month, day);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        billInput = (EditText) findViewById(R.id.billInput);
        newCategoryNameInput = (EditText) findViewById(R.id.addCategory);
        billButton = (Button) findViewById(R.id.button);
        monthYearButton = (Button) findViewById(R.id.monthButton);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner removeCategorySpinner = (Spinner) findViewById(R.id.spinnerRemoveCategory);
        clearData = (Button) findViewById(R.id.clearData);
        addCategoryButton = (Button) findViewById(R.id.addCategoryButton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get data from shared preferences
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gsonObject = new Gson();
        String json = mPrefs.getString("MyBills", "");
        AllBills billsJson = gsonObject.fromJson(json, AllBills.class);
        if (billsJson != null) {
            allBills = billsJson;
            categories = allBills.getAllCategories();
        }
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        // Bottom nav menu
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.allBills:
                        startActivity(new Intent(getApplicationContext(), AllBillsListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.summary:
                        Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        // Date Selector
        monthYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                myYear = String.valueOf(year);
                myMonth = String.valueOf(month);
                myDay = String.valueOf(day);
            }
        };

        // Add default categories to the main object and to the spinner list
        final List<String> categoriesList = new ArrayList<String>();
        categoriesList.clear();
        categoriesList.add("Choose Category:");
        String defaultCategoryOne = "Food";
        String defaultCategoryTwo = "Transportation";
        categoriesList.add(defaultCategoryOne);
        categoriesList.add(defaultCategoryTwo);
        if (allBills.getAllCategories().size() < 2) {
            allBills.addCategory(defaultCategoryOne);
            allBills.addCategory(defaultCategoryTwo);
        }

        // Add all other categories to the spinner list
        for (int i = 0; i < categories.size(); i++) {
            if (!categoriesList.contains(categories.get(i))) {
                categoriesList.add(categories.get(i));
            }
        }

        // Spinner array adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        removeCategorySpinner.setAdapter(dataAdapter);

        // Choose category. Spinner item listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenCategory = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Remove category. Spinner item listener
        removeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryToRemove = removeCategorySpinner.getSelectedItem().toString();
                if (categoryToRemove != "Choose Category:" && categoryToRemove != defaultCategoryOne && categoryToRemove != defaultCategoryTwo) {
                    categoriesList.remove(categoryToRemove);
                    allBills.removeCategory(categoryToRemove);
                    Gson gson = new Gson();
                    String json = gson.toJson(allBills);
                    prefsEditor.putString("MyBills", json);
                    prefsEditor.commit();
                    adapterView.setSelection(0);
                    Toast.makeText(MainActivity.this, categoryToRemove + " category removed", Toast.LENGTH_SHORT).show();
                } else if (categoryToRemove == defaultCategoryOne || categoryToRemove == defaultCategoryTwo) {
                    Toast.makeText(MainActivity.this, "Cannot remove. Default category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Add new category
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(newCategoryNameInput == null) && newCategoryNameInput.length() != 0) {
                    String newCategoryName = newCategoryNameInput.getText().toString();
                    if (!categories.contains(newCategoryName)) {
                        allBills.addCategory(newCategoryName);
                        Gson gson = new Gson();
                        String json = gson.toJson(allBills);
                        prefsEditor.putString("MyBills", json);
                        prefsEditor.commit();
                        categoriesList.add(newCategoryName);
                        Toast.makeText(MainActivity.this, newCategoryName + " category added", Toast.LENGTH_SHORT).show();
                        newCategoryNameInput.getText().clear();
                    } else {
                        Toast.makeText(MainActivity.this, newCategoryName + " exists", Toast.LENGTH_SHORT).show();
                        newCategoryNameInput.getText().clear();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter the category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add bill button listener
        billButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!(billInput == null) && billInput.length() != 0) {
                    final String pickedDate = myMonth + "/" + myDay + "/" + myYear;
                    double setBill = Double.parseDouble(billInput.getText().toString());
                    if (setBill != 0 && chosenCategory != null && !chosenCategory.equals("Choose Category:") && myDay != null && myMonth != null && myYear != null) {
                        // Add new date if not found in allBills object
                        allBills.addNewDate(myYear, myMonth, myDay);
                        // Adds bill to new category if can't find chosen category for this specific date
                        if (!chosenCategory.equals("Choose Category:")) {
                            allBills.addBillToNewCategoryForDate(myYear, myMonth, myDay, chosenCategory);
                        }
                        // Adds bill to existing category for this specific date
                        allBills.addBillToExistingCategoryForDate(setBill, chosenCategory, myYear, myMonth, myDay);
                        // Rewrite bill object to shared preferences after new bill added
                        Gson gson = new Gson();
                        String json = gson.toJson(allBills);
                        prefsEditor.putString("MyBills", json);
                        prefsEditor.commit();
                        Toast.makeText(MainActivity.this, "Bill of $" + setBill + " added" + " to " + chosenCategory + " category. " + "Date: " + myMonth + "/" + myDay + "/" + myYear, Toast.LENGTH_LONG).show();
                        billInput.getText().clear();
                        spinner.setSelection(0);
                    }
                    // Notifications for user
                    if ((myDay == null || myMonth == null || myYear == null)) {
                        Toast.makeText(MainActivity.this, "Date is not selected. Please, select the date", Toast.LENGTH_SHORT).show();
                    } else if (chosenCategory == null || chosenCategory.equals("Choose Category:")) {
                        Toast.makeText(MainActivity.this, "Category is not selected. Please, select the category", Toast.LENGTH_SHORT).show();
                    } else if (setBill == 0) {
                        Toast.makeText(MainActivity.this, "Bill can't equal $0. Please, enter a valid value", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Bill field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Clear all app data
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrefs.edit().clear().apply();
                allBills = new AllBills();
                navigateUpTo(new Intent(MainActivity.this, MainActivity.class));
                startActivity(getIntent());
            }
        });
    }
}
package com.example.billtracker;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class SummaryActivity extends MainActivity {
    public static ArrayList<String> dates = new ArrayList<>();
    public AllBills bills = allBills;
    Calendar cal = Calendar.getInstance();
    public int currentYear = cal.get(Calendar.YEAR);
    public int currentMonth = cal.get(Calendar.MONTH);
    Button monthYear;
    TextView textField;

    // Returns sum of all bills based on category and date
    public double sumOfBills(String month, String year, String categoryName, AllBills bills) {
        ArrayList<Double> allBills = bills.getBillsByCategoryYearMonth(categoryName, year, month);
        double sum = 0;
        for (int i = 0; i < allBills.size(); i++) {
            sum += allBills.get(i);
        }
        return sum;
    }

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        monthYear = (Button) findViewById(R.id.monthYearB);
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        textField = (TextView) findViewById(R.id.textView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.summary);

        // Bottom nav menu
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.allBills:
                        startActivity(new Intent(getApplicationContext(), AllBillsListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.summary:
                        return true;
                }
                return false;
            }
        });

        // Date picker dialog
        dates.add(String.valueOf(currentMonth + 1));
        dates.add(String.valueOf(currentYear));
        monthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        Toast.makeText(SummaryActivity.this, month + "-" + year, Toast.LENGTH_SHORT).show();
                        dates.add(String.valueOf(month));
                        dates.add(String.valueOf(year));
                    }
                });
            }
        });

        // Pie chart
        ArrayList<PieEntry> myPieChart = new ArrayList<>();
        ArrayList<String> allCategories = categories;

        if (!dates.isEmpty()) {
            for (int i = 0; i < allCategories.size(); i++) {
                double sum = sumOfBills(String.valueOf(dates.get(0)), String.valueOf(dates.get(1)), allCategories.get(i), bills);
                if (sum != 0) {
                    myPieChart.add(new PieEntry((float) sum, categories.get(i)));
                }
            }
        }

        if (myPieChart.isEmpty()) {
            pieChart.setVisibility(View.INVISIBLE);
            textField.setText("No data available for " + String.valueOf(dates.get(0)) + "/" + dates.get(1) + ". Try the other date or add your bills");
        } else {
            PieDataSet pieDataSet = new PieDataSet(myPieChart, null);
            final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 0, 0), Color.rgb(255, 192, 0), Color.rgb(127, 127, 127),
                    Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189), Color.rgb(86, 108, 248), Color.rgb(215, 201, 38), Color.rgb(92, 228, 234)};
            pieDataSet.setColors(MY_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(12f);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setCenterText("Spending by category");
            pieChart.setCenterTextSize(20f);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(true);
            pieChart.getDescription().setText(String.valueOf(dates.get(0)) + "/" + String.valueOf(dates.get(1)));
            pieChart.getDescription().setTextSize(20f);
            pieChart.setDrawEntryLabels(false);
            pieChart.animate();
            pieChart.setVisibility(View.VISIBLE);

            // Pie chart legend
            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setWordWrapEnabled(true);
        }
        dates.clear();
    }
}
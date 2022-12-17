package com.example.billtracker;

import java.util.ArrayList;

public class AllBills {

    ArrayList<Dates> dates;
    ArrayList<String> allCategories;

    public AllBills() {
        this.dates = new ArrayList<>();
        this.allCategories = new ArrayList<>();
    }

    // Find specific date object based on year, month, day
    public Dates findDate(String year, String month, String day) {
        for (int i = 0; i < this.dates.size(); i++) {
            Dates checkedDate = this.dates.get(i);
            if (checkedDate.getYear().equals(year) && checkedDate.getMonth().equals(month) && checkedDate.getDay().equals(day)) {
                return checkedDate;
            }
        }
        return null;
    }

    // Add new date object
    public boolean addNewDate(String year, String month, String day) {
        if ((findDate(year, month, day)) == null) {
            this.dates.add(new Dates(year, month, day));
            return true;
        }
        return false;
    }

    // Add bill to new category for the date object
    public boolean addBillToNewCategoryForDate(String year, String month, String day, String categoryName) {
        Dates myDate = findDate(year, month, day);
        return myDate != null && myDate.addBillToNewCategory(categoryName);
    }

    // Find all date objects based on specific month and year
    public ArrayList<Dates> findDatesByMonthYear(String year, String month) {
        ArrayList<Dates> myDatesByMonthYear = new ArrayList<>();
        for (int i = 0; i < this.dates.size(); i++) {
            Dates date = this.dates.get(i);
            if (date.getYear().equals(year) && date.getMonth().equals(month)) {
                myDatesByMonthYear.add(date);
            }
        }
        return myDatesByMonthYear;
    }

    // Add bill to existing category for the date object
    public void addBillToExistingCategoryForDate(double billAmount, String category, String year, String month, String day) {
        Dates foundDate = findDate(year, month, day);
        if (foundDate != null) {
            foundDate.addBillToExistingCategory(billAmount, category);
        }
    }

    // Query all categories assigned to specific month and year. Return all bills for that category
    public ArrayList<Double> getBillsByCategoryYearMonth(String categoryName, String year, String month) {
        ArrayList<Double> bills = new ArrayList<>();
        ArrayList<Dates> myDates = findDatesByMonthYear(year, month);
        if (myDates != null) {
            for (int i = 0; i < myDates.size(); i++) {
                Dates date = myDates.get(i);
                if (date != null) {
                    ArrayList<Bill> billsByMonthYear = date.getBillObjects();
                    for (int j = 0; j < billsByMonthYear.size(); j++) {
                        Bill billObject = billsByMonthYear.get(j);
                        if (billObject.getCategoryName().equals(categoryName)) {
                            ArrayList<Double> getBills = billObject.getBills();
                            bills.addAll(getBills);
                        }
                    }
                }
            }
            return bills;
        }
        return null;
    }

    // Get sum of all bills for specific category for all dates
    public double getSumOfBillsForAllDatesByCategory(String category) {
        ArrayList<Dates> dates = this.dates;
        double sum = 0;
        for (int i = 0; i < dates.size(); i++) {
            Dates date = dates.get(i);
            double sumOfBillsForThisDate = date.getSumOfBillsByCategory(category);
            sum += sumOfBillsForThisDate;
        }
        return sum;
    }


    public ArrayList<String> getAllCategories() {
        return allCategories;
    }

    // Add new category
    public void addCategory(String categoryName) {
        if (categoryName != null) {
            this.allCategories.add(categoryName);
        }
    }

    // Remove category and all bills associated with this category
    public void removeCategory(String categoryName) {
        for (int i = 0; i < this.allCategories.size(); i++) {
            if (this.allCategories.get(i).equals(categoryName)) {
                this.allCategories.remove(i);
            }
        }
        ArrayList<Dates> dates = this.dates;
        for (int i = 0; i < dates.size(); i++) {
            Dates date = dates.get(i);
            if (date.findBillByCategory(categoryName) != null) {
                Bill foundBill = date.findBillByCategory(categoryName);
                date.deleteBill(foundBill);
            }
        }
}
}


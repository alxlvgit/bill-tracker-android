package com.example.billtracker;

import java.util.ArrayList;

public class Dates {
    public String year;
    public String day;
    ArrayList<Bill> billObjects;
    public String month;

    public Dates(String year, String month, String day) {
        this.month = month;
        this.year = year;
        this.day = day;
        this.billObjects = new ArrayList<Bill>();
    }

    public ArrayList<Bill> getBillObjects() {
        return billObjects;
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }


    // Add bill to new category
    public boolean addBillToNewCategory(String categoryName) {
        if (findBillByCategory(categoryName) == null) {
            this.billObjects.add(new Bill(categoryName));
            return true;
        }
        return false;
    }

    // Add bill to existing category
    public void addBillToExistingCategory(double billAmount, String category) {
        Bill foundCategory = findBillByCategory(category);
        if (foundCategory != null) {
            foundCategory.addBill(billAmount);
        }
    }

    // Find bill object based on category
    public Bill findBillByCategory(String category) {
        for (int i = 0; i < this.billObjects.size(); i++) {
            Bill checkedBill = this.billObjects.get(i);
            if (checkedBill.getCategoryName().equals(category)) {
                return checkedBill;
            }
        }
        return null;
    }

    // Get sum of all bills for specific category
    public double getSumOfBillsByCategory(String category) {
        for (int i = 0; i < this.billObjects.size(); i++) {
            Bill checkedCategory = this.billObjects.get(i);
            if (checkedCategory.getCategoryName().equals(category)) {
                ArrayList<Double> allBills = checkedCategory.getBills();
                double sum = 0;
                for (int j = 0; j < allBills.size(); j++) {
                    sum += allBills.get(j);
                }
                return sum;
            }
        }
        return 0;
    }

    // Remove bill
    public void deleteBill(Bill bill) {
        this.billObjects.remove(bill);
    }
}

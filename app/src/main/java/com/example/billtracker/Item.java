package com.example.billtracker;

public class Item {
    String bill;
    String category;
    int image;

    public Item(String bill, String category, int image) {
        this.bill = bill;
        this.category = category;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBill() {
        return bill;
    }

    public String getCategory() {
        return category;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

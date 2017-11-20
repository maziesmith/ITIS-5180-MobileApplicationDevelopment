package com.homework.mad.expenseapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Home Work 2
 * Sanket Patil
 * Atul Kumar Banwar
 * Expense.java
 */
public class Expense implements Serializable {
    private String name;
    private String category;
    private double amount;
    private Date date;
    private String receiptImageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReceipt() {
        return receiptImageUri;
    }

    public void setReceipt(String receipt) {
        this.receiptImageUri = receipt;
    }
}

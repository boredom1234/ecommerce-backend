package com.ecommerce.dto;

import java.util.Map;

public class OrderRequest {
    private double amount;
    private String currency;
    private String receipt;
    private Map<String, String> notes;
    
    public OrderRequest() {
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getReceipt() {
        return receipt;
    }
    
    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
    
    public Map<String, String> getNotes() {
        return notes;
    }
    
    public void setNotes(Map<String, String> notes) {
        this.notes = notes;
    }
} 
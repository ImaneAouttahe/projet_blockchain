package com.java;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private int fromUserId;
    private int toUserId;
    private double amount;
    private String signature;
    private String status; // "pending", "completed", "failed"
    private Date date;
    private String blockchainHash;

    public Transaction(int transactionId, int fromUserId, int toUserId, double amount, String signature, String status, Date date, String blockchainHash) {
        this.transactionId = transactionId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.signature = signature;
        this.status = status;
        this.date = date;
        this.blockchainHash = blockchainHash;
    }

    // Getters et Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    // Méthodes
    public void createTransaction() {
        // Logique pour créer une transaction
    }

    public void validateTransaction() {
        // Logique pour valider une transaction
    }

    public void getTransactionDetails() {
        // Logique pour obtenir les détails de la transaction
    }

    public String getData() {
        return "Transaction ID: " + transactionId +
                "\nFrom User ID: " + fromUserId +
                "\nTo User ID: " + toUserId +
                "\nAmount: " + amount +
                "\nStatus: " + status +
                "\nDate: " + date.toString() +
                "\nBlockchain Hash: " + blockchainHash;
    }
}

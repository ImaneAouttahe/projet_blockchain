package com.java;

import java.security.MessageDigest;
import java.util.*;
import java.util.List;


public class Block {
    private int index;
    private long timestamp;
    private ArrayList<String> transactions;
    private String previousHash;
    private String hash;

    public Block(String previousHash) {
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }
    public Block(){}
    // Getters et Setters
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getTransactions() {
        return transactions; // Retourner la liste des transactions
    }

    public String getPreviousHash() {
        return previousHash;
    }
    public void addTransaction(String transaction) {
        this.transactions.add(transaction);
        this.hash = calculateHash(); // Recalculer le hash quand une transaction est ajoutée
    }
    public String getHash() {
        return hash;
    }

    // Méthodes
    public String calculateHash() {
        StringBuilder input = new StringBuilder(previousHash + Long.toString(timestamp));
        for (String transaction : transactions) {
            input.append(transaction);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.toString().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


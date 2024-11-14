package com.java;


import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Validator {
    private int validatorId;
    private String ipAddress;
    private int port;
    private DatabaseManager dbManager;

    public Validator(int validatorId, String ipAddress, int port) {
        this.validatorId = validatorId;
        this.ipAddress = ipAddress;
        this.port = port;
        this.dbManager = new DatabaseManager();
    }

    // Méthode pour vérifier et valider une transaction
    public boolean validateTransaction(Transaction transaction, PublicKey senderPublicKey) {
        try {
            // Vérification de la signature
            boolean isSignatureValid = SecurityUtils.verifySignature(transaction.getSignature(), transaction.getData(), senderPublicKey);
            if (isSignatureValid) {
                // Calcul du hash de la transaction
                String transactionHash = SecurityUtils.computeHash(transaction);
                transaction.setBlockchainHash(transactionHash);

                // Mise à jour de l'état de la transaction
                dbManager.updateTransactionStatus(transaction.getTransactionId(), "Validated");
                System.out.println("Transaction validée par le validator " + validatorId);
                return true;
            } else {
                System.out.println("La signature de la transaction est invalide.");
                return false;
            }
        } catch (Exception e) {
            // Gestion des exceptions (signature invalide, problème avec le hash, etc.)
            System.out.println("Erreur lors de la validation de la transaction : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour vérifier le hash de la transaction (utilisé par les autres validators)
    public boolean verifyTransactionHash(Transaction transaction) {
        try {
            String computedHash = SecurityUtils.computeHash(transaction);
            return computedHash.equals(transaction.getBlockchainHash());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erreur lors du calcul du hash : " + e.getMessage());
            return false;
        }
    }

    // Getters et Setters pour l'id, l'IP et le port du validator
    public int getValidatorId() {
        return validatorId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}


package com.java;
import java.util.*;
public class User {
    private int userId;
    private String email;  // Utiliser l'email comme identifiant
    private String userName;
    private String password;
    private double balance;
    private String userType; // "client", "admin", "responsable_agence"
    private List<Transaction> transactionHistory;
    private UserKey userKey; // Instance de UserKey pour gérer les clés

    public User(int userId, String email, String userName, String password, double balance, String userType) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
        this.userType = userType;
        this.transactionHistory = new ArrayList<>(); // Initialisation de la liste
        this.userKey = new UserKey(userId); // Génération des clés lors de la création de l'utilisateur
    }


    public String getPublicKey() {
        return userKey.getPublicKey(); // Récupérer la clé publique depuis UserKey
    }

    public String getPrivateKey() {
        return userKey.getPrivateKey(); // Récupérer la clé privée depuis UserKey
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public boolean login(String inputEmail, String inputPassword) {
        return this.email.equals(inputEmail) && this.password.equals(inputPassword);
    }
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    // Méthode pour effectuer une transaction
    public boolean makeTransaction(User toUser, double amount, String signature, String blockchainHash) {
        if (amount <= 0) {
            System.out.println("Le montant de la transaction doit être positif.");
            return false;
        }

        if (this.balance < amount) {
            System.out.println("Solde insuffisant pour effectuer la transaction.");
            return false;
        }

        // Créer une nouvelle transaction
        Transaction transaction = new Transaction(
                generateTransactionId(),  // Méthode pour générer un ID unique
                this.userId,
                toUser.getUserId(),
                amount,
                signature,
                "pending",
                new Date(),
                blockchainHash
        );

        // Déduire le montant du solde de l'utilisateur émetteur
        this.balance -= amount;

        // Ajouter le montant au solde de l'utilisateur récepteur
        toUser.setBalance(toUser.getBalance() + amount);

        // Ajouter la transaction à l'historique des deux utilisateurs
        this.addTransaction(transaction);
        toUser.addTransaction(transaction);

        System.out.println("Transaction effectuée avec succès !");
        return true;
    }

    // Méthode pour générer un ID de transaction unique (par exemple)
    private int generateTransactionId() {
        return transactionHistory.size() + 1; // Simple exemple, peut être amélioré
    }
public boolean viewTransactions() {
    if (transactionHistory.isEmpty()) {
        System.out.println("Aucune transaction trouvée.");
        return false;
    } else {
        System.out.println("Historique des transactions pour l'utilisateur " + userName + ":");
        for (Transaction transaction : transactionHistory) {
            System.out.println("Transaction ID: " + transaction.getTransactionId() +
                    ", Montant: " + transaction.getAmount() +
                    ", Statut: " + transaction.getStatus() +
                    ", Date: " + transaction.getDate());
        }
        return true;
    }
}

}


package com.java;

public class AgencyManager extends User {
    private Agency agency;
    private DatabaseManager dbManager; // Instance de DatabaseManager

    public AgencyManager(int userId, String email, String userName, String password, double balance, String userType, Agency agency) {
        super(userId, email, userName, password, balance, userType);
        this.agency = agency;
        this.dbManager = new DatabaseManager(); // Initialisation de DatabaseManager
    }

    // Méthodes spécifiques aux responsables d'agence
    public void createUserAccount(User user) {
        // Use DatabaseManager to add a user
        if (dbManager.addUser(user)!=-1) {
            System.out.println("Compte utilisateur créé avec succès.");
        } else {
            System.out.println("Échec de la création du compte utilisateur.");
        }
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    // Méthode pour mettre à jour le solde d'un utilisateur
    public void updateUserBalance(User user, double amount) {
        double newBalance = user.getBalance() + amount; // Mettre à jour le solde
        dbManager.updateUserBalance(user.getUserId(), newBalance);
        user.setBalance(newBalance); // Mettre à jour le solde localement
    }

    public void viewAgencyDetails() {
        System.out.println("Détails de l'agence: ");
        System.out.println("Nom de l'agence: " + agency.getAgencyName());
        System.out.println("Localisation: " + agency.getLocation());
        System.out.println("Contact: " + agency.getContactNumber());
    }

    // Getters et Setters pour l'agence
    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }
}

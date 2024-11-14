package com.java;

public class UserView {

    // Méthode pour afficher le menu principal
    public void showMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Voir l'historique des transactions");
        System.out.println("2. Effectuer une transaction");
        System.out.println("3. Quitter");
        System.out.print("Choisissez une option: ");
    }

    // Méthode pour afficher un message
    public void showMessage(String message) {
        System.out.println(message);
    }

    // Méthode pour afficher un message d'erreur
    public void showErrorMessage(String message) {
        System.out.println("Erreur: " + message);
    }
}

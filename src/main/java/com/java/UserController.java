package com.java;

import java.util.Scanner;

public class UserController {
    private User user;  // L'utilisateur actuel
    private UserView userView;  // Instance de la vue
    private DatabaseManager dbManager;  // Instance pour gérer la base de données

    public UserController(User user, UserView userView) {
        this.user = user;
        this.userView = userView;
        this.dbManager = new DatabaseManager();  // Initialisation de DatabaseManager
    }

    // Méthode pour afficher le menu principal
    public void showMainMenu() {
        boolean running = true;
        while (running) {
            // Afficher le menu
            userView.showMenu();

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consommer la nouvelle ligne après la saisie

            switch (choice){
                case 1:
                    viewTransactionHistory();
                    break;
                case 2:
                    makeTransaction();
                    break;
                case 3:
                    System.out.println("Au revoir !");
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }

    // Méthode pour gérer la connexion de l'utilisateur
    private void loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre email: ");
        String inputEmail = scanner.nextLine();
        System.out.print("Entrez votre mot de passe: ");
        String inputPassword = scanner.nextLine();

        if (user.login(inputEmail, inputPassword)) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Email ou mot de passe incorrect.");
        }
    }

    // Méthode pour afficher l'historique des transactions
    private void viewTransactionHistory() {
        if (!user.viewTransactions()) {
            userView.showErrorMessage("Aucune transaction trouvée.");
        }
    }

    // Méthode pour effectuer une transaction
    private void makeTransaction() {
        Scanner scanner = new Scanner(System.in);

        // Demander à l'utilisateur le destinataire et le montant
        System.out.print("Entrez l'ID de l'utilisateur destinataire: ");
        int toUserId = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez le montant de la transaction: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Entrez la signature de la transaction: ");
        String signature = scanner.nextLine();

        System.out.print("Entrez le hash de la blockchain: ");
        String blockchainHash = scanner.nextLine();

        // Récupérer l'utilisateur destinataire à partir de la base de données
        User toUser = dbManager.getUserById(toUserId);

        if (toUser != null) {
            boolean success = user.makeTransaction(toUser, amount, signature, blockchainHash);
            if (success) {
                userView.showMessage("Transaction réussie !");
            } else {
                userView.showErrorMessage("Échec de la transaction.");
            }
        } else {
            userView.showErrorMessage("Utilisateur destinataire non trouvé.");
        }
    }
}

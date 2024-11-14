package com.java;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LoginController {
    private LoginFrame loginFrame;
    private DatabaseManager dbManager;

    public LoginController(DatabaseManager dbManager, LoginFrame loginFrame) {
        this.dbManager = dbManager;
        this.loginFrame = loginFrame;  // Utilisation de l'instance existante de LoginFrame
    }

    // Méthode appelée lors du clic sur le bouton "Login"
    public void login(Stage primaryStage,String email, String password) {
        // Vérifier les informations d'identification de l'utilisateur dans la base de données
        User user = dbManager.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // L'utilisateur est authentifié avec succès
            loginFrame.displayMessage("Connexion réussie.");

            // Rediriger vers le menu principal en fonction du type d'utilisateur
            switch (user.getUserType()) {
                case "admin":
                    // L'utilisateur est un admin
                    Admin admin = new Admin(user.getUserId(), user.getUserName(), user.getEmail(), dbManager, new ArrayList<>());
                    AdminView adminView = new AdminView(primaryStage);  // Passer le Stage à AdminView
                    AdminController adminController = new AdminController(admin, adminView, dbManager);
                    // Afficher le menu de l'admin
                    break;

                case "responsable_agence":
                    // L'utilisateur est un responsable d'agence
                    Agency agency = dbManager.getAgencyByManagerId(user.getUserId());
                    AgencyManager agencyManager = new AgencyManager(user.getUserId(), user.getEmail(), user.getUserName(), user.getPassword(), user.getBalance(), "responsable_agence", agency);
                    AgencyManagerView agencyManagerView = new AgencyManagerView(new AgencyManagerController(agencyManager, null));
                    agencyManagerView.start(primaryStage); // Afficher le menu du responsable d'agence
                    break;

                case "client":
                    // L'utilisateur est un client
                    UserController userController = new UserController(user, new UserView());
                    userController.showMainMenu();  // Afficher le menu du client
                    break;

                default:
                    // Si l'utilisateur n'est pas reconnu
                    loginFrame.displayMessage("Type d'utilisateur inconnu.");
                    break;
            }
        } else {
            // Échec de l'authentification
            loginFrame.displayMessage("Email ou mot de passe incorrect.");
        }
    }






    // Méthode pour afficher la fenêtre de connexion
    public void showLoginFrame() {
        loginFrame.show();
    }

    // Méthode pour fermer la fenêtre de connexion
    public void closeLoginFrame() {
        loginFrame.close();
    }
}

package com.java;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    private DatabaseManager dbManager;  // Gestion de la base de données
    private LoginController loginController;  // Contrôleur de la connexion

    public static void main(String[] args) {
        launch(args);  // Lance l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialisation des composants
        dbManager = new DatabaseManager();  // Crée une instance de DatabaseManager
        LoginFrame loginFrame = new LoginFrame(primaryStage);  // Crée une instance de LoginFrame
        loginController = new LoginController(dbManager, loginFrame);  // Passe l'instance de LoginFrame au contrôleur

        // Passe la référence de la fenêtre de connexion au contrôleur
        loginFrame.setController(loginController);

        // Affiche la fenêtre de login
        loginFrame.show();
    }
}

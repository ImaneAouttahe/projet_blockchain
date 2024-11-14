package com.java;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginFrame {
    private LoginController loginController;
    private Stage stage;

    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginFrame(Stage primaryStage) {
        // Initialisation des composants de l'interface
        emailField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");

        // Action sur le bouton de login
        loginButton.setOnAction(e -> login(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Email :"), emailField,
                new Label("Mot de Passe :"), passwordField,
                loginButton
        );

        Scene scene = new Scene(layout, 300, 200);

        // Initialisation du Stage et définition de la scène
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login");
    }

    // Méthode pour connecter le contrôleur
    public void setController(LoginController controller) {
        this.loginController = controller;
    }

    // Méthode pour effectuer la connexion
    private void login(Stage primaryStage) {
        String email = emailField.getText();
        String password = passwordField.getText();
        loginController.login(primaryStage, email, password);  // Passez primaryStage
    }


    // Afficher un message à l'utilisateur
    public void displayMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher la fenêtre de login
    public void show() {
        stage.show();
    }

    // Méthode pour fermer la fenêtre
    public void close() {
        stage.close();
    }
}

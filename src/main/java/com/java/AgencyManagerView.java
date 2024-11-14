package com.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AgencyManagerView extends Application {

    private AgencyManagerController controller;
    private TextField userIdField, emailField, userNameField, passwordField, balanceField, userTypeField;
    private TextArea agencyDetailsArea;
    private Button createAccountButton, updateBalanceButton, viewDetailsButton, quitButton;

    public AgencyManagerView(AgencyManagerController controller) {
        this.controller = controller;
    }

    public void setController(AgencyManagerController controller) {
        this.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) {

        // Initialiser les champs de saisie
        userIdField = new TextField();
        emailField = new TextField();
        userNameField = new TextField();
        passwordField = new TextField();
        balanceField = new TextField();
        userTypeField = new TextField();

        // Initialiser la zone de texte pour afficher les détails de l'agence
        agencyDetailsArea = new TextArea();
        agencyDetailsArea.setEditable(false);

        // Initialiser les boutons
        createAccountButton = new Button("Créer un compte utilisateur");
        updateBalanceButton = new Button("Mettre à jour le solde");
        viewDetailsButton = new Button("Voir les détails de l'agence");
        quitButton = new Button("Quitter");

        // Actions des boutons
        createAccountButton.setOnAction(e -> controller.createUserAccount());
        updateBalanceButton.setOnAction(e -> controller.updateUserBalance());
        viewDetailsButton.setOnAction(e -> controller.showAgencyDetails());
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Entrez les détails de l'utilisateur :"),
                new HBox(10, new Label("ID Utilisateur :"), userIdField),
                new HBox(10, new Label("Email :"), emailField),
                new HBox(10, new Label("Nom Utilisateur :"), userNameField),
                new HBox(10, new Label("Mot de Passe :"), passwordField),
                new HBox(10, new Label("Solde initial :"), balanceField),
                new HBox(10, new Label("Type Utilisateur :"), userTypeField),
                createAccountButton,
                updateBalanceButton,
                viewDetailsButton,
                agencyDetailsArea,
                quitButton
        );

        // Scene
        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Responsable d'Agence");
        primaryStage.show();
    }

    // Méthode pour obtenir les détails de l'utilisateur à créer
    public User getUserDetailsForAccountCreation() {
        int userId = Integer.parseInt(userIdField.getText());
        String email = emailField.getText();
        String userName = userNameField.getText();
        String password = passwordField.getText();
        double balance = Double.parseDouble(balanceField.getText());
        String userType = userTypeField.getText();

        return new User(userId, email, userName, password, balance, userType);
    }

    // Méthode pour obtenir le montant pour mettre à jour le solde
    public double getAmountForBalanceUpdate() {
        return Double.parseDouble(balanceField.getText());
    }

    // Méthode pour obtenir l'ID de l'utilisateur dont on veut mettre à jour le solde
    public int getUserIdForBalanceUpdate() {
        return Integer.parseInt(userIdField.getText());
    }

    // Afficher les détails de l'agence
    public void showAgencyDetails(String agencyName, String location, String contactNumber) {
        agencyDetailsArea.setText("Nom de l'agence: " + agencyName + "\n" +
                "Localisation: " + location + "\n" +
                "Contact: " + contactNumber);
    }

    public static void showMenu() {
        // Code pour afficher ou gérer l'affichage du menu
        System.out.println("Menu Responsable d'Agence affiché !");
    }
}

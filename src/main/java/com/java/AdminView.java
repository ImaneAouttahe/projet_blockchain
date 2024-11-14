package com.java;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminView {
    private TextField emailField, nameField, passwordField, agencyNameField, agencyLocationField, agencyContactField;
    private TextField userIdDeleteField, agencyIdDeleteField;
    private TableView<Transaction> transactionTable;
    private Button addUserButton, deleteUserButton, addAgencyButton, deleteAgencyButton, viewAllTransactionsButton, sendToValidatorsButton;
    private Stage primaryStage;
    private TextField agencyIdField, managerIdField,managerIpField, managerPortField;
    private Button assignManagerButton;

    public AdminView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeUI();
    }


    private void initializeUI() {
        primaryStage.setTitle("Admin Dashboard");

        // Initialisation des champs de texte et des boutons avec des titres explicites
        emailField = new TextField();
        emailField.setPromptText("Email");

        nameField = new TextField();
        nameField.setPromptText("Nom");

        passwordField = new TextField();
        passwordField.setPromptText("Mot de passe");

        managerIpField = new TextField();  // Champ pour l'adresse IP
        managerIpField.setPromptText("Adresse IP du Responsable");

        managerPortField = new TextField();  // Champ pour le port
        managerPortField.setPromptText("Port du Responsable");

        agencyNameField = new TextField();
        agencyNameField.setPromptText("Nom de l'agence");

        agencyLocationField = new TextField();
        agencyLocationField.setPromptText("Localisation de l'agence");

        agencyContactField = new TextField();
        agencyContactField.setPromptText("Contact de l'agence");

        userIdDeleteField = new TextField();
        userIdDeleteField.setPromptText("ID Utilisateur à supprimer");

        agencyIdDeleteField = new TextField();
        agencyIdDeleteField.setPromptText("ID Agence à supprimer");

        agencyIdField = new TextField();
        agencyIdField.setPromptText("ID Agence");

        managerIdField = new TextField();
        managerIdField.setPromptText("ID Responsable");

        addUserButton = new Button("Ajouter Utilisateur");
        deleteUserButton = new Button("Supprimer Utilisateur");
        addAgencyButton = new Button("Ajouter Agence");
        deleteAgencyButton = new Button("Supprimer Agence");
        assignManagerButton = new Button("Affecter Responsable");
        viewAllTransactionsButton = new Button("Voir Toutes les Transactions");
        sendToValidatorsButton = new Button("Envoyer aux Validateurs");

        transactionTable = new TableView<>();

        // Sections pour la gestion des utilisateurs
        VBox userBox = new VBox(10);
        userBox.getChildren().addAll(
                new Label("Gestion des Utilisateurs"),
                emailField,
                nameField,
                passwordField,
                managerIpField,  // Nouveau champ pour l'adresse IP
                managerPortField,
                addUserButton,
                new Label("Supprimer Utilisateur"),
                userIdDeleteField,
                deleteUserButton
        );

        // Sections pour la gestion des agences
        VBox agencyBox = new VBox(10);
        agencyBox.getChildren().addAll(
                new Label("Gestion des Agences"),
                agencyNameField,
                agencyLocationField,
                agencyContactField,
                addAgencyButton,
                new Label("Supprimer Agence"),
                agencyIdDeleteField,
                deleteAgencyButton
        );

        // Section pour affecter un responsable à une agence
        VBox assignManagerBox = new VBox(10);
        assignManagerBox.getChildren().addAll(
                new Label("Affecter Responsable à l'Agence"),
                agencyIdField,
                managerIdField,
                assignManagerButton
        );

        // Section pour les transactions
        VBox transactionBox = new VBox(10);
        transactionBox.getChildren().addAll(
                new Label("Transactions"),
                transactionTable,
                viewAllTransactionsButton,
                sendToValidatorsButton
        );

        // Layout principal
        HBox mainLayout = new HBox(20, userBox, agencyBox, assignManagerBox, transactionBox); // Ajout de assignManagerBox
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Méthodes pour récupérer les valeurs saisies
    public String getUserEmail() {
        return emailField.getText();
    }

    public int getAgencyIdToAssignManager() {
        try {
            return Integer.parseInt(agencyIdField.getText());
        } catch (NumberFormatException e) {
            return -1; // Si l'entrée n'est pas valide
        }
    }

    public int getManagerIdToAssign() {
        try {
            return Integer.parseInt(managerIdField.getText());
        } catch (NumberFormatException e) {
            return -1; // Si l'entrée n'est pas valide
        }
    }

    public void setAssignManagerAction(Runnable action) {
        assignManagerButton.setOnAction(e -> action.run());
    }

    public String getUserName() {
        return nameField.getText();
    }
    public String getIp() {
        return managerIpField.getText();
    }

    public int getPort() {
        return Integer.parseInt(managerPortField.getText());
    }

    public String getUserPassword() {
        return passwordField.getText();
    }

    public int getUserIdToDelete() {
        return Integer.parseInt(userIdDeleteField.getText());
    }

    public String getAgencyName() {
        return agencyNameField.getText();
    }

    public String getAgencyLocation() {
        return agencyLocationField.getText();
    }

    public String getAgencyContactNumber() {
        return agencyContactField.getText();
    }

    public int getAgencyIdToDelete() {
        return Integer.parseInt(agencyIdDeleteField.getText());
    }

    // Méthodes pour définir les actions des boutons
    public void setAddUserAction(Runnable action) {
        addUserButton.setOnAction(e -> action.run());
    }

    public void setDeleteUserAction(Runnable action) {
        deleteUserButton.setOnAction(e -> action.run());
    }

    public void setAddAgencyAction(Runnable action) {
        addAgencyButton.setOnAction(e -> action.run());
    }

    public void setDeleteAgencyAction(Runnable action) {
        deleteAgencyButton.setOnAction(e -> action.run());
    }

    public void setViewAllTransactionsAction(Runnable action) {
        viewAllTransactionsButton.setOnAction(e -> action.run());
    }

    public void setSendTransactionToValidatorsAction(Runnable action) {
        sendToValidatorsButton.setOnAction(e -> action.run());
    }

    public Transaction getSelectedTransaction() {
        return transactionTable.getSelectionModel().getSelectedItem();
    }

    public void showTransactions(ObservableList<Transaction> transactions) {
        transactionTable.setItems(transactions);
    }

    public void showAdminMenu() {
        // Affiche un panneau avec les actions de l'administrateur
        VBox adminMenuBox = new VBox(10);
        adminMenuBox.getChildren().addAll(
                new Label("Menu Administrateur"),
                new Button("Ajouter Utilisateur"),
                new Button("Supprimer Utilisateur"),
                new Button("Ajouter Agence"),
                new Button("Supprimer Agence"),
                new Button("Voir Transactions")
        );

        Scene adminMenuScene = new Scene(adminMenuBox, 400, 300);
        Stage menuStage = new Stage();
        menuStage.setTitle("Menu Administrateur");
        menuStage.setScene(adminMenuScene);
        menuStage.show();
    }

    // Méthode pour afficher une alerte
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

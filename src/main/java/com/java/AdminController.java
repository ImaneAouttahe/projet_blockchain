package com.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static java.sql.Types.NULL;

public class AdminController {
    private Admin admin;
    private AdminView view;
    private DatabaseManager dbManager;

    public AdminController(Admin admin, AdminView view, DatabaseManager dbManager) {
        this.admin = admin;
        this.view = view;
        this.dbManager = dbManager;

        initialize();
    }

    private void initialize() {
        view.setAddUserAction(this::addUser);
        view.setDeleteUserAction(this::deleteUser);
        view.setAddAgencyAction(this::addAgency);
        view.setDeleteAgencyAction(this::deleteAgency);
        view.setViewAllTransactionsAction(this::viewAllTransactions);
        view.setSendTransactionToValidatorsAction(this::sendTransactionToValidators);
        view.setAssignManagerAction(this::assignAgencyManager);
    }
    private void assignAgencyManager() {
        int agencyId = view.getAgencyIdToAssignManager();
        int managerId = view.getManagerIdToAssign();

        if (agencyId <= 0 || managerId <= 0) {
            view.showAlert("Erreur", "Veuillez fournir un ID d'agence et un ID responsable valides.");
            return;
        }

        admin.assignAgencyManager(agencyId, managerId);
        view.showAlert("Succès", "Responsable affecté à l'agence avec succès.");
    }
    private void addUser() {
        String email = view.getUserEmail();
        String name = view.getUserName();
        String password = view.getUserPassword();
        String Ip=view.getIp();
        int port = view.getPort();

        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs pour ajouter un utilisateur.");
            return;
        }

        User newUser = new User(0, email, name, password, 0, "responsable_agence");

        int userId = admin.addUser(newUser);
        Validator V=new Validator(userId ,Ip , port  );
        admin.addValidator(V);
        showAlert("Succès", "Utilisateur ajouté avec succès.");
    }

    private void deleteUser() {
        int userId = view.getUserIdToDelete();
        admin.deleteUser(userId);
        showAlert("Succès", "Utilisateur supprimé avec succès.");
    }

    private void addAgency() {
        String agencyName = view.getAgencyName();
        String location = view.getAgencyLocation();
        String contactNumber = view.getAgencyContactNumber();

        if (agencyName.isEmpty() || location.isEmpty() || contactNumber.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs pour ajouter une agence.");
            return;
        }

        Agency newAgency = new Agency(0, agencyName, location, contactNumber , NULL);
        admin.addAgency(newAgency);
        showAlert("Succès", "Agence ajoutée avec succès.");
    }

    private void deleteAgency() {
        int agencyId = view.getAgencyIdToDelete();
        admin.deleteAgency(agencyId);
        showAlert("Succès", "Agence supprimée avec succès.");
    }

    private void viewAllTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(dbManager.getAllTransactions());
        view.showTransactions(transactions);
    }

    private void sendTransactionToValidators() {
        Transaction transaction = view.getSelectedTransaction();
        if (transaction == null) {
            showAlert("Erreur", "Veuillez sélectionner une transaction pour la valider.");
            return;
        }

        admin.sendTransactionToValidators(transaction);
        showAlert("Succès", "Transaction envoyée aux validateurs.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

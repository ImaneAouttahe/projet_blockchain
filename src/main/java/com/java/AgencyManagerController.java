package com.java;

public class AgencyManagerController {
    private AgencyManager agencyManager;
    private AgencyManagerView agencyManagerView;

    public AgencyManagerController(AgencyManager agencyManager, AgencyManagerView agencyManagerView) {
        this.agencyManager = agencyManager;
        this.agencyManagerView = agencyManagerView;
    }

    // Créer un compte utilisateur via la vue JavaFX
    public void createUserAccount() {
        User user = agencyManagerView.getUserDetailsForAccountCreation();
        agencyManager.createUserAccount(user);
    }

    // Mettre à jour le solde d'un utilisateur via la vue JavaFX
    public void updateUserBalance() {
        int userId = agencyManagerView.getUserIdForBalanceUpdate();
        User user = agencyManager.getDbManager().getUserById(userId);

        if (user != null) {
            double amount = agencyManagerView.getAmountForBalanceUpdate();
            agencyManager.updateUserBalance(user, amount);
        } else {
            // Gestion des erreurs à l'interface utilisateur si utilisateur non trouvé
            System.out.println("Utilisateur non trouvé");
        }
    }

    // Afficher les détails de l'agence via la vue JavaFX
    public void showAgencyDetails() {
        agencyManagerView.showAgencyDetails(
                agencyManager.getAgency().getAgencyName(),
                agencyManager.getAgency().getLocation(),
                agencyManager.getAgency().getContactNumber()
        );
    }
    public void showMainMenu() {
        // Afficher la vue de l'administrateur
        AgencyManagerView.showMenu();
    }
}

package com.java;

import java.util.*;
import java.io.*;
import java.net.Socket;
import com.google.gson.Gson;

import static java.sql.Types.NULL;

public class Admin extends User {
    private List<Agency> agencies;
    private DatabaseManager dbManager;
    private List<Validator> validators;
    private Gson gson;  // Ajout de l'instance Gson

    public Admin(int adminId, String adminName, String adminEmail, DatabaseManager dbManager, List<Validator> validators) {
        super(adminId, adminEmail, adminName, "GI2", 0, "admin");
        this.agencies = new ArrayList<>();
        this.dbManager = dbManager;
        this.validators = validators; // Initialisation de la liste des validateurs
        this.gson = new Gson();  // Initialisation de Gson
    }
    public int addUser(User user) {
        int Id=dbManager.addUser(user);
        return Id;
    }
    public void addValidator(Validator validator) {
        dbManager.addValidator(validator);
    }

    public void deleteUser(int userId) {
        dbManager.deleteUser(userId);
    }
    public void assignAgencyManager(int agencyId, int managerId) {
        // Recherche de l'agence avec l'ID donné dans la base de données
        Agency agency = dbManager.getAgencyById(agencyId);
        if (agency != null) {
            // Vérification de l'existence du responsable dans la base de données
            User user = dbManager.getUserById(managerId);
            if (user != null && user.getUserType().equals("responsable_agence")) {
                agency.setManagerId(managerId);  // Affectation du responsable
                dbManager.updateAgency(agency);  // Mise à jour dans la base de données
                System.out.println("Responsable affecté à l'agence avec succès.");
            } else {
                System.out.println("Utilisateur non trouvé ou n'est pas un responsable d'agence.");
            }
        } else {
            System.out.println("Agence introuvable.");
        }
    }
    public void addAgency(Agency agency) {
        dbManager.addAgency(agency);
    }

    public void deleteAgency(int agencyId) {
        dbManager.deleteAgency(agencyId);
    }

    public void viewAllTransactions() {
        List<Transaction> transactions = dbManager.getAllTransactions();
        for (Transaction transaction : transactions) {
            System.out.println(transaction); // Ou afficher via l'interface graphique
        }
    }

    public void manageUsers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Options de gestion des utilisateurs : (1) Ajouter un responsable_agence, (2) Supprimer un responsable_agence");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Entrez l'email de responsable_agence : ");
            String email = scanner.nextLine();
            System.out.println("Entrez le nom de responsable_agence : ");
            String name = scanner.nextLine();
            System.out.println("Entrez le mot de passe : ");
            String password = scanner.nextLine();

            User newUser = new User(0, email, name, password, 0, "responsable_agence");
            dbManager.addUser(newUser);
            System.out.println("Utilisateur ajouté avec succès.");
        } else if (choice == 2) {
            System.out.println("Entrez l'ID de l'utilisateur à supprimer : ");
            int userId = scanner.nextInt();
            dbManager.deleteUser(userId);
            System.out.println("Utilisateur supprimé avec succès.");
        }
    }

public void manageAgencies() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Options de gestion des agences : (1) Ajouter une agence, (2) Supprimer une agence");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            addAgency();
        } else if (choice == 2) {
            System.out.println("Entrez l'ID de l'agence à supprimer : ");
            int agencyId = scanner.nextInt();
            removeAgency(agencyId);
        }
    }

    private void addAgency() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez le nom de l'agence : ");
        String agencyName = scanner.nextLine();

        System.out.println("Entrez l'emplacement de l'agence : ");
        String location = scanner.nextLine();

        System.out.println("Entrez le numéro de contact : ");
        String contactNumber = scanner.nextLine();

        Agency agency = new Agency(agencies.size() + 1, agencyName, location, contactNumber , NULL);
        agencies.add(agency);
        dbManager.addAgency(agency);
        System.out.println("Agence ajoutée avec succès.");
    }

    private void removeAgency(int agencyId) {
        Agency agencyToRemove = null;
        for (Agency agency : agencies) {
            if (agency.getAgencyId() == agencyId) {
                agencyToRemove = agency;
                break;
            }
        }

        if (agencyToRemove != null) {
            agencies.remove(agencyToRemove);
            dbManager.deleteAgency(agencyId);
            System.out.println("Agence supprimée avec succès.");
        } else {
            System.out.println("Agence introuvable.");
        }
    }
    // Méthode auxiliaire pour envoyer une transaction à un validator
    public void sendTransactionToValidators(Transaction transaction) {
        String transactionJson = gson.toJson(transaction);
        boolean isValidated = false;
        String validatedHash = null;

        for (Validator validator : validators) {
            try (Socket socket = new Socket(validator.getIpAddress(), validator.getPort());
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(transactionJson);  // Envoyer la transaction

                if (!isValidated) {
                    // Le premier validator valide et retourne le hash
                    validatedHash = in.readLine(); // Suppose que le hash est retourné en réponse
                    System.out.println("Transaction validée par le validator " + validator.getIpAddress() +
                            " avec le hash : " + validatedHash);
                    isValidated = true;
                } else {
                    // Envoyer seulement le hash validé pour les autres validators
                    out.println(validatedHash);
                    System.out.println("Transaction envoyée pour vérification de hash au validator " +
                            validator.getIpAddress());
                }

            } catch (IOException e) {
                System.err.println("Erreur de communication avec le validator " + validator.getIpAddress());
                e.printStackTrace();
            }
        }
    }
}


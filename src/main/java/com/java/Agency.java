package com.java;

public class Agency {
    private int agencyId;
    private String agencyName;
    private String location;
    private String contactNumber;
    private int managerId;  // Champ ajouté pour l'ID du responsable

    public Agency(int agencyId, String agencyName, String location, String contactNumber, int managerId) {
        this.agencyId = agencyId;
        this.agencyName = agencyName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.managerId = managerId;  // Initialisation du responsable
    }

    // Getters et Setters
    public int getAgencyId() {
        return agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getLocation() {
        return location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    // Méthode pour afficher les détails de l'agence
    public void getAgencyDetails() {
        System.out.println("Détails de l'agence:");
        System.out.println("ID : " + agencyId);
        System.out.println("Nom : " + agencyName);
        System.out.println("Emplacement : " + location);
        System.out.println("Numéro de contact : " + contactNumber);
        System.out.println("ID du Responsable : " + managerId);
        System.out.println("hi bdlt");
    }
}

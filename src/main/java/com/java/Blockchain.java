package com.java;

import java.security.PublicKey;
import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> blockchain = new ArrayList<>();
    private static final int MAX_TRANSACTIONS_PER_BLOCK = 5; // Limite de transactions par bloc
    private ArrayList<String> currentTransactions = new ArrayList<>(); // Transactions en attente
    private DatabaseManager databaseManager; // Instance de DatabaseManager

    // Constructeur
    public Blockchain(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        // Ajouter le bloc initial (Genesis block)
        blockchain.add(new Block("0")); // Le hash du bloc initial est "0"
    }

    private boolean validateTransaction(String transaction) {
        try {
            // Décomposer la chaîne de transaction pour obtenir les détails
            String[] transactionParts = transaction.split(":");
            if (transactionParts.length < 4) {
                System.out.println("Format de transaction invalide.");
                return false;
            }

            String fromUserId = transactionParts[0];
            String toUserId = transactionParts[1];
            double amount = Double.parseDouble(transactionParts[2]);
            String signature = transactionParts[3];

            // Récupérer l'objet User de l'émetteur (fromUserId) à l'aide de DatabaseManager
            User fromUser = databaseManager.getUserById(Integer.parseInt(fromUserId));
            if (fromUser == null) {
                System.out.println("Utilisateur émetteur introuvable.");
                return false;
            }

            // Vérifier que l'utilisateur dispose d'un solde suffisant
            if (fromUser.getBalance() < amount) {
                System.out.println("Solde insuffisant pour effectuer la transaction.");
                return false;
            }

            // Obtenir la clé publique de l'utilisateur pour vérifier la signature
            PublicKey publicKey = SecurityUtils.decodePublicKey(fromUser.getPublicKey()); // Supposons que l'utilisateur a une clé publique encodée en Base64

            // Vérifier la signature
            boolean isSignatureValid = SecurityUtils.verifySignature(
                    fromUserId + ":" + toUserId + ":" + amount, // Données originales (sans signature)
                    signature,
                    publicKey
            );

            if (!isSignatureValid) {
                System.out.println("Signature invalide.");
                return false;
            }

            return true; // La transaction est valide
        } catch (Exception e) {
            e.printStackTrace();
            return false; // La transaction est invalide en cas d'exception
        }
    }

    // Ajouter une transaction à la liste en attente
    public boolean addTransaction(String transaction) {
        if (validateTransaction(transaction)) {
            currentTransactions.add(transaction);
            if (currentTransactions.size() >= MAX_TRANSACTIONS_PER_BLOCK) {
                createNewBlock();
            }
            return true;
        } else {
            System.out.println("Transaction invalide et rejetée.");
            return false;
        }
    }

    // Créer un nouveau bloc avec les transactions en attente
    private void createNewBlock() {
        Block previousBlock = blockchain.get(blockchain.size() - 1);
        Block newBlock = new Block(previousBlock.getHash());

        // Ajouter toutes les transactions en attente au nouveau bloc
        for (String transaction : currentTransactions) {
            newBlock.addTransaction(transaction);
        }

        // Vider la liste des transactions après création du bloc
        currentTransactions.clear();
        blockchain.add(newBlock);
    }

    // Ajouter un bloc manuellement
    public void addBlock(ArrayList<String> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction à ajouter.");
            return;
        }

        Block previousBlock = blockchain.get(blockchain.size() - 1);
        Block newBlock = new Block(previousBlock.getHash());

        for (String transaction : transactions) {
            newBlock.addTransaction(transaction);
        }

        blockchain.add(newBlock);
    }

    public boolean isBlockchainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Block hash not equal");
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Previous Block hash not equal");
                return false;
            }
        }
        return true;
    }

    public void printBlockchain() {
        for (Block block : blockchain) {
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Transactions: " + block.getTransactions());
            System.out.println();
        }
    }
}

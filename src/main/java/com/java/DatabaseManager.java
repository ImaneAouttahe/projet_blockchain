package com.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://192.168.1.45:3306/java"; // Changez le nom de la base de données si nécessaire
    private static final String USER = "admin"; // Changez l'utilisateur si nécessaire
    private static final String PASSWORD = "2004"; // Changez le mot de passe si nécessaire

    private Connection connection;

    // Constructeur
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données réussie.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthodes CRUD pour User

    public int addUser(User user) {
        String sql = "INSERT INTO users (email, user_name, password, balance, user_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getPassword());
            statement.setDouble(4, user.getBalance());
            statement.setString(5, user.getUserType());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);  // Récupère l'ID généré
                        System.out.println("Utilisateur ajouté avec succès, ID : " + userId);
                        return userId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Retourne -1 en cas d'erreur
    }

    public boolean addValidator(Validator validator) {
        String sql = "INSERT INTO validators (id_respo ,ip_address, port) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, validator.getValidatorId());
            statement.setString(2, validator.getIpAddress());
            statement.setInt(3, validator.getPort());
            statement.executeUpdate();
            System.out.println("validator ajouté avec succès.");
            return true ;
        } catch (SQLException e) {
            e.printStackTrace();
            return false ;
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("user_type")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public void updateAgency(Agency agency) {
        String updateQuery = "UPDATE agencies SET manager_id = ? WHERE agency_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            // Définir les paramètres de la requête SQL
            preparedStatement.setInt(1, agency.getManagerId());  // Nouveau manager_id
            preparedStatement.setInt(2, agency.getAgencyId());   // ID de l'agence à mettre à jour

            // Exécuter la requête
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("L'agence a été mise à jour avec succès.");
            } else {
                System.out.println("Aucune agence trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'agence : " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userType = resultSet.getString("user_type");
                if ("admin".equals(userType)) {
                    // Récupère la liste des validateurs
                    List<Validator> validators = getAllValidators(); // Utilisation de la méthode ajoutée

                    return new Admin(
                            resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            email,
                            this, // Passez la référence au DatabaseManager
                            validators // Passe la liste des validateurs
                    );
                } else {
                    return new User(
                            resultSet.getInt("user_id"),
                            email,
                            resultSet.getString("user_name"),
                            resultSet.getString("password"),
                            resultSet.getDouble("balance"),
                            userType
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("user_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si l'utilisateur n'est pas trouvé
    }
    public List<Validator> getAllValidators() {
        List<Validator> validators = new ArrayList<>();
        String sql = "SELECT * FROM validators"; // Ajuste selon la table de validateurs
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int validatorId = resultSet.getInt("id_respo");
                String ipAddress = resultSet.getString("ip_address");
                int port = resultSet.getInt("port");

                Validator validator = new Validator(validatorId, ipAddress, port);
                validators.add(validator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return validators;
    }

    public void updateUserBalance(int userId, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, userId);
            statement.executeUpdate();
            System.out.println("Solde mis à jour avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthodes pour gérer les transactions

    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (from_user_id, to_user_id, amount, signature, status, blockchain_hash) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getFromUserId());
            statement.setInt(2, transaction.getToUserId());
            statement.setDouble(3, transaction.getAmount());
            statement.setString(4, transaction.getSignature());
            statement.setString(5, transaction.getStatus());
            statement.setString(6, transaction.getBlockchainHash());
            statement.executeUpdate();
            System.out.println("Transaction ajoutée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getUserTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_user_id = ? OR to_user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("from_user_id"),
                        resultSet.getInt("to_user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("signature"),
                        resultSet.getString("status"),
                        resultSet.getDate("date"),
                        resultSet.getString("blockchain_hash")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


    public Boolean addAgency(Agency agency) {
        String sql = "INSERT INTO agencies (agency_name, location, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, agency.getAgencyName());
            statement.setString(2, agency.getLocation());
            statement.setString(3, agency.getContactNumber());
            statement.executeUpdate();
            System.out.println("Agence ajoutée avec succès.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false ;
        }
    }

    public void deleteAgency(int agencyId) {
        String sql = "DELETE FROM agencies WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, agencyId);
            statement.executeUpdate();
            System.out.println("Agence supprimée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Agency> getAllAgencies() {
        List<Agency> agencies = new ArrayList<>();
        String sql = "SELECT * FROM agencies";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Agency agency = new Agency(
                        resultSet.getInt("agency_id"),
                        resultSet.getString("agency_name"),
                        resultSet.getString("location"),
                        resultSet.getString("contact_number"),
                        NULL
                );
                agencies.add(agency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agencies;
    }
    public void assignManagerToAgency(int agencyId, int managerId) {
        String sql = "UPDATE agencies SET manager_id = ? WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, managerId);
            statement.setInt(2, agencyId);
            statement.executeUpdate();
            System.out.println("Responsable assigné à l'agence avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Agency getAgencyById(int agencyId) {
        String sql = "SELECT * FROM agencies WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, agencyId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Agency(
                        resultSet.getInt("agency_id"),
                        resultSet.getString("agency_name"),
                        resultSet.getString("location"),
                        resultSet.getString("contact_number"),
                        NULL
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Méthode de fermeture de connexion
    public List<Agency> getAgenciesManagedByUser(int managerId) {
        List<Agency> agencies = new ArrayList<>();
        String sql = "SELECT * FROM agencies WHERE manager_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, managerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Agency agency = new Agency(
                        resultSet.getInt("agency_id"),
                        resultSet.getString("agency_name"),
                        resultSet.getString("location"),
                        resultSet.getString("contact_number"),
                        NULL
                );
                agencies.add(agency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agencies;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean userExists(int userId) {
        String sql = "SELECT user_id FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<User> getUsersByType(String userType) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_type = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("user_type")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("from_user_id"),
                        resultSet.getInt("to_user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("signature"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("blockchain_hash")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    public void updateTransactionStatus(int transactionId, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, transactionId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Statut de la transaction mis à jour avec succès.");
            } else {
                System.out.println("Aucune transaction trouvée avec l'ID : " + transactionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Agency getAgencyByManagerId(int managerId) {
        String sql = "SELECT * FROM agencies WHERE manager_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, managerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Agency(
                        resultSet.getInt("agency_id"),
                        resultSet.getString("agency_name"),
                        resultSet.getString("location"),
                        resultSet.getString("contact_number"),
                        NULL
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucune agence n'est trouvée pour ce manager
    }


}

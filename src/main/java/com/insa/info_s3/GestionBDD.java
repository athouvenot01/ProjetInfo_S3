/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.insa.info_s3.Clients.Client;
import com.insa.info_s3.Commande.commande;
import com.insa.info_s3.Machines.Machine;
import com.insa.info_s3.Materiaux.materiaux;
import com.insa.info_s3.Operateurs.Operateur;
import com.insa.info_s3.Operations.Operation;
import com.insa.info_s3.PosteDeTravail.PosteDeTravaille;
import com.insa.info_s3.Produit.Produits;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 *
 * @author Amandine Tvt
 */
public class GestionBDD {
    
    
    private Connection conn;
    
    
    public GestionBDD(Connection conn) {
        this.conn = conn;
    }
    
    public static Connection connectGeneralMySQL(String host,
            int port, String database,
            String user, String pass)
            throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }
    
    
    public static Connection connectSurServeurM3() throws SQLException {
        return connectGeneralMySQL("92.222.25.165", 3306,
                "m3_lrosian01", "m3_lrosian01",
                "30ea71e6");
    }
    
    
    public static double PrixAchat(Connection con, int idProduit, int quantité) {
    double Prix = 0;
    int PrixMater = 0;
    double Duree = 0; // Utilisation d'un type primitif

    String sql = "SELECT * FROM produit WHERE id=?";

    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
        preparedStatement.setInt(1, idProduit);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                double Poids = resultSet.getDouble("poids");
                PrixMater = getPrixbyID(con, resultSet.getInt("idmateriaux"));
                Prix = Prix + Poids * PrixMater;

                Duree = GetDuree(con, idProduit);
                if (!Double.isNaN(Duree)) {
                    Prix = Prix + Duree * 0.20;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Gérez l'exception de manière appropriée dans votre application
    }
    Prix = Prix *quantité;
    return Prix;
}


    
    public static Double GetDuree (Connection con,int idProduit) throws SQLException{
    double Duree = 0;
    String sql ="SELECT idtype FROM operation WHERE id = ?";
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setInt(1, idProduit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 while (resultSet.next()) {
                 Duree=Duree + GetDureeOpe(con,resultSet.getInt("idtypes"));
                 }
            }
    
    
    return Duree;}
    }
    public static Double GetDureeOpe(Connection con, int idtype) throws SQLException {
    Double duree = null;
    String sql = "SELECT AVG(duree) as duree_totale FROM realise WHERE idType=?";
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
        preparedStatement.setInt(1, idtype);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                duree = resultSet.getDouble("duree_totale");
                if (resultSet.wasNull()) {
                    duree = null; // La valeur était NULL dans la base de données
                }
            }
        }
    }
    return duree;
}

    
    
    
    
    public static List<Client> GetClients (Connection con) throws SQLException{
        List<Client> Clients  = new ArrayList <>();
        String sql = "SELECT * FROM client";
        try  (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String prenom = resultSet.getString("prenom");
                    String nom = resultSet.getString("nom");
                    Client client = new Client(id,prenom,nom);
                    Clients.add(client);
                }
            }
        }
        return Clients;
    }
   
    public static List<materiaux> GetMateriaux(Connection con) throws SQLException {
    List<materiaux> Materiaux = new ArrayList<>();
    String sql = "SELECT * FROM materiaux";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String des = resultSet.getString("des");
            int prix = resultSet.getInt("prix");
            
            materiaux materiau = new materiaux(id, des, prix);
            Materiaux.add(materiau);
        }
    }
    return Materiaux;
}
   public static List<commande> GetCommande(Connection con, int idclient) throws SQLException {
    List<commande> commandes = new ArrayList<>();
    String sql = "SELECT id FROM commande WHERE idclient=?";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
        preparedStatement.setInt(1, idclient);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int idCommande = resultSet.getInt("id");
                
                commande Commande = new commande(idCommande, idclient,MontantCommande(con, idCommande));
                commandes.add(Commande);
            }
        }
    }
    return commandes;
}
    public static double MontantCommande (Connection con,int idCommande) throws SQLException{
    double montant=0;
    String sql = "SELECT * FROM achat WHERE id=?";
    
     try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setInt(1, idCommande);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                montant = montant +PrixAchat(con, resultSet.getInt("idproduit"), resultSet.getInt("quantité"));
                }
            }
     }
    return montant;
    }
   

      public static List<Operation> GetOperation(Connection con) throws SQLException {
    List<Operation> operation = new ArrayList<>();
    String sql = "SELECT * FROM typeoperation";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String des = resultSet.getString("des");
      
            Operation operations = new Operation(id, des);
            operation.add(operations);
        }
    }
    return operation;
}
    public static List<Operateur> GetOperateur(Connection con) throws SQLException {
    List<Operateur> operateur = new ArrayList<>();
    String sql = "SELECT * FROM operateur";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String prenom = resultSet.getString("prenom");
            String nom = resultSet.getString("nom");
            int etatoperateur = resultSet.getInt("etatoperateur");
            
            String statutOperateur = (etatoperateur == 1) ? "En poste" : "Au repos";
            
            Operateur operateurs = new Operateur(id, prenom,nom,statutOperateur);
            operateur.add(operateurs);
        }
    }
    return operateur;
}
public static List<PosteDeTravaille> GetPostedeTravail(Connection con) throws SQLException {
    List<PosteDeTravaille> Poste = new ArrayList<>();
    String sql = "SELECT * FROM postetravail";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int idmachine = resultSet.getInt("idmachine");
            int idoperateur = resultSet.getInt("idoperateur");
            PosteDeTravaille poste = new PosteDeTravaille(id, getRefMachinebyId(con, idmachine), getOperateurbyId(con, idoperateur));
            Poste.add(poste);
        }
    }
    return Poste;
}

   public static List<Machine> Getmachine(Connection con) throws SQLException {
    List<Machine> machines = new ArrayList<>();
    String sql = "SELECT * FROM machine";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String ref = resultSet.getString("ref");
            String des = resultSet.getString("des");
            int puissance = resultSet.getInt("puissance");
            int etatMachine = resultSet.getInt("etatmachine");
            
            String statutMachine = (etatMachine == 1) ? "Actif" : "Inactif";
            
            Machine machine = new Machine(id, ref, des, puissance, statutMachine);
            machines.add(machine);
        }
    }
    return machines;
}

    
    
    public static List<Produits> GetProduits (Connection con) throws SQLException{
        List<Produits> Produits  = new ArrayList <>();
        String sql = "SELECT * FROM produit";
        try  (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String ref = resultSet.getString("ref");
                    String des = resultSet.getString("des");
                    int materiaux = resultSet.getInt("idmateriaux");
                    int Poids = resultSet.getInt("poids");
                        
                    Produits Produit = new Produits(id,ref,des,getNomMateriauxById(con, materiaux),Poids);
                    Produits.add(Produit);
                }
            }
        }
        return Produits;
    }
    
    
    public static String getNomMateriauxById(Connection con, int idMateriaux) throws SQLException {
        String nomMateriaux = null;
        String sql = "SELECT des FROM materiaux WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMateriaux);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    nomMateriaux = resultSet.getString("des");
                }
            }
        }
        return nomMateriaux;
    }
     public static int getPrixbyID(Connection con, int idMateriaux) throws SQLException {
        int prixmateriaux = 0;
        String sql = "SELECT prix FROM materiaux WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMateriaux);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    prixmateriaux = resultSet.getInt("prix");
                }
            }
        }
        return prixmateriaux;
    }
    
     public static String getRefMachinebyId(Connection con, int idMachine) throws SQLException {
        String RefMachine = null;
        String sql = "SELECT ref FROM machine WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMachine);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RefMachine = resultSet.getString("ref");
                }
            }
        }
        return RefMachine;
    }
     
     public static String getOperateurbyId(Connection con, int idOpe) throws SQLException {
        String nomPrenomOperateur = null;
        String sql = "SELECT nom,prenom FROM operateur WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idOpe);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                nomPrenomOperateur = nom + " " + prenom;
                }
            }
        }
        return nomPrenomOperateur;
    }
 

    //Création des méthodes liées à la Table Machine 
    //Ici c'est la création de machine en fonction des différentes colonnes de la table
    public static void createMachine(Connection con, String ref, String des, int puissance, int etatmachine) throws SQLException {
        String sql = "INSERT INTO machine (ref, des, puissance, etatmachine) VALUES (?, ?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, ref);
            preparedStatement.setString(2, des);
            preparedStatement.setInt(3, puissance);
            preparedStatement.setInt(4, etatmachine);

            preparedStatement.executeUpdate();
          
        }   
    }
    
    //Ici suppresion d'une machine 
    public static void deleteMachine(Connection con, int machineId) throws SQLException {
        String sql = "DELETE FROM machine WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, machineId);
            deletePosteTravailMachine(con, machineId);
            deleteRealiseMachine(con, machineId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Machine supprimée avec succès !");
            } else {
                System.out.println("Aucune machine trouvée avec l'ID : " + machineId);
            }
        }
    }
    //Permet de changer l'etat machine 
    public static void EtatMachine (Connection con, int Etat, int id)throws SQLException{
        String sql ="UPDATE machine SET etatmachine = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)){
                preparedStatement.setInt(1,Etat);
                preparedStatement.setInt(2,id);
                 preparedStatement.executeUpdate();
        }
    }
    //Permet de changer l'etat opérateur 
    public static void EtatOperateur (Connection con, int Etat, int id)throws SQLException{
        String sql ="UPDATE operateur SET Etat =?  WHERE id = ? ";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)){
           preparedStatement.setInt(1,Etat);
           preparedStatement.setInt(2,id);
           preparedStatement.executeUpdate();     
        }
    }
    
    //Ici création d'un produit 
    public static void createProduit(Connection con, String ref, String des, int idmateriaux, int poids) throws SQLException {
        String sql = "INSERT INTO produit (ref, des, idmateriaux, poids) VALUES (?, ?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, ref);
            preparedStatement.setString(2, des);
            preparedStatement.setInt(3, idmateriaux);
            preparedStatement.setInt(4, poids);
            
            preparedStatement.executeUpdate();
            System.out.println("Produit créé avec succès !");
        }
    }
    
    //Ici suppresion d'un produit 
    public static void deleteProduit(Connection con, int produitID) throws SQLException {
        String sql = "DELETE FROM produit WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, produitID);
            while (getIdByProduit(con, produitID) != 0) {
                deleteOperation(con, getIdByProduit(con, produitID));
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Produit supprimé avec succès !");
            } else {
                System.out.println("Aucun Produit trouvé avec l'ID : " + produitID);
            }
        }
    }
    
    public static int getIdByProduit(Connection con, int idProduit) throws SQLException {
        int idOperation = 0;
        String sql = "SELECT id FROM operation WHERE idproduit = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idProduit);                        

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idOperation = resultSet.getInt("id");
                }
            }
        }
        return idOperation;
    }
    
    //Ici création d'une réalisation
    public static void createRealise(Connection con, int idmachine, int idtype, long duree) throws SQLException {
        String sql = "INSERT INTO realise (idmachine, idtype, duree) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idmachine);
            preparedStatement.setInt(2, idtype);
            preparedStatement.setLong(3, duree);

            preparedStatement.executeUpdate();
            System.out.println("Lien créé avec succès !");
        }
    }
    
    //Ici suppresion d'une réalisation 
    public static void deleteRealiseMachine(Connection con, int machineID) throws SQLException {
        String sql = "DELETE FROM realise WHERE idmachine = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, machineID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("lien supprimé avec succès !");
            } else {
                System.out.println("Aucun lien trouvé avec l'ID : " + machineID);
            }
        }
    }
    
    public static void deleteRealiseType(Connection con, int typeID) throws SQLException {
        String sql = "DELETE FROM realise WHERE idtype = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, typeID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("lien supprimé avec succès !");
            } else {
                System.out.println("Aucun lien trouvé avec l'ID : " + typeID);
            }
        }
    }
    
    //Ici création d'une opération
    public static void createOperation(Connection con, int idtype, int idproduit) throws SQLException {
        String sql = "INSERT INTO operation (idtype, idproduit) VALUES (?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idtype);
            preparedStatement.setInt(2, idproduit);
            
            preparedStatement.executeUpdate();
            System.out.println("Opération créée avec succès !");
        }
    }
    
    //Ici suppresion d'une opération 
    public static void deleteOperation(Connection con, int operationID) throws SQLException {
        String sql = "DELETE FROM operation WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, operationID);
            deletePrecedeAvant(con, operationID);
            deletePrecedeApres(con, operationID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Opération supprimée avec succès !");
            } else {
                System.out.println("Aucune opération trouvée avec l'ID : " + operationID);
            }
        }
    }
    
    //Ici création d'une suite
    public static void createPrecede(Connection con, int opavant, int opapres) throws SQLException {
        String sql = "INSERT INTO precede (opavant, opapres) VALUES (?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setLong(1, opavant);
            preparedStatement.setLong(2, opapres);


            preparedStatement.executeUpdate();
            System.out.println("Suite créée avec succès !");
        }
    }
    
    //Ici suppresion d'un lien 
    public static void deletePrecedeAvant(Connection con, int avant) throws SQLException {
        String sql = "DELETE FROM precede WHERE opavant = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, avant);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Suite supprimée avec succès !");
            } else {
                System.out.println("Aucune suite trouvée avec l'opération avant : " + avant);
            }
        }
    }
    
    public static void deletePrecedeApres(Connection con, int apres) throws SQLException {
        String sql = "DELETE FROM precede WHERE opapres = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, apres);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Suite supprimée avec succès !");
            } else {
                System.out.println("Aucune suite trouvée avec l'opération avant : " + apres);
            }
        }
    }
    
    public static void deletePrecedeAvantApres(Connection con, int avant, int apres) throws SQLException {
        String sql = "DELETE FROM precede WHERE opavant = ? AND opapres = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, avant);
            preparedStatement.setInt(2, apres);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Suite supprimée avec succès !");
            } else {
                System.out.println("Aucune suite trouvée avec l'opération avant : " + apres);
            }
        }
    }
    
    //ici Création d'un nouveau type opération
    public static void createTypeOperation(Connection con, String des) throws SQLException {
        String sql = "INSERT INTO typeoperation (des) VALUES (?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, des);
            
            preparedStatement.executeUpdate();
            System.out.println("TypeOpération créée avec succès !");
        }
    }
    
    //Ici suppresion d'un type opération 
    public static void deleteTypeOperation(Connection con, int TypeoperationID) throws SQLException {
        String sql = "DELETE FROM typeoperation WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, TypeoperationID);
            deleteRealiseType(con, TypeoperationID);
            while (getIdByType(con, TypeoperationID) != 0) {
                deleteOperation(con, getIdByType(con, TypeoperationID));
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Type Opération supprimé avec succès !");
            } else {
                System.out.println("Aucun type opération trouvé avec l'ID : " + TypeoperationID);
            }
        }
    }
    
    public static int getIdByType(Connection con, int idType) throws SQLException {
        int idOperation = 0;
        String sql = "SELECT id FROM operation WHERE idtype = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idType);                        

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idOperation = resultSet.getInt("id");
                }
            }
        }
        return idOperation;
    }
    
    //Ici c'est la création de materiaux
    public static void createMateriaux(Connection con, String des, double prix) throws SQLException {
        String sql = "INSERT INTO materiaux (des, prix) VALUES (?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, des);
            preparedStatement.setDouble(2, prix);

            preparedStatement.executeUpdate();
            System.out.println("Matériaux créée avec succès !");
        }
    }
    
    //Ici suppresion d'une materiaux 
    public static void deleteMateriaux(Connection con, int materiauxId) throws SQLException {
        String sql = "DELETE FROM materiaux WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, materiauxId);
            while (getIdByMateriaux(con, materiauxId) != 0) {
                deleteProduit(con, getIdByMateriaux(con, materiauxId));
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Matériaux supprimé avec succès !");
            } else {
                System.out.println("Aucun matériaux trouvé avec l'ID : " + materiauxId);
            }
        }
    }
    
    public static int getIdByMateriaux(Connection con, int idMateriaux) throws SQLException {
        int idProduit = 0;
        String sql = "SELECT id FROM produit WHERE idmateriaux = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMateriaux);                        

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idProduit = resultSet.getInt("id");
                }
            }
        }
        return idProduit;
    }
    
    //Ici c'est la création d'un opérateur
    public static void createOperateur(Connection con, String prenom, String nom, int etatoperateur) throws SQLException {
        String sql = "INSERT INTO operateur (prenom, nom, etatoperateur) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, prenom);
            preparedStatement.setString(2, nom);
            preparedStatement.setInt(3, etatoperateur);

            preparedStatement.executeUpdate();
            System.out.println("Opérateur créé avec succès !");
        }
    }
    
    //Ici suppresion d'un opérateur 
    public static void deleteOperateur(Connection con, int operateurId) throws SQLException {
        String sql = "DELETE FROM operateur WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, operateurId);
            deletePosteTravailOperateur(con, operateurId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Opérateur supprimé avec succès !");
            } else {
                System.out.println("Aucun opérateur trouvé avec l'ID : " + operateurId);
            }
        }
    }
    
    //Ici c'est la création du poste de travail
    public static void createPosteTravail(Connection con, int idmachine, int idoperateur) throws SQLException {
        String sql = "INSERT INTO postetravail (idmachine, idoperateur) VALUES (?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idmachine);
            preparedStatement.setInt(2, idoperateur);

            preparedStatement.executeUpdate();
            System.out.println("Poste de Travail créé avec succès !");
        }   
    }
    
    //Ici suppresion d'un poste de travail 
    public static void deletePosteTravail(Connection con, int posteTravailId) throws SQLException {
        String sql = "DELETE FROM postetravail WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, posteTravailId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Poste de Travail supprimé avec succès !");
            } else {
                System.out.println("Aucun poste de travail trouvé avec l'ID : " + posteTravailId);
            }
        }
    }
    
    public static void deletePosteTravailOperateur(Connection con, int OperateurId) throws SQLException {
        String sql = "DELETE FROM postetravail WHERE idoperateur = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, OperateurId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Poste de Travail supprimé avec succès !");
            } else {
                System.out.println("Aucun poste de travail trouvé avec l'ID : " + OperateurId);
            }
        }
    }
    
    public static void deletePosteTravailMachine(Connection con, int MachineId) throws SQLException {
        String sql = "DELETE FROM postetravail WHERE idmachine = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, MachineId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Poste de Travail supprimé avec succès !");
            } else {
                System.out.println("Aucun poste de travail trouvé avec l'ID : " + MachineId);
            }
        }
    }
    
    //Ici c'est la création du poste d'un achat
    public static void createAchat(Connection con, int idproduit, int quantité, int idcommande) throws SQLException {
        String sql = "INSERT INTO achat (idproduit, quantité, idcommande) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idproduit);
            preparedStatement.setInt(2, quantité);
            preparedStatement.setInt(3, idcommande);

            preparedStatement.executeUpdate();
            System.out.println("Achat créé avec succès !");
        }   
    }
    
    //Ici suppresion d'un achat
    public static void deleteAchat(Connection con, int achatId) throws SQLException {
        String sql = "DELETE FROM achat WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, achatId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Achat supprimé avec succès !");
            } else {
                System.out.println("Aucun achat trouvé avec l'ID : " + achatId);
            }
        }
    }
    
    //Ici c'est la création du poste d'une commande
    public static void createCommande(Connection con, int idclient) throws SQLException {
        String sql = "INSERT INTO commande (idclient) VALUES (?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idclient);

            preparedStatement.executeUpdate();
            System.out.println("Commande créée avec succès !");
        }   
    }
    
    //Ici suppresion d'une commande
    public static void deleteCommande(Connection con, int commandeId) throws SQLException {
        String sql = "DELETE FROM commande WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, commandeId);
            while (getIdByCommande(con, commandeId) != 0) {
                deleteAchat(con, getIdByCommande(con, commandeId));
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Commande supprimée avec succès !");
            } else {
                System.out.println("Aucune commande trouvée avec l'ID : " + commandeId);
            }
        }
    }
    
    public static int getIdByCommande(Connection con, int idCommande) throws SQLException {
        int idAchat = 0;
        String sql = "SELECT id FROM achat WHERE idcommande = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCommande);                        

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idAchat = resultSet.getInt("id");
                }
            }
        }
        return idAchat;
    }
    
    //Ici c'est la création du poste d'une commande
    public static void createClient(Connection con, String nom, String prenom) throws SQLException {
        String sql = "INSERT INTO client (nom, prenom) VALUES (?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);

            preparedStatement.executeUpdate();
            System.out.println("Client créé avec succès !");
        }   
    }
    
    //Ici suppresion d'une commande
    public static void deleteClient(Connection con, int clientId) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, clientId);
            while (getIdByClient(con, clientId) != 0) {
                deleteCommande(con, getIdByClient(con, clientId));
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Client supprimé avec succès !");
            } else {
                System.out.println("Aucun client trouvé avec l'ID : " + clientId);
            }
        }
    }
    
    public static int getIdByClient(Connection con, int idClient) throws SQLException {
        int idCommande = 0;
        String sql = "SELECT id FROM commande WHERE idclient = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idClient);                        

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idCommande = resultSet.getInt("id");
                }
            }
        }
        return idCommande;
    }
  
  
    public static void creeBase(Connection conn) throws SQLException {
        //Connection conn = connSGBD.getCon();
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "create table machine (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  ref varchar(20),\n"
                    + "  des text, \n"
                    + "  puissance integer, \n"
                    + "  etatmachine int \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table produit (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  ref varchar(20),\n"
                    + "  des text, \n"
                    + "  idmateriaux integer,\n"
                    + "  poids integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table operation (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  idtype integer,\n"
                    + "  idproduit integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table typeoperation (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  des text \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table realise (\n"
                    + "  idmachine integer,\n"
                    + "  idtype integer, \n"
                    + "  duree integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table precede (\n"
                    + "  opavant integer,\n"
                    + "  opapres integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table materiaux (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  des text,\n"
                    + "  prix real \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table operateur (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  prenom text,\n"
                    + "  nom text,\n"
                    + "  etatoperateur integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table postetravail (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  idmachine integer,\n"
                    + "  idoperateur integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table achat (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  idproduit integer,\n"
                    + "  quantité integer,\n"
                    + "  idcommande integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table commande (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  idclient integer \n"
                    + ")"
            );
            st.executeUpdate(
                    "create table client (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  nom text,\n"
                    + "  prenom text \n"
                    + ")"
            );
            st.executeUpdate(
                    "alter table operation \n"
                    + "  add constraint fk_operation_idproduit \n"
                    + "  foreign key (idproduit) references produit(id)");
            st.executeUpdate(
                    "alter table operation \n"
                    + "  add constraint fk_operation_idtype \n"
                    + "  foreign key (idtype) references typeoperation(id)");
            st.executeUpdate(
                    "alter table precede \n"
                    + "  add constraint fk_precede_opavant \n"
                    + "  foreign key (opavant) references operation(id)");
            st.executeUpdate(
                    "alter table precede \n"
                    + "  add constraint fk_precede_opapres \n"
                    + "  foreign key (opavant) references operation(id)");
            st.executeUpdate(
                    "alter table realise \n"
                    + "  add constraint fk_realise_idmachine \n"
                    + "  foreign key (idmachine) references machine(id)");
            st.executeUpdate(
                    "alter table realise \n"
                    + "  add constraint fk_realise_idtype \n"
                    + "  foreign key (idtype) references typeoperation(id)");
            st.executeUpdate(
                    "alter table produit \n"
                    + "  add constraint fk_produit_idmateriaux \n"
                    + "  foreign key (idmateriaux) references materiaux(id)");
            st.executeUpdate(
                    "alter table postetravail \n"
                    + "  add constraint fk_postetravail_idmachine \n"
                    + "  foreign key (idmachine) references machine(id)");
            st.executeUpdate(
                    "alter table postetravail \n"
                    + "  add constraint fk_postetravail_idoperateur \n"
                    + "  foreign key (idoperateur) references operateur(id)");
            st.executeUpdate(
                    "alter table achat \n"
                    + "  add constraint fk_achat_idcommande \n"
                    + "  foreign key (idcommande) references commande(id)");
            st.executeUpdate(
                    "alter table commande \n"
                    + "  add constraint fk_commande_idclient \n"
                    + "  foreign key (idclient) references client(id)");
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    
    public static void deleteSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate("alter table commande drop constraint fk_commande_idclient");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table achat drop constraint fk_achat_idcommande");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table postetravail drop constraint fk_postetravail_idoperateur");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table postetravail drop constraint fk_postetravail_idmachine");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table produit drop constraint fk_produit_idmateriaux");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table realise drop constraint fk_realise_idtype");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table realise drop constraint fk_realise_idmachine");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table precede drop constraint fk_precede_opapres");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table precede drop constraint fk_precede_opavant");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table operation drop constraint fk_operation_idtype");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table operation drop constraint fk_operation_idproduit");
            } catch (SQLException ex) {
            }
            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate("drop table client");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table commande");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table achat");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table postetravail");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table operateur");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table materiaux");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table precede");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table realise");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table typeoperation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table operation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table produit");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table machine");
            } catch (SQLException ex) {
            }
        }
    }
    
    
    public static void lecture(Connection con, File fin) throws IOException, SQLException {
        if (con == null) {
            throw new SQLException("Connexion à la base de données échouée");
        }       
        try (BufferedReader bin = new BufferedReader(new FileReader(fin))) {
            String line; 
            while ((line = bin.readLine()) != null && line.length() != 0) {
                String[] bouts = line.split(";");
                if (bouts[0].equals("machine")) { 
                    String ref = bouts[1];
                    String des = bouts[2];
                    int puissance = Integer.parseInt(bouts[3]);
                    int etatmachine = Integer.parseInt(bouts[4]);
                    
                    try {
                        createMachine(con, ref, des, puissance, etatmachine);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de la machine");
                    }
                }  
                if (bouts[0].equals("produit")) { 
                    String ref = bouts[1];
                    String des = bouts[2];
                    int idmateriaux = Integer.parseInt(bouts[3]);
                    int poids = Integer.parseInt(bouts[3]);
                    
                    try {
                        createProduit(con, ref, des, idmateriaux, poids);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du produit");
                    }
                }  
                if (bouts[0].equals("realise")) { 
                    int idmachine = Integer.parseInt(bouts[1]);
                    int idtype = Integer.parseInt(bouts[2]);
                    long duree = Long.parseLong(bouts[3]);
                    
                    try {
                        createRealise(con, idmachine, idtype, duree);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de réalise");
                    }
                }  
                if (bouts[0].equals("operation")) { 
                    int idtype = Integer.parseInt(bouts[1]);
                    int idproduit = Integer.parseInt(bouts[2]);
                    
                    try {
                        createOperation(con, idtype, idproduit);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de l'opération");
                    }
                } 
                if (bouts[0].equals("precede")) { 
                    int opavant = Integer.parseInt(bouts[1]);
                    int opapres = Integer.parseInt(bouts[2]);
                    
                    try {
                        createPrecede(con, opavant, opapres);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de la suite");
                    }
                }
                if (bouts[0].equals("typeoperation")) { 
                    String des = bouts[1];
                    
                    try {
                        createTypeOperation(con, des);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du type opération");
                    }
                }
                if (bouts[0].equals("materiaux")) { 
                    String des = bouts[1];
                    double prix = Double.parseDouble(bouts[2]);
                    
                    try {
                        createMateriaux(con, des, prix);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du materiaux");
                    }
                } 
                if (bouts[0].equals("operateur")) { 
                    String prenom = bouts[1];
                    String nom = bouts[2];
                    int etatoperateur = Integer.parseInt(bouts[3]);
                    
                    try {
                        createOperateur(con, prenom, nom, etatoperateur);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de l'operateur");
                    }
                } 
                if (bouts[0].equals("postetravail")) { 
                    int idoperateur = Integer.parseInt(bouts[1]);
                    int idmachine = Integer.parseInt(bouts[2]);
                    
                    try {
                        createPosteTravail(con, idoperateur, idmachine);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du poste de travail");
                    }
                } 
                if (bouts[0].equals("achat")) { 
                    int idproduit = Integer.parseInt(bouts[1]);
                    int quantité = Integer.parseInt(bouts[2]);
                    int idcommande = Integer.parseInt(bouts[3]);
                    
                    try {
                        createAchat(con, idproduit, quantité, idcommande);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de l'achat");
                    }
                }
                if (bouts[0].equals("commande")) { 
                    int idclient = Integer.parseInt(bouts[1]);
                    
                    try {
                        createCommande(con, idclient);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de la commande");
                    }
                }
                if (bouts[0].equals("client")) { 
                    String prenom = bouts[1];
                    String nom = bouts[2];
                    
                    try {
                        createClient(con, prenom, nom);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du client");
                    }
                }
            }
        }
    }
    
    
    public static Object[][] getTableValue(Connection connection, String tableName) throws SQLException {
        List<List<Object>> resultMatrix = new ArrayList<>();

        // Créer une requête SQL pour récupérer toutes les valeurs de la table
        String sql = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Parcourir les résultats de la requête
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                int columnCount = resultSet.getMetaData().getColumnCount();

                // Récupérer les valeurs par colonne
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }

                // Ajouter la ligne à la matrice de résultats
                resultMatrix.add(row);
            }
        }

        // Convertir la liste de listes en tableau bidimensionnel
        Object[][] resultArray = new Object[resultMatrix.size()][];
        for (int i = 0; i < resultMatrix.size(); i++) {
            List<Object> row = resultMatrix.get(i);
            resultArray[i] = row.toArray(new Object[0]);
        }

        return resultArray;
    }
    
    
   
    public static void debut() throws IOException {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("connecté");
            deleteSchema(con);
            creeBase(con);
            //createMachine(con,"F04","rapide",30);
            lecture(con, new File("lecture.txt"));
            deleteClient(con, 1);
        } catch (SQLException ex) {
            System.err.println("Code d'erreur SQL : " + ex.getErrorCode());
            System.err.println("Message d'erreur SQL : " + ex.getMessage());
            throw new Error("Connection impossible", ex);
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        debut();
        
    }
    
    
    
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.insa.info_s3.Machines.Machine;
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
    
  public static List<Machine> Getmachine (Connection con )throws SQLException{
  List<Machine> Machines  = new ArrayList <>();
  String sql = "SELECT * FROM machine";
  try  (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
    try (ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String ref = resultSet.getString("ref");
                        String des = resultSet.getString("des");
                        int puissance = resultSet.getInt("puissance");
                        int etatmachine = resultSet.getInt("etatmachine");
                        
                        Machine machine = new Machine(id, ref, des,puissance,etatmachine);
                        Machines.add(machine);
  
        }
  
    }
  }
  
  return Machines;
  
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
                        
        
                        
                        Produits Produit = new Produits(id,ref,des,getNomMateriauxById(con, materiaux));
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
            System.out.println("Machine créée avec succès !");
        }   
    }
    
    //Ici suppresion d'une machine 
    public static void deleteMachine(Connection con, int machineId) throws SQLException {
        String sql = "DELETE FROM machine WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, machineId);
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
    public static void createProduit(Connection con, String ref, String des, int idmateriaux) throws SQLException {
        String sql = "INSERT INTO produit (ref, des, idmateriaux) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, ref);
            preparedStatement.setString(2, des);
            preparedStatement.setInt(3, idmateriaux);
            
            preparedStatement.executeUpdate();
            System.out.println("Produit créée avec succès !");
        }
    }
    
    //Ici suppresion d'un produit 
    public static void deleteProduit(Connection con, int produitID) throws SQLException {
        String sql = "DELETE FROM produit WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, produitID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Produit supprimé avec succès !");
            } else {
                System.out.println("Aucun Produit trouvé avec l'ID : " + produitID);
            }
        }
    }
    
    //Ici création d'une réalisation
    public static void createRealise(Connection con, int idmachine, int idtype, long duree) throws SQLException {
        String sql = "INSERT INTO realise (idmachine, idtype, duree) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, idmachine);
            preparedStatement.setInt(2, idtype);
            preparedStatement.setLong(3, duree);

            preparedStatement.executeUpdate();
            System.out.println("Lien créée avec succès !");
        }
    }
    
    //Ici suppresion d'une réalisation 
    public static void deleteRealise(Connection con, int realiseID) throws SQLException {
        String sql = "DELETE FROM realise WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, realiseID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("lien supprimé avec succès !");
            } else {
                System.out.println("Aucun lien trouvé avec l'ID : " + realiseID);
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
    public static void deletePrecede(Connection con, int precedeID) throws SQLException {
        String sql = "DELETE FROM precede WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, precedeID);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Suite supprimée avec succès !");
            } else {
                System.out.println("Aucune suite trouvée avec l'ID : " + precedeID);
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
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Type Opération supprimé avec succès !");
            } else {
                System.out.println("Aucun type opération trouvé avec l'ID : " + TypeoperationID);
            }
        }
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
    
    //Ici suppresion d'une machine 
    public static void deleteMateriaux(Connection con, int materiauxId) throws SQLException {
        String sql = "DELETE FROM materiaux WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, materiauxId);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Matériaux supprimé avec succès !");
            } else {
                System.out.println("Aucun matériaux trouvé avec l'ID : " + materiauxId);
            }
        }
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
                System.out.println("Aucun pote de travail trouvé avec l'ID : " + posteTravailId);
            }
        }
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
                    + "  idmateriaux integer \n"
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
                    
                    try {
                        createProduit(con, ref, des, idmateriaux);
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


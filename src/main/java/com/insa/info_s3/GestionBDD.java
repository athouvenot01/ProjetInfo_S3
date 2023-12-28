/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


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
    
  
    //Création des méthodes liées à la Table Machine 
    //Ici c'est la création de machine en fonction des différentes colonnes de la table
    public static void createMachine(Connection con, String ref, String des, int puissance) throws SQLException {
        String sql = "INSERT INTO machine (ref, des, puissance) VALUES (?, ?, ?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, ref);
            preparedStatement.setString(2, des);
            preparedStatement.setInt(3, puissance);

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
    public static void createRealise(Connection con, long durée) throws SQLException {
        String sql = "INSERT INTO realise (durée) VALUES (?)"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setLong(1, durée);

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
  
  
    public static void creeBase(Connection conn) throws SQLException {
        //Connection conn = connSGBD.getCon();
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "create table machine (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  ref varchar(20),\n"
                    + "  des text, \n"
                    + "  puissance integer \n"
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
    
    
    public static void lecture(File fin) throws IOException, SQLException {
        Connection con = connectSurServeurM3();
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
                    
                    try {
                        createMachine(con, ref, des, puissance);
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
                    long duree = Long.parseLong(bouts[1]);
                    
                    try {
                        createRealise(con, duree);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création de réalise");
                    }
                }  
                
                // manque opération, typeoperation et precede
                
                if (bouts[0].equals("materiaux")) { 
                    String des = bouts[1];
                    double prix = Double.parseDouble(bouts[2]);
                    
                    try {
                        createMateriaux(con, des, prix);
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la création du materiaux");
                    }
                } 
            }
        }
    }
    
   
    public static void debut() {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("connecté");
            creeBase(con);
            createMachine(con,"F04","rapide",30);
            //deleteSchema(con);
        } catch (SQLException ex) {
            System.err.println("Code d'erreur SQL : " + ex.getErrorCode());
            System.err.println("Message d'erreur SQL : " + ex.getMessage());
            throw new Error("Connection impossible", ex);
        }
    }
    
    
    public static void main(String[] args) {
        debut();
        
    }
    
    
    
}

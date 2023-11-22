/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author Amandine Tvt
 */
public class GestionBDD {
    
    
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
       int newMachineId = getFirstAvailableId(con,"machine");

    String sql = "INSERT INTO machine (id, ref, des, puissance) VALUES (?, ?, ?, ?)"; 
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
        preparedStatement.setInt(1, 3); // Id statique de 5, vous pouvez ajuster selon vos besoins
        preparedStatement.setString(2, ref);
        preparedStatement.setString(3, des);
        preparedStatement.setInt(4, puissance);

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
private static int getFirstAvailableId(Connection con, String tableName) throws SQLException {
    String sql = "SELECT MIN(id + 1) FROM " + tableName + " WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " WHERE id = id + 1)";
    try (Statement statement = con.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

        if (resultSet.next()) {
            int availableId = resultSet.getInt(1);
            if (availableId > 0) {
                return availableId;
            }
        }
    }

    // Si aucun ID disponible n'est trouvé, retournez 1 (par exemple)
    return 1;
}

   
    public static void debut() {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("connecté");
            createMachine(con,"F04","rapide",30);
        } catch (SQLException ex) {
            System.err.println("Code d'erreur SQL : " + ex.getErrorCode());
            System.err.println("Message d'erreur SQL : " + ex.getMessage());
            ex.printStackTrace();
            throw new Error("Connection impossible", ex);
        }
    }
    
    
    public static void main(String[] args) {
        debut();
        
    }
    
    
    
}

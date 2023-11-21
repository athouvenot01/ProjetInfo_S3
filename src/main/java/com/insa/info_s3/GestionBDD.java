/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        
        
    public static void debut() {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("connecté");
        } catch (SQLException ex) {
            throw new Error("Connection impossible", ex);
        }
    }
    
    
    public static void main(String[] args) {
        debut();
    }
    
    
    
}

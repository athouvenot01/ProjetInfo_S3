/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Amandine Tvt
 */
public class Const_dest {
    
    public static void creeBase(Connection conn) throws SQLException {
        //Connection conn = connSGBD.getCon();
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "create table machine (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  ref varchar(20),\n"
                    + "  description text \n"
                    + "  puissance integer \n"
                    + ")");
            st.executeUpdate(
                    "create table produit (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  ref varchar(20),\n"
                    + "  description text, \n"
                    + ")");
            st.executeUpdate(
                    "create table operation (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  idtype integer,\n"
                    + "  idproduit integer, \n"
                    + ")");
            st.executeUpdate(
                    "create table precede (\n"
                    + "  opavant integer,\n"
                    + "  opapres integer, \n"
                    + ")");
            st.executeUpdate(
                    "create table typeoperation (\n"
                    + "  id integer primary key auto_increment,\n"
                    + "  description text, \n"
                    + ")");
            st.executeUpdate(
                    "create table realise (\n"
                    + "  idmachine integer primary key auto_increment,\n"
                    + "  idtype integer, \n"
                    + "  duree integer, \n"
                    + ")");
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
                    + "  foreign key (opavant) references opération(id)");
            st.executeUpdate(
                    "alter table precede \n"
                    + "  add constraint fk_precede_opapres \n"
                    + "  foreign key (opavant) references opération(id)");
            st.executeUpdate(
                    "alter table realise \n"
                    + "  add constraint fk_realise_idmachine \n"
                    + "  foreign key (idmachine) references machine(id)");
            st.executeUpdate(
                    "alter table realise \n"
                    + "  add constraint fk_realise_idtype \n"
                    + "  foreign key (idtype) references typeoperation(id)");
            st.executeUpdate(
                    "alter table realise \n"
                    + "  add constraint fk_realise_idmachine \n"
                    + "  foreign key (idmachine) references machine(id)");
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public static void suppBase(Connection conn) throws SQLException {
        
    }
}

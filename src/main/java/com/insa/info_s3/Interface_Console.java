/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emilien
 */
public class Interface_Console {
    
public static void main(String[] args) {
        InterfaceC();  
    }
    
    
public static void InterfaceC (){
    String ref,des;
    try (Connection con = GestionBDD.connectSurServeurM3()) {
        int i;
        int choix;
        System.out.println("Quelle table voulez vous modifier ?(1=produit,2=machine,3=operation,4=renitialisation");
        i=Lire.i();
        switch (i){
            case 1 -> {
                System.out.println("Creer ou supprimer produit ?");
                choix=Lire.i();
                if (choix==1) {
                System.out.println("Pourrais je avoir votre reference ?");
                ref=Lire.S();
                System.out.println("Pourrais je avoir votre description ?");
                des=Lire.S();
                GestionBDD.createProduit(con, ref, des);
                }
                else if(choix==2) {
                    
                }
                break;
            }
            case 2 -> {
                System.out.println("Créer ou supprimer machine ?");
                choix=Lire.i();
                break;
            }
            case 3 -> {
                System.out.println("Créer ou supprimer oppération ?");
                choix=Lire.i();
                break;
            }
            case 4 -> {
                System.out.println("réenitialiser?");
                choix=Lire.i();
                break;
            }
        }
    } catch (SQLException ex) {
        System.out.println("probleme : " + ex.getLocalizedMessage());
        ex.printStackTrace();
    }
}
}

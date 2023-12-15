/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.*;

/**
 *
 * @author Emilien
 */
public class Interface_Console {
    
public static void main(String[] args) {
        InterfaceC();  
    }
    
    
public static void InterfaceC (){
    //debut();
    int i;
    String choix;
    System.out.println("Quelle table voulez vous modifier ?(1=produit,2=machine,3=opération,4=renitialisation");
    i=Lire.i();
    switch (i){
    case 1 -> { 
        System.out.println("Créer ou supprimer produit ?");
        choix=Lire.S();
        if (choix=="Créer") {
        
        }
        else if (choix=="supprimer") {
        
        }
        break;
        }
    case 2 -> { 
       System.out.println("Créer ou supprimer machine ?");
       choix=Lire.S();
        break;
        }
    case 3 -> { 
        System.out.println("Créer ou supprimer oppération ?");
        choix=Lire.S();
        break;
        }
    case 4 -> { 
        System.out.println("réenitialiser?");
        choix=Lire.S();
        break;
        }
    }
}
}

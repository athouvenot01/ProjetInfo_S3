/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMateriaux;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loicrosian
 */

@Route(value = "parametre_View", layout = UI.class)
public class parametre_View extends Div {
    
    public parametre_View(){
        try (Connection con = GestionBDD.connectSurServeurM3()){
            
            H2 titre_View = new H2("Paramètres BDD");
            H3 titre = new H3 ("Ici, vous pouvez ajouter différents paramètres de la base de données");
        
            Button Boutton_mat = new Button ("Voulez-vous ajouter un matéraiaux ?");
            Button sup_mat = new Button("Voulez-vous supprimer un matériaux ?");
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue initial des paramètres
                getUI().ifPresent(ui -> ui.navigate(""));
            });
        
            Boutton_mat.addClickListener(click -> {
                TextField des_mat = new TextField("Entrez le nom du matéraiaux ");
                TextField prix_mat = new TextField("Entrez le prix du matériaux ");
                Button valider = new Button ("valider");
                valider.addClickListener(enter -> {
                    String valeurTextField = prix_mat.getValue();
                    String valeur_des = des_mat.getValue();
                    try {
                        // Convertir la valeur en int
                        double prix = Double.parseDouble(valeurTextField);
                        createMateriaux(con, valeur_des, prix);
                        afficherNotification("le matériaux a bien été créé");
                    }catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un nombre valide");
                    } catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                valider.addClickShortcut(Key.ENTER);
                add(
                    des_mat,
                    prix_mat,
                    valider);
            });
            
            sup_mat.addClickListener(click -> {
                TextField id_mat = new TextField("entrez l'id du matériaux à supprimer");
                Button valider = new Button ("entrer");
                valider.addClickListener(enter -> {
                    String valeur_id = id_mat.getValue();
                    try {
                        // Convertir la valeur en int
                        int valeurInt = Integer.parseInt(valeur_id);
                        deleteProduit(con, valeurInt);
                        afficherNotification("le matériaux a bien été supprimé");
                    }catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    } catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                valider.addClickShortcut(Key.ENTER);
                add(
                    id_mat,
                    valider);
            });
            
             add(
                titre_View,
                titre,
                new HorizontalLayout(Boutton_mat, sup_mat),
                actualiser
                );
    
            
        }catch (SQLException ex) {
            System.out.println("probleme : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        
    } 
    
     public void afficherNotification(String message) {
        // Créer une notification
        Notification notification = new Notification(
                message,
                3000, // Durée d'affichage en millisecondes
                Notification.Position.MIDDLE
        );

        // Afficher la notification
        notification.open();
    }
     
}

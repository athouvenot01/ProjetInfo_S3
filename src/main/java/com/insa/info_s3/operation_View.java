/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createOperation;
import static com.insa.info_s3.GestionBDD.createProduit;
import static com.insa.info_s3.GestionBDD.deleteOperation;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loicrosian
 */

@Route(value = "operation_View", layout = UI.class)
public class operation_View extends Div {
    
    public operation_View() {
        
        try (Connection con = GestionBDD.connectSurServeurM3()){
           
            H2 titre_View = new H2("Registre des opérations");
            Button B1 = new Button ("voulez-vous supprimer une opération de la liste ?");
            Button B2 = new Button ("voulez-vous ajouter une opération à la liste ?");
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                TextField Nom1 = new TextField("entrez l'id de l'opération à supprimer");
                Button valider = new Button ("entrer");
                valider.addClickListener(enter -> {
                    String valeurTextField = Nom1.getValue();
                    try {
                        // Convertir la valeur en int
                        int id_operation = Integer.parseInt(valeurTextField);
                        deleteOperation(con, id_operation);
                        afficherNotification("l'opération a bien été supprimé");
                    }catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    } catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                valider.addClickShortcut(Key.ENTER);
                add(
                    Nom1,
                    valider);
            });
              
            B2.addClickListener(click -> {
                TextField type = new TextField("entrez l'id du type d'opération à ajouter");
                TextField produit = new TextField("entrez l'id du produit à ajouter");
                Button entrer = new Button("valider");
             
                entrer.addClickListener(enter -> {
                    String valeur_type = type.getValue();
                    String valeur_prod = produit.getValue();
                    try {
                        // Convertir la valeur en int
                        int idtype = Integer.parseInt(valeur_type);
                        int idproduit = Integer.parseInt(valeur_prod);
                        createOperation(con, idtype, idproduit);
                    } catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    }catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                        
                add(
                    type, 
                    produit, 
                    entrer);
            });
            
         
            // Données pour la grille (liste de listes)
            List<List<String>> donnees = Arrays.asList(
                Arrays.asList("A1", "B1", "C1"),
                Arrays.asList("A2", "B2", "C2"),
                Arrays.asList("A3", "B3", "C3")
            );

            // Créer une grille avec les colonnes
            Grid<List<String>> grid = new Grid<>();
            grid.setItems(donnees);

            // Ajouter des colonnes à la grille
            for (int i = 0; i < donnees.get(0).size(); i++) {
                int indiceColonne = i;
                grid.addColumn(item -> item.get(indiceColonne)).setHeader("Colonne " + (indiceColonne + 1));
            }

            
            
            add(
                titre_View, 
                new VerticalLayout(grid),
                new HorizontalLayout(B1, B2, actualiser) 
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

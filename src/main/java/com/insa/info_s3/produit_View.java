/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import static com.insa.info_s3.GestionBDD.getTableValue;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author loicrosian
 */

@Route(value = "produit_View", layout = UI.class)
public class produit_View extends Div {
    
    public produit_View() {
        
        try (Connection con = GestionBDD.connectSurServeurM3()){
           
            Button B1 = new Button ("voulez-vous supprimer un produit de la liste ?");
            Button B2 = new Button ("voulez-vous ajouter un produit à la liste ?");
            
            B1.addClickListener(click -> {
                TextArea Nom1 = new TextArea("entrez l'id du produit à supprimer");
                Button valider = new Button ("entrer");
                valider.addClickListener(enter -> {
                    String valeurTextField = Nom1.getValue();
                    try {
                        // Convertir la valeur en int
                        int valeurInt = Integer.parseInt(valeurTextField);
                        deleteProduit(con, valeurInt);
                    }catch (NumberFormatException ex) {
                        Notification notification = new Notification(
                        "Veuillez entrer un nombre entier valide",
                        3000, // Durée d'affichage en millisecondes
                        Notification.Position.MIDDLE
                        );
                    notification.open();
                    } catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                valider.addClickShortcut(Key.ENTER);
                add(
                    Nom1,
                    valider);
            });
                    
            
            
            List<String> chaines = Arrays.asList(
                "Chaine 1",
                "Chaine 2",
                "Chaine 3"
            );
            
            
            /*Grid<String> grid = new Grid<>();
            grid.setItems(chaines);
            grid.addColumn(String::toString).setHeader("liste des produits disponibles");
            grid.setWidth("1000px");
            grid.setHeight("300px");*/

            
         
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

            // Ajouter la grille à la mise en page
            add(grid);
            
            
            add(
                new VerticalLayout(grid), 
                new HorizontalLayout(B1, B2) 
                );
            
            
        }catch (SQLException ex) {
            System.out.println("probleme : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    
    }
}

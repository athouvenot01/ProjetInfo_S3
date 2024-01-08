/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createOperateur;
import static com.insa.info_s3.GestionBDD.createProduit;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author loicrosian
 */

@Route(value = "operateur_View", layout = UI.class)
public class operateur_View extends Div {

    public operateur_View() throws SQLException{
       
        try (Connection con = GestionBDD.connectSurServeurM3()){
            H2 titre_View = new H2("Registre des opérateurs");
            Button B1 = new Button ("voulez-vous supprimer un opérateur de la liste ?");
            Button B2 = new Button ("voulez-vous ajouter un opérateur à la liste ?");
            
            Button actualiser = new Button("Actualiser"); 
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                TextField Nom1 = new TextField("entrez l'id de l'opérateur à supprimer");
                Button valider = new Button ("entrer");
                valider.addClickListener(enter -> {
                    String valeurTextField = Nom1.getValue();
                    try {
                        // Convertir la valeur en int
                        int valeurInt = Integer.parseInt(valeurTextField);
                        deleteProduit(con, valeurInt);
                        afficherNotification("l'opérateur a bien été supprimé");
                    }catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    }catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            valider.addClickShortcut(Key.ENTER);
            add(
                Nom1,
                valider);
            });
            
            B2.addClickListener(click -> {
                TextField nom = new TextField("entrez le nom de l'opérateur à ajouter");
                TextField prenom = new TextField("entrez le prénom de l'opérateur à ajouter");
                TextField machine = new TextField("entrez l'id de la machine liée à l'opérateur");
                TextField etatoperateur = new TextField("entrez l'état de l'opérateur");
                Button entrer = new Button("valider");
             
                entrer.addClickListener(enter -> {
                    String valeur_nom = nom.getValue();
                    String valeur_prenom = prenom.getValue();
                    String valeur_machine = machine.getValue();
                    String valeur_etatoperateur = etatoperateur.getValue();
                    try {
                        // Convertir la valeur en int
                        int id_machine = Integer.parseInt(valeur_machine);
                        int etat_operateur = Integer.parseInt(valeur_etatoperateur);
                        createOperateur(con, valeur_prenom, valeur_nom, id_machine,etat_operateur);
                    } catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    }catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                        
                add(
                    nom, 
                    prenom, 
                    machine,
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

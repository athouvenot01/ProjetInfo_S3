/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createProduit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import static com.insa.info_s3.GestionBDD.getTableValue;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
           
            H2 titre_View = new H2("Registre des produits");
            Button B1 = new Button ("Supprimer un produit",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un produit",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                TextField Nom1 = new TextField("entrez l'id du produit à supprimer");
                Button valider = new Button ("entrer");
                valider.addClickListener(enter -> {
                    String valeurTextField = Nom1.getValue();
                    try {
                        // Convertir la valeur en int
                        int valeurInt = Integer.parseInt(valeurTextField);
                        deleteProduit(con, valeurInt);
                        afficherNotification("le produit a bien été supprimé");
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
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouveau produit");

                VerticalLayout dialogLayout = createDialogLayout();
                dialog.add(dialogLayout);

                Button saveButton = createSaveButton(dialog);
                Button cancelButton = new Button("Cancel", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);


                dialog.open();
                
                /*TextField ref = new TextField("entrez la référence du produit à ajouter");
                TextField des = new TextField("entrez la description du produit à ajouter");
                TextField mat = new TextField("entrez l'id du matériaux du produit à ajouter");
                Button entrer = new Button("valider");
             
                entrer.addClickListener(enter -> {
                    String valeur_ref = ref.getValue();
                    String valeur_des = des.getValue();
                    String valeur_mat = mat.getValue();
                    try {
                        // Convertir la valeur en int
                        int id_mat = Integer.parseInt(valeur_mat);
                        createProduit(con, valeur_ref, valeur_des, id_mat);
                    } catch (NumberFormatException ex) {
                        afficherNotification("Veuillez saisir un entier valide");
                    }catch (SQLException ex) {
                        Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                        
                add(
                    ref, 
                    des, 
                    mat,
                    entrer);*/
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
    
    private static VerticalLayout createDialogLayout() {

        TextField id = new TextField("référence ");
        TextField des = new TextField("description ");
        TextField puissance = new TextField("id du matériau");

        VerticalLayout dialogLayout = new VerticalLayout(id,
                des,puissance);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }
    
    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }
    
}

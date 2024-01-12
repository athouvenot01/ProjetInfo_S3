/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createOperateur;
import static com.insa.info_s3.GestionBDD.createProduit;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Operateurs.Operateur;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
    private Grid<Operateur> grid = new Grid<>();
    public operateur_View() throws SQLException{
       
        try (Connection con = GestionBDD.connectSurServeurM3()){
            H2 titre_View = new H2("Registre des opérateurs");
            Button B1 = new Button ("Supprimer un opérateur",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un opérateur",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
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
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouvel opérateur");

                VerticalLayout dialogLayout = createDialogLayout();
                dialog.add(dialogLayout);

                Button saveButton = createSaveButton(dialog);
                Button cancelButton = new Button("Cancel", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);


                dialog.open();
                
                /*
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
                    
                });
                        
                add(
                    nom, 
                    prenom, 
                    machine,
                    entrer);*/
            });
            
            // Données pour la grille (liste de listes)
           List<Operateur> Operateurs = GestionBDD.GetOperateur(con);
            grid.addColumn(Operateur::getId).setHeader("IdOperateur");
            grid.addColumn(Operateur::getPrenom).setHeader("Prenom");
            grid.addColumn(Operateur::getNom).setHeader("Nom");
            grid.addColumn(Operateur::getEtatOperateur).setHeader("Etat");
            grid.setItems(Operateurs);    

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
    
    private static VerticalLayout createDialogLayout() {

        TextField id = new TextField("Prénom ");
        TextField des = new TextField("Nom ");
        TextField puissance = new TextField("id de la machine");
        TextField etat_machine = new TextField("état de l'opérateur");

        VerticalLayout dialogLayout = new VerticalLayout(id,
                des,puissance, etat_machine);
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

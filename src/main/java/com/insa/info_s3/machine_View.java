/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Machines.Machine;
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
import com.vaadin.flow.component.textfield.NumberField;
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

@Route(value = "machine_View", layout = UI.class)
public class machine_View extends Div {
    
    public machine_View() throws SQLException {
        try (Connection con = GestionBDD.connectSurServeurM3()){
            H2 titre_View = new H2("Registre des produits");
            Button B1 = new Button ("Supprimer une machine ",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter une machine",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                TextField Nom1 = new TextField("id de la machine");
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

                dialog.setHeaderTitle("Nouvelle machine");

                VerticalLayout dialogLayout;
                try {
                    dialogLayout = createDialogLayout();
                    dialog.add(dialogLayout);
                } catch (SQLException ex) {
                    Logger.getLogger(machine_View.class.getName()).log(Level.SEVERE, null, ex);
                }
                

                //Button saveButton = createSaveButton(dialog);
                Button cancelButton = new Button("Cancel", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                //dialog.getFooter().add(saveButton);


                dialog.open();
            });
            
            // Créer une grille avec les colonnes
            List<Machine> Machines = GestionBDD.Getmachine(con);
            Grid<Machine> grid = new Grid<>();
            grid.addColumn(Machine::getId).setHeader("Id");
            grid.addColumn(Machine::getRef).setHeader("ref");
            grid.addColumn(Machine::getDes).setHeader("des");
            grid.addColumn(Machine::getPuissance).setHeader("Puissance");
            grid.addColumn(Machine::getEtatmachine).setHeader("Etat Machine");
            
            grid.setItems(Machines);
           
            add(
                titre_View, 
                grid,
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
    
    private static VerticalLayout createDialogLayout() throws SQLException {
        
        try (Connection con = GestionBDD.connectSurServeurM3()){

            TextField id = new TextField("référence ");
            TextField des = new TextField("description ");
            NumberField puissance = new NumberField("puissance");
            NumberField etat_machine = new NumberField("état de la machine ");
            
            int nombreInt = (int) puissance.getValue();
           

            VerticalLayout dialogLayout = new VerticalLayout(id,
                des,puissance, etat_machine);
            dialogLayout.setPadding(false);
            dialogLayout.setSpacing(false);
            dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
            dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
        
            Button saveButton = new Button("Add");
            saveButton.addClickListener(e-> {
                
                createMachine(con,id.getValue(),des.getValue(), nombreInt, etat_machine.getValue());
               
            });

            return dialogLayout;
            
        }
       
    }
    
    
    /*private static Button createSaveButton(Dialog dialog) {
        try (Connection con = GestionBDD.connectSurServeurM3()){
            Button saveButton = new Button("Add");
            saveButton.addClickListener(click -> {
            createMachine(con, id.getValue);
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        r   eturn saveButton;

        }*/
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createOperation;
import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.deleteOperation;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Machines.Machine;
import com.insa.info_s3.Operations.Operation;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author loicrosian
 */

@Route(value = "operation_View", layout = UI.class)
public class operation_View extends Div {
    
    private Grid<Operations.Operation> grid = new Grid<>();
    
    public operation_View() throws SQLException {
        
        try (Connection con = GestionBDD.connectSurServeurM3()){
           
            H2 titre_View = new H2("Registre des opérations");
            Button B1 = new Button ("Supprimer une opération",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter une opération",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                TextField Nom1 = new TextField("id de l'opération");
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
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouvelle Operation");

                VerticalLayout dialogLayout;
                
                try {

                    dialogLayout = createDialogLayout(dialog);

                    dialog.add(dialogLayout);
                    Button cancelButton = new Button("Cancel", e -> dialog.close());
                    dialog.getFooter().add(cancelButton);
                    


                dialog.open();
                } catch (Exception ex) {
                    Logger.getLogger(operation_View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
         
            // Créer une grille avec les colonnes
            List<Operations.Operation> Operations = GestionBDD.GetOperation(con);
            grid.addColumn(Operation::getId).setHeader("Id");
            grid.addColumn(Operation::getDes).setHeader("des");
            grid.setItems(Operations);

            
            
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
    
    private static VerticalLayout createDialogLayout(Dialog dialog) throws Exception {
        Connection con = GestionBDD.connectSurServeurM3();

        IntegerField idtype = new IntegerField("id du type d'opération");
        IntegerField idproduit = new IntegerField("id du produit");
    
        VerticalLayout dialogLayout = new VerticalLayout(idtype, idproduit);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        Button saveButton = new Button("Add");
        dialog.getFooter().add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            try {
                createOperation(con, idtype.getValue(), idproduit.getValue());
                dialog.close();
            
            } catch (SQLException ex) {
                // Gérer l'exception, par exemple, afficher un message d'erreur
                ex.printStackTrace();
            }
        });

    

    return dialogLayout;
    }
    
}

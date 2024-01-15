/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.createOperateur;
import static com.insa.info_s3.GestionBDD.createProduit;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Operateurs.Operateur;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author loicrosian
 */

@Route(value = "operateur_View", layout = UI.class)
public class operateur_View extends Div {
    
    private Grid<Operateur> grid = new Grid<>();
    private ComboBox<String> comboBox = new ComboBox<>("Etat de l'opérateur ");
    
    public operateur_View() throws SQLException{
       
        Connection con = GestionBDD.connectSurServeurM3();
        
            H2 titre_View = new H2("Registre des opérateurs");
            Button B1 = new Button ("Supprimer un opérateur",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un opérateur",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            
            B1.addClickListener(click -> {
                Set<Operateur> selectedItems = grid.getSelectedItems();
                
                if (selectedItems.isEmpty()) {
                    Notification.show("Aucun opérateur sélectionné", 2000, Notification.Position.TOP_CENTER);
                
                } else {
                    Operateur selectedBean = selectedItems.iterator().next();
                    int value = selectedBean.getId();
                    try {GestionBDD.deleteOperateur(con, value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateOperateurs(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Opérateur "+ selectedBean.getPrenom()+" "+selectedBean.getNom()+" supprimé avec succès" , 5000, Notification.Position.TOP_CENTER);
                }
            });
            
            
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouvel opérateur");

                VerticalLayout dialogLayout;
                
                try {

                    dialogLayout = createDialogLayout(dialog);

                    dialog.add(dialogLayout);
                    Button cancelButton = new Button("Annuler", e -> dialog.close());
                    dialog.getFooter().add(cancelButton);
                    
                    dialog.open();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(machine_View.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                new HorizontalLayout(B2, B1) 
            );
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
    
    
    private VerticalLayout createDialogLayout(Dialog dialog) throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();

        TextField prenom = new TextField("Prenom");
        TextField nom = new TextField("Nom");

        
        List<String> items = new ArrayList<>(
                Arrays.asList("En poste", "Au repos"));
        comboBox.setAllowCustomValue(true);
        comboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            comboBox.setItems(items);
            comboBox.setValue(customValue);
            });
        comboBox.setItems(items);
        comboBox.setHelperText("sélectionner l'état de l'opérateur");

        VerticalLayout dialogLayout = new VerticalLayout(prenom, nom, comboBox);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        Button saveButton = new Button("Ajout");
        dialog.getFooter().add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            try {
                int state = getValueComboBox(comboBox);
                createOperateur(con, prenom.getValue(), nom.getValue(), state);
                Notification.show("L'opérateur a été ajouté vec succès");
                dialog.close();
                try {UpdateOperateurs(con);} catch (SQLException ex){ex.printStackTrace();}

            } catch (SQLException ex) {
                // Gérer l'exception, par exemple, afficher un message d'erreur
                ex.printStackTrace();
            }
        });
        return dialogLayout;
    }
    
    
    private void UpdateOperateurs (Connection con) throws SQLException{
        List<Operateur> Operateurs = GestionBDD.GetOperateur(con);
        grid.setItems(Operateurs);
    }
    
    public int getValueComboBox(ComboBox<String> comboBox){
        if ("Actif".equals(comboBox.getValue())){
            int state = 1;
            return state ;
        }else{
            int state = 0;
            return state;
        }
    }
}

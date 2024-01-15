/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.createMateriaux;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Machines.Machine;
import com.insa.info_s3.Materiaux.materiaux;
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
import java.util.Set;

/**
 *
 * @author loicrosian
 */

@Route(value = "matériau_View", layout = UI.class)
public class materiaux_View extends Div {
    
    private Grid<Materiaux.materiaux> grid = new Grid<>();
    
    public materiaux_View() throws SQLException{
        
        Connection con = GestionBDD.connectSurServeurM3();
                
            H2 titre_View = new H2("Registre des Matériaux");
            Button B1 = new Button ("Supprimer un matériau",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un matériau",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                        
            
            B1.addClickListener(click -> {
                Set<materiaux> selectedItems = grid.getSelectedItems();
                
                if(selectedItems.isEmpty()) {
                    Notification.show("Aucune ligne selectionnée");
                
                } else {
                    materiaux selectedBean = selectedItems.iterator().next();
                    int value = selectedBean.getId();
                    try {GestionBDD.deleteMateriaux(con, value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateMateriaux(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Matériau "+ selectedBean.getDes()+" supprimé avec succès" , 5000, Notification.Position.TOP_CENTER);
                }
            });
            
              
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouveau matériau");

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
            
            // Créer une grille avec les colonnes
            List<Materiaux.materiaux> Materiaux = GestionBDD.GetMateriaux(con);
            grid.addColumn(materiaux::getId).setHeader("Id");
            grid.addColumn(materiaux::getDes).setHeader("des");
            grid.addColumn(materiaux::getPrix).setHeader("prix €");
            grid.setItems(Materiaux);
            
            add(
                titre_View, 
                new VerticalLayout(grid),
                new HorizontalLayout(B2, B1) 
            );        
    }
    
    private VerticalLayout createDialogLayout(Dialog dialog) throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();

        TextField des = new TextField("Description");

        NumberField prix = new NumberField("Prix");
        Div WSufix = new Div();
        WSufix.setText("€");
        prix.setSuffixComponent(WSufix);

        VerticalLayout dialogLayout = new VerticalLayout(des, prix);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        Button saveButton = new Button("Ajout");
        dialog.getFooter().add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            try {
                createMateriaux(con, des.getValue(), prix.getValue());
                Notification.show("Le matériau a été créé vec succès");
                dialog.close();
                try {UpdateMateriaux(con);} catch (SQLException ex){ex.printStackTrace();}

            } catch (SQLException ex) {
                // Gérer l'exception, par exemple, afficher un message d'erreur
                ex.printStackTrace();
            }
        });
        return dialogLayout;
    }


    private void UpdateMateriaux (Connection con) throws SQLException{
        List<materiaux> Materiaux = GestionBDD.GetMateriaux(con);
        grid.setItems(Materiaux);
    }

}
    


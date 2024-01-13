/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.insa.info_s3.Clients.Client;
import static com.insa.info_s3.GestionBDD.createClient;
import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import com.insa.info_s3.Machines.Machine;
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

@Route(value = "clients_View", layout = UI.class)
public class Clients_View extends Div{
    
    private Grid<Client> grid = new Grid<>();
    
    public Clients_View() throws SQLException{
        
        Connection con = GestionBDD.connectSurServeurM3();
        
            H2 titre_View = new H2("Registre des clients");
            Button B1 = new Button ("Supprimer un client ",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un client",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                //getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            
            B1.addClickListener(click -> {
                Set<Client> selectedItems = grid.getSelectedItems();
                
                if(selectedItems.isEmpty()) {
                    Notification.show("Aucune ligne selectionnée");
                
                } else {
                    Client selectedBean = selectedItems.iterator().next();
                    int value = selectedBean.getId();
                    try {GestionBDD.deleteClient(con, value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateClients(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Client "+ selectedBean.getPrenom()+" "+selectedBean.getNom()+" supprimé avec succès" , 5000, Notification.Position.TOP_CENTER);
                }
            });
            
              
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouveau Client");

                VerticalLayout dialogLayout;
                
                try {

                    dialogLayout = createDialogLayout(dialog);

                    dialog.add(dialogLayout);
                    Button cancelButton = new Button("Cancel", e -> dialog.close());
                    dialog.getFooter().add(cancelButton);
                    


                dialog.open();
                } catch (SQLException ex) {
                    Logger.getLogger(machine_View.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
            
            // Créer une grille avec les colonnes
            List<Client> Clients = GestionBDD.GetClients(con);
            grid.addColumn(Client::getId).setHeader("Id");
            grid.addColumn(Client::getPrenom).setHeader("Prenom");
            grid.addColumn(Client::getNom).setHeader("Nom");
            grid.setItems(Clients);
           
            add(
                titre_View, 
                grid,
                new HorizontalLayout(B1, B2, actualiser) 
                );
        
    }
    
    private VerticalLayout createDialogLayout(Dialog dialog) throws SQLException {
    Connection con = GestionBDD.connectSurServeurM3();
    
    TextField nom = new TextField("nom");
    TextField prenom = new TextField("prenom");
    
    
   
    VerticalLayout dialogLayout = new VerticalLayout(nom, prenom);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

    Button saveButton = new Button("Add");
    dialog.getFooter().add(saveButton);
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(e -> {
        try {
            createClient(con,nom.getValue(), prenom.getValue());
            dialog.close();
            try {UpdateClients(con);} catch (SQLException ex){ex.printStackTrace();}
            
        } catch (SQLException ex) {
            // Gérer l'exception, par exemple, afficher un message d'erreur
            ex.printStackTrace();

        }
    });

    

    return dialogLayout;
}
    
    private void UpdateClients (Connection con) throws SQLException{
        List<Client> Clients = GestionBDD.GetClients(con);
        grid.setItems(Clients);
    }
    
}

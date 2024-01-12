/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMachine;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loicrosian
 */
public class materiaux_View {
    
    public materiaux_View() throws SQLException{
        try (Connection con = GestionBDD.connectSurServeurM3()){
            H2 titre_View = new H2("Registre des Matériaux");
            Button B1 = new Button ("Supprimer un matériau",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un matériau",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button actualiser = new Button("Actualiser");
            actualiser.addClickListener(e -> {
                // Utiliser la classe UI pour naviguer à la vue principale
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            B1.addClickListener(click -> {
                /*TextField Nom1 = new TextField("id de la machine");
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
                    valider);*/
            });
              
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();

                dialog.setHeaderTitle("Nouveau matériau");

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
                
                

                //Button saveButton = createSaveButton(dialog);
                //Button cancelButton = new Button("Cancel", e -> dialog.close());
                //dialog.getFooter().add(cancelButton);
                //dialog.getFooter().add(saveButton);
                //dialog.open();
            });
            
            // Créer une grille avec les colonnes
            List<Machines.Machine> Machines = GestionBDD.Getmachine(con);
            Grid<Machines.Machine> grid = new Grid<>();
            grid.addColumn(Machines.Machine::getId).setHeader("Id");
            grid.addColumn(Machines.Machine::getRef).setHeader("ref");
            grid.addColumn(Machines.Machine::getDes).setHeader("des");
            grid.addColumn(Machines.Machine::getPuissance).setHeader("Puissance");
            grid.addColumn(Machines.Machine::getEtatmachine).setHeader("Etat Machine");
            grid.setItems(Machines);
           
            add(
                titre_View, 
                grid,
                new HorizontalLayout(B1, B2, actualiser) 
                );
        }
    }

    
    public void afficherNotification(String message) throws SQLException {
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
    
    TextField id = new TextField("Référence");
    TextField des = new TextField("Description");
    
    IntegerField puissance = new IntegerField("Puissance");
    Div WSufix = new Div();
    WSufix.setText("Watt");
    puissance.setSuffixComponent(WSufix);
    
    List<String> items = new ArrayList<>(
            Arrays.asList("Inactif", "Actif"));
    
    ComboBox<String> comboBox = new ComboBox<>("Etat de la machine ");
    comboBox.setAllowCustomValue(true);
    comboBox.addCustomValueSetListener(e -> {
        String customValue = e.getDetail();
        items.add(customValue);
        comboBox.setItems(items);
        comboBox.setValue(customValue);
        });
    comboBox.setItems(items);
    comboBox.setHelperText("sélectionner l'état de la machine");
   
    VerticalLayout dialogLayout = new VerticalLayout(id, des, puissance, comboBox);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

    Button saveButton = new Button("Add");
    dialog.getFooter().add(saveButton);
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(e -> {
        try {
            int state = getValueComboBox(comboBox);
            createMachine(con, id.getValue(), des.getValue(), puissance.getValue(), state);
            dialog.close();
            
        } catch (SQLException ex) {
            // Gérer l'exception, par exemple, afficher un message d'erreur
            ex.printStackTrace();

        }
    });

    

    return dialogLayout;
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
    


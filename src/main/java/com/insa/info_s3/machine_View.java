/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createRealise;
import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import static com.insa.info_s3.GestionBDD.getidMachineByRef;
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
import java.util.Set;

/**
 *
 * @author loicrosian
 */

@Route(value = "machine_View", layout = UI.class)
public class machine_View extends Div {
    
    private Grid<Machine> grid = new Grid<>();
    private ComboBox<Operations.Operation> comboop = new ComboBox<>("type d'opérations");
    private TextField ref = new TextField("Référence");
    private TextField des = new TextField("Description");
    private ComboBox<String> comboBox = new ComboBox<>("Etat de la machine ");
    private NumberField duree = new NumberField("Durée");
    
    public machine_View() throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();
            
            H2 titre_View = new H2("Registre des machines");
            Button B1 = new Button ("Supprimer une machine ",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter une machine",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
                        
            List<Machine> Machines = GestionBDD.Getmachine(con);
            grid.addColumn(Machine::getId).setHeader("Id");
            grid.addColumn(Machine::getRef).setHeader("ref");
            grid.addColumn(Machine::getDes).setHeader("des");
            grid.addColumn(Machine::getPuissance).setHeader("Puissance");
            grid.addColumn(Machine::getEtatmachine).setHeader("Etat Machine");
            grid.setItems(Machines);
            
            add(
                titre_View, 
                new VerticalLayout(grid),
                new HorizontalLayout(B2, B1) 
            );
            
            
            B1.addClickListener(click -> {
                Set<Machine> selectedItems = grid.getSelectedItems();
                
                if(selectedItems.isEmpty()) {
                    Notification.show("Aucune ligne selectionnée");
                
                } else {
                    Machine selectedBean = selectedItems.iterator().next();
                    int value = selectedBean.getId();
                    try {GestionBDD.deleteMachine(con, value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateMachines(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Machine "+ selectedBean.getDes()+" supprimée avec succès" , 5000, Notification.Position.TOP_CENTER);
                }
            });
              
            
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Nouvelle machine");
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
    
        

        IntegerField puissance = new IntegerField("Puissance");
        Div WSufix = new Div();
        WSufix.setText("Watt");
        puissance.setSuffixComponent(WSufix);
        
        List<Operations.Operation> Operations = GestionBDD.GetOperation(con);
        
        comboop.setAllowCustomValue(true);
        comboop.setItems(Operations);
        comboop.setHelperText("sélectionner l'opération que réalise la machine");
        
        
        Div DSufix = new Div();
        DSufix.setText("secondes");
        duree.setSuffixComponent(DSufix);

        List<String> items = new ArrayList<>(
                Arrays.asList("Inactif", "Actif"));
        comboBox.setAllowCustomValue(true);
        comboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            items.add(customValue);
            comboBox.setItems(items);
            comboBox.setValue(customValue);
            });
        comboBox.setItems(items);
        comboBox.setHelperText("sélectionner l'état de la machine");

        VerticalLayout dialogLayout = new VerticalLayout(ref, des, puissance, comboBox,comboop, duree);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        Button saveButton = new Button("Ajout");
        dialog.getFooter().add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            if (ChampRemplis()){
                try {
                    int state = getValueComboBox(comboBox);
                    createMachine(con, ref.getValue(), des.getValue(), puissance.getValue(), state);
                    createRealise(con, getidMachineByRef(con, ref.getValue()),comboop.getValue().getId(),duree.getValue().longValue());
                    Notification.show("La machine a été crée avec succès");
                    dialog.close();
                    try {UpdateMachines(con);} catch (SQLException ex){ex.printStackTrace();}
                
                } catch (SQLException ex) {
                    // Gérer l'exception, par exemple, afficher un message d'erreur
                    ex.printStackTrace();
                }
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
    
    
    private void UpdateMachines (Connection con) throws SQLException{
        List<Machine> Machines = GestionBDD.Getmachine(con);
        grid.setItems(Machines);
    }
    
    public boolean ChampRemplis(){
        if (ref.getValue()!=null && des.getValue()!=null && duree.getValue()!=null && comboBox.getValue()!=null && comboop.getValue()!=null){
            return true;
        }
        return false ;
    }
    
}
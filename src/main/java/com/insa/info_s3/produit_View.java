/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import static com.insa.info_s3.GestionBDD.createMachine;
import static com.insa.info_s3.GestionBDD.createOperation;
import static com.insa.info_s3.GestionBDD.createProduit;
import static com.insa.info_s3.GestionBDD.createRealise;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import static com.insa.info_s3.GestionBDD.getTableValue;
import static com.insa.info_s3.GestionBDD.deleteProduit;
import static com.insa.info_s3.GestionBDD.getIdProduitByDesProduit;
import static com.insa.info_s3.GestionBDD.getIdproduitByDes;
import static com.insa.info_s3.GestionBDD.getidMachineByRef;
import com.insa.info_s3.Materiaux.materiaux;
import com.insa.info_s3.Produit.Produits;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author loicrosian
 */

@Route(value = "produit_View", layout = UI.class)
public class produit_View extends Div {
    
    private Grid<Produits> grid = new Grid<>();
    private ComboBox<Materiaux.materiaux> combomat = new ComboBox<>("matériaux");
    private TextField ref = new TextField("Référence");
    private TextField des = new TextField("Description");
    private IntegerField poids = new IntegerField("poids");
    private ComboBox<Operations.Operation> comboop = new ComboBox<>("type d'opérations");
    
    public produit_View() throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();
           
            H2 titre_View = new H2("Registre des produits");
            Button B1 = new Button ("Supprimer un produit",VaadinIcon.TRASH.create());
            Button B2 = new Button ("Ajouter un produit",VaadinIcon.PLUS.create());
            B1.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            B2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
                         
            B2.addClickListener(click -> {
                
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Nouveau produit");
                VerticalLayout dialogLayout;
                
                try {

                    dialogLayout = createDialogLayout(dialog);
                    
                    dialog.add(dialogLayout);
                    Button cancelButton = new Button("Annuler", e -> dialog.close());
                    dialog.getFooter().add(cancelButton);
                   
                    dialog.open();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(produit_View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
         
            List<Produits> Produit = GestionBDD.GetProduits(con);
            grid.addColumn(Produits::getId).setHeader("Id");
            grid.addColumn(Produits::getRef).setHeader("ref");
            grid.addColumn(Produits::getDes).setHeader("des");
            grid.addColumn(Produits::getMateriaux).setHeader("Matériaux");
            grid.addColumn(Produits::getPoids).setHeader("Poids");
            grid.setItems(Produit);

            add(
                titre_View, 
                new VerticalLayout(grid),
                new HorizontalLayout(B2, B1) 
                );
            
            
            B1.addClickListener(click -> {
                 Set<Produits> selectedItems = grid.getSelectedItems();
    
                if (selectedItems.isEmpty()) {
                    Notification.show("Aucune ligne sélectionnée", 2000, Notification.Position.TOP_CENTER);
                
                } else {
                    Produits selectedBean = selectedItems.iterator().next();
                    int prop1Value = selectedBean.getId();
                    try {GestionBDD.deleteProduit(con, prop1Value);} catch (SQLException ex){ex.printStackTrace();}
                    try {UpdateProduit(con);} catch (SQLException ex){ex.printStackTrace();}
                    
                    Notification.show("Produit "+ selectedBean.getDes()+" supprimé avec succès : " , 5000, Notification.Position.TOP_CENTER);
                }
            });
    }
    
    
    private VerticalLayout createDialogLayout(Dialog dialog) throws SQLException {

        Connection con = GestionBDD.connectSurServeurM3();
        
       
        
        List<Operations.Operation> Operations = GestionBDD.GetOperation(con);
        
        comboop.setAllowCustomValue(true);
        comboop.setItems(Operations);
        comboop.setHelperText("sélectionner le type de l'opération");
    
        List<Materiaux.materiaux> Materiaux = GestionBDD.GetMateriaux(con);
        
        combomat.setAllowCustomValue(true);
        
        combomat.setItems(Materiaux);
        combomat.setHelperText("sélectionner le matériau du produit");
        
        
        Div PSufix = new Div();
        PSufix.setText("kg");
        poids.setSuffixComponent(PSufix);

        VerticalLayout dialogLayout = new VerticalLayout(ref, des,poids,combomat, comboop);
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
                    createProduit(con, ref.getValue(), des.getValue(), combomat.getValue().getId(),poids.getValue());
                    createOperation(con, comboop.getValue().getId(),getIdProduitByDesProduit(con,des.getValue()));
                    Notification.show("Le produit a été crée avec succès");
                    dialog.close();
                    try {UpdateProduit(con);} catch (SQLException ex){ex.printStackTrace();}
                
                } catch (SQLException ex) {
                    // Gérer l'exception, par exemple, afficher un message d'erreur
                    ex.printStackTrace();
                }
            }
        });
        return dialogLayout;
    }
    
    
    private void UpdateProduit (Connection con) throws SQLException{
        List<Produits> Produit = GestionBDD.GetProduits(con);
        grid.setItems(Produit);
    }
    
    public boolean ChampRemplis(){
        if (ref.getValue()!=null && des.getValue()!=null && poids.getValue()!=null && combomat.getValue()!=null && comboop.getValue()!= null){
            return true;
        }
        return false ;
    }
    
}

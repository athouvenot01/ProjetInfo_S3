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
import com.insa.info_s3.Produit.Produits;
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
import java.util.Set;

/**
 *
 * @author loicrosian
 */

@Route(value = "produit_View", layout = UI.class)
public class produit_View extends Div {
    private Grid<Produits> grid = new Grid<>();
    public produit_View() throws SQLException {
        
        Connection con = GestionBDD.connectSurServeurM3();
           
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
            
         
            List<Produits> Produit = GestionBDD.GetProduits(con);
            grid.addColumn(Produits::getId).setHeader("Id");
            grid.addColumn(Produits::getRef).setHeader("ref");
            grid.addColumn(Produits::getDes).setHeader("des");
            grid.addColumn(Produits::getMateriaux).setHeader("Matériaux");
            grid.setItems(Produit);

            
            add(
                titre_View, 
                new VerticalLayout(grid),
                new HorizontalLayout(B1, B2, actualiser) 
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
    private void UpdateProduit (Connection con) throws SQLException{
    List<Produits> Produit = GestionBDD.GetProduits(con);
    grid.setItems(Produit);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import static com.insa.info_s3.GestionBDD.getTableValue;
import com.vaadin.flow.component.button.Button;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author loicrosian
 */

@Route(value = "produit_View", layout = UI.class)
public class produit_View extends Div {
    
    public produit_View() {
        
        try (Connection con = GestionBDD.connectSurServeurM3()){
            /*// Créez un composant Grid
                Grid<Object> liste_produit = new Grid<>(Object.class);
                
                // Ajoutez les colonnes au Grid
                liste_produit.setColumns();
                
                // Ajoutez les lignes au Grid
                liste_produit.setItems();
                
                // Ajoutez le Grid à la mise en page
                add(liste_produit);
            */
            
            /*Object[][] liste_produit = getTableValue(con, produit);
            add(liste_produit);
            */
            
            List<String> chaines = Arrays.asList(
                "Chaine 1",
                "Chaine 2",
                "Chaine 3"
            );
            Grid<String> grid = new Grid<>();
            grid.setItems(chaines);
            
            grid.addColumn(String::toString).setHeader("Colonne de Chaînes");

            add(grid);
            
            
            
        } catch (SQLException ex) {
            System.out.println("probleme : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    
    }
}

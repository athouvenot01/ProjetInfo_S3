/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import static com.insa.info_s3.GestionBDD.getTableValue;

/**
 *
 * @author loicrosian
 */

@Route(value = "produit_View", layout = UI.class)
public class produit_View extends Div {
    
   Grid <String[]> liste_produit = new Grid<>();
   liste_produit = getTableValue(con, "produit");
   
   add(liste_produit);
  
   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.insa.info_s3;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

/**
 *
 * @author loicrosian
 */
@Route
public class UI extends VerticalLayout {
    
     public UI() {
        
        H1 title = new H1 ("Base de donnée");
        Button produit = new Button ("produit");
        Button machine = new Button ("machine"); 
        Button realise = new Button ("réalise ");
        Button typeoperation = new Button ("type d'opération");
        
        produit.addClickListener(click ->{
            Grid<String> liste_produit = new Grid<>();
            liste_produit.setHeight("auto");
            liste_produit = getTableValue(conn, "produit");
            add(liste_produit);
        });
       
         machine.addClickListener(click ->{
            Grid<String> liste_machine = new Grid<>();
            liste_machine.setHeight("auto");
            liste_machine = getTableValue(conn, "produit");
            add(liste_machine);
            
        });
         
          realise.addClickListener(click ->{
            Grid<String> liste_realise = new Grid<>();
            liste_realise.setHeight("auto");
            liste_realise = getTableValue(conn, "produit");
            add(liste_realise);
        });
          
           typeoperation.addClickListener(click ->{
            Grid<String> liste_typeoperation = new Grid<>();
            liste_typeoperation.setHeight("auto");
            liste_typeoperation = getTableValue(conn, "produit");
            add(liste_typeoperation);
        });
        
           
        add(
            new HorizontalLayout(title), 
            new VerticalLayout(produit, machine, realise, typeoperation));
         
         
         
         
         
         
         
         
         
         /*H1 titre = new H1 ("liste de tâches à faire");
        VerticalLayout todolist = new VerticalLayout() ;
        TextArea taches = new TextArea();
        Button bouton_ajout = new Button("ajouter la tâches à faire");
        bouton_ajout.addClickListener( click -> {
            Checkbox verification = new Checkbox (taches.getValue());
            todolist.add(verification);
        });
        bouton_ajout.addClickShortcut(Key.ENTER);
        
        add(titre, todolist, new HorizontalLayout(taches, bouton_ajout));
        */
    }          
    
}

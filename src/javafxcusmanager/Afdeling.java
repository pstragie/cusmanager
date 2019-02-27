/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Afdeling {
    
    private SimpleStringProperty afdelingsnaam;
    private SimpleStringProperty afdelingscategorie;
    
    public Afdeling(String afdelingsnaamString, String afdelingscategorieString) {
        this.afdelingsnaam = new SimpleStringProperty(afdelingsnaamString);
        this.afdelingscategorie = new SimpleStringProperty(afdelingscategorieString);
    }
    
    public String getAfdelingsNaam() {
        return afdelingsnaam.get();
    }
    public void setAfdelingsNaam(String afdelingsnaamString) {
        afdelingsnaam.set(afdelingsnaamString);
    }
    public String getAfdelingsCategorie() {
        return afdelingscategorie.get();
    }
    public void setAfdelingsCategorie(String afdelingscategorieString) {
        afdelingscategorie.set(afdelingscategorieString);
    }
    
    public StringProperty afdelingsnaamProperty() {
        return afdelingsnaam;
    }
    public StringProperty afdelingscategorieProperty() {
        return afdelingscategorie;
    }
    @Override
    public String toString() {
        String s = new String();
        s = afdelingsnaam.get();
        return s;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Umpire {
    private final SimpleStringProperty umpirenaam;
    private final SimpleStringProperty umpirelicentie;
    private final SimpleStringProperty umpirestraat;
    private final SimpleStringProperty umpirehuisnummer;
    private final SimpleStringProperty umpirepostcode;
    private final SimpleStringProperty umpirestad;
    private final SimpleStringProperty umpiretelefoon;
    private final SimpleStringProperty umpireemail;
    private final SimpleStringProperty umpireclub;
    private Boolean actief;
    private final ArrayList<Afdeling> umpireafdelingen;
    

    public Umpire(String umpirenaamString, String umpirelicentieString, String umpirestraatString, String umpirehuisnummerString, String umpirepostcodeString, String umpirestadString, String umpiretelefoonString, String umpireemailString, String umpireclubString, ArrayList umpireafdelingenArray, Boolean bool) {
        this.umpirenaam = new SimpleStringProperty(umpirenaamString);
        this.umpirelicentie = new SimpleStringProperty(umpirelicentieString);
        this.umpirestraat = new SimpleStringProperty(umpirestraatString);
        this.umpirehuisnummer = new SimpleStringProperty(umpirehuisnummerString);
        this.umpirepostcode = new SimpleStringProperty(umpirepostcodeString);
        this.umpirestad = new SimpleStringProperty(umpirestadString);
        this.umpiretelefoon = new SimpleStringProperty(umpiretelefoonString);
        this.umpireemail = new SimpleStringProperty(umpireemailString);
        this.umpireclub = new SimpleStringProperty(umpireclubString);
        this.umpireafdelingen = new ArrayList<Afdeling>(umpireafdelingenArray);
        this.actief = new Boolean(bool);
    }

    public String getUmpireNaam() {
        return umpirenaam.get();
    }
    public void setUmpireNaam(String umpirenaamString) {
        umpirenaam.set(umpirenaamString);
    }
    public String getUmpireLicentie() {
        return umpirelicentie.get();
    }
    public void setUmpireLicentie(String umpirelicentieString) {
        umpirelicentie.set(umpirelicentieString);
    }

    public String getUmpireStraat() {
        return umpirestraat.get();
    }
    public void setUmpireStraat(String umpirestraatString) {
        umpirestraat.set(umpirestraatString);
    }
    public String getUmpireHuisnummer() {
        return umpirehuisnummer.get();
    }
    public void setUmpireHuisnummer(String umpirehuisnummerString) {
        umpirehuisnummer.set(umpirehuisnummerString);
    }

    public String getUmpirePostcode() {
        return umpirepostcode.get();
    }
    public void setUmpirePostcode(String umpirepostcodeString) {
        umpirepostcode.set(umpirepostcodeString);
    }

    public String getUmpireStad() {
        return umpirestad.get();
    }
    public void setUmpireStad(String umpirestadString) {
        umpirestad.set(umpirestadString);
    }
    public String getUmpireTelefoon() {
        return umpiretelefoon.get();
    }
    public void setUmpireTelefoon(String umpiretelefoonString) {
        umpiretelefoon.set(umpiretelefoonString);
    }

    public String getUmpireEmail() {
        return umpireemail.get();
    }
    public void setUmpireEmail(String umpireemailString) {
        umpireemail.set(umpireemailString);
    }

    public String getUmpireClub() {
        return umpireclub.get();
    }
    public void setUmpireClub(String umpireclubString) {
        umpireclub.set(umpireclubString);
    }
    public ArrayList<Afdeling> getUmpireAfdelingen() {
        return umpireafdelingen;
    }

    public void setUmpireAfdelingen(Afdeling umpireafdelingenArray) {
        umpireafdelingen.add(umpireafdelingenArray);
    }
    
    public Boolean getActief() {
        return actief.booleanValue();
    }
    public void setActief(Boolean bool) {
        actief = bool;
    }

    public StringProperty umpirenaamProperty() {
        return umpirenaam;
    }
    public StringProperty umpirelicentieProperty() {
        return umpirelicentie;
    }
    public StringProperty umpirestraatProperty() {
        return umpirestraat;
    }
    public StringProperty umpirehuisnummerProperty() {
        return umpirehuisnummer;
    }
    public StringProperty umpirepostcodeProperty() {
        return umpirepostcode;
    }
    public StringProperty umpirestadProperty() {
        return umpirestad;
    }
    public StringProperty umpiretelefoonProperty() {
        return umpiretelefoon;
    }
    public StringProperty umpireemailProperty() {
        return umpireemail;
    }
    
    public ArrayList<Afdeling> umpireafdelingenProperty() {
        return umpireafdelingen;
    }
    
    @Override
    public String toString() {
        String string = new String();
        
        
        string = umpirenaam.get() + ", " + umpirelicentie.get() + ", " + umpirestraat.get() + ", " + umpirehuisnummer.get() + ", " + umpirepostcode.get() + ", " + umpirestad.get() + " " + umpiretelefoon.get() + ", " + umpireemail.get() + ", " + umpireclub.get() + "\n";

        return string;
    }
}

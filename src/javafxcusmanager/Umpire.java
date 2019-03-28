/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Pieter Stragier
 * @author www.pws-solutions.be
 * @version 1.0
 * @since 1.0
 */
public class Umpire {
    private final SimpleStringProperty umpirenaam;
    private final SimpleStringProperty umpirevoornaam;
    private final SimpleStringProperty umpirelicentie;
    private final SimpleStringProperty umpirestraat;
    private final SimpleStringProperty umpirehuisnummer;
    private final SimpleStringProperty umpirepostcode;
    private final SimpleStringProperty umpirestad;
    private final SimpleStringProperty umpiretelefoon;
    private final SimpleStringProperty umpireemail;
    //private final SimpleStringProperty umpireclub;
    private final ObjectProperty<Club> umpireclub;
    private Boolean actief;
    private final ArrayList<Afdeling> umpireafdelingen;
    private final SimpleStringProperty latitude;
    private final SimpleStringProperty longitude;
    /** Represents the umpires details
     * 
     * @param umpirenaamString
     * @param umpirevoornaamString
     * @param umpirelicentieString
     * @param umpirestraatString
     * @param umpirehuisnummerString
     * @param umpirepostcodeString
     * @param umpirestadString
     * @param umpiretelefoonString
     * @param umpireemailString
     * @param umpireclubString
     * @param umpireafdelingenArray
     * @param bool
     * @param latitudeString
     * @param longitudeString 
     */
    public Umpire(String umpirenaamString, String umpirevoornaamString, String umpirelicentieString, String umpirestraatString, String umpirehuisnummerString, String umpirepostcodeString, String umpirestadString, String umpiretelefoonString, String umpireemailString, Club umpireclubString, ArrayList umpireafdelingenArray, Boolean bool, String latitudeString, String longitudeString) {
        this.umpirenaam = new SimpleStringProperty(umpirenaamString);
        this.umpirevoornaam = new SimpleStringProperty(umpirevoornaamString);
        this.umpirelicentie = new SimpleStringProperty(umpirelicentieString);
        this.umpirestraat = new SimpleStringProperty(umpirestraatString);
        this.umpirehuisnummer = new SimpleStringProperty(umpirehuisnummerString);
        this.umpirepostcode = new SimpleStringProperty(umpirepostcodeString);
        this.umpirestad = new SimpleStringProperty(umpirestadString);
        this.umpiretelefoon = new SimpleStringProperty(umpiretelefoonString);
        this.umpireemail = new SimpleStringProperty(umpireemailString);
        this.umpireclub = new SimpleObjectProperty(umpireclubString);
        this.umpireafdelingen = new ArrayList<Afdeling>(umpireafdelingenArray);
        this.actief = new Boolean(bool);
        this.latitude = new SimpleStringProperty(latitudeString);
        this.longitude = new SimpleStringProperty(longitudeString);
    }
    
    /** Gets the umpire's last name.
     * 
     * @return A string representing the umpire's last name. 
     */
    public String getUmpireNaam() {
        return umpirenaam.get();
    }
    /** Sets the umpire's last name.
     * 
     * @param umpirenaamString A String containing the umpire's last name.
     */
    public void setUmpireNaam(String umpirenaamString) {
        umpirenaam.set(umpirenaamString);
    }
    /** Gets the umpire's first name.
     * 
     * @return A string representing the umpire's first name.
     */
    public String getUmpireVoornaam() {
        return umpirevoornaam.get();
    }
    /** Sets the umpire's first name.
     * 
     * @param umpirevoornaamString A String containing the umpire's first name.
     */
    public void setUmpireVoornaam(String umpirevoornaamString) {
        umpirevoornaam.set(umpirevoornaamString);
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

    public Club getUmpireClub() {
        return umpireclub.get();
    }
    public void setUmpireClub(Club umpireclubString) {
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

    public String getLatitude() {
        return latitude.get();
    }
    public void setLatitude(String latitudeString) {
        latitude.set(latitudeString);
    }
    public String getLongitude() {
        return longitude.get();
    }
    public void setLongitude(String longitudeString) {
        longitude.set(longitudeString);
    }
    
    public StringProperty umpirenaamProperty() {
        return umpirenaam;
    }
    public StringProperty umpirevoornaamProperty() {
        return umpirevoornaam;
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
    public StringProperty latitudeProperty() {
        return latitude;
    }
    public StringProperty longitudeProperty() {
        return longitude;
    }
    @Override
    public String toString() {
        String string = new String();
        string = umpirenaam.get() + ", " + umpirevoornaam.get();

        return string;
    }
}

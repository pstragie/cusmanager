/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Club {

    private final SimpleStringProperty clubnaam;
    private final SimpleStringProperty liga;
    private final SimpleStringProperty clubnummer;
    private final SimpleStringProperty clubvoorzitter;
    private final SimpleStringProperty clubemail;
    private final SimpleStringProperty clubtelefoon;
    private final SimpleStringProperty clubstraat;
    private final SimpleStringProperty clubstraatnummer;
    private final SimpleStringProperty clubpostcode;
    private final SimpleStringProperty clubstad;
    private final SimpleStringProperty clubwebsite;
    private Boolean visible;
    private final ArrayList<Team> clubteams;
    

    public Club(String clubnaamString, String ligaString, String clubnummerString, String clubvoorzitterString, String clubstraatString, String clubstraatnummerString, String clubpostcodeString, String clubstadString, String clubemailString, String clubtelefoonString, String clubwebsiteString, ArrayList clubteamsArray, Boolean bool) {
        this.clubnaam = new SimpleStringProperty(clubnaamString);
        this.liga = new SimpleStringProperty(ligaString);
        this.clubnummer = new SimpleStringProperty(clubnummerString);
        this.clubvoorzitter = new SimpleStringProperty(clubvoorzitterString);
        this.clubemail = new SimpleStringProperty(clubemailString);
        this.clubtelefoon = new SimpleStringProperty(clubtelefoonString);
        this.clubstraat = new SimpleStringProperty(clubstraatString);
        this.clubstraatnummer = new SimpleStringProperty(clubstraatnummerString);
        this.clubpostcode = new SimpleStringProperty(clubpostcodeString);
        this.clubstad = new SimpleStringProperty(clubstadString);
        this.clubwebsite = new SimpleStringProperty(clubwebsiteString);
        this.clubteams = new ArrayList<Team>(clubteamsArray);
        this.visible = new Boolean(bool);
    }

    public String getClubNaam() {
        return clubnaam.get();
    }
    public void setClubNaam(String clubnaamString) {
        clubnaam.set(clubnaamString);
    }
    public String getLiga() {
        return liga.get();
    }
    public void setLiga(String ligaString) {
        liga.set(ligaString);
    }
    public String getClubNummer() {
        return clubnummer.get();
    }
    public void setClubNummer(String clubnummerString) {
        clubnummer.set(clubnummerString);
    }

    public String getVoorzitter() {
        return clubvoorzitter.get();
    }
    public void setVoorzitter(String clubvoorzitterString) {
        clubvoorzitter.set(clubvoorzitterString);
    }
    
    public String getClubEmail() {
        return clubemail.get();
    }
    public void setClubEmail(String clubemailString) {
        clubemail.set(clubemailString);
    }

    public String getClubTelefoon() {
        return clubtelefoon.get();
    }
    public void setClubTelefoon(String clubtelefoonString) {
        clubtelefoon.set(clubtelefoonString);
    }

    public String getClubStraat() {
        return clubstraat.get();
    }
    public void setClubStraat(String clubstraatString) {
        clubstraat.set(clubstraatString);
    }
    public String getClubStraatNummer() {
        return clubstraatnummer.get();
    }
    public void setClubStraatNummer(String clubstraatnummerString) {
        clubstraatnummer.set(clubstraatnummerString);
    }

    public String getClubPostcode() {
        return clubpostcode.get();
    }
    public void setClubPostcode(String clubpostcodeString) {
        clubpostcode.set(clubpostcodeString);
    }

    public String getClubStad() {
        return clubstad.get();
    }
    public void setClubStad(String clubstadString) {
        clubstad.set(clubstadString);
    }
    
    public String getClubWebsite() {
        return clubwebsite.get();
    }
    
    public void setClubWebsite(String clubwebsiteString) {
        clubwebsite.set(clubwebsiteString);
    }
    public ArrayList<Team> getClubTeams() {
        return clubteams;
    }

    public void setClubTeams(Team clubteamsArray) {
        clubteams.add(clubteamsArray);
    }

    public Boolean getVisible() {
        return visible.booleanValue();
    }
    public void setVisible(Boolean bool) {
        visible = bool;
    }
    
    public StringProperty clubnaamProperty() {
        return clubnaam;
    }
    public StringProperty ligaProperty() {
        return liga;
    }
    public StringProperty clubstraatProperty() {
        return clubstraat;
    }
    public StringProperty clubnummerProperty() {
        return clubnummer;
    }
    public StringProperty clubstraatnummerProperty() {
        return clubstraatnummer;
    }
    public StringProperty clubpostcodeProperty() {
        return clubpostcode;
    }
    public StringProperty clubstadProperty() {
        return clubstad;
    }
    public StringProperty clubemailProperty() {
        return clubemail;
    }
    public StringProperty clubvoorzitterProperty() {
        return clubvoorzitter;
    }
    public StringProperty clubwebsiteProperty() {
        return clubwebsite;
    }
    
    @Override
    public String toString() {
        String string = new String();
        string = clubnaam.get();
        return string;
    }
}
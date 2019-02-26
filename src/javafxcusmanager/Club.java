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

/**
 *
 * @author pieter
 */
public class Club {

    private final SimpleStringProperty clubnaam;
    private final SimpleStringProperty clubnummer;
    private final SimpleStringProperty clubvoorzitter;
    private final SimpleStringProperty clubemail;
    private final SimpleStringProperty clubtelefoon;
    private final SimpleStringProperty clubstraat;
    private final SimpleStringProperty clubstraatnummer;
    private final SimpleStringProperty clubpostcode;
    private final SimpleStringProperty clubstad;
    private final ObjectProperty<Team> clubteams;
    

    public Club(String clubnaamString, String clubnummerString, String clubvoorzitterString, String clubemailString, String clubtelefoonString, String clubstraatString, String clubstraatnummerString, String clubpostcodeString, String clubstadString, Team clubteamsString) {
        this.clubnaam = new SimpleStringProperty(clubnaamString);
        this.clubnummer = new SimpleStringProperty(clubnummerString);
        this.clubvoorzitter = new SimpleStringProperty(clubvoorzitterString);
        this.clubemail = new SimpleStringProperty(clubemailString);
        this.clubtelefoon = new SimpleStringProperty(clubtelefoonString);
        this.clubstraat = new SimpleStringProperty(clubstraatString);
        this.clubstraatnummer = new SimpleStringProperty(clubstraatnummerString);
        this.clubpostcode = new SimpleStringProperty(clubpostcodeString);
        this.clubstad = new SimpleStringProperty(clubstadString);
        this.clubteams = new SimpleObjectProperty<>(this, "clubteams", clubteamsString);
    }

    public String getClubNaam() {
        return clubnaam.get();
    }
    public void setClubNaam(String clubnaamString) {
        clubnaam.set(clubnaamString);
    }
    public String getClubNummer() {
        return clubnummer.get();
    }
    public void setClubNummer(String clubnummerString) {
        clubnummer.set(clubnummerString);
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
    public ObjectProperty<Team> getClubTeams() {
        return clubteams;
    }

    public void setClubTeams(Team clubteamsString) {
        this.clubteams.set(clubteamsString);
    }


    @Override
    public String toString() {
        String string = new String();
        
        
        string = clubnaam.get() + ", " + clubnummer.get() + ", " + clubvoorzitter.get() + ", " + clubemail.get() + ", " + clubtelefoon.get() + ", " + clubstraat.get() + " " + clubstraatnummer.get() + ", " + clubpostcode.get() + ", " + clubstad.get() + "\n";

        return string;
    }
}
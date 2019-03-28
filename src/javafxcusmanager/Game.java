/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Game {

    private Database database;
    private final SimpleStringProperty gameindex;
    private final SimpleStringProperty afdeling;
    private final SimpleStringProperty week;
    private final SimpleStringProperty hometeam;
    private final SimpleStringProperty visitingteam;
    private final ObjectProperty<Umpire> plateumpire;
    private final ObjectProperty<Umpire> base1umpire;
    private final ObjectProperty<Umpire> base2umpire;
    private final ObjectProperty<Umpire> base3umpire;
    private final SimpleStringProperty gamenumber;
    private final ObjectProperty<LocalDate> gamedatum ;
    private final SimpleStringProperty gameuur;
    private final SimpleStringProperty seizoen;
    private final ObjectProperty<Club> homeclub;

    /** Class Game
     * 
     * @param gameindexString
     * @param afdelingString
     * @param weekString
     * @param gameDate
     * @param gameUur
     * @param homeTeamName
     * @param visitingTeamName
     * @param plateUmpireName
     * @param base1UmpireName
     * @param base2UmpireName
     * @param base3UmpireName
     * @param gamenumberString
     * @param seizoenString 
     */
    public Game(String gameindexString, String afdelingString, String weekString, LocalDate gameDate, String gameUur, String homeTeamName, String visitingTeamName, Umpire plateUmpireName, Umpire base1UmpireName, Umpire base2UmpireName, Umpire base3UmpireName, String gamenumberString, String seizoenString, Club homeclub) {
        this.gameindex = new SimpleStringProperty(gameindexString);
        this.afdeling = new SimpleStringProperty(afdelingString);
        this.week = new SimpleStringProperty(weekString);
        this.gamedatum = new SimpleObjectProperty<>(this, "gamedatum", gameDate);
        this.gameuur = new SimpleStringProperty(gameUur);
        this.hometeam = new SimpleStringProperty(homeTeamName);
        this.visitingteam = new SimpleStringProperty(visitingTeamName);
        this.plateumpire = new SimpleObjectProperty<>(this, "umpirelicentie", plateUmpireName);
        this.base1umpire = new SimpleObjectProperty<>(this, "umpirelicentie", base1UmpireName);
        this.base2umpire = new SimpleObjectProperty<>(this, "umpirelicentie", base2UmpireName);
        this.base3umpire = new SimpleObjectProperty<>(this, "umpirelicentie", base3UmpireName);
        this.gamenumber = new SimpleStringProperty(gamenumberString);
        this.seizoen = new SimpleStringProperty(seizoenString);
        this.homeclub = new SimpleObjectProperty<>(this, "clubnummer", homeclub);
    }

    public Club getHomeClub() {
        return homeclub.get();
    }
    public void setHomeClub(Club club) {
        this.homeclub.set(club);
    }
    /** Get Game Index number
     * 
     * @return 
     */
    public String getGameindex() {
        return gameindex.get();
    }
    
    /** Set Game Index number
     * 
     * @param gameindexString 
     */
    public void setGameindex(String gameindexString) {
        gameindex.set(gameindexString);
        
    }
    /** Get afdeling
     * 
     * @return String afdeling
     */
    public String getAfdelingString() {
        return afdeling.get();
    }
    /** Set afdeling
     * 
     * @param afdelingString 
     */
    public void setAfdelingString(String afdelingString) {
        afdeling.set(afdelingString);
    }
    /** Get week
     * 
     * @return String weeknummer
     */
    public String getWeekString() {
        return week.get();
    }
    /** Set week
     * 
     * @param weekString 
     */
    public void setWeekString(String weekString) {
        week.set(weekString);
    }
    /** Get Name Home team
     * 
     * @return String home team
     */
    public String getHomeTeamName() {
        return hometeam.get();
    }
    /** Set Home team
     * 
     * @param homeTeamName String home team
     */
    public void setHomeTeamName(String homeTeamName) {
        hometeam.set(homeTeamName);
    }
    /** Get Visiting team
     * 
     * @return String visiting team
     */
    public String getVisitingTeamName() {
        return visitingteam.get();
    }
    /** Set Visiting team
     * 
     * @param visitingTeamName String visiting team
     */
    public void setVisitingTeamName(String visitingTeamName) {
        visitingteam.set(visitingTeamName);
    }
    /** Get Plate umpire name and surname
     * 
     * @return String plate umpire name
     */
    public Umpire getPlateUmpireName() {
        return plateumpire.get();
    }
    /** Set plate umpire name
     * 
     * @param plateUmpireName String plate umpire
     */
    public void setPlateUmpireName(Umpire plateUmpireName) {
        this.plateumpire.set(plateUmpireName);
    }
    
    /*public String getBaseUmpireNameString() {
        String s = new String(baseumpires.stream().collect(Collectors.joining(", ")));
        return s;
    }*/
    /** Get First Base umpire
     * 
     * @return String First Base umpire
     */
    public Umpire getBase1UmpireName() {
        return base1umpire.get();
    }
    /** Set First Base umpire
     * 
     * @param base1Ump 
     */
    public void setBase1UmpireName(Umpire base1Ump) {
        this.base1umpire.set(base1Ump);
    }
    /** Get Second Base umpire
     * 
     * @return String Second Base umpire
     */
    public Umpire getBase2UmpireName() {
        return base2umpire.get();
    }
    /** Set Second Base umpire
     * 
     * @param base2Ump 
     */
    public void setBase2UmpireName(Umpire base2Ump) {
        this.base2umpire.set(base2Ump);
    }
    /** Get Third Base umpire
     * 
     * @return String Third Base umpire
     */
     public Umpire getBase3UmpireName() {
        return base3umpire.get();
    }
    /** Set Third Base umpire
     * 
     * @param base3Ump 
     */
    public void setBase3UmpireName(Umpire base3Ump) {
        this.base3umpire.set(base3Ump);
    }
    /** Get Game Date
     * 
     * @return LocalDate class
     */
    public LocalDate getGameDatum() {
        return gamedatum.get();
    }
    /** Set Game Date
     * 
     * @param gameDate LocalDate class
     */
    public void setGameDatum(LocalDate gameDate) {
        this.gamedatum.set(gameDate);
    }
    
    public ObjectProperty<LocalDate> gamedatumProperty() {
        return gamedatum;
    }
    /** Get LocalTime Game time uur:minuten
     * 
     * @return LocalTime class
     */
    public String getGameUur() {
        return gameuur.get();
    }
    /** Set LocalTime Game time uur:minuten
     * 
     * @param gameUur LocalTime class
     */
    public void setGameUur(String gameUur) {
        this.gameuur.set(gameUur);
    }
    public StringProperty gameuurProperty() {
        return gameuur;
    }
    
    public String getGameNumber() {
        return gamenumber.get();
    }
    public void setGameNumber(String gamenumberString) {
        this.gamenumber.set(gamenumberString);
    }
    
    public String getSeizoen() {
        return seizoen.get();
    }
    public void setSeizoen(String seizoenString) {
        this.seizoen.set(seizoenString);
    }
    
    public StringProperty gameindexProperty() {
        return gameindex;
    }
    public StringProperty afdelingProperty() {
        return afdeling;
    }
    public StringProperty weekProperty() {
        return week;
    }
    public StringProperty hometeamProperty() {
        return hometeam;
    }
    public StringProperty visitingteamProperty() {
        return visitingteam;
    }
    public ObjectProperty plateumpireProperty() {
        return plateumpire;
    }
    public ObjectProperty base1umpireProperty() {
        return base1umpire;
    }
    public ObjectProperty base2umpireProperty() {
        return base2umpire;
    }
    public ObjectProperty base3umpireProperty() {
        return base3umpire;
    }
    public StringProperty gamenumberProperty() {
        return gamenumber;
    }
    public StringProperty seizoenProperty() {
        return seizoen;
    }
    public ObjectProperty homeclubProperty() {
        return homeclub;
    }
    
    @Override
    public String toString() {
        String string = afdeling.get() + ", week: " + week.get() + ", " + gamedatum.get() + ", " + gameuur.get() + ", " + gameindex.get() + "\n";
        return string;
    }

    
}

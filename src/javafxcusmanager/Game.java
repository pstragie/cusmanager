/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.time.MonthDay;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Game {

    private final SimpleStringProperty afdeling;
    private final SimpleStringProperty week;
    private final SimpleStringProperty hometeam;
    private final SimpleStringProperty visitingteam;
    private final SimpleStringProperty plateumpire;
    private final SimpleStringProperty base1umpire;
    private final SimpleStringProperty base2umpire;
    private final SimpleStringProperty base3umpire;
    private final ObjectProperty<MonthDay> gamedatum ;
    private final ObjectProperty<LocalTime> gameuur;

    public Game(String afdelingString, String weekString, MonthDay gameDate, LocalTime gameUur, String homeTeamName, String visitingTeamName, String plateUmpireName, String base1UmpireName, String base2UmpireName, String base3UmpireName) {
        this.afdeling = new SimpleStringProperty(afdelingString);
        this.week = new SimpleStringProperty(weekString);
        this.gamedatum = new SimpleObjectProperty<>(this, "gamedatum", gameDate);
        this.gameuur = new SimpleObjectProperty<>(this, "gameuur", gameUur);
        this.hometeam = new SimpleStringProperty(homeTeamName);
        this.visitingteam = new SimpleStringProperty(visitingTeamName);
        this.plateumpire = new SimpleStringProperty(plateUmpireName);
        this.base1umpire = new SimpleStringProperty(base1UmpireName);
        this.base2umpire = new SimpleStringProperty(base2UmpireName);
        this.base3umpire = new SimpleStringProperty(base3UmpireName);
    }

    public String getAfdelingString() {
        return afdeling.get();
    }
    public void setAfdelingString(String afdelingString) {
        afdeling.set(afdelingString);
    }
    public String getWeekString() {
        return week.get();
    }
    public void setWeekString(String weekString) {
        week.set(weekString);
    }
    
    public String getHomeTeamName() {
        return hometeam.get();
    }
    public void setHomeTeamName(String homeTeamName) {
        hometeam.set(homeTeamName);
    }

    public String getVisitingTeamName() {
        return visitingteam.get();
    }
    public void setVisitingTeamName(String visitingTeamName) {
        visitingteam.set(visitingTeamName);
    }

    public String getPlateUmpireName() {
        return plateumpire.get();
    }
    public void setPlateUmpireName(String plateUmpireName) {
        plateumpire.set(plateUmpireName);
    }
    
    /*public String getBaseUmpireNameString() {
        String s = new String(baseumpires.stream().collect(Collectors.joining(", ")));
        return s;
    }*/
    
    public String getBase1UmpireName() {
        return base1umpire.get();
    }
    
    public void setBase1UmpireName(String base1Ump) {
        base1umpire.set(base1Ump);
    }
    
    public String getBase2UmpireName() {
        return base2umpire.get();
    }
    
    public void setBase2UmpireName(String base2Ump) {
        base2umpire.set(base2Ump);
    }
     public String getBase3UmpireName() {
        return base3umpire.get();
    }
    
    public void setBase3UmpireName(String base3Ump) {
        base3umpire.set(base3Ump);
    }
    
    public MonthDay getGameDatum() {
        return gamedatum.get();
    }
        
    
    public void setGameDatum(MonthDay gameDate) {
        this.gamedatum.set(gameDate);
    }

    public ObjectProperty<MonthDay> gamedatumProperty() {
        return gamedatum;
    }

    public LocalTime getGameUur() {
        return gameuur.get();
    }
    public void setGameUur(LocalTime gameUur) {
        this.gameuur.set(gameUur);
    }
    public ObjectProperty<LocalTime> gameuurProperty() {
        return gameuur;
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
    public StringProperty plateumpireProperty() {
        return plateumpire;
    }
    public StringProperty base1umpireProperty() {
        return base1umpire;
    }
    public StringProperty base2umpireProperty() {
        return base2umpire;
    }
    public StringProperty base3umpireProperty() {
        return base3umpire;
    }
    
    
    @Override
    public String toString() {
        String string = new String();
        string = afdeling.get() + ", week: " + week.get() + ", " + gamedatum.get() + ", " + gameuur.get() + ", " + hometeam.get() + ", " + visitingteam.get() + ", " + plateumpire.get() + "\n";
        
        return string;
    }

    
}

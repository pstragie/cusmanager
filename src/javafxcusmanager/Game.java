/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
    private final ArrayList<String> baseumpires;
    private final ObjectProperty<MonthDay> gamedatum ;
    private final ObjectProperty<LocalTime> gameuur;

    public Game(String afdelingString, String weekString, MonthDay gameDate, LocalTime gameUur, String homeTeamName, String visitingTeamName, String plateUmpireName, ArrayList<String> baseUmpArray) {
        this.afdeling = new SimpleStringProperty(afdelingString);
        this.week = new SimpleStringProperty(weekString);
        this.gamedatum = new SimpleObjectProperty<>(this, "gamedatum", gameDate);
        this.gameuur = new SimpleObjectProperty<>(this, "gameuur", gameUur);
        this.hometeam = new SimpleStringProperty(homeTeamName);
        this.visitingteam = new SimpleStringProperty(visitingTeamName);
        this.plateumpire = new SimpleStringProperty(plateUmpireName);
        this.baseumpires = baseUmpArray;
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
    public String getBaseUmpireNameString() {
        String s = new String(baseumpires.stream().collect(Collectors.joining(", ")));
        return s;
    }
    
    public ArrayList<String> getBaseUmpireNames() {
        return baseumpires;
    }
    
    public void setBaseUmpireName(ArrayList<String> baseUmpArray) {
        baseumpires.addAll(baseUmpArray);
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
    
    @Override
    public String toString() {
        String string = new String();
        string = afdeling.get() + ", week: " + week.get() + ", " + gamedatum.get() + ", " + gameuur.get() + ", " + hometeam.get() + ", " + visitingteam.get() + ", " + plateumpire.get() + "\n";
        
        return string;
    }

    
}

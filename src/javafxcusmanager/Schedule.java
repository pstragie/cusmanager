/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author pieter
 */

    
public class Schedule {
    private final SimpleStringProperty afdeling;
    private final SimpleStringProperty week;
    private final ArrayList<Game> games;
    //private final ObservableList<Game> games = FXCollections.observableArrayList();
    
    public Schedule(String afdelingsNaam, String weekString, ArrayList<Game> gameArray) {
        this.afdeling = new SimpleStringProperty(afdelingsNaam);
        this.week = new SimpleStringProperty(weekString);
        this.games = new ArrayList<>(gameArray);
    }

    public String getAfdelingsNaam() {
        return afdeling.get();
    }
    public void setAfdelingsNaam(String afdelingsNaam) {
        afdeling.set(afdelingsNaam);
    }
    public String getWeekString() {
        return week.get();
    }
    public void setWeekString(String weekString) {
        week.set(weekString);
    }
    
    public ArrayList<Game> getGameArray() {
        return games;
    }
    public void setGameArray(ArrayList<Game> gameArray) {
        games.addAll(gameArray);
    }
    
    //@XmlElementWrapper(name="games")
    //@XmlElement(name = "game")
    public List<Game> getGames() {
        return new ArrayList<>(games);
    }

    

}


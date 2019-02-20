/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author pieter
 */
public class Game {

    private final SimpleStringProperty gamedate;
    private final SimpleStringProperty hometeam;
    private final SimpleStringProperty visitingteam;
    private final SimpleStringProperty plateumpire;
    private final ArrayList<String> baseumpires;

    public Game(String gameDateString, String homeTeamName, String visitingTeamName, String plateUmpireName, ArrayList<String> baseUmpArray) {
        this.gamedate = new SimpleStringProperty(gameDateString);
        this.hometeam = new SimpleStringProperty(homeTeamName);
        this.visitingteam = new SimpleStringProperty(visitingTeamName);
        this.plateumpire = new SimpleStringProperty(plateUmpireName);
        this.baseumpires = baseUmpArray;
    }

    public String getGameDateString() {
        return gamedate.get();
    }
    public void setGameDateString(String gameDateString) {
        gamedate.set(gameDateString);
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



}

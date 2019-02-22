/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author pieter
 */
public class Team {
    
    private final SimpleStringProperty teamnaam;
    private final SimpleStringProperty teamafdeling;
    
    public Team(String teamnaamString, String teamafdelingString) {
        this.teamnaam = new SimpleStringProperty(teamnaamString);
        this.teamafdeling = new SimpleStringProperty(teamafdelingString);
    }
    
    public String getTeamNaam() {
        return teamnaam.get();
    }
    public void setTeamNaam(String teamnaamString) {
        teamnaam.set(teamnaamString);
    }
    public String getTeamAfdeling() {
        return teamafdeling.get();
    }
    public void setTeamAfdeling(String teamafdelingString) {
        teamafdeling.set(teamafdelingString);
    }
    
    @Override
    public String toString() {
        String s = new String();
        s = teamnaam.get() + ": " + teamafdeling.get();
        return s;
    }
}

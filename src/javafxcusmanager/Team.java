/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Team {
    
    private SimpleStringProperty teamNaam;
    private SimpleStringProperty teamAfdeling;
    
    public Team(String teamnaam, String teamafdeling) {
        this.teamNaam = new SimpleStringProperty(teamnaam);
        this.teamAfdeling = new SimpleStringProperty(teamafdeling);
    }
    
    public String getTeamNaam() {
        return teamNaam.get();
    }
    public void setTeamNaam(String teamnaam) {
        teamNaam.set(teamnaam);
    }
    public String getTeamAfdeling() {
        return teamAfdeling.get();
    }
    public void setTeamAfdeling(String teamafdeling) {
        teamAfdeling.set(teamafdeling);
    }
    
    public StringProperty teamNaamProperty() {
        return teamNaam;
    }
    public StringProperty teamAfdelingProperty() {
        return teamAfdeling;
    }
    @Override
    public String toString() {
        String s = new String();
        s = teamNaam.get() + ": " + teamAfdeling.get();
        return s;
    }
}

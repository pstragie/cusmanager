/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusman;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pieter
 */
public class Team {
    
    private SimpleStringProperty teamNaam;
    private ObjectProperty<Afdeling> teamAfdeling;
    
    public Team(String teamnaam, Afdeling teamafdeling) {
        this.teamNaam = new SimpleStringProperty(teamnaam);
        this.teamAfdeling = new SimpleObjectProperty<>(this, "afdelingsnaam", teamafdeling);
    }
    
    public String getTeamNaam() {
        return teamNaam.get();
    }
    public void setTeamNaam(String teamnaam) {
        teamNaam.set(teamnaam);
    }
    public Afdeling getTeamAfdeling() {
        return teamAfdeling.get();
    }
    public void setTeamAfdeling(Afdeling teamafdeling) {
        this.teamAfdeling.set(teamafdeling);
    }
    
    public StringProperty teamNaamProperty() {
        return teamNaam;
    }
    public ObjectProperty<Afdeling> teamAfdelingProperty() {
        return teamAfdeling;
    }
    
    @Override
    public String toString() {
        String s = new String();
        s = teamNaam.get() + ": " + teamAfdeling.get();
        return s;
    }
}

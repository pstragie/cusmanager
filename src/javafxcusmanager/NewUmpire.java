/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author pieter
 */
public class NewUmpire {
    /**
	 * @param args
	 */
	
	
    /* Paneel met invoervakken voor nieuwe club */
	// Attributen
    private boolean confirmed = false;

    private Umpires umpires;
    public TextField voornaamtf, familienaamtf, clubtf, straattf, huisnummertf, postcodetf, stadtf, afdelingentf;
    private Label vnaamLabel, fnaamLabel, clubLabel, straatLabel, huisnrLabel, pcLabel, stadLabel, afdelingenLabel;
    private Button toevoegen, annuleren;
    private Afdelingen afdlijst;
    private ObservableList<Afdeling> afdelingen;
    private ObservableList<String> afdelingenArray;
    private ComboBox afdelingenComboBox;

    public GridPane NewUmpire(ObservableList afdelingen) {
	
            this.afdelingen = afdelingen;

            // Schakel lay-outmanager uit
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER_LEFT);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));


        
	
	
	
		
		
		
		// Maak de tekstvakken
                
		voornaamtf = new TextField( );
                voornaamtf.setAlignment(Pos.CENTER_LEFT);
                voornaamtf.setMinWidth(30.0);
		familienaamtf = new TextField ( );
		familienaamtf.setAlignment(Pos.CENTER_LEFT);
		clubtf = new TextField ( );
		clubtf.setAlignment(Pos.CENTER_LEFT);
                afdelingentf = new TextField ();
                afdelingentf.setAlignment(Pos.CENTER_LEFT);
		straattf = new TextField ( );
		straattf.setAlignment(Pos.CENTER_LEFT);
		huisnummertf = new TextField ( );
		huisnummertf.setAlignment(Pos.CENTER_LEFT);
		postcodetf = new TextField ( );
		postcodetf.setAlignment(Pos.CENTER_LEFT);
		stadtf = new TextField ( );
		stadtf.setAlignment(Pos.CENTER_LEFT);
		
		// Maak de labels
		vnaamLabel = new Label( "Voornaam" );
		fnaamLabel = new Label( "Familienaam" );
                afdelingenLabel = new Label( "Afdelingen" );
		clubLabel = new Label( "Club" );
		straatLabel = new Label( "Straat" );
		huisnrLabel = new Label( "Huisnummer" );
		pcLabel = new Label( "Postcode" );
		stadLabel = new Label( "Stad" );
		
		toevoegen = new Button( "Toevoegen" );
		annuleren = new Button( "Annuleren" );
		
                // Combobox
                afdelingenComboBox = new ComboBox();
                    // List with afdelingen
                    
                    ObservableList<String> data = FXCollections.<String>observableArrayList(afdelingen);
                    afdelingenComboBox.setItems(data);
                      

                afdelingenComboBox.setValue("Gold");
                
		toevoegen.setOnAction((ActionEvent event) -> {
                    confirmed = true;
                    String voornaam = voornaamtf.getText();
                    System.out.println("Voornaam: " + voornaam);
                    String afd = afdelingenComboBox.getValue().toString();
                    System.out.println("Afdeling: " + afd);
                    umpires = new Umpires();
                    umpires.setList(voornaam, afd);
                });
		annuleren.setOnAction((ActionEvent event) -> {
                    // Close window
                    
		});
	
                
		// Bepaal van alle componenten de plaats en afmeting
                
		grid.add(vnaamLabel, 0, 1 );
		grid.add(voornaamtf, 1, 1 );
		grid.add(fnaamLabel, 0, 2 );
		grid.add(familienaamtf, 1, 2 );
		grid.add(clubLabel, 0, 3 );
		grid.add(clubtf, 1, 3 );
		grid.add(straatLabel, 0, 4 );
		grid.add(straattf, 1, 4 );
		grid.add(huisnrLabel, 0, 5 );
		grid.add(huisnummertf, 1, 5 );
		grid.add(pcLabel, 0, 6 );
		grid.add(postcodetf, 1, 6 );
		grid.add(stadLabel, 0, 7 );
		grid.add(stadtf, 1, 7 );
		grid.add(toevoegen, 0, 9 );
		grid.add(annuleren, 1, 9 );
                grid.add(afdelingenLabel, 0, 8 );
                grid.add(afdelingenComboBox, 1, 8 );
		
		return grid;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	}

	public String getInputFieldText() {
	    return voornaamtf.getText();
	}
    }


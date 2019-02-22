/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author pieter
 */
public class NewClub {
    /**
	 * @param args
	 */
	
	

    public class ClubPanel extends Pane {
	
        /* Paneel met invoervakken voor nieuwe club */
	// Attributen
	private static final long serialVersionUID = 1L;
	private boolean confirmed = false;
	private MainPanel mainPanel;

	private Clubs clubs;
	public TextField clubnaamtf, afdelingtf, voorzittertf, straattf, huisnummertf, postcodetf, stadtf;
	private Label clubLabel,  afdelingLabel, voorzitterLabel, straatLabel, huisnrLabel, pcLabel, stadLabel;
	private Button toevoegen, annuleren;
        
        // Constructor
        public ClubPanel() {
        
        }

	// Constructor
	public Pane clubPanel() {
		
			
		// Schakel lay-outmanager uit
		GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER_LEFT);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                
                
		
		
		
		// Maak de tekstvakken
                
		clubnaamtf = new TextField( );
                clubnaamtf.setAlignment(Pos.CENTER_LEFT);
                clubnaamtf.setMinWidth(30.0);
		afdelingtf = new TextField ( );
		afdelingtf.setAlignment(Pos.CENTER_LEFT);
		voorzittertf = new TextField ( );
		voorzittertf.setAlignment(Pos.CENTER_LEFT);
		straattf = new TextField ( );
		straattf.setAlignment(Pos.CENTER_LEFT);
		huisnummertf = new TextField ( );
		huisnummertf.setAlignment(Pos.CENTER_LEFT);
		postcodetf = new TextField ( );
		postcodetf.setAlignment(Pos.CENTER_LEFT);
		stadtf = new TextField ( );
		stadtf.setAlignment(Pos.CENTER_LEFT);
		
		// Maak de labels
		clubLabel = new Label( "Clubnaam" );
		afdelingLabel = new Label( "Afdeling" );
		voorzitterLabel = new Label( "Voorzitter" );
		straatLabel = new Label( "Straat" );
		huisnrLabel = new Label( "Huisnummer" );
		pcLabel = new Label( "Postcode" );
		stadLabel = new Label( "Stad" );
		
		toevoegen = new Button( "Toevoegen" );
		annuleren = new Button( "Annuleren" );
		
		toevoegen.setOnAction((ActionEvent event) -> {
                    confirmed = true;
                    String clubnaam = clubnaamtf.getText();
                    System.out.println("Club: " + clubnaam);
                    String afd = afdelingtf.getText();
                    System.out.println("Afdeling: " + afd);
                    clubs = new Clubs();
                    clubs.setList(clubnaam, afd);
                    
                });
		annuleren.setOnAction((ActionEvent event) -> {
                    // Close window
                    
		});
	
		// Bepaal van alle componenten de plaats en afmeting
                
		grid.add(clubLabel, 0, 1 );
		grid.add(clubnaamtf, 1, 1 );
		grid.add(afdelingLabel, 0, 2 );
		grid.add(afdelingtf, 1, 2 );
		grid.add(voorzitterLabel, 0, 3 );
		grid.add(voorzittertf, 1, 3 );
		grid.add(straatLabel, 0, 4 );
		grid.add(straattf, 1, 4 );
		grid.add(huisnrLabel, 0, 5 );
		grid.add(huisnummertf, 1, 5 );
		grid.add(pcLabel, 0, 6 );
		grid.add(postcodetf, 1, 6 );
		grid.add(stadLabel, 0, 7 );
		grid.add(stadtf, 1, 7 );
		grid.add(toevoegen, 0, 8 );
		grid.add(annuleren, 1, 8 );
		
		return grid;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	}

	public String getInputFieldText() {
	    return clubnaamtf.getText();
	}
    }
}

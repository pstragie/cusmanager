/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Pieter Stragier
 */
public class DatabaseGames {
    public Statement stm;
    public Connection con;
    
    public DatabaseGames() {
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } 
    }
    
    /** Voeg een game toe aan de database
     * 
     * @param week
     * @param afdeling
     * @param gamedate
     * @param gametime
     * @param hometeam
     * @param visitingteam
     * @param plateumpire
     * @param base1umpire
     * @param base2umpire
     * @param base3umpire
     * @param gamenumber 
     */
    public void insertGamesInDatabase(Integer week, String afdeling, Date gamedate, Time gametime, String hometeam, String visitingteam, String plateumpire, String base1umpire, String base2umpire, String base3umpire, String gamenumber) {
        try {
            stm = con.createStatement();
            // Replace data if exists, insert if not exist {
            stm.executeUpdate("INSERT INTO APP.Games " + "VALUES ('" + week + "', '" + afdeling + "', '" + gamedate + "', '" + gametime + "', '" + hometeam + "', '" + visitingteam + "', '" + plateumpire + "', '" + base1umpire + "', '" + base2umpire + "', '" + base3umpire + "', '" + gamenumber + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting team: " + e);
        } finally {
            if(stm!=null) {
                try{
                    stm.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update een team in de database
     * 
     * @param week
     * @param afdeling
     * @param gamedate
     * @param gametime
     * @param hometeam
     * @param visitingteam
     * @param plateumpire
     * @param base1umpire
     * @param base2umpire
     * @param base3umpire
     * @param gamenumber 
     */
    public void updateGameToDatabase(Integer week, String afdeling, Date gamedate, Time gametime, String hometeam, String visitingteam, String plateumpire, String base1umpire, String base2umpire, String base3umpire, String gamenumber) {
        try {
            stm = con.createStatement();
            // Update row
            stm.executeUpdate("UPDATE APP.Games " + "SET week = '" + week + "', afdeling = '" + afdeling + "', gamedate = '" + gamedate + "', gametime = '" + gametime + "', hometeam = '" + hometeam + "', visitingteam = '" + visitingteam + "', plateumpire = '" + plateumpire + "', base1umpire = '" + base1umpire + "', base2umpire = '" + base2umpire + "', base3umpire = '" + base3umpire + "'" + "WHERE gamenumber = '" + gamenumber + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            if(stm!=null) {
                try{
                    stm.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
}

package javafxcusmanager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pieter Stragier
 * @version 1.0
 * @since 1.0
 */
public class Database {
    private MainPanel mainpanel;
    public Statement stmt;
    public Connection con;
    public Preferences pref;


    public Database() {
        try {
            String host = "jdbc:derby://localhost:1527/cusdb;create=false";
            String unm = "pstragier";
            String pswrd = "Isabelle30?";
            con = DriverManager.getConnection(host, unm, pswrd);
            //con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
        } catch(SQLException e) {
            System.err.println("SQL Exception database: " + e);
        } 
        pref = Preferences.userNodeForPackage(AppSettings.class);

    }
    
    /** Haal alle afdelingen uit de database
     * 
     * @return Lijst van afdelingen 
     */
    public ArrayList<Afdeling> getAllAfdelingenFromDatabase() {
        ArrayList<Afdeling> arrayAfdelingen = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Afdelingen";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String n = rs.getString("afdelingsnaam");
                String d = rs.getString("discipline");
                String k = rs.getString("kbbsf");
                Afdeling afd = new Afdeling(n, d);
                arrayAfdelingen.add(afd);
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayAfdelingen;
    }
    
    /** Verwijder alle afdelingen uit de database
     * 
     */
    public void deleteAllAfdelingenInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Afdelingen WHERE 1=1";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Verwijder 1 afdeling uit de database
     * 
     * @param afdelingsnaam naam van de afdeling die verwijderd moet worden.
     */
    public void deleteAfdelingFromDatabase(String afdelingsnaam) {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Afdelingen WHERE afdelingsnaam = '" + afdelingsnaam + "' ";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Nieuwe afdeling toevoegen aan de database
     * 
     * @param naam Naam van de afdeling.
     * @param discipline Naam van de discipline (baseball/softball).
     * @param kbbsf Bool: KBBSF verantwoordelijk voor deze afdeling.
     */
    public void insertNewAfdelingToDatabase(String naam, String discipline, Boolean kbbsf) {
        try {
            
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Afdelingen " + "VALUES ('" + naam + "', '" + discipline + "', '" + kbbsf + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update afdeling in database
     * 
     * @param naam
     * @param discipline
     * @param kbbsf 
     */
    public void updateAfdelingToDatabase(String naam, String discipline, Boolean kbbsf) {
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Afdelingen " + "SET discipline = '" + discipline + "', kbbsf = '" + kbbsf + "'" + "WHERE afdelingsnaam = '" + naam + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Controleer of een afdeling bestaat
     * 
     * @param tableName Tabelnaam in de database
     * @param naam Naam van de afdeling
     * @return Boolean
     * @throws SQLException 
     */
    public Boolean checkIfAfdelingExists(String tableName, String naam) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from " + tableName + " where afdelingsnaam = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, naam);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    // CLUBS
    /** Haal alle clubs op uit de database
     * 
     * @return Lijst van clubs
     */
    public ArrayList<Club> getAllClubsFromDatabase() {
        ArrayList<Club> arrayClubs = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Clubs";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("clubnaam");
                String d = rs.getString("clubvoorzitter");
                String c = rs.getString("clubnummer");
                String e = rs.getString("clubstraat");
                String f = rs.getString("clubhuisnummer");
                String g = rs.getString("clubpostcode");
                String h = rs.getString("clubstad");
                String i = rs.getString("clubemail");
                String j = rs.getString("clubtelefoon");
                String b = rs.getString("liga");
                String k = rs.getString("clubwebsite");
                Boolean l = rs.getBoolean("visible");
                String lat = rs.getString("latitude");
                String lon = rs.getString("longitude");
                ArrayList<Team> teamArray = new ArrayList<>();
                teamArray.addAll(getTeamsFromDatabase(c));
                //System.out.println("DB -> clubs: visible = " + l);
                Club cl = new Club(a, b, c, d, e, f, g, h, i, j, k, teamArray, l, lat, lon);
                arrayClubs.add(cl);
            }
            
        } catch (SQLException e) {
            System.err.println("Get all clubs: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayClubs;
    }
    
    /** Haal 1 club uit de database
     * 
     * @param clubnummer
     * @return Geeft een object (Club) terug
     */
    public Club getClubFromDatabase(String clubnummer) {
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Clubs WHERE clubnummer = '" + clubnummer + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("clubnaam");
                String d = rs.getString("clubvoorzitter");
                String c = rs.getString("clubnummer");
                String e = rs.getString("clubstraat");
                String f = rs.getString("clubhuisnummer");
                String g = rs.getString("clubpostcode");
                String h = rs.getString("clubstad");
                String i = rs.getString("clubemail");
                String j = rs.getString("clubtelefoon");
                String b = rs.getString("liga");
                String k = rs.getString("clubwebsite");
                Boolean l = rs.getBoolean("visible");
                String lat = rs.getString("latitude");
                String lon = rs.getString("longitude");
                teamArray.addAll(getTeamsFromDatabase(c));
                //System.out.println("DB -> clubs: visible = " + l);
                cl = new Club(a, b, c, d, e, f, g, h, i, j, k, teamArray, l, lat, lon);
                
            }
            
        } catch (SQLException e) {
            System.err.println("Get club: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return cl;
    }
    
    /** Verwijder alle clubs uit de database
     * 
     */
    public void deleteAllClubsInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Clubs WHERE 1=1";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Verwijder 1 club uit de database
     * 
     * @param clubnaam 
     */
    public void deleteClubFromDatabase(String clubnaam) {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM APP.Clubs WHERE clubnaam = '"+clubnaam+"'";
            stmt.executeUpdate(sql);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Verwijder een team uit de database
     * 
     * @param team 
     */
    public void removeTeamFromDatabase(Team team) {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM APP.Teams WHERE (teamnaam = '" + team.getTeamNaam() + "' AND teamafdeling = '" + team.getTeamAfdeling().getAfdelingsNaam() + "')";
            stmt.executeUpdate(sql);
        } catch(SQLException e) {
            System.err.println("Error removing team: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Voeg een nieuwe club toe aan de database
     * 
     * @param clubnaam
     * @param clubvoorzitter
     * @param clubnummer
     * @param clubstraat
     * @param clubhuisnummer
     * @param clubpostcode
     * @param clubstad
     * @param clubemail
     * @param clubtelefoon
     * @param liga
     * @param clubwebsite 
     * @param visible Boolean
     */
    public void insertNewClubToDatabase(String clubnaam, String clubvoorzitter, String clubnummer, String clubstraat, String clubhuisnummer, String clubpostcode, String clubstad, String clubemail, String clubtelefoon, String liga, String clubwebsite, Boolean visible, String lat, String lon) {
        
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Clubs " + "VALUES ('" + clubnaam + "', '" + clubvoorzitter + "', '" + clubnummer + "', '" + clubstraat + "', '" + clubhuisnummer + "', '" + clubpostcode + "', '" + clubstad + "', '" + clubemail + "', '" + clubtelefoon + "', '" + liga + "', '" + clubwebsite + "', '" + visible + "', '" + lat + "', '" + lon + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting club: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update club in de database
     * 
     * @param clubnaam
     * @param clubvoorzitter
     * @param clubnummer
     * @param clubstraat
     * @param clubhuisnummer
     * @param clubpostcode
     * @param clubstad
     * @param clubemail
     * @param clubtelefoon
     * @param liga
     * @param clubwebsite
     * @param visible 
     */
    public void updateClubToDatabase(String clubnaam, String clubvoorzitter, String clubnummer, String clubstraat, String clubhuisnummer, String clubpostcode, String clubstad, String clubemail, String clubtelefoon, String liga, String clubwebsite, Boolean visible) {
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Clubs " + "SET liga = '" + liga + "', clubnummer = '" + clubnummer + "', clubvoorzitter = '" + clubvoorzitter + "', clubstraat = '" + clubstraat + "', clubhuisnummer = '" + clubhuisnummer + "', clubpostcode = '" + clubpostcode + "', clubstad = '" + clubstad + "', clubemail = '" + clubemail + "', clubtelefoon = '" + clubtelefoon + "', clubwebsite = '" + clubwebsite + "', visible = '" + visible + "' " + "WHERE clubnummer = '" + clubnummer + "'");
        } catch(SQLException e) {
            System.err.println("Club update SQL Exception: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update umpire location (latitude and longitude
     * 
     * @param clubnummer
     * @param latitude
     * @param longitude 
     */
    public void updateClubLocationInDatabase(String clubnummer, String latitude, String longitude) {
        System.out.println("Update Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Clubs " + "SET latitude = '" + latitude + "', longitude = '" + longitude + "'" + "WHERE clubnummer = '" + clubnummer + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Get longitude from club database
     * 
     * @param clubnummer
     * @return 
     */
    public String getLongitudeFromClubDatabase(String clubnummer) {
        String longi = "";
        ResultSet rs = null;
        System.out.println("Get longitude from Club in Database...");
        try {
            stmt = con.createStatement();
            String sql = "Select longitude from APP.Clubs" + " where clubnummer = '" + clubnummer + "'";  
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                longi = rs.getString("longitude");
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return longi;
    }
    
    /** Get Latitude from Club database
     * 
     * @param umpirelicentie
     * @return 
     */
    public String getLatitudeFromClubDatabase(String clubnummer) {
        String longi = "";
        ResultSet rs = null;
        System.out.println("Get lat from Club in Database...");
        try {
            stmt = con.createStatement();
            String sql = "Select latitude from APP.Clubs" + " where clubnummer = '" + clubnummer + "'";  
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                longi = rs.getString("latitude");
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return longi;
    }
    
    /** Controleer of club bestaat in de database
     * 
     * @param tableName
     * @param naam
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfClubExists(String tableName, String clubnummer) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from " + tableName + " where clubnummer = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, clubnummer);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
     // UMPIRES
    /** Haal alle umpires uit de database
     * 
     * @return Lijst van umpires. 
     */
    public ArrayList<Umpire> getAllUmpiresFromDatabase() {
        ArrayList<Umpire> arrayUmpires = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Umpires";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("umpirenaam");
                String v = rs.getString("umpirevoornaam");
                String b = rs.getString("umpirelicentie");
                String c = rs.getString("umpirestraat");
                String d = rs.getString("umpirehuisnummer");
                String e = rs.getString("umpirepostcode");
                String f = rs.getString("umpirestad");
                String g = rs.getString("umpiretelefoon");
                String h = rs.getString("umpireemail");
                String i = rs.getString("umpireclub");
                String j = rs.getString("afdeling");
                Boolean k = rs.getBoolean("actief");
                String lat = rs.getString("latitude");
                String lon = rs.getString("longitude");
                ArrayList<Afdeling> afdArray = new ArrayList<>();
                if (j == null || j.isEmpty()) {
                    afdArray = new ArrayList<>();
                } else {
                    String[] s = j.split(",");
                    for (String parts : s) {
                        String[] p = parts.split(":");
                        Afdeling tempafd = new Afdeling(p[0], p[1]);

                        afdArray.add(tempafd);
                    }
                }
                // Get Object Club from list based on clubnaam
                Club uclub = getClubFromDatabase(i);
                
                Umpire u = new Umpire(a, v, b, c, d, e, f, g, h, uclub, afdArray, k, lat, lon);
                arrayUmpires.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Get umpires: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayUmpires;
    }
    
    /** Haal 1 umpire uit de database
     * 
     * @param umpirelicentie
     * @return 
     */
    public Umpire getUmpireFromDatabase(String umpirelicentie) {
        System.out.println("Umpire requested from database!");
        ArrayList<Afdeling> afdarray = new ArrayList<>();
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        Umpire u = new Umpire("", "", "", "", "", "", "", "", "", cl, afdarray, Boolean.TRUE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Umpires WHERE umpirelicentie = '" + umpirelicentie + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("umpirenaam");
                String v = rs.getString("umpirevoornaam");
                String b = rs.getString("umpirelicentie");
                String c = rs.getString("umpirestraat");
                String d = rs.getString("umpirehuisnummer");
                String e = rs.getString("umpirepostcode");
                String f = rs.getString("umpirestad");
                String g = rs.getString("umpiretelefoon");
                String h = rs.getString("umpireemail");
                String i = rs.getString("umpireclub");
                String j = rs.getString("afdeling");
                Boolean k = rs.getBoolean("actief");
                String lat = rs.getString("latitude");
                String lon = rs.getString("longitude");
                ArrayList<Afdeling> afdArray = new ArrayList<>();
                
                if (j == null || j.isEmpty()) {
                    afdArray = new ArrayList<>();
                } else {
                    String[] s = j.split(",");
                    for (String parts : s) {
                        String[] p = parts.split(":");
                        Afdeling tempafd = new Afdeling(p[0], p[1]);

                        afdArray.add(tempafd);
                    }
                }
                // Get Object Club from list based on clubnaam
                Club uclub = getClubFromDatabase(i);
                
                u = new Umpire(a, v, b, c, d, e, f, g, h, uclub, afdArray, k, lat, lon);
                
            }
        } catch (SQLException e) {
            System.err.println("Get umpires: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return u;
    }
    
    /** Verwijder alle umpires uit de database
     * 
     */
    public void deleteAllUmpiresInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Umpires WHERE 1=1";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Verwijder 1 umpire uit de database
     * 
     * @param umpirenaam 
     */
    public void deleteUmpireFromDatabase(String licentie) {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM APP.Umpires WHERE umpirelicentie = '" + licentie + "'";
            stmt.executeUpdate(sql);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Voeg een nieuwe umpire toe aan de database
     * 
     * @param umpirenaam
     * @param umpirevoornaam
     * @param umpirelicentie
     * @param umpirestraat
     * @param umpirehuisnummer
     * @param umpirepostcode
     * @param umpirestad
     * @param umpiretelefoon
     * @param umpireemail
     * @param umpireclubnummer
     * @param afdeling
     * @param actief 
     */
    public void insertNewUmpireToDatabase(String umpirenaam, String umpirevoornaam, String umpirelicentie, String umpirestraat, String umpirehuisnummer, String umpirepostcode, String umpirestad, String umpiretelefoon, String umpireemail, String umpireclubnummer, String afdeling, Boolean actief, String lat, String lon) {
        System.out.println("Insert Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            //System.out.println("Inserting into Umpires: " + umpirelicentie + ", " + umpirehuisnummer + ", " + umpirepostcode + ", " + umpirestad + ", " + umpiretelefoon + ", " + umpireemail + ", " + umpireclub + ", " + afdeling ", " + umpirenaam + ", " + actief + ", " + umpirevoornaam + ".");
            stmt.executeUpdate("INSERT INTO APP.Umpires " + "VALUES ('" + umpirelicentie + "', '" + umpirehuisnummer + "', '" + umpirepostcode + "', '" + umpirestad + "', '" + umpiretelefoon + "', '" + umpireemail + "', '" + umpireclubnummer + "', '" + umpirestraat + "', '" + afdeling + "', '" + umpirenaam + "', '" + actief + "', '" + umpirevoornaam + "', '" + lat + "', '" + lon + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update umpire in de database
     * 
     * @param umpirenaam
     * @param umpirevoornaam
     * @param umpirelicentie
     * @param umpirestraat
     * @param umpirehuisnummer
     * @param umpirepostcode
     * @param umpirestad
     * @param umpiretelefoon
     * @param umpireemail
     * @param umpireclub
     * @param afdeling
     * @param actief 
     */
    public void updateUmpireToDatabase(String umpirenaam, String umpirevoornaam, String umpirelicentie, String umpirestraat, String umpirehuisnummer, String umpirepostcode, String umpirestad, String umpiretelefoon, String umpireemail, String umpireclub, String afdeling, Boolean actief) {
        System.out.println("Update Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Umpires " + "SET umpirestraat = '" + umpirestraat + "', umpirehuisnummer = '" + umpirehuisnummer + "', umpirepostcode = '" + umpirepostcode + "', umpirestad = '" + umpirestad + "', umpiretelefoon = '" + umpiretelefoon + "', umpireemail = '" + umpireemail + "', umpireclub = '" + umpireclub + "', afdeling = '" + afdeling + "', actief = '" + actief + "', umpirenaam = '" + umpirenaam + "', umpirevoornaam = '" + umpirevoornaam + "' " + "WHERE umpirelicentie = '" + umpirelicentie + "'");
            //stmt.executeUpdate("UPDATE APP.Umpires " + "SET umpirenaam = '" + umpirenaam + "', umpirelicentie = '" + umpirelicentie + "', umpirehuisnummer = '" + umpirehuisnummer + "' + umpirepostcode = '" + umpirepostcode + "' + umpirestad = '" + umpirestad + "' + umpiretelefoon = '" + umpiretelefoon + "' + umpireemail = '" + umpireemail + "' + umpireclub = '" + umpireclub + "' + umpirestraat = '" + umpirestraat + "' + afdeling = '" + afdeling + "' + actief = '" + actief + "' " + "WHERE umpirenaam = '" + umpirenaam + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update umpire location (latitude and longitude
     * 
     * @param umpirelicentie
     * @param latitude
     * @param longitude 
     */
    public void updateUmpireLocationInDatabase(String umpirelicentie, String latitude, String longitude) {
        System.out.println("Update Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Umpires " + "SET latitude = '" + latitude + "', longitude = '" + longitude + "'" + "WHERE umpirelicentie = '" + umpirelicentie + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Get longitude from umpire database
     * 
     * @param umpirelicentie
     * @return 
     */
    public String getLongitudeFromUmpireDatabase(String umpirelicentie) {
        String longi = "";
        ResultSet rs = null;
        System.out.println("Get longitude from Umpire in Database...");
        try {
            stmt = con.createStatement();
            String sql = "Select longitude from APP.Umpires" + " where umpirelicentie = '" + umpirelicentie + "'";  
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                longi = rs.getString("longitude");
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return longi;
    }
    
    /** Get Latitude from Umpire database
     * 
     * @param umpirelicentie
     * @return 
     */
    public String getLatitudeFromUmpireDatabase(String umpirelicentie) {
        String longi = "";
        ResultSet rs = null;
        System.out.println("Get lat from Umpire in Database...");
        try {
            stmt = con.createStatement();
            String sql = "Select latitude from APP.Umpires" + " where umpirelicentie = '" + umpirelicentie + "'";  
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                longi = rs.getString("latitude");
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return longi;
    }
    /** Controleer of umpire bestaat in de database
     * 
     * @param tableName "Umpires"
     * @param licentienummer Licentienummer umpire
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfUmpireExists(String licentienummer) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from APP.Umpires" + " where umpirelicentie = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, licentienummer);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    // TEAMS
    /** Haal alle teams uit de database
     * 
     * @param club
     * @return 
     */
    public ArrayList<Team> getTeamsFromDatabase(String clubnummer) {
        ArrayList<Team> arrayTeams = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Teams WHERE club = '" + clubnummer + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("teamnaam");
                String d = rs.getString("discipline");
                Afdeling b = new Afdeling(rs.getString("teamafdeling"), d);
                Team t = new Team(a, b);
                arrayTeams.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Get teams: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayTeams;
    }
    
    public ArrayList<Team> getTeamFromDatabase(Club club, String afdeling) {
        ArrayList<Team> arrayTeams = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Teams WHERE club = '" + club.getClubNummer() + "' AND teamafdeling = '" + afdeling + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("teamnaam");
                String d = rs.getString("discipline");
                Afdeling b = new Afdeling(rs.getString("teamafdeling"), d);
                Team t = new Team(a, b);
                arrayTeams.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Get teams: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayTeams;
    }
    /** Verwijder alle teams uit de database
     * 
     */
    public void deleteAllTeamsInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Teams WHERE 1=1";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Voeg een team toe aan de database
     * 
     * @param teamnaam
     * @param teamafdeling
     * @param clubnummer
     * @param discipline 
     */
    public void insertTeamsInDatabase(String teamnaam, String teamafdeling, String clubnummer, String discipline) {
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Teams " + "VALUES ('" + teamnaam + "', '" + teamafdeling + "', '" + clubnummer + "', '" + discipline + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting team: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update een team in de database
     * 
     * @param teamnaam
     * @param teamafdeling
     * @param club
     * @param discipline 
     */
    public void updateTeamToDatabase(String teamnaam, String teamafdeling, String club, String discipline) {
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Teams " + "SET teamafdeling = '" + teamafdeling + "', club = '" + club + "' + discipline = '" + discipline + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Controleer of team bestaat in de database
     * 
     * @param tableName
     * @param naam
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfTeamExists(String tableName, String naam) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from " + tableName + " where teamnaam = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, naam);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    // CLUBS
    /** Haal alle games op uit de database
     * 
     * @return Lijst van games
     */
    public ArrayList<Game> getAllGamesFromDatabase() {
        System.out.println("Get all games from database................");
        ArrayList<Game> arrayGames = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Games";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String w = rs.getString("week");
                String a = rs.getString("afdeling");
                String gd = rs.getString("gamedate");
                String gt = rs.getString("gametime");
                String ht = rs.getString("hometeam");
                String vt = rs.getString("visitingteam");
                Umpire pu = getUmpireFromDatabase(rs.getString("plateumpire"));
                Umpire b1 = getUmpireFromDatabase(rs.getString("base1umpire"));
                Umpire b2 = getUmpireFromDatabase(rs.getString("base2umpire"));
                Umpire b3 = getUmpireFromDatabase(rs.getString("base3umpire"));
                String gn = rs.getString("gamenumber");
                String gi = rs.getString("gameindex");
                String se = rs.getString("seizoen");
                String atfield = rs.getString("atfield");
                Club hc = getClubFromDatabase(atfield);
                
                mainpanel = new MainPanel();
                LocalDate gdatum = mainpanel.stringToLocalDate(gd);
                System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
                Game g = new Game(gi, a, w, gdatum, gt, ht, vt, pu, b1, b2, b3, gn, se, hc);
                
                arrayGames.add(g);
            }
            
        } catch (SQLException e) {
            System.err.println("Get games: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayGames;
    }
    
    /** Haal 1 club uit de database
     * 
     * @param gameindex
     * @return Geeft een object (Game) terug
     */
    public Game getGameFromDatabase(String gameindex) {
        String defaultGamehour = pref.get("DefaultGameTime", "14:00");
        Game g = new Game("", "", "", LocalDate.now(), defaultGamehour, "", "", null, null, null, null, "", "", null);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Games WHERE gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String w = rs.getString("week");
                String a = rs.getString("afdeling");
                String gd = rs.getString("gamedate");
                String gt = rs.getString("gametime");
                String ht = rs.getString("hometeam");
                String vt = rs.getString("visitingteam");
                Umpire pu = getUmpireFromDatabase(rs.getString("plateumpire"));
                Umpire b1 = getUmpireFromDatabase(rs.getString("base1umpire"));
                Umpire b2 = getUmpireFromDatabase(rs.getString("base2umpire"));
                Umpire b3 = getUmpireFromDatabase(rs.getString("base3umpire"));
                String gn = rs.getString("gamenumber");
                String gi = rs.getString("gameindex");
                String se = rs.getString("seizoen");
                Club hc = getClubFromTeam(ht);
                mainpanel = new MainPanel();
                LocalDate gdatum = mainpanel.stringToLocalDate(gd);
                System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
                g = new Game(gi, w, a, gdatum, gt, ht, vt, pu, b1, b2, b3, gn, se, hc);                
            }
            
        } catch (SQLException e) {
            System.err.println("Get game: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return g;
    }
    
    /** Verwijder alle games uit de database
     * 
     */
    public void deleteAllGamesInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Games WHERE 1=1";
            stmt.executeUpdate(sql2);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Verwijder 1 game uit de database
     * 
     * @param gameindex 
     */
    public void deleteGameFromDatabase(String gameindex) {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM APP.Games WHERE gameindex = '"+gameindex+"'";
            stmt.executeUpdate(sql);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Insert a new game to the database
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
     * @param gameindex
     * @param seizoen 
     */
    public void insertNewGameToDatabase(Integer week, String afdeling, String gamedate, String gametime, String hometeam, String visitingteam, String plateumpire, String base1umpire, String base2umpire, String base3umpire, String gamenumber, String gameindex, String seizoen, String atfield) {
        System.out.println("Insert Game To Database...");
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO APP.Games " + "VALUES ('" + afdeling + "', '" + hometeam + "', '" + visitingteam + "', '" + plateumpire + "', '" + base1umpire + "', '" + base2umpire + "', '" + base3umpire + "', '" + gamenumber + "', '" + gameindex + "', '" + week + "', '" + seizoen + "', '" + gamedate + "', '" + gametime + "', '" + atfield + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting game: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update game in the database
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
     * @param gameindex
     * @param seizoen 
     */
    public void updateGameToDatabase(Integer week, String afdeling, String gamedate, String gametime, String hometeam, String visitingteam, String plateumpire, String base1umpire, String base2umpire, String base3umpire, String gamenumber, String gameindex, String seizoen, String homeclubnummer) {
        System.out.println("Update Game in Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Games " + "SET week = '" + Integer.toString(week) + "', afdeling = '" + afdeling + "', gamedate = '" + gamedate + "', gametime = '" + gametime + "', hometeam = '" + hometeam + "', visitingteam = '" + visitingteam + "', plateumpire = '" + plateumpire + "', base1umpire = '" + base1umpire + "', base2umpire = '" + base2umpire + "', base3umpire = '" + base3umpire + "', gamenumber = '" + gamenumber + "' + seizoen = '" + seizoen + "' + atfield = '" + homeclubnummer + "' " + "WHERE gameindex = '" + gameindex + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update a single item in a game
     * 
     * @param item
     * @param waarde
     * @param gameindex 
     */
    public void updateSingleItemInGameInDatabase(String item, String waarde, String gameindex) {
        System.out.println("Update Game in Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Games " + "SET "+item+" = '" + waarde + "' " + "WHERE gameindex = '" + gameindex + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating single item in games: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Controleer of game bestaat in de database
     * 
     * @param gameindex
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfGameExists(String gameindex) throws SQLException {
        ResultSet rs = null;
        System.out.println("Checking if game exists... current Games: ?" );
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from APP.Games where gameindex = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, gameindex);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    public void insertDistanceToDatabase(Umpire ump, Club club, String distance) {
        System.out.println("Insert Distance To Database...");
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO APP.Distances " + "VALUES ('" + ump.getUmpireLicentie() + "', '" + club.getClubNummer() + "', '" + distance + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting distance: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update distance in database
     * 
     * @param ump
     * @param club
     * @param distance 
     */
    public void updateDistanceToDatabase(Umpire ump, Club club, String distance) {
        System.out.println("Update Distance in Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Distances " + "SET distance = '" + distance + "' " + "WHERE umpire = '" + ump.getUmpireLicentie() + "' AND club = '" + club.getClubNummer() + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating umpire: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Controleer of distance bestaat in de database
     * 
     * @param ump
     * @param club
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfDistanceExists(Umpire ump, Club club) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from APP.Distances where umpire = ? AND club = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, ump.getUmpireLicentie());
            ps.setString(2, club.getClubNummer());
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    public String getClubFromDatabaseLatLon(String latitude, String longitude) {
        String clubnaam = "";
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.CLUBS WHERE latitude = '" + latitude + "' AND longitude = '" + longitude + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                clubnaam = rs.getString("clubnaam");
                System.out.println("Club from latlon = " + clubnaam);
                
            }
            
        } catch (SQLException e) {
            System.err.println("Get club Lat Lon: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }

        return clubnaam;
    }

    public Club getClubFromTeam(String team) {
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT 1 from APP.TEAMS WHERE teamnaam = '" + team + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String clubnummer = rs.getString("club");
                System.out.println("Club from team = " + clubnummer);
                cl = getClubFromDatabase(clubnummer);
            }
            
        } catch (SQLException e) {
            System.err.println("Get club from team: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        
        return cl;
    }
    
    /** Get Club from gameschedule row based on gameindex
     * 
     * @param gameindex
     * @return 
     */
    public Club getClubFromGameIndex(String gameindex) {
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT 1 from APP.GAMES WHERE gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String clubnummer = rs.getString("atfield");
                System.out.println("Get club from gameschedule: " + clubnummer);
                cl = getClubFromDatabase(clubnummer);
            }
            
        } catch (SQLException e) {
            System.err.println("Get club from game index: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        
        return cl;
    }
    
    public Club getHomeClubFromGameSchedule(String gameindex) {
        String clubnummer = "";
        try {
            stmt = con.createStatement();
            String sql = "SELECT 1 from APP.GAMES where gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                clubnummer = rs.getString("atfield");
            }
        } catch (SQLException e) {
            System.err.println("Get club from gameschedule: " + e);
        }
        return getClubFromDatabase(clubnummer);
    }
    
    /** get umpire from game schedule
     * 
     * @param gameindex
     * @param umpirepos "plateumpire", "base1umpire", "base2umpire", "base3umpire"
     * @return 
     */
    public Umpire getUmpireFromGameSchedule(String gameindex, String umpirepos) {
        String umplicentie = "";
        try {
            stmt = con.createStatement();
            String sql = "SELECT 1 from APP.GAMES where gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                umplicentie = rs.getString(umpirepos);
            }
        } catch (SQLException e) {
            System.err.println("Get ump from gameschedule: " + e);
        }
        return getUmpireFromDatabase(umplicentie);
    }
    
    public String getDistFromUmpireClub(String umpirelicentie, String clubnummer) {
        String distance = "";
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE, "", "");
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.DISTANCES WHERE umpire = '" + umpirelicentie + "' AND club = '" + clubnummer + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                distance = rs.getString("distance");
                System.out.println("Distance = " + distance);
                
            }
            
        } catch (SQLException e) {
            System.err.println("Get Dist from umpire club: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }

        return distance;
    }
    
    /** Get vergoeding euro string from database
     * 
     * @param afdeling afdeling of "km"
     * @return 
     */
    public String getVergoedingFromDatabase(String afdeling) {
        String s = "0,000";
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Vergoedingen where afdeling = '"+afdeling+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("afdeling");
                String e = rs.getString("euro");
                s = e;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return s;
    }
    
    public ArrayList<Vergoeding> getAllVergoedingenFromDatabase() {
        ArrayList<Vergoeding> arrayVergoedingen = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Vergoedingen";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("afdeling");
                String e = rs.getString("euro");
                Vergoeding verg = new Vergoeding(a, e);
                arrayVergoedingen.add(verg);
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return arrayVergoedingen;
    }
    /** Insert onkostenvergoedingen into database
     * 
     * @param afdeling
     * @param euro 
     */
    public void insertVergoedingToDatabase(String afdeling, String euro) {
        System.out.println("Insert Vergoeding To Database...");
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO APP.Vergoedingen " + "VALUES ('" + afdeling + "', '" + euro + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting vergoeding: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
    
    /** Update vergoeding in the database
     * 
     * @param afdeling
     * @param euro 
     */
    public void updateVergoedingToDatabase(String afdeling, String euro) {
        System.out.println("Update Vergoeding in Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Vergoedingen " + "SET euro = '" + euro + "'" + "where afdeling = '"+afdeling+"'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating vergoeding: " + e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    } 
    
    public Boolean checkIfVergoedingExists(String afdeling) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from APP.Vergoedingen where afdeling = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, afdeling);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    public void deleteAfdelingFromVergoedingDatabase(String afdeling) {
        try {
            stmt = con.createStatement();
            String sql = "DELETE FROM APP.Vergoedingen WHERE afdeling = '"+afdeling+"'";
            stmt.executeUpdate(sql);
        } catch(SQLException e) {
            System.err.println(e);
        } finally {
            if(stmt!=null) {
                try{
                    stmt.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
    }
}

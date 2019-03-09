package javafxcusmanager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
    public Statement stmt;
    public Connection con;
    public Database() {
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } 
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
                ArrayList<Team> teamArray = new ArrayList<>();
                teamArray.addAll(getTeamsFromDatabase(a));
                //System.out.println("DB -> clubs: visible = " + l);
                Club cl = new Club(a, b, c, d, e, f, g, h, i, j, k, teamArray, l);
                arrayClubs.add(cl);
            }
            
        } catch (SQLException e) {
            System.err.println("Get clubs: " + e);
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
     * @param clubnaam
     * @return Geeft een object (Club) terug
     */
    public Club getClubFromDatabase(String clubnaam) {
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Clubs WHERE clubnaam = '" + clubnaam + "'";
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
                teamArray.addAll(getTeamsFromDatabase(a));
                //System.out.println("DB -> clubs: visible = " + l);
                cl = new Club(a, b, c, d, e, f, g, h, i, j, k, teamArray, l);
                
            }
            
        } catch (SQLException e) {
            System.err.println("Get clubs: " + e);
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
    public void insertNewClubToDatabase(String clubnaam, String clubvoorzitter, String clubnummer, String clubstraat, String clubhuisnummer, String clubpostcode, String clubstad, String clubemail, String clubtelefoon, String liga, String clubwebsite, Boolean visible) {
        
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Clubs " + "VALUES ('" + clubnaam + "', '" + clubvoorzitter + "', '" + clubnummer + "', '" + clubstraat + "', '" + clubhuisnummer + "', '" + clubpostcode + "', '" + clubstad + "', '" + clubemail + "', '" + clubtelefoon + "', '" + liga + "', '" + clubwebsite + "', '" + visible + "')");
            
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
            stmt.executeUpdate("UPDATE APP.Clubs " + "SET liga = '" + liga + "', clubnummer = '" + clubnummer + "' + clubvoorzitter = '" + clubvoorzitter + "' + clubstraat = '" + clubstraat + "' + clubhuisnummer = '" + clubhuisnummer + "' + clubpostcode = '" + clubpostcode + "' + clubstad = '" + clubstad + "' + clubemail = '" + clubemail + "' + clubtelefoon = '" + clubtelefoon + "' + clubwebsite = '" + clubwebsite + "' + visible = '" + visible + "' " + "WHERE clubnaam = '" + clubnaam + "'");
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
    
    /** Controleer of club bestaat in de database
     * 
     * @param tableName
     * @param naam
     * @return
     * @throws SQLException 
     */
    public Boolean checkIfClubExists(String tableName, String naam) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from " + tableName + " where clubnaam = ?";  
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
                ArrayList<Afdeling> afdArray = new ArrayList<>();
                String[] s = j.split(",");
                for (String parts : s) {
                    String[] p = parts.split(":");
                    Afdeling tempafd = new Afdeling(p[0], p[1]);
                                
                    afdArray.add(tempafd);
                }
                // Get Object Club from list based on clubnaam
                Club uclub = getClubFromDatabase(i);
                
                Umpire u = new Umpire(a, v, b, c, d, e, f, g, h, uclub, afdArray, k);
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
     * @param umpirenaam
     * @return Object (Umpire)
     */
    public Umpire getUmpireFromDatabase(String umpirenaam) {
        System.out.println("Umpire requested from database!");
        ArrayList<Afdeling> afdarray = new ArrayList<>();
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", teamArray, Boolean.FALSE);
        Umpire u = new Umpire("", "", "", "", "", "", "", "", "", cl, afdarray, Boolean.TRUE);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Umpires WHERE umpirenaam = '" + umpirenaam + "'";
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
                ArrayList<Afdeling> afdArray = new ArrayList<>();
                String[] s = j.split(",");
                for (String parts : s) {
                    String[] p = parts.split(":");
                    Afdeling tempafd = new Afdeling(p[0], p[1]);
                                
                    afdArray.add(tempafd);
                }
                // Get Object Club from list based on clubnaam
                Club uclub = getClubFromDatabase(i);
                
                u = new Umpire(a, v, b, c, d, e, f, g, h, uclub, afdArray, k);
                
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
     * @param umpireclub
     * @param afdeling
     * @param actief 
     */
    public void insertNewUmpireToDatabase(String umpirenaam, String umpirevoornaam, String umpirelicentie, String umpirestraat, String umpirehuisnummer, String umpirepostcode, String umpirestad, String umpiretelefoon, String umpireemail, String umpireclub, String afdeling, Boolean actief) {
        System.out.println("Insert Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            //System.out.println("Inserting into Umpires: " + umpirelicentie + ", " + umpirehuisnummer + ", " + umpirepostcode + ", " + umpirestad + ", " + umpiretelefoon + ", " + umpireemail + ", " + umpireclub + ", " + afdeling ", " + umpirenaam + ", " + actief + ", " + umpirevoornaam + ".");
            stmt.executeUpdate("INSERT INTO APP.Umpires " + "VALUES ('" + umpirelicentie + "', '" + umpirehuisnummer + "', '" + umpirepostcode + "', '" + umpirestad + "', '" + umpiretelefoon + "', '" + umpireemail + "', '" + umpireclub + "', '" + umpirestraat + "', '" + afdeling + "', '" + umpirenaam + "', '" + actief + "', '" + umpirevoornaam + "')");
            
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
    public ArrayList<Team> getTeamsFromDatabase(String club) {
        ArrayList<Team> arrayTeams = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Teams WHERE club = '" + club + "' ";
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
     * @param club
     * @param discipline 
     */
    public void insertTeamsInDatabase(String teamnaam, String teamafdeling, String club, String discipline) {
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Teams " + "VALUES ('" + teamnaam + "', '" + teamafdeling + "', '" + club + "', '" + discipline + "')");
            
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
}

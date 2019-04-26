package cusman;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

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

    private void setDBSystemDir() {
        // Decide on the db system directory: <userhome>/.addressbook/
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.derby";

        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
    }
    
    public Database() {
        setDBSystemDir();
        String dbName = System.getProperty("derby.system.home");
        String host = "jdbc:derby:";
        //String host = "jdbc:derby://https://www.pws-solutions.be:21/cusdb;create=true";
        //String unm = "pieteup276";
        //String pswrd = "ipttoha6";
        //String host = "jdbc:derby:DefaultCusDB;create=true";
        //String host = "jdbc:derby:DefaultCusManDB;create=false";
        //String unm = "pstragier";
        //String pswrd = "Isabelle30?";
        String unm = "APP";
        String pswrd = "CUSMan";
        Properties props = new Properties();
        props.put("user", unm);
        props.put("password", pswrd);
        try {
            System.out.println("Trying to connect to database!");
            con = DriverManager.getConnection(host + dbName + ";create=true", props);
//            createTableVergoedingen(con);
//            createTableAfdelingen(con);
//            createTableClubs(con);
//            createTableDistances(con);
//            createTableGames(con);
//            createTableLandcodes(con);
//            createTableTeams(con);
//            createTableUitbetalingen(con);
//            createTableUmpires(con);
            
            //con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
        } catch(SQLException e) {
            System.err.println("SQL Exception database: " + e);
        } 
        pref = Preferences.userNodeForPackage(AppSettings.class);

    }
    
    Connection createConnection() {
        String host = "jdbc:derby:";
        String dbName = System.getProperty("derby.system.home");
        String unm = "APP";
        String pswrd = "CUSMan";
        Properties props = new Properties();
        props.put("user", unm);
        props.put("password", pswrd);
        try {
            Class driverClass = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //LOG.log(Level.INFO, "Driver loaded: {0}", driverClass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "Driver not found", ex);
        }

        try {
            Connection connection = DriverManager.getConnection(host + dbName + ";create=false", props);


            Logger.getLogger(Database.class.getName()).log(Level.INFO, "Connection created: {0}", connection);
            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "Unable to connect to DB", ex);
            return null;
        }
    }
    
    private boolean createTableAfdelingen(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.AFDELINGEN(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " afdelingsnaam VARCHAR(30),\n"
                + " discipline VARCHAR(30),\n"
                + " kbbsf BOOLEAN)";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableClubs(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.CLUBS(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " clubnaam VARCHAR(50),\n"
                + " clubvoorzitter VARCHAR(50),\n"
                + " clubnummer VARCHAR(50),\n"
                + " clubstraat VARCHAR(50),\n"
                + " clubhuisnummer VARCHAR(50),\n"
                + " clubpostcode VARCHAR(50),\n"
                + " clubstad VARCHAR(50),\n"
                + " clubemail VARCHAR(50),\n"
                + " clubtelefoon VARCHAR(50),\n"
                + " liga VARCHAR(50),\n"
                + " clubwebsite VARCHAR(50),\n"
                + " visible BOOLEAN,\n"
                + " latitude VARCHAR(50),\n"
                + " longitude VARCHAR(50),\n"
                + " clublandcode VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableDistances(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.DISTANCES(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " umpire VARCHAR(50),\n"
                + " club VARCHAR(50),\n"
                + " distance VARCHAR(50))";
                
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableGames(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.GAMES(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " afdeling VARCHAR(50),\n"
                + " hometeam VARCHAR(50),\n"
                + " visitingteam VARCHAR(50),\n"
                + " plateumpire VARCHAR(50),\n"
                + " base1umpire VARCHAR(50),\n"
                + " base2umpire VARCHAR(50),\n"
                + " base3umpire VARCHAR(50),\n"
                + " gamenumber VARCHAR(50),\n"
                + " gameindex VARCHAR(50),\n"
                + " week VARCHAR(50),\n"
                + " seizoen VARCHAR(50),\n"
                + " gamedate BOOLEAN,\n"
                + " gametime VARCHAR(50),\n"
                + " atfield VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableLandcodes(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.LANDCODES(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " landcode VARCHAR(50),\n"
                + " land VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            stmt = con.createStatement();
            
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableTeams(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.TEAMS(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " teamnaam VARCHAR(50),\n"
                + " teamafdeling VARCHAR(50),\n"
                + " club VARCHAR(50),\n"
                + " discipline VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableUitbetalingen(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.UITBETALINGEN(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " umpirelicentie VARCHAR(50),\n"
                + " kilometers DOUBLE,\n"
                + " kmeuro DOUBLE,\n"
                + " aantalwedstrijden INTEGER,\n"
                + " wedstrijdvergoeding DOUBLE,\n"
                + " totaal DOUBLE,\n"
                + " uitbetaald BOOLEAN)";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableUmpires(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.UMPIRES(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " umpirelicentie VARCHAR(50),\n"
                + " umpirehuisnummer VARCHAR(50),\n"
                + " umpirepostcode VARCHAR(50),\n"
                + " umpirestad VARCHAR(50),\n"
                + " umpiretelefoon VARCHAR(50),\n"
                + " umpireemail VARCHAR(50),\n"
                + " umpireclub VARCHAR(50),\n"
                + " umpirestraat VARCHAR(50),\n"
                + " afdeling VARCHAR(2000),\n"
                + " umpirenaam VARCHAR(50),\n"
                + " actief BOOLEAN,\n"
                + " umpirevoornaam VARCHAR(50),\n"
                + " latitude VARCHAR(50),\n"
                + " longitude VARCHAR(50),\n"
                + " umpirelandcode VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
    }
    
    private boolean createTableVergoedingen(Connection dbConnection) {
        System.out.println("Create tables");
        boolean bCreatedTables = false;
        Statement statement = null;
        String strCreateAddressTable = "CREATE TABLE APP.VERGOEDINGEN(\n"
                + " id integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + " afdeling VARCHAR(50),\n"
                + " euro VARCHAR(50))";
        try {
            statement = dbConnection.createStatement();
            //statement.execute("DROP TABLE APP.Afdelingen");
            statement.executeUpdate(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            
            Logger lgr = Logger.getLogger(Database.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return bCreatedTables;
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
            stmt.executeUpdate("INSERT INTO APP.Afdelingen " + "VALUES (default, '" + naam + "', '" + discipline + "', '" + kbbsf + "')");
            
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting new afdeling: " + e);
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
                String b = rs.getString("clubvoorzitter");
                String c = rs.getString("clubnummer");
                String d = rs.getString("clubstraat");
                String e = rs.getString("clubhuisnummer");
                String f = rs.getString("clubpostcode");
                String g = rs.getString("clubstad");
                String h = rs.getString("clubemail");
                String i = rs.getString("clubtelefoon");
                String j = rs.getString("liga");
                String k = rs.getString("clubwebsite");
                Boolean l = rs.getBoolean("visible");
                String lat = rs.getString("latitude");
                String lon = rs.getString("longitude");
                String lc = rs.getString("clublandcode");
                ArrayList<Team> teamArray = new ArrayList<>();
                teamArray.addAll(Database.this.getTeamsFromDatabase(c));
                //System.out.println("DB -> clubs: visible = " + l);
                Club cl = new Club(a, b, c, d, e, f, g, h, i, j, k, l, lat, lon, lc, teamArray);
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
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "",  "", "", teamArray);
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
                String lc = rs.getString("clublandcode");

                teamArray.addAll(Database.this.getTeamsFromDatabase(c));
                //System.out.println("DB -> clubs: visible = " + l);
                cl = new Club(a, b, c, d, e, f, g, h, i, j, k, l, lat, lon, lc, teamArray);
                
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
     * @param clubland
     * @param clubemail
     * @param clubtelefoon
     * @param liga
     * @param clubwebsite
     * @param visible
     * @param lat
     * @param lon 
     */
    public void insertNewClubToDatabase(String clubnaam, String clubvoorzitter, String clubnummer, String clubstraat, String clubhuisnummer, String clubpostcode, String clubstad, String clubland, String clubemail, String clubtelefoon, String liga, String clubwebsite, Boolean visible, String lat, String lon) {
        System.out.println(clubnaam + ", " + clubvoorzitter + ", " + clubnummer + ", " + clubstraat + ", " + clubhuisnummer + ", " + clubpostcode + ", " + clubstad + ", " + clubemail + ", " + clubtelefoon + ", " + liga + ", " + clubwebsite + ", " + visible + ", " + lat + ", " + lon);
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Clubs " + "VALUES (default, '" + clubnaam + "', '" + clubvoorzitter + "', '" + clubnummer + "', '" + clubstraat + "', '" + clubhuisnummer + "', '" + clubpostcode + "', '" + clubstad + "', '" + clubemail + "', '" + clubtelefoon + "', '" + liga + "', '" + clubwebsite + "', '" + visible + "', '" + lat + "', '" + lon + "', '" + clubland + "')");
            
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
    public void updateClubToDatabase(String clubnaam, String clubvoorzitter, String clubnummer, String clubstraat, String clubhuisnummer, String clubpostcode, String clubstad, String clubLand, String clubemail, String clubtelefoon, String liga, String clubwebsite, Boolean visible) {
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Clubs " + "SET liga = '" + liga + "', clubnummer = '" + clubnummer + "', clubvoorzitter = '" + clubvoorzitter + "', clubstraat = '" + clubstraat + "', clubhuisnummer = '" + clubhuisnummer + "', clubpostcode = '" + clubpostcode + "', clubstad = '" + clubstad + "', clublandcode = '" + clubLand + "', clubemail = '" + clubemail + "', clubtelefoon = '" + clubtelefoon + "', clubwebsite = '" + clubwebsite + "', visible = '" + visible + "' " + "WHERE clubnummer = '" + clubnummer + "'");
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
            System.err.println("SQL Exception while updating club location: " + e);
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
                String lc = rs.getString("umpirelandcode");
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
                
                Umpire u = new Umpire(a, v, b, c, d, e, f, lc, g, h, uclub, afdArray, k, lat, lon);
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
        //System.out.println("Umpire requested from database!");
        ArrayList<Afdeling> afdarray = new ArrayList<>();
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", teamArray);
        Umpire u = new Umpire("", "", "", "", "", "", "", "", "", "", cl, afdarray, Boolean.TRUE, "", "");
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
                String lc = rs.getString("umpirelandcode");
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
                
                u = new Umpire(a, v, b, c, d, e, f, lc, g, h, uclub, afdArray, k, lat, lon);
                
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
     * @param umpirelandcode
     * @param umpiretelefoon
     * @param umpireemail
     * @param umpireclubnummer
     * @param afdeling
     * @param actief 
     * @param lat 
     * @param lon 
     */
    public void insertNewUmpireToDatabase(String umpirenaam, String umpirevoornaam, String umpirelicentie, String umpirestraat, String umpirehuisnummer, String umpirepostcode, String umpirestad, String umpirelandcode, String umpiretelefoon, String umpireemail, String umpireclubnummer, String afdeling, Boolean actief, String lat, String lon) {
        System.out.println("Insert Umpire To Database...");
        System.out.println("bool = " + actief);
        try {
            stmt = con.createStatement();
            // Replace data if exists, insert if not exist {
            //System.out.println("Inserting into Umpires: " + umpirelicentie + ", " + umpirehuisnummer + ", " + umpirepostcode + ", " + umpirestad + ", " + umpiretelefoon + ", " + umpireemail + ", " + umpireclub + ", " + afdeling ", " + umpirenaam + ", " + actief + ", " + umpirevoornaam + ".");
            stmt.executeUpdate("INSERT INTO APP.Umpires " + "VALUES (default, '" + umpirelicentie + "', '" + umpirehuisnummer + "', '" + umpirepostcode + "', '" + umpirestad + "', '" + umpiretelefoon + "', '" + umpireemail + "', '" + umpireclubnummer + "', '" + umpirestraat + "', '" + afdeling + "', '" + umpirenaam + "', '" + actief + "', '" + umpirevoornaam + "', '" + lat + "', '" + lon + "', '" + umpirelandcode + "')");
            
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
    public void updateUmpireToDatabase(String umpirenaam, String umpirevoornaam, String umpirelicentie, String umpirestraat, String umpirehuisnummer, String umpirepostcode, String umpirestad, String umpirelandcode, String umpiretelefoon, String umpireemail, String umpireclub, String afdeling, Boolean actief) {
        System.out.println("Update Umpire To Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Umpires " + "SET umpirestraat = '" + umpirestraat + "', umpirehuisnummer = '" + umpirehuisnummer + "', umpirepostcode = '" + umpirepostcode + "', umpirestad = '" + umpirestad + "', umpirelandcode = '" + umpirelandcode + "', umpiretelefoon = '" + umpiretelefoon + "', umpireemail = '" + umpireemail + "', umpireclub = '" + umpireclub + "', afdeling = '" + afdeling + "', actief = '" + actief + "', umpirenaam = '" + umpirenaam + "', umpirevoornaam = '" + umpirevoornaam + "' " + "WHERE umpirelicentie = '" + umpirelicentie + "'");
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
            System.err.println("SQL Exception while updating umpire location: " + e);
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
    
    public ArrayList<Team> getTeamsFromDatabase(Club club, String afdeling) {
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
    
    public Team getTeamFromDatabase(String teamnaam, String teamafdeling) {
        Team t = new Team(null, null);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Teams WHERE teamnaam = '" + teamnaam + "' AND teamafdeling = '" + teamafdeling + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("teamnaam");
                String d = rs.getString("discipline");
                Afdeling b = new Afdeling(rs.getString("teamafdeling"), d);
                t = new Team(a, b);
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
        return t;
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
            stmt.executeUpdate("INSERT INTO APP.Teams " + "VALUES (default, '" + teamnaam + "', '" + teamafdeling + "', '" + clubnummer + "', '" + discipline + "')");
            
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
    
    // GAMES
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
                Team ht = getTeamFromDatabase(rs.getString("hometeam"), a);
                Team vt = getTeamFromDatabase(rs.getString("visitingteam"), a);
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
                //System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
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
    
    /** Haal 1 game uit de database
     * 
     * @param gameindex
     * @return Geeft een object (Game) terug
     */
    public Game getGameFromDatabase(String gameindex) {
        String defaultGamehour = pref.get("DefaultGameTime", "14:00");
        Game g = new Game("", "", "", LocalDate.now(), defaultGamehour, null, null, null, null, null, null, "", "", null);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Games WHERE gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String w = rs.getString("week");
                String a = rs.getString("afdeling");
                String gd = rs.getString("gamedate");
                String gt = rs.getString("gametime");
                Team ht = getTeamFromDatabase(rs.getString("hometeam"), a);
                Team vt = getTeamFromDatabase(rs.getString("visitingteam"), a);
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
                //System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
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
     * @param atfield 
     */
    public void insertNewGameToDatabase(Integer week, String afdeling, String gamedate, String gametime, String hometeam, String visitingteam, String plateumpire, String base1umpire, String base2umpire, String base3umpire, String gamenumber, String gameindex, String seizoen, String atfield) {
        System.out.println("Insert Game To Database...");
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO APP.Games " + "VALUES (default, '" + afdeling + "', '" + hometeam + "', '" + visitingteam + "', '" + plateumpire + "', '" + base1umpire + "', '" + base2umpire + "', '" + base3umpire + "', '" + gamenumber + "', '" + gameindex + "', '" + week + "', '" + seizoen + "', '" + gamedate + "', '" + gametime + "', '" + atfield + "')");
            
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
            stmt.executeUpdate("UPDATE APP.Games " + "SET week = '" + Integer.toString(week) + "', afdeling = '" + afdeling + "', gamedate = '" + gamedate + "', gametime = '" + gametime + "', hometeam = '" + hometeam + "', visitingteam = '" + visitingteam + "', plateumpire = '" + plateumpire + "', base1umpire = '" + base1umpire + "', base2umpire = '" + base2umpire + "', base3umpire = '" + base3umpire + "', gamenumber = '" + gamenumber + "', seizoen = '" + seizoen + "', atfield = '" + homeclubnummer + "' " + "WHERE gameindex = '" + gameindex + "'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating game: " + e);
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
            String sql = "Select * from APP.Games where gameindex = ?";  
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
    
    public Boolean checkIfWeekHasGame(Integer week, String afdeling) throws SQLException {
        ResultSet rs = null;
        System.out.println("Checking if game exists... current Games: ?" );
        
        try {
            stmt = con.createStatement();
            String sql = "Select * from APP.Games where week = ? AND afdeling = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, Integer.toString(week));
            ps.setString(2, afdeling);
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
    
    public Integer countNumberOfGames(Integer week, String afdeling) throws SQLException {
        ResultSet rs = null;
        Integer count = 0;
        try {
            stmt = con.createStatement();
            String sql = "SELECT COUNT(*) FROM APP.GAMES AS COUNT WHERE WEEK = ? AND AFDELING = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, Integer.toString(week));
            ps.setString(2, afdeling);
            rs = ps.executeQuery();
            System.out.println("RS = " + rs);
            System.out.println(rs.getInt("count"));
            while(rs.next()){
                count=rs.getInt("count");
            }
        } catch(SQLException e) {
            
        }
        System.out.println("count = " + count);
        return count;
    }
    public Boolean checkForEmptyGame(Integer week, String afd) {
        Boolean empty;
        empty = false;
        ResultSet rs = null;
        System.out.println("Checking for empty line...");
        try {
            stmt = con.createStatement();
            String sql = "Select * from APP.Games where afdeling = ? and week = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, afd);
            ps.setString(2, Integer.toString(week));
            rs = ps.executeQuery();
            
            while(rs.next()) {
                String w = rs.getString("week");
                String a = rs.getString("afdeling");
                String gd = rs.getString("gamedate");
                String gt = rs.getString("gametime");
                Team ht = getTeamFromDatabase(rs.getString("hometeam"), a);
                Team vt = getTeamFromDatabase(rs.getString("visitingteam"), a);
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
                //System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
                if (ht == null && vt == null) {
                    empty = true;
                    System.out.println("Empty line found!");
                    return true;
                } else {
                    empty = false;
                    System.out.println("Line not empty");
                }
            }
            
                    
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if(stmt!=null) {
                    try {
                        stmt.close();
                    } catch(SQLException e) {
                        System.err.println("Could not close query statement");
                    }
                }
            }
        }
        
        return empty;
    }
    public void insertDistanceToDatabase(Umpire ump, Club club, String distance) {
        System.out.println("Insert Distance To Database...");
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO APP.Distances " + "VALUES (default, '" + ump.getUmpireLicentie() + "', '" + club.getClubNummer() + "', '" + distance + "')");
            
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
            System.err.println("SQL Exception while updating distance: " + e);
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
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", teamArray);
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
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", teamArray);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.TEAMS WHERE teamnaam = '" + team + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String clubnummer = rs.getString("club");
                //System.out.println("Club from team = " + clubnummer);
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
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", teamArray);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.GAMES WHERE gameindex = '" + gameindex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String clubnummer = rs.getString("atfield");
                //System.out.println("Get club from gameschedule: " + clubnummer);
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
            String sql = "SELECT * from APP.GAMES where gameindex = '" + gameindex + "'";
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
        String distance = "0.0";
        ArrayList<Team> teamArray = new ArrayList<>();
        Club cl = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", teamArray);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.DISTANCES WHERE umpire = '" + umpirelicentie + "' AND club = '" + clubnummer + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                distance = rs.getString("distance");
                //System.out.println("Distance = " + distance);
                
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
    public String getVergoedingenFromDatabase(String afdeling) {
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
    
    public ArrayList<Vergoedingskosten> getAllVergoedingenFromDatabase() {
        ArrayList<Vergoedingskosten> arrayVergoedingen = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Vergoedingen";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("afdeling");
                String e = rs.getString("euro");
                Vergoedingskosten verg = new Vergoedingskosten(a, e);
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
            stmt.executeUpdate("INSERT INTO APP.Vergoedingen " + "VALUES (default, '" + afdeling + "', '" + euro + "')");
            
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
    
    // Landcodes
    /** Store landcodes in database
     * 
     * @param landcode 
     */
    public void storeLandCode(String landcode) {
        try {
            String land = "";
            if (null != landcode) switch (landcode) {
                case "BE":
                    land = "België";
                    break;
                case "NL":
                    land = "Nederland";
                    break;
                case "FR":
                    land = "Frankrijk";
                    break;
                case "DE":
                    land = "Duitsland";
                    break;
                default:
                    break;
            }
            //con = createConnection();
            stmt = con.createStatement();
            if (!checkIfLandcodeExists(landcode)) {
                stmt.executeUpdate("INSERT INTO APP.landcodes " + "VALUES (default, '" + landcode + "', '" + land + "')");
            }
        } catch(SQLException e) {
            System.err.println("SQLException when inserting landcodes:" + e);
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
    
    /** Get land codes from database
     * 
     * @return 
     */
    public ArrayList<String> getLandcodesFromDatabase() {
        ArrayList<String> landcodes = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Landcodes";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String a = rs.getString("landcode");
                String e = rs.getString("land");
                landcodes.add(a);
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
        return landcodes;
    }
    
    public void deleteAllLandcodesInDatabase() {
        try {
            stmt = con.createStatement();
            String sql2 = "DELETE FROM APP.Landcodes WHERE 1=1";
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
    public Boolean checkIfLandcodeExists(String landcode) throws SQLException {
        ResultSet rs = null;
        Statement stmt1 = null;
        try {
            stmt1 = con.createStatement();
            String sql = "Select 1 from APP.Landcodes where landcode = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, landcode);
            rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } finally {
            //rs.close();
            if(stmt1!=null) {
                try{
                    stmt1.close();
                } catch(SQLException e) {
                    System.err.println("Could not close query statement");
                }
            }
        }
        return Boolean.TRUE;
    }
    
    /** Get vergoeding euro string from database
     * 
     * @param umpirelicentie afdeling of "km"
     * @return 
     */
    public Vergoeding getUitbetalingenFromDatabase(String umpirelicentie) {
        Vergoeding vergoeding = new Vergoeding(null, 0.0, 0.0, 0, 0.0, 0.0, false);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Uitbetalingen where umpirelicentie = '"+umpirelicentie+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String ump = rs.getString("umpirelicentie");
                Double km = rs.getDouble("kilometers");
                Double kme = rs.getDouble("kmeuro");
                Integer aw = rs.getInt("aantalwedstrijden");
                Double wv = rs.getDouble("wedstrijdvergoeding");
                Double tot = rs.getDouble("totaal");
                Boolean u = rs.getBoolean("uitbetaald");
                Umpire umpire = getUmpireFromDatabase(ump);
                vergoeding = new Vergoeding(umpire, km, kme, aw, wv, tot, u);
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
        return vergoeding;
    }
    
    public ArrayList<Vergoeding> getAllUitbetalingenFromDatabase() {
        Vergoeding vergoeding = new Vergoeding(null, 0.0, 0.0, 0, 0.0, 0.0, false);
        ArrayList<Vergoeding> arrayVergoedingen = new ArrayList<>();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Uitbetalingen";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String ump = rs.getString("umpirelicentie");
                Double km = rs.getDouble("kilometers");
                Double kme = rs.getDouble("kmeuro");
                Integer aw = rs.getInt("aantalwedstrijden");
                Double wv = rs.getDouble("wedstrijdvergoeding");
                Double tot = rs.getDouble("totaal");
                Boolean u = rs.getBoolean("uitbetaald");
                Umpire umpire = getUmpireFromDatabase(ump);
                vergoeding = new Vergoeding(umpire, km, kme, aw, wv, tot, u);
                arrayVergoedingen.add(vergoeding);
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
     * @param u
     * @param km
     * @param kme
     * @param aw
     * @param wv
     * @param tot
     * @param uitbetaald 
     */
    public void insertUitbetalingToDatabase(Umpire u, Double km, Double kme, Integer aw, Double wv, Double tot, Boolean uitbetaald) {
        System.out.println("Insert Uitbetaling To Database...");
        try {
            stmt = con.createStatement();
            System.out.println("kmOne type = " + km + ": " +  km.getClass().getName());
                    System.out.println("kmE type = " + kme + ": " + kme.getClass().getName());
                    System.out.println("aw type = " + aw + ": " + aw.getClass().getName());
                    System.out.println("verg type = " + wv + ": " +  wv.getClass().getName());
                    System.out.println("tot type = " + tot + ": " +  tot.getClass().getName());
            stmt.executeUpdate("INSERT INTO APP.Uitbetalingen " + "VALUES (default, '" + u.getUmpireLicentie() + "', " + km + ", " + kme + ", " + aw + ", " + wv + ", " + tot + ", '" + uitbetaald + "')");
        } catch(SQLException e) {
            System.err.println("SQL Exception while inserting uitbetaling: " + e);
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
    public void updateUitbetalingenToDatabase(Umpire u, Double km, Double kme, Integer aw, Double wv, Double tot, Boolean uitbetaald) {
        System.out.println("Update Uitbetaling in Database...");
        try {
            stmt = con.createStatement();
            // Update row
            stmt.executeUpdate("UPDATE APP.Uitbetalingen " + "SET kilometers = " + km + ", kmeuro = " + kme + ", aantalwedstrijden = " + aw + ", wedstrijdvergoeding = " + wv + ", totaal = " + tot + ", uitbetaald = '" + uitbetaald + "' "+ "where umpirelicentie = '"+u.getUmpireLicentie()+"'");
        } catch(SQLException e) {
            System.err.println("SQL Exception while updating uitbetaling: " + e);
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
    
    public Boolean checkIfUitbetalingExists(Umpire umpire) throws SQLException {
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            String sql = "Select 1 from APP.Uitbetalingen where umpirelicentie = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            //rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE afdelingsnaam = '" + naam + "'");
            ps.setString(1, umpire.getUmpireLicentie());
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
    
    public void exportGamesToWorksheet(ObservableList<Afdeling> afdelingen, Integer wk1, Integer wk2) throws FileNotFoundException {
        
        HSSFWorkbook wb = new HSSFWorkbook();//for earlier version use HSSF
        
        HSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        //headerFont.setColor(IndexedColors.BLACK.getIndex());
        
        
        afdelingen.forEach(afd -> { 
            ResultSet rs = null;

            try {
                String sql = "Select * from APP.GAMES WHERE afdeling = ? AND week >= ? AND week <= ? ORDER BY WEEK ASC";
                //rs = stmt.executeQuery();
                stmt = con.createStatement();
                //stmt = con.createStatement();
                //String sql = "Select 1 from APP.Vergoedingen where afdeling = ?";  
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, afd.getAfdelingsNaam());
                ps.setString(2, Integer.toString(wk1));
                ps.setString(3, Integer.toString(wk2));
                rs = ps.executeQuery();

                //Apache POI Jar Link-
                //http://a.mbbsindia.com/poi/release/bin/poi-bin-3.13-20150929.zip

                

                HSSFSheet sheet = wb.createSheet(afd.getAfdelingsNaam());
                HSSFRow header = sheet.createRow(0);
                CellStyle headerCellStyle = wb.createCellStyle();
                headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
                headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
                headerCellStyle.setFont(headerFont);
                
                
                header.createCell(0).setCellValue("afdeling");
                header.createCell(1).setCellValue("datum");
                header.createCell(2).setCellValue("aanvangsuur");
                header.createCell(3).setCellValue("home team");
                header.createCell(4).setCellValue("visiting team");
                header.createCell(5).setCellValue("@ field");
                header.createCell(6).setCellValue("plate umpire");
                header.createCell(7).setCellValue("base 1 umpire");
                header.createCell(8).setCellValue("base 2 umpire");
                header.createCell(9).setCellValue("base 3 umpire");
                header.setRowStyle(headerCellStyle);
                
                
                sheet.setZoom(115);//scale-150%
                Integer wk = wk1;
                int index = 1;
                while(rs.next()){
                    String nieuweWeek = rs.getString("week");
                    if (Integer.parseInt(nieuweWeek) > wk) {
                        index++;
                        wk = Integer.parseInt(nieuweWeek);
                    }
                    HSSFRow row = sheet.createRow(index);
                    row.createCell(0).setCellValue(rs.getString("afdeling"));
                    row.createCell(1).setCellValue(rs.getString("gamedate"));
                    row.createCell(2).setCellValue(rs.getString("gametime"));
                    row.createCell(3).setCellValue(rs.getString("hometeam"));
                    row.createCell(4).setCellValue(rs.getString("visitingteam"));
                    String field = rs.getString("atfield");
                    Club veld = getClubFromDatabase(field);
                    row.createCell(5).setCellValue(veld.getClubNaam());
                    Umpire pu = getUmpireFromDatabase(rs.getString("plateumpire"));
                    row.createCell(6).setCellValue(pu.getUmpireVoornaam() + " " + pu.getUmpireNaam());
                    Umpire b1 = getUmpireFromDatabase(rs.getString("base1umpire"));
                    row.createCell(7).setCellValue(b1.getUmpireVoornaam() + " " + b1.getUmpireNaam());
                    Umpire b2 = getUmpireFromDatabase(rs.getString("base2umpire"));
                    row.createCell(8).setCellValue(b2.getUmpireVoornaam() + " " + b2.getUmpireNaam());
                    Umpire b3 = getUmpireFromDatabase(rs.getString("base3Umpire"));
                    row.createCell(9).setCellValue(b3.getUmpireVoornaam() + " " + b3.getUmpireNaam());
                    index++;                   
                }
                
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
                sheet.autoSizeColumn(6);
                sheet.autoSizeColumn(7);
                sheet.autoSizeColumn(8);
                sheet.autoSizeColumn(9);
                FileOutputStream fileOut = new FileOutputStream("Wedstrijdschema.xlsx");// before 2007 version xls
                wb.write(fileOut);
                fileOut.close();

                stmt.close();
                rs.close();

                } catch (SQLException | FileNotFoundException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Wedstrijdschema geëxporteerd naar Excel Sheet.");
        alert.showAndWait();
    }
    
    public void exportUitbetalingenToWorksheet() throws FileNotFoundException, IOException {
        
        HSSFWorkbook wb = new HSSFWorkbook();//for earlier version use HSSF
        HSSFSheet sheet = wb.createSheet("Vergoedingen");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("umpire naam");
        header.createCell(1).setCellValue("# games");
        header.createCell(2).setCellValue("kilometers (km)");
        header.createCell(3).setCellValue("kilometers €");
        header.createCell(4).setCellValue("games €");
        header.createCell(5).setCellValue("totaal €");
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.setZoom(115);//scale-150%

        
        HSSFDataFormat cf = wb.createDataFormat();
        CellStyle currencyCellStyle = wb.createCellStyle();
        currencyCellStyle.setDataFormat(cf.getFormat("[$€-413] #.##0,00;[$€-413] #.##0,00-"));

        ResultSet rs = null;
        Double totdist = 0.0;
        Double totdistverg = 0.0;
        Double totwed = 0.0;
        Double tottot = 0.0;
        int index = 1;
        try {
            String sql = "Select * from APP.UITBETALINGEN";
            //rs = stmt.executeQuery();
            stmt = con.createStatement();
            //stmt = con.createStatement();
            //String sql = "Select 1 from APP.Vergoedingen where afdeling = ?";  
            PreparedStatement ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            //Apache POI Jar Link-
            //http://a.mbbsindia.com/poi/release/bin/poi-bin-3.13-20150929.zip

            
            while(rs.next()){
                HSSFRow row = sheet.createRow(index);
                Umpire ump = getUmpireFromDatabase(rs.getString("umpirelicentie"));
                row.createCell(0).setCellValue(ump.getUmpireVoornaam() + " " + ump.getUmpireNaam());
                row.createCell(1).setCellValue(rs.getString("aantalwedstrijden"));
                Double dist = Double.parseDouble(rs.getString("kilometers"));
                row.createCell(2).setCellValue(round(dist, 3));
                totdist += dist;
                Double distverg = Double.parseDouble(rs.getString("kmeuro"));
                row.createCell(3).setCellValue(round(distverg, 2));
                row.getCell(3).setCellStyle(currencyCellStyle);
                totdistverg += distverg;
                Double wedverg = Double.parseDouble(rs.getString("wedstrijdvergoeding"));
                row.createCell(4).setCellValue(round(wedverg, 2));
                row.getCell(4).setCellStyle(currencyCellStyle);
                totwed += wedverg;
                Double totverg = Double.parseDouble(rs.getString("totaal"));
                row.createCell(5).setCellValue(round(totverg, 2));
                row.getCell(5).setCellStyle(currencyCellStyle);

                tottot += totverg;
                index++;                   
            }
            
            stmt.close();
            rs.close();

            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            } 
       
        index++;
        HSSFRow rowb = sheet.createRow(index);
        rowb.createCell(2).setCellValue(round(totdist, 3));
        rowb.createCell(3).setCellValue(round(totdistverg, 2));
        rowb.getCell(3).setCellStyle(currencyCellStyle);
        rowb.createCell(4).setCellValue(round(totwed, 2));
        rowb.getCell(4).setCellStyle(currencyCellStyle);
        rowb.createCell(5).setCellValue(round(tottot, 2));
        rowb.getCell(5).setCellStyle(currencyCellStyle);

        
        FileOutputStream fileOut = new FileOutputStream("vergoedingen.xlsx");// before 2007 version xls
        wb.write(fileOut);
        fileOut.close();
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vergoedingen geëxporteerd naar Excel Sheet.");
            alert.showAndWait();
    }
    
    public void exportUitbetalingUmpireToWorksheet(ObservableList<Umpire> umpires, ObservableList<Afdeling> afdelingen) throws FileNotFoundException {
        
        HSSFWorkbook wb = new HSSFWorkbook();//for earlier version use HSSF
        
        HSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);

        umpires.forEach(u -> {
            ResultSet rs = null;
            Double totdist = 0.0;
            Double totdisverg = 0.0;
            Double totverg = 0.0;
            Double tottot = 0.0;
            try {
                String sql = "Select * from APP.UITBETALINGEN where umpirelicentie = ?";
                //rs = stmt.executeQuery();
                stmt = con.createStatement();
                //stmt = con.createStatement();
                //String sql = "Select 1 from APP.Vergoedingen where afdeling = ?";  
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, u.getUmpireLicentie());
                rs = ps.executeQuery();

                
                
                
                HSSFFont cellUmpFont = wb.createFont();
                cellUmpFont.setColor(IndexedColors.BLUE.getIndex());
                CellStyle cellUmpStyle = wb.createCellStyle();
                cellUmpStyle.setFont(cellUmpFont);
                
                HSSFDataFormat cf = wb.createDataFormat();
                CellStyle currencyCellStyle = wb.createCellStyle();
                currencyCellStyle.setDataFormat(cf.getFormat("[€-413] #.##0,00;[€-413] #.##0,00-"));
                //Apache POI Jar Link-
                //http://a.mbbsindia.com/poi/release/bin/poi-bin-3.13-20150929.zip
                Umpire ump = getUmpireFromDatabase(u.getUmpireLicentie());
                HSSFSheet sheet = wb.createSheet(ump.getUmpireNaam() + " " + ump.getUmpireVoornaam());
                HSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("umpire");
                header.createCell(1).setCellValue("# games");
                header.createCell(2).setCellValue("kilometers (km)");
                header.createCell(3).setCellValue("kilometers €");
                header.createCell(4).setCellValue("games €");
                header.createCell(5).setCellValue("totaal €");
                
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
                sheet.setZoom(115);//scale-150%
        
                int index = 1;
                while(rs.next()){
                    HSSFRow row = sheet.createRow(index);
                    //Umpire ump = getUmpireFromDatabase(rs.getString("umpirelicentie"));
                    row.createCell(0).setCellValue(ump.getUmpireVoornaam() + " " + ump.getUmpireNaam());
                    row.createCell(1).setCellValue(rs.getString("aantalwedstrijden"));
                    row.createCell(2).setCellValue(round(Double.parseDouble(rs.getString("kilometers")), 3));
                    row.createCell(3).setCellValue(round(Double.parseDouble(rs.getString("kmeuro")), 2));
                    row.createCell(4).setCellValue(round(Double.parseDouble(rs.getString("wedstrijdvergoeding")), 2));
                    row.createCell(5).setCellValue(round(Double.parseDouble(rs.getString("totaal")), 2));
                    index++;                   
                }
                
                index++;
                
                

                ArrayList<Game> games = getAllGamesFromDatabase();
                Integer rownumber = 0;
                
                HSSFRow rowb = sheet.createRow(index);
                rowb.createCell(0).setCellValue("afdeling");
                rowb.createCell(1).setCellValue("wedstrijdnummer");
                rowb.createCell(2).setCellValue("datum");
                rowb.createCell(3).setCellValue("uur");
                rowb.createCell(4).setCellValue("home team");
                rowb.createCell(5).setCellValue("visiting team");
                rowb.createCell(6).setCellValue("@ field");
                rowb.createCell(7).setCellValue("Plate umpire");
                rowb.createCell(8).setCellValue("Base 1 umpire");
                rowb.createCell(9).setCellValue("Base 2 umpire");
                rowb.createCell(10).setCellValue("Base 3 umpire");
                rowb.createCell(11).setCellValue("kilometers");
                rowb.createCell(12).setCellValue("km vergoeding €");
                rowb.createCell(13).setCellValue("vergoeding");
                rowb.createCell(14).setCellValue("totale vergoeding");
                
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
                sheet.autoSizeColumn(6);
                sheet.autoSizeColumn(7);
                sheet.autoSizeColumn(8);
                sheet.autoSizeColumn(9);
                sheet.autoSizeColumn(10);
                sheet.autoSizeColumn(11);
                sheet.autoSizeColumn(12);
                sheet.autoSizeColumn(13);
                sheet.autoSizeColumn(14);
                sheet.setZoom(115);//scale-150%
                index++;
                for (Game g : games) {
                    HSSFRow row = sheet.createRow(index);
                    if (g.getPlateUmpire().getUmpireLicentie().equals(u.getUmpireLicentie()) || (g.getBase1Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) || (g.getBase2Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) || (g.getBase3Umpire().getUmpireLicentie().equals(u.getUmpireLicentie()))) {
                        row.createCell(0).setCellValue(g.getAfdelingString());
                        row.createCell(1).setCellValue(g.getGameNumber());
                        row.createCell(2).setCellValue(mainpanel.localDateToString(g.getGameDatum()));
                        row.createCell(3).setCellValue(g.getGameUur());
                        row.createCell(4).setCellValue(g.getHomeTeam().getTeamNaam());
                        row.createCell(5).setCellValue(g.getVisitingTeam().getTeamNaam());
                        row.createCell(6).setCellValue(g.getHomeClub().getClubNaam());
                        row.createCell(7).setCellValue(g.getPlateUmpire().getUmpireNaam() + " " + g.getPlateUmpire().getUmpireVoornaam());
                        row.createCell(8).setCellValue(g.getBase1Umpire().getUmpireNaam() + " " + g.getBase1Umpire().getUmpireVoornaam());
                        row.createCell(9).setCellValue(g.getBase2Umpire().getUmpireNaam() + " " + g.getBase2Umpire().getUmpireVoornaam());
                        row.createCell(10).setCellValue(g.getBase3Umpire().getUmpireNaam() + " " + g.getBase3Umpire().getUmpireVoornaam());
                        if (g.getPlateUmpire().getUmpireLicentie().equals(u.getUmpireLicentie())) {
                            rownumber = 7;
                        } else if (g.getBase1Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) {
                            rownumber = 8;
                        } else if (g.getBase2Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) {
                            rownumber = 9;
                        } else if (g.getBase3Umpire().getUmpireLicentie().equals(u.getUmpireLicentie())) {
                            rownumber = 10;
                        }
                        row.getCell(rownumber).setCellStyle(cellUmpStyle);
                        
                        Double distance = 2 * Double.parseDouble(getDistFromUmpireClub(u.getUmpireLicentie(), g.getHomeClub().getClubNummer()));
                        totdist += distance;
                        row.createCell(11).setCellValue(round(distance, 3));
                        Double kmverg = round(distance * Double.parseDouble(getVergoedingenFromDatabase("km")), 2);
                        totdisverg += kmverg;
                        row.createCell(12).setCellValue(kmverg);
                        row.getCell(12).setCellStyle(currencyCellStyle);
                        Double afdverg = Double.parseDouble(getVergoedingenFromDatabase(g.getAfdelingString()));
                        totverg += afdverg;
                        row.createCell(13).setCellValue(afdverg);
                        row.getCell(13).setCellStyle(currencyCellStyle);
                        Double somverg = kmverg + afdverg;
                        tottot += somverg;
                        row.createCell(14).setCellValue(round(somverg, 2));
                        row.getCell(14).setCellStyle(currencyCellStyle);
                        index++;
                    }
                    
                }
                index++;
                HSSFRow rowc = sheet.createRow(index);
                rowc.createCell(11).setCellValue(round(totdist, 3));
                rowc.createCell(12).setCellValue(totdisverg);
                rowc.getCell(12).setCellStyle(currencyCellStyle);
                rowc.createCell(13).setCellValue(totverg);
                rowc.getCell(13).setCellStyle(currencyCellStyle);
                rowc.createCell(14).setCellValue(tottot);
                rowc.getCell(14).setCellStyle(currencyCellStyle);
                
                
                FileOutputStream fileOut = new FileOutputStream("vergoedingen_details.xlsx");// before 2007 version xls
                wb.write(fileOut);
                fileOut.close();
            
                stmt.close();
                rs.close();

                } catch (SQLException | FileNotFoundException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
       
        
        
        
        
        });
        
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vergoedingen geëxporteerd naar Excel Sheet.");
            alert.showAndWait();
    }
    
    private static double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    public Boolean umpireHasGameOnSameDay(Umpire umpire, LocalDate gameDatum) {
        Boolean bool = false;
        mainpanel = new MainPanel();
        String datum = mainpanel.americanDateToString(gameDatum);
        ArrayList<Game> games = checkForDoubleBooking(datum);
        games.forEach(g -> {
            System.out.println("Game: " + g.getGameNumber() + ", " + g.getHomeClub());
        });
        if (games.size() > 0) {
            bool = true;
        } 
        return bool;
    }
    
    /** Get all games from database on certain date
     * 
     * @param datum
     * @return 
     */
    private ArrayList<Game> checkForDoubleBooking(String datum) {
        System.out.println("Get all games from database....on date....." + datum);
        ArrayList<Game> arrayGames = new ArrayList<>();
        System.out.println("Datum = " + datum);
        try {
            stmt = con.createStatement();
            String sql = "SELECT * from APP.Games where gamedate = '" + datum + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String w = rs.getString("week");
                String a = rs.getString("afdeling");
                String gd = rs.getString("gamedate");
                String gt = rs.getString("gametime");
                Team ht = getTeamFromDatabase(rs.getString("hometeam"), a);
                Team vt = getTeamFromDatabase(rs.getString("visitingteam"), a);
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
                //System.out.println("DB -> games: " + w + ", " + a + ", " + gd + ", " + gt + ", " + ht + ", " + vt + ", " + pu + ", " + b1 + ", " + b2 + ", " + b3 + ", " + gn + ", " + gi + ", " + se + ".");
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
}

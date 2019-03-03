package javafxcusmanager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pieter
 */
public class Database {
    public Statement stmt;
    
    public Database() {
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/cusdb;create=false;user=pstragier;password=Isabelle30?");
            stmt = con.createStatement();

            
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        }
    }
    
    public void addNewAfdelingToDatabase(String naam, String discipline, Boolean kbbsf) {
        try {
            
            
            // Replace data if exists, insert if not exist {
            stmt.executeUpdate("INSERT INTO APP.Afdelingen " + "VALUES ('" + naam + "', '" + discipline + "', '" + kbbsf + "')");
            
            // Write afdelingen (obsolete)
            ResultSet rs = stmt.executeQuery("SELECT * FROM APP.Afdelingen");
            while(rs.next()) {
                String s = rs.getString("afdelingsnaam");
                System.out.println(s);
            }
        } catch(SQLException e) {
            System.err.println("SQL Exception: " + e);
        } 
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

/**
 *
 * @author pieter
 */
import java.sql.*;
 
public class JavaDB {  // JDK 7 and above
   public static void main(String[] args) {
      try (
         // Step 1: Allocate a database "Connection" object
         Connection conn = DriverManager.getConnection(
               "jdbc:derby:test_db;create=true");
 
         // Step 2: Allocate a "Statement" object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3 & 4: Execute a SQL UPDATE via executeUpdate()
         //   which returns an int indicating the number of rows affected.
         // Increase the price by 7% and qty by 1 for id=1001
         int returnCode;
         returnCode = stmt.executeUpdate(
               "create table test_table (id int primary key, name varchar(20))");
         System.out.println(returnCode + " records affected.");
 
         returnCode = stmt.executeUpdate(
               "insert into test_table values (1, 'one'), (2, 'two')");
         System.out.println(returnCode + " records affected.");
 
         ResultSet rset = stmt.executeQuery("select * from test_table");
         while (rset.next()) {
            System.out.println(rset.getInt("id") + ", " + rset.getString("name"));
         }
      } catch(SQLException ex) {
         ex.printStackTrace();
      }
      // Step 5: Close the resources - Done automatically by try-with-resources
 
      // Shutdown the Derby
      try {
         DriverManager.getConnection("jdbc:derby:;shutdown=true");
      } catch (SQLException ex)  {
         if ( ex.getSQLState().equals("XJ015") ) {
            System.out.println("successfully shutdown!");
         }
      }
   }
}
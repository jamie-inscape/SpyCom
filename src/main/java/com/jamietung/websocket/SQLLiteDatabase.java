/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamietung.websocket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jamietung
 */
public class SQLLiteDatabase {
    
    /**
     * Return mysql database Connection.
     * @return con
     */
    public static Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spyDB?useSSL=false", "root", "BalloonCat");
            return con;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    /**
     * Return select all result set.
     * @return 
     */
    public static ResultSet selectAll() {
        try{  
            Connection con = connect();
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from spy");  
            return rs;
              
        }catch(Exception e){ 
            System.out.println(e);
        }   
        return null;
    }
    
    
    
    /**
     * add player with specified id
     * @param id
     * @param status
     */
    public static void addPlayer(int id, String userName, String password, String spyId, int team) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();  
            stmt.executeUpdate("INSERT INTO spyDB.spy (id, name, password, spyid, team) " +
"VALUES ("+id+",'"+ userName + "',"+ password + ",'"+ spyId + ",'"+ team + "', 0)"); 
        } catch (Exception ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * check login and password.
     * @param agentId
     * @param password
     * @return 
     */
    public static int checkLogin(String agentId, String password) {
        Connection con = connect();
        try { 
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("SELECT password, team from spyDB.spy where spyid = " + agentId);
            while(rs.next()) {
                String pw = rs.getString("password");
                System.out.println("pw:"+pw);
                if (pw.equals(password)) {
                    int team = rs.getInt("team");
                    return team;
                } else {
                    return 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * Add clue to mysql database.
     * @param clue
     * @param agentId
     * @param team 
     */
    public static void addClue(String clue, int team) {
        Connection con = connect();
        try {
            Statement stmt=con.createStatement();  
            stmt.executeUpdate("INSERT INTO spyDB.clues (team, clue) VALUES (" + team + ",'"+ clue + "')"); 
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * get clues 
     * @param team
     * @param number
     * @return 
     */
    public static ResultSet getClues(int team, int number) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();
            return stmt.executeQuery("SELECT clue from spyDB.clues where team = " + team + " Limit " + number);
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Return true if code with specific team is revealed.
     * @param code
     * @param team
     * @return 
     */
    public static boolean checkIsSeen(String code, int team) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT revealed from spyDB.codes where team = " + team + " and code =" + code);
            while(rs.next()) {
                String revealed = rs.getString("revealed");
                if (revealed.equals("y")) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Return true if code with team is in table.
     * @param code
     * @param team
     * @return 
     */
    public static boolean checkIsValidCode(String code, int team) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(code) as numFound from spyDB.codes where team = " + team + " and code =" + code);
            while(rs.next()){ 
                int numFound = rs.getInt("numFound");
                if (numFound > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * changed revealed to true.
     * @param code 
     */
    public static void makeSeen(String code) {
        Connection con = connect();
        try {
            Statement stmt=con.createStatement();  
            System.out.println("update spyDB.codes set revealed = 'y' where code = " + code);
            stmt.executeUpdate("update spyDB.codes set revealed = 'y' where code = " + code); 
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return number of revealed clues for team.
     * @param team
     * @return 
     */
    public static int countRevealed(int team) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(code) as numFound from spyDB.codes where team = " + team + " and revealed = 'y'");
            while(rs.next()){ 
                int numFound = rs.getInt("numFound");
                return numFound;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
}

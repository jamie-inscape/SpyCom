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
    public static void addClue(String clue, String agentId, int team) {
        Connection con = connect();
        try {
            Statement stmt=con.createStatement();  
            stmt.executeUpdate("INSERT INTO spyDB.clues (team, clue, agentId) VALUES (" + team + ",'"+ clue + "','"+ agentId + "')"); 
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * get clues 
     * @param team
     * @return 
     */
    public static ResultSet getClues(int team) {
        try {
            Connection con = connect();
            Statement stmt=con.createStatement();
            return stmt.executeQuery("SELECT agentId, clue from spyDB.clues where team = " + team);
        } catch (SQLException ex) {
            Logger.getLogger(SQLLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

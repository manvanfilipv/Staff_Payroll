/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Pay;

import com.mycompany.CS360DB.CS360DB;
import com.mycompany.User.UserDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manos
 */
public class PayDB {
    
    /**
     * Add the pay in database
     *
     * @param pay
     * @return 
     * @throws ClassNotFoundException
     */
    public static Boolean addPay(Pay pay) throws ClassNotFoundException {
        
        Statement stmt = null;
        Connection con = null;
        Boolean canAdd = false;
        
        try {
            Calendar cal = Calendar.getInstance();
            //int currday = cal.get(Calendar.DAY_OF_MONTH);
            int currday = 1;
            if (currday == 1){
                con = CS360DB.getConnection();
                stmt = con.createStatement();

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("INSERT INTO ")
                        .append(" wages (fullname, date, pay) ")
                        .append(" VALUES (")
                        .append("'").append(pay.getFullname()).append("',")
                        .append("'").append(pay.getDate()).append("',")
                        .append("'").append(pay.getPay()).append("');");

                String generatedColumns[] = {"userid"};
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
                stmtIns.executeUpdate();

                System.out.println("#DB: " + "Successfully Added Pay.");
                canAdd = true;
            } else {
                System.out.println("#DB: Addition of Pay failed - Not the 1st Day of Month.");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        
        return canAdd;
    }
        
    /**
    * Returns true if pay can change 
    *
    * @param p
    * @return 
    * @throws ClassNotFoundException
    */
    public static boolean canChange(Pay p) throws ClassNotFoundException {

        boolean canChange = false;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM wages ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(p.getID()).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {           
                // if current pay is bigger from the new pay
                if (Double.parseDouble(res.getString("pay")) > p.getPay()){
                    canChange = true;
                }
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        return canChange;
    }
    
    /**
     * Updates information for specific pay
     *
     * @param p
     * @return 
     * @throws ClassNotFoundException
     */
    public static Boolean updatePay(Pay p) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        Boolean canUpdate = false;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE wages ")
                    .append(" SET ")
                    .append(" fullname = ").append("'").append(p.getFullname()).append("',")
                    .append(" date = ").append("'").append(p.getDate()).append("',")
                    .append(" pay = ").append("'").append(p.getPay()).append("'")
                    .append(" WHERE userid = ").append("'").append(p.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successful Update of Pay.");
            canUpdate = true;

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        
        return canUpdate;
    }
    
    /**
     * Delete information for specific pay based on id
     *
     * @param id
     * @param end
     * @throws ClassNotFoundException
     */
    public static void deletePay(Integer id,String end) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE wages ")
                    .append(" SET ")
                    .append(" date = ").append("'").append(end).append("',")
                    .append(" pay = ").append("'").append(-0.0).append("'")
                    .append(" WHERE userid = ").append("'").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successful Deletion of Pay.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }
    
    
    /**
     * Delete information for specific pay
     *
     * @param pay
     * @throws ClassNotFoundException
     */
    public static void deletePay(Pay pay) throws ClassNotFoundException {
        
        Statement stmt = null;
        Connection con = null;
        
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM wages ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(pay.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successful Deletion of Pay.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }
    
    /**
     * Get pay based on ID
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static Pay getPay(Integer id) throws ClassNotFoundException {
        
        Pay pay = null;
        Statement stmt = null;
        Connection con = null;
        
        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM wages ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                pay = new Pay();
                pay.setFullname(res.getString("fullname"));
                pay.setDate(res.getString("date"));
                pay.setPay(res.getDouble("pay"));
                pay.setID(res.getInt("userid"));
            }
            
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return pay;
    }
    
    /**
     * Get a list of pays
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Pay> getPays() throws ClassNotFoundException {
        List<Pay> pays = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM wages;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Pay pay = new Pay();
                pay.setDate(res.getString("date"));
                pay.setFullname(res.getString("fullname"));
                pay.setID(res.getInt("userid"));
                pay.setPay(res.getDouble("pay"));
                pays.add(pay);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return pays;
    }
    
    /**
     * Close db connection
     *
     * @param stmt
     * @param con
     */
    private static void closeDBConnection(Statement stmt, Connection con) {
        // Close connection
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PayDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

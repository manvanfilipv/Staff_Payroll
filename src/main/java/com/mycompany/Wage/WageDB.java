/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Wage;

import com.mycompany.CS360DB.CS360DB;
import com.mycompany.Pay.Pay;
import com.mycompany.Pay.PayDB;
import com.mycompany.User.User;
import com.mycompany.User.UserDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author manos
 */
public class WageDB {

    private static double basic = 1000.0;
    private static double research_bonus = 200.0;
    private static double library_bonus = 100.0;

    /**
     * Returns basic wage
     *
     * @return
     */
    public static double getBasic() {
        return basic;
    }

    /**
     * Sets basic wage
     *
     * @param bsc
     */
    public static void setBasic(double bsc) {
        basic = bsc;
    }

    /**
     * Returns research_bonus
     *
     * @return
     */
    public static double getResearchBonus() {
        return research_bonus;
    }

    /**
     * Sets research_bonus
     *
     * @param research
     */
    public static void setResearchBonus(double research) {
        research_bonus = research;
    }

    /**
     * Returns library_bonus
     *
     * @return
     */
    public static double getLibraryBonus() {
        return library_bonus;
    }

    /**
     * Sets library_bonus
     *
     * @param library
     */
    public static void setLibraryBonus(double library) {
        library_bonus = library;
    }

    /**
     * Calculate and Add pay for a specific user id
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws java.text.ParseException
     */
    public static String CalculateWage(Integer id) throws ClassNotFoundException, ParseException {

        String result = "";

        Wage wage = WageDB.getWage(id);
        wage.setCategory(wage.getCategory());
        wage.setStatus(wage.getStatus());
        wage.setYears(wage.getYears());
        wage.setID(id);

        String category = wage.getCategory();

        double pay = 0;

        Calendar cal = Calendar.getInstance();
        //int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currday = cal.get(Calendar.DAY_OF_MONTH);
        int lastday = currday;
        int juvenile = 0;
        String[] parts = UserDB.getUser(wage.getID()).getYearChildren().split(",");
        if (!Arrays.toString(parts).equals("[]")) {
            for (int i = 0; i < parts.length; i++) {
                if (Integer.parseInt(parts[i]) < 18) {
                    juvenile++;
                }
            }
        }
        if (lastday == currday) {
            switch (category.trim().toUpperCase()) {
                case "PERMANENT TEACHER":
                    pay += GetBasic(id);
                    if (wage.getYears() > 1) {
                        for (int i = 0; i < wage.getYears() - 1; i++) {
                            pay += (pay * 0.15);
                        }
                    }
                    if (wage.getStatus().toUpperCase().trim().equals("MARRIED")) {
                        pay += (0.05 * pay);
                    }
                    if (juvenile > 0) {
                        pay += (juvenile * 0.05 * pay);
                    }
                    pay += WageDB.getResearchBonus();
                    break;
                case "PERMANENT ADMIN":
                    pay += GetBasic(id);
                    if (wage.getYears() > 1) {
                        for (int i = 0; i < wage.getYears() - 1; i++) {
                            pay += (pay * 0.15);
                        }
                    }
                    if (wage.getStatus().toUpperCase().trim().equals("MARRIED")) {
                        pay += (0.05 * pay);
                    }
                    if (juvenile > 0) {
                        pay += (juvenile * 0.05 * pay);
                    }
                    break;
                case "CONTRACT ADMIN":
                    pay += GetBasic(id);
                    if (wage.getStatus().toUpperCase().trim().equals("MARRIED")) {
                        pay += (0.05 * pay);
                    }
                    if (juvenile > 0) {
                        pay += (juvenile * 0.05 * pay);
                    }
                    break;
                case "CONTRACT TEACHER":
                    pay += GetBasic(id);
                    if (wage.getStatus().toUpperCase().trim().equals("MARRIED")) {
                        pay = (0.05 * pay) + pay;
                    }
                    if (juvenile > 0) {
                        pay += (juvenile * 0.05 * pay);
                    }
                    pay += WageDB.getLibraryBonus();
                    break;
                default:
                    System.out.println("#DB: This kind of category does not exist.");
            }
            Pay p = new Pay();

            
            //p.setDate(yearMonth.lengthOfMonth() + "-" + st[1] + "-" + st[2]);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String today = simpleDateFormat.format(date);
            YearMonth yearMonth;

            String[] tod = simpleDateFormat.format(date).split("-", 3);
            String[] st = UserDB.getUser(id).getStartDate().split("-", 3);

            Date d_today = simpleDateFormat.parse(today);
            Date d_start = simpleDateFormat.parse(UserDB.getUser(id).getStartDate());

            long diff_start_today = d_start.getTime() - d_today.getTime();
            long diff_days_start_today = diff_start_today / (24 * 60 * 60 * 1000);
            

            if (UserDB.getUser(id).getCategory().equals("Contract Teacher")
                    || UserDB.getUser(id).getCategory().equals("Contract Admin")
                    || UserDB.getUser(id).getCategory().equals("Contract Teacher(Promoted")
                    || UserDB.getUser(id).getCategory().equals("Contract Admin(Promoted)")) {
                Date d_end = simpleDateFormat.parse(UserDB.getUser(id).getEndDate());
                long diff_end_today = d_end.getTime() - d_today.getTime();
                long diff_days_end_today = diff_end_today / (24 * 60 * 60 * 1000);
                if (diff_days_start_today > 0) {
                    p.setDate("-");                    
                }
                yearMonth = YearMonth.of(Integer.parseInt(st[2]), Integer.parseInt(st[1]));
                String lastday_start = yearMonth.lengthOfMonth()+"-"+st[1]+"-"+st[2];
                Date d_lastday_start = simpleDateFormat.parse(lastday_start);
                long diff_lastday_start = d_lastday_start.getTime() - d_today.getTime();
                long diff_days_lastday_start_today = diff_lastday_start / (24 * 60 * 60 * 1000);
                if ((diff_days_lastday_start_today < 0) && (diff_days_end_today>0)){
                    String[] d1;
                    if (tod[1].equals("01")){
                        d1 = (tod[0]+"-"+"12"+"-"+Integer.toString(Integer.parseInt(tod[2]) - 1)).split("-",3);
                    } else {
                        d1 = (tod[0]+"-"+Integer.toString(Integer.parseInt(tod[1]) - 1)+"-"+tod[2]).split("-",3);
                    }   
                    yearMonth = YearMonth.of(Integer.parseInt(d1[2]), Integer.parseInt(d1[1]));
                    String d2 = yearMonth.lengthOfMonth()+"-"+ d1[1]+"-"+d1[2];
                    p.setDate(d2);
                } else if (diff_days_end_today<0){
                    p.setDate(UserDB.getUser(id).getEndDate());
                }
            } else if (UserDB.getUser(id).getCategory().equals("Permanent Teacher")
                    || UserDB.getUser(id).getCategory().equals("Permanent Admin")){
                if (diff_days_start_today> 0){
                    p.setDate("-");
                }
                yearMonth = YearMonth.of(Integer.parseInt(st[2]), Integer.parseInt(st[1]));
                String lastday_start = yearMonth.lengthOfMonth()+"-"+st[1]+"-"+st[2];
                Date d_lastday_start = simpleDateFormat.parse(lastday_start);
                long diff_lastday_start = d_lastday_start.getTime() - d_today.getTime();
                long diff_days_lastday_start_today = diff_lastday_start / (24 * 60 * 60 * 1000);
                if (diff_days_lastday_start_today<0){
                    String[] d1;
                    if (tod[1].equals("01")){
                        d1 = (tod[0]+"-"+"12"+"-"+Integer.toString(Integer.parseInt(tod[2]) - 1)).split("-",3);
                    } else {
                        d1 = (tod[0]+"-"+Integer.toString(Integer.parseInt(tod[1]) - 1)+"-"+tod[2]).split("-",3);
                    }  
                    yearMonth = YearMonth.of(Integer.parseInt(d1[2]), Integer.parseInt(d1[1]));
                    String d2 = yearMonth.lengthOfMonth()+"-"+ d1[1]+"-"+d1[2];
                    p.setDate(d2);
                }
            }
            
            p.setFullname(UserDB.getUser(id).getfullName());
            p.setID(id);
            p.setPay(pay);

            if (PayDB.getPay(id) != null) {
                if (PayDB.updatePay(p)) {
                    result = "Pay Updated";
                }
            } else {
                if (PayDB.addPay(p)) {
                    result = "Pay Added";
                }
            }

        } else {
            System.out.println("#DB: Hiring an Employee is permitted only in the 1st day of the Month.");
        }
        return result;
    }

    /**
     * Get Basic Wage for a specific user id
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static double GetBasic(Integer id) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        double basic_wage = 0.0;
        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                basic_wage = res.getDouble("basic_wage");
            } else {
                System.out.println("#DB: User with ID " + id + " does not exist in order to calculate Wage.");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return basic_wage;
    }

    /**
     * Update Basic Wage for Permanent in Database
     *
     * @param value
     * @param ID
     * @return
     * @throws ClassNotFoundException
     */
    public static Integer ChangeBasic(double value, Integer ID) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        Integer result = 0;

        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(ID).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {

                if (res.getString("category").equals("Permanent Teacher") || res.getString("category").equals("Permanent Admin")) {
                    if (value > UserDB.getUser(ID).getBasic()) {
                        updatePermanentBasic(value, ID);
                        System.out.println("#DB: Basic Wage of Permanent Updated");
                        result++;
                    }
                }

                setBasic(res.getDouble("basic_wage"));

            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return result;
    }

    /**
     * Update Basic Wage for Permanent in Database
     *
     * @param value
     * @param ID
     * @throws ClassNotFoundException
     */
    public static void updatePermanentBasic(double value, Integer ID) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" basic_wage = ").append("'").append(value).append("'")
                    .append(" WHERE userid = ").append("'").append(ID).append("';");

            stmt.executeUpdate(insQuery.toString());

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Add the wage in database
     *
     * @param wage
     * @return
     * @throws ClassNotFoundException
     */
    public static Boolean addWage(Wage wage) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        boolean canAdd = false;
        try {

            Calendar cal = Calendar.getInstance();
            //int currday = cal.get(Calendar.DAY_OF_MONTH);
            int currday = 1;
            if (currday == 1) {
                con = CS360DB.getConnection();
                stmt = con.createStatement();

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("INSERT INTO ")
                        .append(" payroll (category, status,years) ")
                        .append(" VALUES (")
                        .append("'").append(wage.getCategory()).append("',")
                        .append("'").append(wage.getStatus()).append("',")
                        .append("'").append(wage.getYears()).append("');");
                String generatedColumns[] = {"userid"};
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
                stmtIns.executeUpdate();

                System.out.println("#DB: " + "Successfully added Payroll.");
                canAdd = true;

            } else {
                System.out.println("#DB: Add of Payroll Failed  - Not the 1st day of the Month.");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return canAdd;
    }

    /**
     * Updates information for specific wage
     *
     * @param wage
     * @throws ClassNotFoundException
     */
    public static void updateWage(Wage wage) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            wage.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE payroll ")
                    .append(" SET ")
                    .append(" status = ").append("'").append(wage.getStatus()).append("',")
                    .append(" years = ").append("'").append(wage.getYears()).append("',")
                    .append(" category = ").append("'").append(wage.getCategory()).append("'")
                    .append(" WHERE userid = ").append("'").append(wage.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successfully updated Wage.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Returns true if user can be deleted
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static Integer canDelete(Integer id) throws ClassNotFoundException {

        Integer canDelete = 0;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM payroll ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                if (res.getString("category").toUpperCase().equals("PERMANENT TEACHER")
                        || res.getString("category").toUpperCase().equals("PERMANENT ADMIN")) {

                    Calendar cal = Calendar.getInstance();
                    //int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    int lastday;
                    int currday = cal.get(Calendar.DAY_OF_MONTH);
                    lastday = currday;
                    if (lastday == currday) {
                        canDelete = 1;
                    } else {
                        canDelete = -3;
                    }
                } else {
                    canDelete = -2;
                }
            } else {
                canDelete = -1;
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        return canDelete;
    }

    /**
     * Promote if can into permanent
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     * @throws java.text.ParseException
     */
    public static Integer promote(Integer id) throws ClassNotFoundException, ParseException {

        Wage wage = new Wage();
        User user = new User();
        Integer canPromote = 0;
        Statement stmt = null;
        Connection con = null;
        double basic_wage;

        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM payroll ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {

                Calendar cal = Calendar.getInstance();
                //int currday = cal.get(Calendar.DAY_OF_MONTH);
                int currday = 1;
                if (currday == 1) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1 = new Date();
                    String today = formatter.format(date1);
                    switch (res.getString("category").trim().toUpperCase()) {

                        case "CONTRACT TEACHER":
                            String[] result = fix_end_date(UserDB.getUser(id).getStartDate(), UserDB.getUser(id).getEndDate(), today).split(",", 3);
                            UserDB.updateEndDate(id, result[1]);
                            UserDB.updateStartDate(id, result[2]);
                            UserDB.updateCategory(id, "Contract Teacher(Promoted)");
                            WageDB.updateCategory(id, "Contract Teacher(Promoted)");
                            // New entry table user
                            user.setfullName(UserDB.getUser(id).getfullName());
                            user.setBank(UserDB.getUser(id).getBank());
                            user.setCategory("Permanent Teacher");
                            basic_wage = 0.0;
                            for (int i = 0; i < UserDB.getUsers().size(); i++) {
                                if (UserDB.getUsers().get(i).getCategory().equals("Permanent Teacher")) {
                                    if (WageDB.getBasic() != UserDB.getUsers().get(i).getBasic()) {
                                        basic_wage = UserDB.getUsers().get(i).getBasic();
                                        break;
                                    }
                                }
                            }
                            if (basic_wage != 0.0) {
                                user.setBasic(basic_wage);
                            } else {
                                user.setBasic(WageDB.getBasic());
                            }
                            user.setChildren(UserDB.getUser(id).getChildren());
                            user.setDepartment(UserDB.getUser(id).getDepartment());
                            user.setEndDate("-");
                            user.setIBAN(UserDB.getUser(id).getIBAN());
                            user.setStartDate(result[0]);
                            user.setStatus(UserDB.getUser(id).getStatus());
                            user.setTelephone(UserDB.getUser(id).getTelephone());
                            user.setYearChildren(UserDB.getUser(id).getYearChildren());
                            user.setAddress(UserDB.getUser(id).getAddress());
                            UserDB.addUser(user);
                            // New entry table wage
                            wage.setCategory("Permanent Teacher");
                            wage.setStatus(res.getString("status"));
                            wage.setYears(res.getInt("years"));
                            WageDB.addWage(wage);
                            // New entry table pay
                            CalculateWage(UserDB.getUsers().get(UserDB.getUsers().size() - 1).getUserID());
                            updateWage(wage);
                            canPromote = 1;
                            break;
                        case "CONTRACT ADMIN":
                            String[] results = fix_end_date(UserDB.getUser(id).getStartDate(), UserDB.getUser(id).getEndDate(), today).split(",", 3);
                            UserDB.updateEndDate(id, results[1]);
                            UserDB.updateStartDate(id, results[2]);
                            UserDB.updateCategory(id, "Contract Admin(Promoted)");
                            WageDB.updateCategory(id, "Contract Admin(Promoted)");
                            // New entry table user
                            user.setfullName(UserDB.getUser(id).getfullName());
                            user.setBank(UserDB.getUser(id).getBank());
                            user.setCategory("Permanent Admin");
                            basic_wage = 0.0;
                            for (int i = 0; i < UserDB.getUsers().size(); i++) {
                                if (UserDB.getUsers().get(i).getCategory().equals("Permanent Admin")) {
                                    if (WageDB.getBasic() != UserDB.getUsers().get(i).getBasic()) {
                                        basic_wage = UserDB.getUsers().get(i).getBasic();
                                        break;
                                    }
                                }
                            }
                            if (basic_wage != 0.0) {
                                user.setBasic(basic_wage);
                            } else {
                                user.setBasic(WageDB.getBasic());
                            }
                            user.setChildren(UserDB.getUser(id).getChildren());
                            user.setDepartment(UserDB.getUser(id).getDepartment());
                            user.setEndDate("-");
                            user.setIBAN(UserDB.getUser(id).getIBAN());
                            user.setStartDate(results[0]);
                            user.setStatus(UserDB.getUser(id).getStatus());
                            user.setTelephone(UserDB.getUser(id).getTelephone());
                            user.setYearChildren(UserDB.getUser(id).getYearChildren());
                            user.setAddress(UserDB.getUser(id).getAddress());
                            UserDB.addUser(user);
                            // New entry table wage
                            wage.setCategory("Permanent Admin");
                            wage.setStatus(res.getString("status"));
                            wage.setYears(res.getInt("years"));
                            WageDB.addWage(wage);
                            // New entry table pay
                            CalculateWage(UserDB.getUsers().get(UserDB.getUsers().size() - 1).getUserID());
                            updateWage(wage);
                            canPromote = 1;
                            break;
                        default:
                            System.out.println("#DB: User with ID " + id + " is already permanent.");
                            break;
                    }
                } else {
                    System.out.println("#DB: Promotion Failed - Not the 1st day of the Month.");
                    canPromote = 2;
                }
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PayDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return canPromote;
    }

    public static String fix_end_date(String start, String end, String today) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String[] tod = today.split("-", 3);
        String day_today = tod[0];
        String month_today = tod[1];
        String years_today = tod[2];

        Date d1 = simpleDateFormat.parse(end);
        Date d2 = simpleDateFormat.parse(today);
        Date d3 = simpleDateFormat.parse(start);

        String end_date_contract = "";
        String start_date_permanent = "";
        String start_date_contract = start;

        long diff = d2.getTime() - d1.getTime();
        long diff_start = d3.getTime() - d2.getTime();
        long diff_days_start_ = diff_start / (24 * 60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        Calendar calendar = new GregorianCalendar();

        // if promotion_date before end_date
        if (diff_days_start_ > 0) {
            if (month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today) + 1, 1, 1);
            } else if (day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today), Integer.parseInt(day_today));
            } else if (!month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today) + 1, 1);
            }
            calendar.add(Calendar.MONTH, -1);
            start_date_permanent = simpleDateFormat.format(calendar.getTime());
            start_date_contract = "(CANCELLED)";
            end_date_contract = "(CANCELLED)";
        } else if (diffDays <= 0) {
            if (month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today) + 1, 1, 1);
            } else if (day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today), Integer.parseInt(day_today));
            } else if (!month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today) + 1, 1);
            }
            calendar.add(Calendar.MONTH, -1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            end_date_contract = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, +1);
            start_date_permanent = simpleDateFormat.format(calendar.getTime());
        } else if (diffDays > 0) {
            end_date_contract = end;
            if (month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today) + 1, 1, 1);
            } else if (day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today), Integer.parseInt(day_today));
            } else if (!month_today.equals("12") && !day_today.equals("01")) {
                calendar = new GregorianCalendar(Integer.parseInt(years_today), Integer.parseInt(month_today) + 1, 1);
            }
            calendar.add(Calendar.MONTH, -1);
            start_date_permanent = simpleDateFormat.format(calendar.getTime());
        }

        return (start_date_permanent + "," + end_date_contract + "," + start_date_contract);
    }

    /**
     * Delete information for specific wage based on ID
     *
     * @param id
     * @throws ClassNotFoundException
     */
    public static void deleteWage(Integer id) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE payroll ")
                    .append(" SET ")
                    .append(" category = ").append("'").append(UserDB.getUser(id).getCategory()).append("'")
                    .append(" WHERE userid = ").append("'").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successfully Deleted Wage.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete information for specific wage
     *
     * @param wage
     * @throws ClassNotFoundException
     */
    public static void deleteWage(Wage wage) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM payroll ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(wage.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successfully Deleted Payroll.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(WageDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Get wage based on ID
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static Wage getWage(Integer id) throws ClassNotFoundException {

        Wage wage = null;
        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM payroll ")
                    .append(" WHERE ")
                    .append(" userid = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                wage = new Wage();
                wage.setCategory(res.getString("category"));
                wage.setStatus(res.getString("status"));
                wage.setYears(res.getInt("years"));
                wage.setID(res.getInt("userid"));
            } else {
                System.out.println("#DB: User with ID " + id + " does not exist.");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return wage;
    }

    /**
     * Get a list of wages
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Wage> getWages() throws ClassNotFoundException {
        List<Wage> wages = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM payroll;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Wage wage = new Wage();
                wage.setCategory(res.getString("category"));
                wage.setStatus(res.getString("status"));
                wage.setID(res.getInt("userid"));
                wage.setYears(Integer.parseInt(res.getString("years")));
                wages.add(wage);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return wages;
    }

    /**
     * Update years of payroll based on ID
     *
     * @param id
     * @param years
     * @throws ClassNotFoundException
     */
    public static void updateYears(Integer id, String years) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE payroll ")
                    .append(" SET ")
                    .append(" years = ").append("'").append(years).append("'")
                    .append(" WHERE userid = '").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            System.out.println("#DB: Successfully updated Years of Employee.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Update category of payroll based on ID
     *
     * @param id
     * @param category
     * @throws ClassNotFoundException
     */
    public static void updateCategory(Integer id, String category) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE payroll ")
                    .append(" SET ")
                    .append(" category = ").append("'").append(category).append("'")
                    .append(" WHERE userid = '").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            System.out.println("#DB: Successfully updated Category of Payroll.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
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
                Logger.getLogger(UserDB.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDB.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

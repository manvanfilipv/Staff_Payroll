/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.User;

import com.mycompany.CS360DB.CS360DB;
import com.mycompany.Pay.Pay;
import com.mycompany.Pay.PayDB;
import com.mycompany.Wage.WageDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manos
 */
public class UserDB {

    /**
     * Make Payments per category
     *
     * @param Category
     * @return
     * @throws ClassNotFoundException
     */
    public static String PayUsers(String Category) throws ClassNotFoundException {

        String data = "";
        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM wages;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            Calendar cal = Calendar.getInstance();
            //int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int currday = cal.get(Calendar.DAY_OF_MONTH);
            int lastday = currday;
            if (lastday == currday) {
                while (res.next() == true) {
                    if (WageDB.getWage(Integer.parseInt(res.getString("userid"))).getCategory().toLowerCase()
                            .equals(Category.toLowerCase())) {
                        User user = getUser(res.getInt("userid"));
                        data += "<b>ID</b> - " + res.getString("userid")
                                + "<br>Name - " + res.getString("fullname")
                                + "<br>Category - " + user.getCategory()
                                + "<br>Bank - " + user.getBank()
                                + "<br>IBAN - " + user.getIBAN()
                                + "<br>Pay - " + Double.parseDouble(res.getString("pay")) + "<br><br>";
                    }
                }
            } else {
                data = ("#DB: Payment Failed - Not the last day of the Month.");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return data;
    }

    /**
     * Get sum of pays for a specific category
     *
     * @param Category
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Pay> StatusPayPerCategory(String Category) throws ClassNotFoundException {

        List<Pay> users = new ArrayList<>();
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
                if (WageDB.getWage(Integer.parseInt(res.getString("userid"))).getCategory().toLowerCase()
                        .equals(Category.toLowerCase())) {
                    Pay pay = new Pay();
                    pay.setID(Integer.parseInt(res.getString("userid")));
                    pay.setFullname(res.getString("fullname"));
                    pay.setPay(Double.parseDouble(res.getString("pay")));
                    pay.setDate(res.getString("date"));
                    users.add(pay);
                }
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return users;
    }

    /**
     * Get sum of pays for a specific category
     *
     * @param Category
     * @return
     * @throws ClassNotFoundException
     * @throws java.text.ParseException
     */
    public static String getSum(String Category) throws ClassNotFoundException, ParseException {

        Statement stmt = null;
        Connection con = null;
        StringBuilder result = new StringBuilder();

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM wages;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            String category;
            String[] start_user;
            String[] end_user;
            double pay;
            List<String> years = new ArrayList<>();
            List<Double> sums = new ArrayList<>();

            while (res.next() == true) {
                category = WageDB.getWage(Integer.parseInt(res.getString("userid"))).getCategory();
                if (category.toLowerCase().equals(Category.toLowerCase())) {
                    if (category.equals("Permanent Teacher") || category.equals("Permanent Admin")) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String[] today = dtf.format(now).split("-", 3);
                        start_user = UserDB.getUser(Integer.parseInt(res.getString("userid"))).getStartDate().split("-", 3);
                        Integer first_year_months = 12 - Integer.parseInt(start_user[1]) + 1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = new Date();
                        String todays = simpleDateFormat.format(date);

                        Date d_today = simpleDateFormat.parse(todays);
                        Date d_start = simpleDateFormat.parse(UserDB.getUser(Integer.parseInt(res.getString("userid"))).getStartDate());

                        long diff_start_today = d_start.getTime() - d_today.getTime();
                        long diff_days_start_today = diff_start_today / (24 * 60 * 60 * 1000);
                        if (diff_days_start_today < 0) {

                            if (!years.contains(Integer.toString(Integer.parseInt(start_user[2])))) {
                                years.add(Integer.toString(Integer.parseInt(start_user[2])));
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2])));
                                pay = Double.parseDouble(res.getString("pay")) * first_year_months;
                                if (index < sums.size() && index != -1) {
                                    //index not exists
                                    pay += sums.get(years.indexOf(start_user[2]));
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            } else {
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2])));
                                pay = Double.parseDouble(res.getString("pay")) * first_year_months;
                                if (index < sums.size() && index != -1) {
                                    //index not exists
                                    pay += sums.get(years.indexOf(start_user[2]));
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            }
                        }
                        System.out.println(years);
                        System.out.println(sums);
                        Integer remaining_years = Integer.parseInt(today[2]) - Integer.parseInt(start_user[2]);
                        for (int i = 0; i < remaining_years - 1; i++) { //1
                            if (!years.contains(Integer.toString(Integer.parseInt(start_user[2]) + i + 1))) {
                                years.add(Integer.toString(Integer.parseInt(start_user[2]) + i + 1));
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2]) + i + 1));
                                pay = Double.parseDouble(res.getString("pay")) * 12;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(years.indexOf(start_user[2]) + i + 1);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            } else {
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2]) + i + 1));
                                pay = Double.parseDouble(res.getString("pay")) * 12;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(years.indexOf(start_user[2]) + i + 1);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            }

                        }
                        System.out.println(years);
                        System.out.println(sums);
                        long monthsBetween = ChronoUnit.MONTHS.between(
                                LocalDate.parse(today[2] + "-01-01").withDayOfMonth(1),
                                LocalDate.parse(today[2] + "-" + today[1] + "-" + today[0]).withDayOfMonth(1));

                        YearMonth yearMonth = YearMonth.of(Integer.parseInt(today[2]), Integer.parseInt(today[1]));
                        if (yearMonth.lengthOfMonth() == Integer.parseInt(today[0])) {
                            monthsBetween += 1;
                        }
                        if (!years.contains(Integer.toString(Integer.parseInt(today[2])))) {
                            years.add(Integer.toString(Integer.parseInt(today[2])));
                        }
                        Integer index = years.indexOf(Integer.toString(Integer.parseInt(today[2])));

                        pay = Double.parseDouble(res.getString("pay")) * monthsBetween;
                        if (index < sums.size() && index != -1) {
                            pay += sums.get(index);
                            sums.set(index, pay);
                        } else {
                            sums.add(index, pay);
                        }
                        System.out.println(years);
                        System.out.println(sums);
                        System.out.println("------------------------------------");

                    } else if (category.equals("Contract Teacher") || category.equals("Contract Admin")) {
                        start_user = UserDB.getUser(Integer.parseInt(res.getString("userid"))).getStartDate().split("-", 3);
                        end_user = UserDB.getUser(Integer.parseInt(res.getString("userid"))).getEndDate().split("-", 3);

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String[] end = UserDB.getUser(Integer.parseInt(res.getString("userid"))).getEndDate().split("-", 3);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = new Date();
                        String todays = simpleDateFormat.format(date);

                        Date d_today = simpleDateFormat.parse(todays);
                        Date d_start = simpleDateFormat.parse(UserDB.getUser(Integer.parseInt(res.getString("userid"))).getStartDate());
                        Date d_end = simpleDateFormat.parse(UserDB.getUser(Integer.parseInt(res.getString("userid"))).getEndDate());

                        long diff_start_today = d_start.getTime() - d_today.getTime();
                        long diff_end_today = d_end.getTime() - d_today.getTime();

                        long diff_days_start_today = diff_start_today / (24 * 60 * 60 * 1000);
                        long diff_days_end_today = diff_end_today / (24 * 60 * 60 * 1000);

                        if (diff_days_start_today > 0) {
                            // Step 1
                            long monthsBetween = ChronoUnit.MONTHS.between(
                                    LocalDate.parse(start_user[2] + "-" + start_user[1] + "-" + start_user[0]).withDayOfMonth(1),
                                    LocalDate.parse(start_user[2] + "-12-31").withDayOfMonth(1)) + 1;
                            if (!years.contains(Integer.toString(Integer.parseInt(start_user[2])))) {
                                years.add(Integer.toString(Integer.parseInt(start_user[2])));
                            }
                            Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2])));
                            pay = Double.parseDouble(res.getString("pay")) * monthsBetween;
                            if (index < sums.size() && index != -1) {
                                pay += sums.get(index);
                                sums.set(index, pay);
                            } else {
                                sums.add(index, pay);
                            }

                            // Step 2
                            monthsBetween = ChronoUnit.MONTHS.between(
                                    LocalDate.parse(Integer.toString(Integer.parseInt(start_user[2]) + 1) + "-01-01").withDayOfMonth(1),
                                    LocalDate.parse(end[2] + "-" + end[1] + "-" + end[0]).withDayOfMonth(1)) + 1;
                            if (monthsBetween <= 12) {
                                if (!years.contains(Integer.toString(Integer.parseInt(start_user[2])))) {
                                    years.add(Integer.toString(Integer.parseInt(start_user[2])));
                                }
                                index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2])));
                                pay = Double.parseDouble(res.getString("pay")) * monthsBetween;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(index);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            } else {
                                Integer years_passed = 0;
                                long months = 0;
                                long temp = monthsBetween;
                                for (int i = 0; i < monthsBetween; i++) { //34
                                    if (temp > 12) {
                                        years_passed++;
                                    } else {
                                        months = temp;
                                        break;
                                    }
                                    temp -= 12;
                                }
                                for (int i = 0; i < years_passed; i++) {
                                    if (!years.contains(Integer.toString(Integer.parseInt(start_user[2]) + i + 1))) {
                                        years.add(Integer.toString(Integer.parseInt(start_user[2]) + i + 1));
                                    }
                                    index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2]) + i + 1));
                                    pay = Double.parseDouble(res.getString("pay")) * 12;
                                    if (index < sums.size() && index != -1) {
                                        pay += sums.get(index);
                                        sums.set(index, pay);
                                    } else {
                                        sums.add(index, pay);
                                    }
                                }
                                if (!years.contains(Integer.toString(Integer.parseInt(end[2])))) {
                                    years.add(Integer.toString(Integer.parseInt(end[2])));
                                }
                                index = years.indexOf(Integer.toString(Integer.parseInt(end[2])));
                                pay = Double.parseDouble(res.getString("pay")) * months;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(index);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            }
                            System.out.println(years);
                            System.out.println(sums);
                        } else if ((diff_days_start_today <= 0 && diff_days_end_today <= 0) || (diff_days_start_today < 0 && diff_days_end_today>0)) {
                            // 01-01-2018 31-12-2019

                            long monthsBetween = ChronoUnit.MONTHS.between(
                                    LocalDate.parse(start_user[2] + "-" + start_user[1] + "-" + start_user[0]).withDayOfMonth(1),
                                    LocalDate.parse(end[2] + "-" + end[1] + "-" + end[0]).withDayOfMonth(1)) + 1;
                            if (monthsBetween <= 12) {
                                if (!years.contains(Integer.toString(Integer.parseInt(start_user[2])))) {
                                    years.add(Integer.toString(Integer.parseInt(start_user[2])));
                                }
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2])));
                                pay = Double.parseDouble(res.getString("pay")) * monthsBetween;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(index);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            } else {
                                Integer years_passed = 0;
                                long months = 0;
                                long temp = monthsBetween;
                                for (int i = 0; i < monthsBetween; i++) { //34
                                    if (temp >= 12) {
                                        years_passed++;
                                    } else {
                                        months = temp;
                                        break;
                                    }
                                    temp -= 12;
                                }
                                System.out.println("YEARS "+ years_passed);
                                System.out.println("MONTHS "+months);
                                for (int i = 0; i < years_passed; i++) {
                                    if (!years.contains(Integer.toString(Integer.parseInt(start_user[2]) + i ))) {
                                        years.add(Integer.toString(Integer.parseInt(start_user[2]) + i));
                                    }
                                    Integer index = years.indexOf(Integer.toString(Integer.parseInt(start_user[2]) + i));
                                    pay = Double.parseDouble(res.getString("pay")) * 12;
                                    if (index < sums.size() && index != -1) {
                                        pay += sums.get(index);
                                        sums.set(index, pay);
                                    } else {
                                        sums.add(index, pay);
                                    }
                                }
                                if (!years.contains(Integer.toString(Integer.parseInt(end[2])))) {
                                    years.add(Integer.toString(Integer.parseInt(end[2])));
                                }
                                Integer index = years.indexOf(Integer.toString(Integer.parseInt(end[2])));
                                pay = Double.parseDouble(res.getString("pay")) * months;
                                if (index < sums.size() && index != -1) {
                                    pay += sums.get(index);
                                    sums.set(index, pay);
                                } else {
                                    sums.add(index, pay);
                                }
                            }
                            System.out.println("---"+years);
                            System.out.println("---"+sums);
                        }
                    }
                }
            }
            System.out.println(sums);
            System.out.println(years);

            if (sums.isEmpty() || years.isEmpty()) {
                result.append("#DB: No users under this category.");
            } else {
                if (sums.size() == years.size()) {
                    result.append("#DB <b>Category</b> -> ").append(Category).append("<br>");
                    for (int i = 0; i < sums.size(); i++) {
                        result.append("<b>Year: </b>").append(years.get(i))
                                .append("<b>   Sum: </b>").append(sums.get(i)).append("<br>");
                    }
                }
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return result.toString();
    }

    /**
     * Get payroll based on category
     *
     * @param Category
     * @return
     * @throws ClassNotFoundException
     */
    public static String getPayroll(String Category) throws ClassNotFoundException {

        List<Double> pay;
        pay = new ArrayList<>();
        StringBuilder result = new StringBuilder();

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
                if (WageDB.getWage(Integer.parseInt(res.getString("userid"))).getCategory()
                        .toUpperCase().equals(Category.toUpperCase())) {
                    pay.add(Double.parseDouble(res.getString("pay")));
                }
            }

            if (!pay.isEmpty()) {
                double max;
                double min;
                double avg;
                max = Collections.max(pay);
                min = Collections.min(pay);
                // Calculate avg
                Double sum = 0.0;
                for (Double mark : pay) {
                    sum += mark;
                }
                avg = sum / pay.size();
                result.append("Category -> ").append(Category).append("<br> Max -> ")
                        .append(max).append("<br> Min -> ").append(min).append("<br> Avg -> ")
                        .append(avg);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        if (!result.toString().equals("")) {
            return result.toString();
        } else {
            return "No Users Under this Category";
        }
    }

    /**
     * Get a list of users
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<User> getUsers() throws ClassNotFoundException {
        List<User> users = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                User user = new User();
                user.setfullName(res.getString("fullname"));
                user.setStatus(res.getString("status"));
                user.setUserID(res.getInt("userid"));
                user.setChildren(res.getInt("children"));
                user.setYearChildren(res.getString("year_children"));
                user.setCategory(res.getString("category"));
                user.setDepartment(res.getString("department"));
                user.setStartDate(res.getString("start_date"));
                user.setEndDate(res.getString("end_date"));
                user.setAddress(res.getString("address"));
                user.setTelephone(res.getString("telephone"));
                user.setIBAN(res.getString("IBAN"));
                user.setBank(res.getString("bank"));
                user.setBasic(Double.parseDouble(res.getString("basic_wage")));
                users.add(user);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return users;
    }

    /**
     * Get all names
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> getAllUsersNames() throws ClassNotFoundException {

        List<String> userNames = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT fullname FROM users;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                userNames.add(res.getString("fullname"));
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return userNames;
    }

    /**
     * Get user based on ID
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static User getUser(Integer id) throws ClassNotFoundException {

        User user = null;
        Statement stmt = null;
        Connection con = null;

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
                user = new User();
                user.setUserID(res.getInt("userid"));
                user.setStatus(res.getString("status"));
                user.setfullName(res.getString("fullname"));
                user.setUserID(res.getInt("userid"));
                user.setChildren(res.getInt("children"));
                user.setYearChildren(res.getString("year_children"));
                user.setCategory(res.getString("category"));
                user.setDepartment(res.getString("department"));
                user.setStartDate(res.getString("start_date"));
                user.setEndDate(res.getString("end_date"));
                user.setAddress(res.getString("address"));
                user.setTelephone(res.getString("telephone"));
                user.setBasic(Double.parseDouble(res.getString("basic_wage")));
                user.setIBAN(res.getString("IBAN"));
                user.setBank(res.getString("bank"));
            } else {
                System.out.println("#DB: User with ID  " + id + " does not exist.");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return user;
    }

    /**
     * Add the member in the database.
     *
     * @param user
     * @return
     * @throws ClassNotFoundException
     */
    public static Boolean addUser(User user) throws ClassNotFoundException {

        // Check that we have all we need
        try {
            user.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean canAdd = false;
        Statement stmt = null;
        Connection con = null;
        try {

            //Calendar cal = Calendar.getInstance();
            //int currday = cal.get(Calendar.DAY_OF_MONTH);
            int currday = 1;
            if (currday == 1) {

                con = CS360DB.getConnection();
                stmt = con.createStatement();

                StringBuilder insQuery = new StringBuilder();

                insQuery.append("INSERT INTO ")
                        .append(" users (basic_wage, fullname, status, children, year_children, category, "
                                + "department, start_date, end_date, address, telephone, IBAN, "
                                + "bank) ")
                        .append(" VALUES (")
                        .append("'").append(user.getBasic()).append("',")
                        .append("'").append(user.getfullName()).append("',")
                        .append("'").append(user.getStatus()).append("',")
                        .append("'").append(user.getChildren()).append("',")
                        .append("'").append(user.getYearChildren()).append("',")
                        .append("'").append(user.getCategory()).append("',")
                        .append("'").append(user.getDepartment()).append("',")
                        .append("'").append(user.getStartDate()).append("',")
                        .append("'").append(user.getEndDate()).append("',")
                        .append("'").append(user.getAddress()).append("',")
                        .append("'").append(user.getTelephone()).append("',")
                        .append("'").append(user.getIBAN()).append("',")
                        .append("'").append(user.getBank()).append("');");

                String generatedColumns[] = {"userid"};
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
                stmtIns.executeUpdate();

                // Get information magically completed from database
                ResultSet rs = stmtIns.getGeneratedKeys();
                if (rs.next()) {
                    // Update value of setID based on database
                    int id = rs.getInt(1);
                    user.setUserID(id);
                }
                System.out.println("#DB: Successfully Added Employee with ID " + user.getUserID() + ".");
                canAdd = true;

            } else {
                System.out.println("#DB: Addition of Employee failed - Not the 1st of the Month.");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return canAdd;
    }

    /**
     * Update information for specific user
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void updateUser(User user) throws ClassNotFoundException {

        // Check that we have all we need
        try {
            user.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" fullname = ").append("'").append(user.getfullName()).append("',")
                    .append(" status = ").append("'").append(user.getStatus()).append("',")
                    .append(" children = ").append("'").append(user.getChildren()).append("',")
                    .append(" year_children = ").append("'").append(user.getYearChildren()).append("',")
                    .append(" category = ").append("'").append(user.getCategory()).append("',")
                    .append(" department = ").append("'").append(user.getDepartment()).append("',")
                    .append(" start_date = ").append("'").append(user.getStartDate()).append("',")
                    .append(" end_date = ").append("'").append(user.getEndDate()).append("',")
                    .append(" address = ").append("'").append(user.getAddress()).append("',")
                    .append(" telephone = ").append("'").append(user.getTelephone()).append("',")
                    .append(" IBAN = ").append("'").append(user.getIBAN()).append("',")
                    .append(" address = ").append("'").append(user.getAddress()).append("',")
                    .append(" bank = ").append("'").append(user.getBank()).append("'")
                    .append(" WHERE userid = ").append("'").append(user.getUserID()).append("';");
            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: Successfully updated User.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Update category of user based on ID
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

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" category = ").append("'").append(category).append("'")
                    .append(" WHERE userid = '").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            System.out.println("#DB: Successfully updated Category of Employee.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Update end_date of user based on ID
     *
     * @param id
     * @param end_date
     * @throws ClassNotFoundException
     */
    public static void updateEndDate(Integer id, String end_date) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" end_date = ").append("'").append(end_date).append("'")
                    .append(" WHERE userid = '").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            System.out.println("#DB: Successfully updated End date of Employee.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Update start_date of user based on ID
     *
     * @param id
     * @param start_date
     * @throws ClassNotFoundException
     */
    public static void updateStartDate(Integer id, String start_date) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" start_date = ").append("'").append(start_date).append("'")
                    .append(" WHERE userid = '").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            System.out.println("#DB: Successfully updated Start date of Employee.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete information for specific user based on ID
     *
     * @param id
     * @param end
     * @throws ClassNotFoundException
     */
    public static void deleteUser(Integer id, String end) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" end_date = ").append("'").append(end).append("',")
                    .append(" category = ").append("'").append(UserDB.getUser(id).getCategory()).append("(Retired/Fired)").append("'")
                    .append(" WHERE userid = ").append("'").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());

            WageDB.deleteWage(id);
            PayDB.deletePay(id, end);

            System.out.println("#DB: Successfully Deleted User.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Make an SQL question to the database
     *
     * @param question
     * @return
     * @throws ClassNotFoundException
     */
    public static String SQL(String question) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        String text = "";

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            try {
                insQuery.append(question);
                stmt.executeUpdate(insQuery.toString());
            } catch (SQLException ex) {
                text = ex.toString();
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
        return text;
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
                Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.CS360DB;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author manos
 */
public class CS360DB {

    private static final String URL = "jdbc:mysql://localhost";
    private static final String DATABASE = "test";
    private static final int PORT = 3306;
    private static final String UNAME = "root";
    private static final String PASSWD = "";

    /**
     * Attempts to establish a database connection
     *
     * @return a connection to the database
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL + ":" + PORT + "/" + DATABASE + "?characterEncoding=UTF-8", UNAME, PASSWD);
    }

    public static String getUserName() {
        return UNAME;
    }

}
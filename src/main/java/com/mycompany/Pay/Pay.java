/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Pay;

/**
 *
 * @author manos
 */
public class Pay {
    private String fullname;
    private String date;
    private double pay;
    private Integer userID;
    
    /**
     * Default Constructor
     *
     */
    public Pay() {
        this.fullname = "";
        this.date = "";
        this.pay = 0.0;
        this.userID = 0;
    }
    
    /**
     * Constructor
     *
     * @param fullname
     * @param date
     * @param pay
     * @param userID
     */
    public Pay(String fullname, String date, double pay, Integer userID){
        this.fullname = fullname;
        this.date = date;
        this.pay = pay;
        this.userID = userID;
    }
    
    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((fullname == null || fullname.trim().isEmpty())
            || (date == null || date.trim().isEmpty())
            || (String.valueOf(pay) == null || String.valueOf(pay).trim().isEmpty())) {
            throw new Exception("Missing fields for pay!");  // Something went wrong with the fields
        }
    }
    
    /* Setters - Getters */
    
    /**
     * Returns Full name
     *
     * @return
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * Sets the Full name
     *
     * @param fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    /**
     * Returns Date
     *
     * @return
     */
    public String getDate() {
        return date;
    }
    /**
     * Sets the Date
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * Returns Date
     *
     * @return
     */
    public double getPay() {
        return pay;
    }
    /**
     * Sets pay
     *
     * @param pay
     */
    public void setPay(double pay) {
        this.pay = pay;
    }
    
    /**
     * Returns ID
     *
     * @return
     */
    public Integer getID() {
        return userID;
    }
    /**
     * Sets the ID
     *
     * @param id
     */
    public void setID(Integer id) {
        this.userID = id;
    }
    
}

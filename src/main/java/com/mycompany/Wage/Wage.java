/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Wage;

import com.mycompany.User.UserDB;
import java.text.ParseException;

/**
 *
 * @author manos
 */
public class Wage {

    private String category;
    private String status;
    private Integer years;
    private Integer userID;

    /**
     * Default Constructor
     *
     */
    public Wage() {
        this.category = "";
        this.status = "";
        this.years = 0;
        this.userID = 0;
    }

    /**
     * Constructor
     *
     * @param category
     * @param status
     * @param years
     * @param userID
     */
    public Wage(String category, String status, Integer years, Integer userID) {
        this.category = category;
        this.status = status;
        this.years = years;
        this.userID = userID;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((category == null || category.trim().isEmpty())
                || (status == null || status.trim().isEmpty())
                || (years == null || years < 0)) {
            throw new Exception("Missing fields for wage!");  // Something went wrong with the fields
        }
    }

    /**
     * Method that changes basic pays and bonus
     *
     * @param pay
     * @param value
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    public Integer ChangeSums(String pay, double value) throws ClassNotFoundException, ParseException {
        Integer updatePay = 0;
        Integer canChange = 0;
        switch (pay.trim()) {
            case "BWP":
                for (int i = 0; i < UserDB.getUsers().size(); i++) {
                    updatePay += WageDB.ChangeBasic(value, UserDB.getUsers().get(i).getUserID());
                }
                break;
            case "RB":
                if (value > WageDB.getResearchBonus()) {
                    WageDB.setResearchBonus(value);
                    updatePay++;
                }
                break;
            case "LB":
                if (value > WageDB.getLibraryBonus()) {
                    WageDB.setLibraryBonus(value);
                    updatePay++;
                }
                break;
            default:
                canChange = -1;
                break;
        }
        // Update Pays
        if (updatePay > 0) {
            canChange = 1;
            for (int i = 0; i < UserDB.getUsers().size(); i++) {
                WageDB.CalculateWage(UserDB.getUsers().get(i).getUserID());
            }
        }
        return canChange;
    }

    /* Setters - Getters */
    /**
     * Get the Category
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the Category
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns family status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the family status
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns years of service
     *
     * @return
     */
    public Integer getYears() {
        return years;
    }

    /**
     * Sets the years of service
     *
     * @param years
     */
    public void setYears(Integer years) {
        this.years = years;
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

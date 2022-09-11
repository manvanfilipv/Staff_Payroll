/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.User;

import java.io.Serializable;

/**
 *
 * @author manos
 */
public class User implements Serializable {

    private String fullname;
    private String status;          // Marital Status
    private Integer children;        // Number of children
    private String year_children;
    private String category;
    private String department;
    private String startDate;       // (format: ΧΧΧΧ-MM-HH)
    private String endDate;
    private String address;
    private String telephone;
    private String iban;
    private String bank;
    private double basic;
    private Integer userID;

    /**
     * Default Constructor
     *
     */
    public User() {
        this.fullname = "";
        this.status = "";
        this.children = 0;
        this.year_children = "";
        this.category = "";
        this.department = "";
        this.startDate = "";
        this.endDate = "";
        this.address = "";
        this.telephone = "";
        this.iban = "";
        this.bank = "";
        this.userID = 0;
    }

    /**
     * Constructor
     *
     * @param fullname
     * @param status
     * @param children
     * @param year_children
     * @param category
     * @param Department
     * @param startDate
     * @param endDate
     * @param address
     * @param telephone
     * @param iban
     * @param bank
     * @param basic
     */
    public User(String fullname,
            String status,
            Integer children,
            String year_children,
            String category,
            String Department,
            String startDate,
            String endDate,
            String address,
            String telephone,
            String iban,
            String bank,
            double basic) {
        this.fullname = fullname;
        this.status = status;
        this.children = children;
        this.year_children = year_children;
        this.category = category;
        this.department = Department;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.telephone = telephone;
        this.iban = iban;
        this.bank = bank;
        this.basic = basic;
        this.userID = 0;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((fullname == null || fullname.trim().isEmpty())
                || (status == null || status.trim().isEmpty())
                || (children == null || children < 0)
                || (children > 0 && year_children == null)
                || (category == null || category.trim().isEmpty())
                || (basic < 0)
                || (department == null || department.trim().isEmpty())
                || (address == null || address.trim().isEmpty())
                || (telephone == null || telephone.trim().isEmpty())
                || (iban == null || iban.trim().isEmpty())
                || (bank == null || bank.trim().isEmpty())) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
        }
    }

    /* Getters and Setters */
    /**
     * Get the user ID
     *
     * @return
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * Set the userID
     *
     * @param userID
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    
    /**
     * Get basic wage
     *
     * @return basic
     */
    public double getBasic() {
        return basic;
    }

    /**
     * Set Basic
     *
     * @param Basic
     */
    public void setBasic(double Basic) {
        this.basic = Basic;
    }

    /**
     * Get last name
     *
     * @return lastName
     */
    public String getfullName() {
        return fullname;
    }

    /**
     * Set last name
     *
     * @param fullname
     */
    public void setfullName(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Get the marital status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the number of children
     *
     * @return
     */
    public Integer getChildren() {
        return children;
    }

    /**
     * Set the number of children
     *
     * @param children
     */
    public void setChildren(Integer children) {
        this.children = children;
    }

    /**
     * Get the ages of children
     *
     * @return
     */
    public String getYearChildren() {
        return year_children;
    }

    /**
     * Set the ages of children
     *
     * @param year_children
     */
    public void setYearChildren(String year_children) {
        this.year_children = year_children;
    }

    /**
     * Get the category of personnel
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of personnel
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get Department
     *
     * @return
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Set Department
     *
     * @param department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Get startDate
     *
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set StartDate
     *
     * @param StartDate
     */
    public void setStartDate(String StartDate) {
        this.startDate = StartDate;
    }

    /**
     * Get endDate
     *
     * @return
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set EndDate
     *
     * @param EndDate
     */
    public void setEndDate(String EndDate) {
        this.endDate = EndDate;
    }

    /**
     * Get address
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get telephone
     *
     * @return
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Set telephone
     *
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Get IBAN T
     *
     * @return
     */
    public String getIBAN() {
        return iban;
    }

    /**
     * Set town
     *
     * @param iban
     */
    public void setIBAN(String iban) {
        this.iban = iban;
    }

    /**
     * Get bank
     *
     * @return
     */
    public String getBank() {
        return bank;
    }

    /**
     * Set address
     *
     * @param bank
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /* End of Setters-Getters */
    /**
     * Returns a string representation of this object
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(fullname).append("\n")
                .append("Status: ").append(status).append("\n")
                .append("Children: ").append(children).append("\n")
                .append("Ages of Children: ").append(year_children).append("\n")
                .append("Category: ").append(category).append("\n")
                .append("Department ").append(department).append("\n")
                .append("Start Date: ").append(startDate).append("\n")
                .append("End Date: ").append(endDate).append("\n")
                .append("Address: ").append(address).append("\n")
                .append("Phone: ").append(telephone).append("\n")
                .append("IBAN: ").append(iban).append("\n")
                .append("Bank: ").append(bank).append("\n")
                .append("User ID: ").append(userID).append("\n");

        return sb.toString();

    }

}

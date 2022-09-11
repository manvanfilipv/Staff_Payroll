/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mycompany.User.User;
import com.mycompany.User.UserDB;
import com.mycompany.Wage.WageDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author manva
 */
@WebServlet(name = "UpdateYears", urlPatterns = {"/UpdateYears"})
public class UpdateYears extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.text.ParseException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            List<User> Users = UserDB.getUsers();
            StringBuilder result = new StringBuilder();
            result.append("<b>Updated Years of Users with IDs </b><br>");
            for (int i = 0; i < WageDB.getWages().size(); i++) {

                // Get Today
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String today = simpleDateFormat.format(date);
                //today = "01-02-2024";
                String [] to = today.split("-",3);
                String today_day = to[0];
                String today_month = to[1];
                String today_year = to[2];
                
                if (Users.get(i).getCategory().equals("Contract Teacher(Promoted)") 
                || Users.get(i).getCategory().equals("Contract Admin(Promoted)") 
                || Users.get(i).getCategory().equals("Retired/Fired")) continue;
                   
                
                String [] st = Users.get(i).getStartDate().split("-", 3);
                String st_day = st[0];
                String st_month = st[1];
                String st_year = st[2];
                
                if (today_day.equals(st_day) && today_month.equals(st_month)){
                    int diff_years = Integer.parseInt(today_year) - Integer.parseInt(st_year);
                    if ( diff_years > 0){
                        if (diff_years > WageDB.getWages().get(i).getYears()){
                            WageDB.updateYears(Users.get(i).getUserID(),Integer.toString(diff_years));
                            result.append(Users.get(i).getUserID()).append("<br>");
                        }
                    }
                }
                    
                /*
                Date d1 = simpleDateFormat.parse(Users.get(i).getStartDate());
                Date d2 = simpleDateFormat.parse(today);
                long diff = d2.getTime() - d1.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                
                //System.out.println(diffDays);
                if (diffDays>0){
                    //System.out.println(diffDays % 365 == 0);
                    if (diffDays % 365 == 0){
                        long years = diffDays / 365;
                        //System.out.println(years);
                        if (years != WageDB.getWages().get(i).getYears()){
                            WageDB.updateYears(Users.get(i).getUserID(),Long.toString(years));
                            result.append(Users.get(i).getUserID()).append("<br>");
                        }
                    }
                } else {
                    
                }
                */
            }

            out.print(result);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UpdateYears.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(UpdateYears.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

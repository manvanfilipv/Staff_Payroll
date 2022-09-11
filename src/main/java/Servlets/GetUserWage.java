/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mycompany.User.User;
import com.mycompany.User.UserDB;
import com.mycompany.Wage.Wage;
import com.mycompany.Wage.WageDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Manos
 */
@WebServlet(name = "GetUserWage", urlPatterns = {"/GetUserWage"})
public class GetUserWage extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            User user = UserDB.getUser(Integer.parseInt(request.getParameter("userid")));
            Wage wage = WageDB.getWage(Integer.parseInt(request.getParameter("userid")));
            StringBuilder result = new StringBuilder();
            if (user == null) {
                out.print("User with specific ID does not exist.");
            } else {
                result.append("<b>User</b> ").append("<br>ID -> ")
                        .append(user.getUserID()).append("<br>Fullname -> ")
                        .append(user.getfullName()).append("<br>Status -> ")
                        .append(user.getStatus()).append("<br>Address -> ")
                        .append(user.getAddress()).append("<br>Bank -> ")
                        .append(user.getBank()).append("<br>Category -> ")
                        .append(user.getCategory()).append("<br>Department -> ")
                        .append(user.getDepartment()).append("<br>Phone -> ")
                        .append(user.getTelephone()).append("<br>IBAN -> ")
                        .append(user.getIBAN()).append("<br>Start Date -> ")
                        .append(user.getStartDate()).append("<br>End Date -> ")
                        .append(user.getEndDate()).append("<br>Children -> ")
                        .append(user.getChildren()).append("<br>Age of Children -> ")
                        .append(user.getYearChildren()).append("<br>")
                        .append("<b>Wage</b> ").append("<br>ID -> ")
                        .append(wage.getID()).append("<br>Category -> ")
                        .append(wage.getCategory()).append("<br> Status -> ")
                        .append(wage.getStatus()).append("<br> Years -> ")
                        .append(wage.getYears()).append("<br><br> Basic Wage -> ")
                        .append(user.getBasic()).append("<br> Research Bonus -> ")
                        .append(WageDB.getResearchBonus()).append("<br> Library Bonus -> ")
                        .append(WageDB.getLibraryBonus());
                out.print(result);
            }
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetUserWage.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetUserWage.class.getName()).log(Level.SEVERE, null, ex);
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

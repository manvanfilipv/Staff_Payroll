/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mycompany.User.User;
import com.mycompany.User.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "GetEmployees", urlPatterns = {"/GetEmployees"})
public class GetEmployees extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            StringBuilder result = new StringBuilder();
            result.append("<b>Users</b><br>");
            List <User> users = UserDB.getUsers();
            for (int i=0;i<users.size();i++){
                String rep = users.get(i).toString();
                rep = rep.replace("Name:", "<b>Name:</b>");
                rep = rep.replace("Status:", "<b>Status:</b>");
                rep = rep.replace("Children:", "<b>Children:</b>");
                rep = rep.replace("Ages of ", "<b>Ages of </b>");
                rep = rep.replace("Category:", "<b>Category:</b>");
                rep = rep.replace("Department ", "<b>Department:</b>");
                rep = rep.replace("Start Date:", "<b>Start Date:</b>");
                rep = rep.replace("End Date:", "<b>End Date:</b>");
                rep = rep.replace("Address:", "<b>Address:</b>");
                rep = rep.replace("Phone:", "<b>Phone:</b>");
                rep = rep.replace("IBAN:", "<b>IBAN:</b>");
                rep = rep.replace("Bank:", "<b>Bank:</b>");
                rep = rep.replace("User ID:", "<b>User ID:</b>");
                result.append(rep).append("<br>");
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetEmployees.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GetEmployees.class.getName()).log(Level.SEVERE, null, ex);
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

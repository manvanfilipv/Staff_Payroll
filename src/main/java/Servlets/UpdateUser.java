/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mycompany.Pay.Pay;
import com.mycompany.Pay.PayDB;
import com.mycompany.User.User;
import com.mycompany.User.UserDB;
import com.mycompany.Wage.Wage;
import com.mycompany.Wage.WageDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
@WebServlet(name = "UpdateUser", urlPatterns = {"/UpdateUser"})
public class UpdateUser extends HttpServlet {

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
            
            User user = new User();
            User old_user = UserDB.getUser(Integer.parseInt(request.getParameter("userid")));
            Wage old_wage = WageDB.getWage(Integer.parseInt(request.getParameter("userid")));
            System.out.println("ID-> "+old_user);
            user.setAddress(request.getParameter("address"));
            user.setBank(request.getParameter("bank"));
            user.setCategory(old_user.getCategory());
            user.setChildren(Integer.parseInt(request.getParameter("children")));
            user.setYearChildren(request.getParameter("year_children"));
            user.setDepartment(request.getParameter("department"));
            user.setIBAN(request.getParameter("IBAN"));
            user.setStatus(request.getParameter("status"));
            user.setTelephone(request.getParameter("phone"));
            user.setUserID(Integer.parseInt(request.getParameter("userid")));
            UserDB.updateUser(user);
            
            Wage wage = new Wage();
            wage.setCategory(old_user.getCategory());
            wage.setStatus(request.getParameter("status"));
            wage.setYears(old_wage.getYears());
            wage.setID(Integer.parseInt(request.getParameter("userid")));
            WageDB.updateWage(wage);
            
            StringBuilder result = new StringBuilder();
            result.append("#DB: Employee + Payroll" );
            result.append(WageDB.CalculateWage(Integer.parseInt(request.getParameter("userid"))));
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
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
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
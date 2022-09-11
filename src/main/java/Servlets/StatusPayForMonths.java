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
 * @author Manos
 */
@WebServlet(name = "StatusPayForMonths", urlPatterns = {"/StatusPayForMonths"})
public class StatusPayForMonths extends HttpServlet {

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

            StringBuilder result = new StringBuilder();
            
            List<User> users = UserDB.getUsers();
            List<Pay> pays = PayDB.getPays();
            
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String start_user;
            String end_user;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date d_start = simpleDateFormat.parse(start);
            Date d_end = simpleDateFormat.parse(end);
            Date d_start_user;
            Date d_end_user;

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getCategory().equals("Contract Teacher")
                        || users.get(i).getCategory().equals("Contract Admin")
                        || users.get(i).getCategory().equals("Contract Teacher(Promoted)")
                        || users.get(i).getCategory().equals("Contract Admin(Promoted)")) {
                    start_user = users.get(i).getStartDate();
                    end_user = users.get(i).getEndDate();
                    d_start_user = simpleDateFormat.parse(start_user);
                    d_end_user = simpleDateFormat.parse(end_user);

                    if ((d_start_user.equals(d_start) || d_start_user.after(d_start)) && (d_end_user.equals(d_end) || d_end_user.before(d_end))) {
                        result.append("<b>ID</b> -> ").append(users.get(i).getUserID())
                        .append("<br>Fullname -> ").append(users.get(i).getfullName())
                        .append("<br>Pay -> ").append(pays.get(i).getPay())
                        .append("<br>Category -> ").append(users.get(i).getCategory())
                        .append("<br>Start Date -> ").append(users.get(i).getStartDate())
                        .append("<br>End Date -> ").append(users.get(i).getEndDate())
                        .append("<br><br>");
                    }
                } else if (users.get(i).getCategory().equals("Permanent Admin")
                        || users.get(i).getCategory().equals("Permanent Teacher")
                        || users.get(i).getCategory().equals("Permanent Admin(Retired/Fired)")
                        || users.get(i).getCategory().equals("Permanent Teacher(Retired/Fired)")) {
                    start_user = users.get(i).getStartDate();
                    d_start_user = simpleDateFormat.parse(start_user);
                    if ((d_start_user.equals(d_start) || d_start_user.after(d_start)) && (d_start_user.before(d_end) || d_start_user.equals(d_end))) {
                        result.append("<b>ID</b> -> ").append(users.get(i).getUserID())
                        .append("<br>Fullname -> ").append(users.get(i).getfullName())
                        .append("<br>Pay -> ").append(pays.get(i).getPay())
                        .append("<br>Category -> ").append(users.get(i).getCategory())
                        .append("<br>Start Date -> ").append(users.get(i).getStartDate())
                        .append("<br><br>");
                    }
                }
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
            Logger.getLogger(StatusPayForMonths.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StatusPayForMonths.class.getName()).log(Level.SEVERE, null, ex);
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

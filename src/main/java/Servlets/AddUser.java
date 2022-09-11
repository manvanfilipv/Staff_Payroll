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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
@WebServlet(name = "AddUser", urlPatterns = {"/AddUser"})
public class AddUser extends HttpServlet {

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
            User user = new User();
            user.setfullName(request.getParameter("fullname"));
            user.setAddress(request.getParameter("address"));
            user.setBank(request.getParameter("bank"));
            user.setCategory(request.getParameter("category"));

            user.setChildren(Integer.parseInt(request.getParameter("children")));
            user.setYearChildren(request.getParameter("year_children"));
            user.setDepartment(request.getParameter("department"));
            user.setIBAN(request.getParameter("IBAN"));
            user.setStartDate(request.getParameter("start_date"));
            String start_date = request.getParameter("start_date");
            String end_date = request.getParameter("end_date");

            if (request.getParameter("category").equals("Permanent Teacher") || request.getParameter("category").equals("Permanent Admin")) {
                user.setEndDate("-");
            } else {
                String[] st = start_date.split("-", 3);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = new GregorianCalendar(Integer.parseInt(st[2]), Integer.parseInt(st[1]), Integer.parseInt(st[0]));
                calendar.add(Calendar.MONTH, -1);
                //System.out.println("Date : " + sdf.format(calendar.getTime()));

                //add one month
                calendar.add(Calendar.MONTH,Integer.parseInt(end_date));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                //System.out.println("Date : " + sdf.format(calendar.getTime()));
                user.setEndDate(sdf.format(calendar.getTime()));
            }
            user.setStatus(request.getParameter("status"));
            user.setTelephone(request.getParameter("phone"));
            if (request.getParameter("basic").equals("")) {
                user.setBasic(WageDB.getBasic());
            } else {
                user.setBasic(Double.parseDouble(request.getParameter("basic")));
            }

            if (UserDB.addUser(user)) {
                out.print(user.getUserID());
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
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
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

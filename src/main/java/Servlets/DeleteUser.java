/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mycompany.User.UserDB;
import com.mycompany.Wage.WageDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
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
@WebServlet(name = "DeleteUser", urlPatterns = {"/DeleteUser"})
public class DeleteUser extends HttpServlet {

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
            Integer result = WageDB.canDelete(Integer.parseInt(request.getParameter("userid")));
            
            switch (result){
                case -3:
                    out.print("#DB: Not the last day of the month.");
                    break;
                case -2:
                    out.print("#DB: Employee not Permanent.");
                    break;
                case -1:
                    out.print("#DB: Employee not found.");
                    break;
                case 1:
                    String end;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1 = new Date();
                    String today = formatter.format(date1);
                    String [] to = today.split("-",3);
                    YearMonth yearMonth = YearMonth.of(Integer.parseInt(to[2]), Integer.parseInt(to[1]));
                    if (to[0].equals(yearMonth.lengthOfMonth())){
                        end = today;
                    } else {
                        end = yearMonth.lengthOfMonth()+"-"+to[1]+"-"+to[2];
                    }
                    UserDB.deleteUser(Integer.parseInt(request.getParameter("userid")),end);
                    out.print("#DB: Employee Successfully deleted!");
                    break;
                default:
                    out.print("#DB: Unknown error occured.");
                    break;
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
            Logger.getLogger(DeleteUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DeleteUser.class.getName()).log(Level.SEVERE, null, ex);
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

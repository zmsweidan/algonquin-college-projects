/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import builder.EmployeeBuilder;
import business.EmployeeLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import transferobjects.Employee;

/**
 * This class represents the HttpServlet used in the retrieval of the employee table
 * @author Zaid Sweidan
 */
public class EmployeeAddView extends HttpServlet {
    
    private String errorMessage;
    private String emp_no, first_name, last_name, gender, birth_date, hire_date;
    
    @Override
    public void init(){
        errorMessage = null;
        emp_no=""; first_name=""; last_name=""; gender=""; birth_date=""; hire_date="";
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        errorMessage = null;
        
        //add results
        if (request.getMethod().equalsIgnoreCase("POST")) {
            Map<String, String[]> data = request.getParameterMap();
            EmployeeBuilder builder = new EmployeeBuilder();
            try {
                builder.build(data);
                Employee employee = builder.get();
                new EmployeeLogic().addEmployee(employee);
                response.sendRedirect("EmployeeTable");
            }
            catch(Exception e) {
                if ( e.getMessage() == null)
                    errorMessage = "Please enter ALL fields";
                else
                    errorMessage = e.getMessage();
            }
        }
        
        //generate form
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            
            out.println("<head>");
                out.println("<title>Servlet EmployeeAddView</title>");            
            out.println("</head>");
            
            out.println("<body>");
                out.println("<h1>Servlet EmployeeAddView at " + request.getContextPath() + "</h1>");
                out.println("<form action=\"EmployeeAdd\" method=\"post\">");

                    out.println("Employee Number:<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.EMP_NO + "\" value=\"" + "" + "\"><br>");

                    out.println("Birth Date(YYYY-MM-DD):<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.BIRTH_DATE + "\" value=\"" + "" + "\"><br>");

                    out.println("First Name:<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.FIRST_NAME + "\" value=\"" + "" + "\"><br>");

                    out.println("Last Name:<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.LAST_NAME + "\" value=\"" + "" + "\"><br>");

                    out.println("Gender(M/F):<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.GENDER + "\" value=\"" + "" + "\"><br>");

                    out.println("Hire Date(YYYY-MM-DD):<br>");
                    out.println("<input type=\"text\" name=\"" + Employee.HIRE_DATE + "\" value=\"" + "" + "\"><br>");

                    out.println("<input style=\"margin-top:20px;\" type=\"submit\" value=\"Submit\">");

                out.println("</form>");
                
            if (errorMessage != null) {
                out.println("<h3 style=\"color:red;\">" + errorMessage + "</h1>");
            }
            
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        processRequest(request, response);
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

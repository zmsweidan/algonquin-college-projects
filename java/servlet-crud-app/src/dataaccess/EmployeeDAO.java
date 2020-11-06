/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.util.List;
import transferobjects.Employee;

/**
 * This interface provides the methods needed for an implementation of a DAO
 * class for the employees table in the database
 * 
 * @author Zaid Sweidan
 */
public interface EmployeeDAO {
    
    /**
     * Produces a list of all the employees
     * @return List of Employees
     */
    List<Employee> getAllEmployees();
    
    /**
     * Retrieves a specific employee based on their id
     * @param id employee id
     * @return Employee instance
     */
    Employee getEmployeeByID(int id);
    
    /**
     * adds an Employee to the database
     * @param employee Employee object
     */
    void addEmployee(Employee employee);
    
    /**
     * updates an Employee from the database
     * @param employee Employee object
     */
    void updateEmployee(Employee employee);
   
    /**
     * deletes an Employee from the database
     * @param employee Employee object
     */
    void deleteEmployee(Employee employee);
}

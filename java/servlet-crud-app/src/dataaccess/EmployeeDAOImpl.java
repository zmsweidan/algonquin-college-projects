package dataaccess;

import factory.DTOFactoryCreator;
import factory.EmployeeFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import transferobjects.Employee;

/**
 * This class implements EmployeeDAO for direct usage in data access operations
 * 
 * @author Zaid Sweidan
 */
public class EmployeeDAOImpl implements EmployeeDAO{


    //SQL statements
    /**
     * SQL statement for return all employees (limited to 100)
     */
    private static final String GET_ALL_EMPLOYEES = "SELECT emp_no, birth_date, first_name, last_name, gender, hire_date FROM employees ORDER BY emp_no LIMIT 100";
    /**
     * SQL statement for returning an employee by their code
     */
    private static final String GET_BY_CODE_EMPLOYEE = "SELECT emp_no, birth_date, first_name, last_name, gender, hire_date FROM employees WHERE emp_no = ?";
    /**
     * SQL statement for inserting an employee
     */
    private static final String INSERT_EMPLOYEE = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES(?, ?, ?, ?, ?, ?)";
    /**
     * SQL statement for updating information about an employee
     */
    private static final String UPDATE_EMPLOYEE = "UPDATE employees SET birth_date = ?, first_name = ?, last_name = ?, gender = ?, hire_date = ? WHERE emp_no = ?";
    /**
     * SQL statement for deleting an employee based on their employee code
     */
    private static final String DELETE_EMPLOYEE = "DELETE FROM employees WHERE emp_no = ?";
    
    
    //Overriden methods
    /**
     * Produces a list of all employees in the database
     * @return List of Employees
     */
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = Collections.EMPTY_LIST;
        try ( Connection connection = DataSource.getConnection();
              PreparedStatement pstmt = connection.prepareStatement(GET_ALL_EMPLOYEES);) 
        {
            ResultSet rs = pstmt.executeQuery();
            employees = DTOFactoryCreator.createBuilder(Employee.class).createListResultSet(rs);
        }
        catch(SQLException ex) {
            Logger.getLogger(EmployeeFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employees;
    }


     /**
     * Retrieves a specific employee based on their id
     * @param id employee id
     * @return Employee object
     */
    @Override
    public Employee getEmployeeByID(int id) {
        ResultSet rs = null;
        Employee employee = null;
        try( Connection connection = DataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement( GET_BY_CODE_EMPLOYEE); )
        {
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                employee = new Employee();
                employee.setEmpNo(rs.getInt("emp_no"));
                employee.setBirthDate(rs.getDate("birth_date"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setGender(rs.getString("gender"));
                employee.setHireDate(rs.getDate("hire_date"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return employee;
    }
    
    /**
     * Adds an Employee to the database
     * @param employee Employee object
     */
    @Override
    public void addEmployee(Employee employee) {
        try( Connection connection = DataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT_EMPLOYEE); )
        {
            pstmt.setInt(1, employee.getEmpNo());
            pstmt.setDate(2, employee.getBirthDate());
            pstmt.setString(3, employee.getFirstName());
            pstmt.setString(4, employee.getLastName());
            pstmt.setString(5, employee.getGender());
            pstmt.setDate(6, employee.getHireDate());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     * Updates an Employee from the database
     * @param employee Employee object
     */
    @Override
    public void updateEmployee(Employee employee) {
        try( Connection connection = DataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE_EMPLOYEE); )
        {
            pstmt.setDate(1, employee.getBirthDate());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setString(4, employee.getGender());
            pstmt.setDate(5, employee.getHireDate());
            pstmt.setInt(6, employee.getEmpNo());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     * Deletes an Employee from the database
     * @param employee Employee object
     */
    @Override
    public void deleteEmployee(Employee employee) {
        try( Connection connection = DataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE_EMPLOYEE); )
        {
            pstmt.setInt(1, employee.getEmpNo());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
}

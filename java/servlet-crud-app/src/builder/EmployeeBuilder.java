package builder;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import transferobjects.Employee;

/**
 * This class builds an Employee object from either a map or result set
 * @author Zaid Sweidan
 */
public class EmployeeBuilder {
    
    /**
     * singleton employee instance
     */
    private final Employee employee;
    
    /**
     * Default constructor that initiates the Employee singleton object
     */
    public EmployeeBuilder() {
        employee = new Employee();
    }
    
    /**
     * Builds an employee from a result set
     * @param rs ResultSet object
     * @throws SQLException 
     */
    public void build(ResultSet rs) throws SQLException{ 
        employee.setEmpNo(rs.getInt(Employee.EMP_NO));
        employee.setFirstName(rs.getString(Employee.FIRST_NAME));
        employee.setLastName(rs.getString(Employee.LAST_NAME));
        employee.setGender(rs.getString(Employee.GENDER));
        employee.setBirthDate(rs.getDate(Employee.BIRTH_DATE));
        employee.setHireDate(rs.getDate(Employee.HIRE_DATE));
    }
    
    /**
     * Builds a employee from a map object
     * @param map Map of parameters stored as string objects
     */
    public void build(Map<String, String[]> map){ 
        employee.setEmpNo(Integer.parseInt( map.get(Employee.EMP_NO)[0]));
        employee.setFirstName(map.get(Employee.FIRST_NAME)[0]);
        employee.setLastName(map.get(Employee.LAST_NAME)[0]);
        employee.setGender(map.get(Employee.GENDER)[0]);
        employee.setBirthDate(Date.valueOf(map.get(Employee.BIRTH_DATE)[0]));
        employee.setHireDate(Date.valueOf(map.get(Employee.HIRE_DATE)[0]));
    }
    
    /**
     * Retrieves the employee singleton object
     * @return Employee object
     */
    public Employee get() {
        return employee;
    }
}

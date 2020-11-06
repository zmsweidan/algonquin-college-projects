package factory;

import builder.EmployeeBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import transferobjects.Employee;

/**
 * This class extends from an AbstractFactory for the Employee class and performs
 * operations that retrieve data from the employee table
 * 
 * @author Zaid Sweidan
 */
public class EmployeeFactory extends AbstractFactory<Employee>{
    
    /**
     * Creates an employee object from an executed SQL query
     * @param rs ResultSet object from executed query
     * @return Employee object
     */
    @Override
    public Employee createFromResultSet(ResultSet rs) {
        EmployeeBuilder builder = new EmployeeBuilder();
        try {
            builder.build(rs);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return builder.get();
    }

    /**
     * Creates an employee object from parameter map
     * @param map map of parameters stored as string objects
     * @return Employee object
     */
    @Override
    public Employee createFromMap(Map<String, String[]> map) {
        EmployeeBuilder builder = new EmployeeBuilder();
        builder.build(map);
        return builder.get();
    }
}

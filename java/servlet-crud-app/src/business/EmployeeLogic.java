package business;

import dataaccess.EmployeeDAO;
import dataaccess.EmployeeDAOImpl;
import java.sql.Date;
import java.util.List;
import transferobjects.Employee;

/**
 * This class contains the business logic used in the Employee DAO arrangement
 * @author Zaid Sweidan
 */
public class EmployeeLogic {

    /**
     * maximum employee number size
     */
    private static final int EMPLOYEE_CODE_MAX_SIZE = Integer.MAX_VALUE;
    /**
     * maximum employee first name length
     */
    private static final int EMPLOYEE_FIRSTNAME_MAX_LENGTH = 14;
    /**
     * maximum employee last name length
     */
    private static final int EMPLOYEE_LASTNAME_MAX_LENGTH = 16;
    /**
     * maximum gender descriptor length
     */
    private static final int EMPLOYEE_GENDER_MAX_LENGTH = 1;

    /**
     * EmployeeDao singleton object
     */
    private EmployeeDAO employeeDAO = null;

    /**
     * Default constructor that creates a singleton employeeDAO object
     */
    public EmployeeLogic() {
        employeeDAO = new EmployeeDAOImpl();
    }

    /**
     * Retrieves the list of all employees in the database
     * @return Employee List
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    /**
     * Retrieves an employee based on their employee number
     * @param ID employee number
     * @return Employee object
     */
    public Employee getEmployeeByID(int ID){
       return employeeDAO.getEmployeeByID(ID);
    }

    /**
     * Adds an employee to the database and validates all input data
     * @param employee employee object to be added
     */
    public void addEmployee(Employee employee) {
        cleanEmployee(employee);
        validateEmployee(employee);
        employeeDAO.addEmployee(employee);
    }
    
    /**
     * Deletes an employee from the database if the input code is valid
     * @param employee employee object to be added
     */
    public void deleteEmployee(Employee employee){
        employeeDAO.deleteEmployee(employee);
    }

    /**
     * Cleans up the names of the employee strings
     * @param employee object to clean
     */
    private void cleanEmployee(Employee employee) {
        if (employee.getFirstName() != null) {
            employee.setFirstName(employee.getFirstName().trim());
        }
        if (employee.getLastName() != null) {
            employee.setLastName(employee.getFirstName().trim());
        }
        if (employee.getGender() != null) {
            employee.setGender(employee.getGender().trim());
        }
    }

    /**
     * Performs all validation operations on an employee
     * @param employee object to validate
     */
    private void validateEmployee(Employee employee) {
        validateId(employee.getEmpNo(),"Employee Code", 1, EMPLOYEE_CODE_MAX_SIZE, false);
        validateString(employee.getFirstName(), "First Name", EMPLOYEE_FIRSTNAME_MAX_LENGTH, false);
        validateString(employee.getLastName(), "Last Name", EMPLOYEE_LASTNAME_MAX_LENGTH, false);
        validateGender(employee.getGender(), "Gender", EMPLOYEE_GENDER_MAX_LENGTH, false);
        validateDate(employee.getBirthDate(), "Birth Date", false);
        validateDate(employee.getBirthDate(), "Hire Date", false);
    }
    
    /**
     * Validates the employee number
     * @param value string value of the data being checked
     * @param fieldName string value of the field name
     * @param minSize minimum size of the employee number
     * @param maxSize maximum size of the employee number
     * @param isNullAllowed boolean value of null condition
     */
    private void validateId(int value, String fieldName, int minSize, int maxSize, boolean isNullAllowed ){;
        if (value < minSize) {
            throw new IllegalArgumentException(String.format("%s cannot be smaller %d characters", fieldName, minSize));
        } else if (value > maxSize) {
            throw new IllegalArgumentException(String.format("%s cannot exceed %d characters", fieldName, maxSize));
        } else if (employeeDAO.getEmployeeByID(value) != null){
            throw new IllegalArgumentException(String.format("%s %d already exists in the database!", fieldName, value));
        }
    }
    
    /**
     * Validates the gender of the employee
     * @param value string value of the data being checked
     * @param fieldName string value of the field name
     * @param maxLength integer value of the max character length of the data
     * @param isNullAllowed boolean value of null condition
     */
    private void validateGender(String value, String fieldName, int maxLength, boolean isNullAllowed){
        validateString(value, fieldName, maxLength, isNullAllowed);
        char gender = Character.toUpperCase(value.charAt(0));
        if (gender != 'M' && gender != 'F')
            throw new IllegalArgumentException(String.format("%s please either enter 'M' or 'F'", fieldName));
    }
    
    /**
     * Validates date parameters entered into the database
     * @param date date entered
     * @param fieldName string value of the field name
     * @param isNullAllowed boolean value of null condition
     */
    private void validateDate(Date date, String fieldName, boolean isNullAllowed) {
        if (date == null && isNullAllowed) {
            // null permitted, nothing to validate
        } else if (date == null) {
            if( !isNullAllowed)
                throw new IllegalArgumentException(String.format("%s cannot be null", fieldName));
        } else if ( date.after(new Date(System.currentTimeMillis())) )
            throw new IllegalArgumentException(String.format("%s cannot be in the future!", fieldName));;
    }

    /**
     * Validates string input data
     * @param value string value of the data being checked
     * @param fieldName string value of the field name
     * @param maxLength integer value of the max character length of the data
     * @param isNullAllowed boolean value of null condition
     */
    private void validateString(String value, String fieldName, int maxLength, boolean isNullAllowed) {
        if (value == null && isNullAllowed) {
            // null permitted, nothing to validate
        } else if (value == null) {
            if( !isNullAllowed)
                throw new IllegalArgumentException(String.format("%s cannot be null", fieldName));
        } else if (value.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be empty or only whitespace", fieldName));
        } else if (value.length() > maxLength) {
            throw new IllegalArgumentException(String.format("%s cannot exceed %d characters", fieldName, maxLength));
        }
    }
}

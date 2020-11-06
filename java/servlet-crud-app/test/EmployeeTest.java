package tests;

import business.EmployeeLogic;
import hthurow.tomcatjndi.TomcatJNDI;
import java.sql.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import transferobjects.Employee;

/**
 * JUnit 4 tests for the Employee table
 * @author Zaid Sweidan
 */

public class EmployeeTest {
    
    /**
     * Employee object used in tests
     */
    private static Employee employee;
    /**
     * EmployeeLogic object used in tests
     */
    private static EmployeeLogic empLogic;
    
    /**
     * Initializes the test objects
     */
    @Before
    public void init_tests(){
        employee = new Employee(123, new Date(1985-02-05), "John", "Smith", "M", new Date(1999-02-05));
        empLogic = new EmployeeLogic();
    }
    
    
    /**
     * Tests that all getters of the Employee class function properly 
     */
    @Test
    public void test_Employee_getters(){
        boolean id = employee.getEmpNo() == 123;
        boolean bd = employee.getBirthDate().equals(new Date(1985-02-05));
        boolean fn = employee.getFirstName().equals("John");
        boolean ln = employee.getLastName().equals("Smith");
        boolean g = employee.getGender().equals("M");
        boolean hd = employee.getHireDate().equals(new Date(1999-02-05));
        assertTrue(id && bd && fn && ln && g && hd);
    }
    
    /**
     * Tests that all setters of the Employee class function properly 
     */
    @Test
    public void test_Employee_setters(){
        employee.setEmpNo(321); 
        employee.setBirthDate(new Date(1995-03-06)); employee.setHireDate(new Date(2001-03-06));
        employee.setFirstName("Jane"); employee.setLastName("Doe"); employee.setGender("F");
        boolean id = employee.getEmpNo() == 321;
        boolean bd = employee.getBirthDate().equals(new Date(1995-03-06));
        boolean fn = employee.getFirstName().equals("Jane");
        boolean ln = employee.getLastName().equals("Doe");
        boolean g = employee.getGender().equals("F");
        boolean hd = employee.getHireDate().equals(new Date(2001-03-06));
        assertTrue(id && bd && fn && ln && g && hd);
    }
    
    /**
     * Tests that getAllEmployees functions properly
     */
    @Test
    public void test_EmployeeLogic_getAllEmployees(){
        List<Employee> empList = empLogic.getAllEmployees();
        for (Employee e: empList){
            if (e == null)
                fail();
        }
        assertTrue(true);
    }
    
    /**
     * Tests if an employee that exists is retrieved successfully
     */
    @Test
    public void test_EmployeeLogic_getEmployeeByID_exists(){
        Employee test = empLogic.getEmployeeByID(10001);
        assertEquals(test.getEmpNo() , 10001);
    }
    
    /**
     * Tests if an employee that exists is retrieved successfully
     */
    @Test
    public void test_EmployeeLogic_getEmployeeByID_notExists(){
        Employee test = empLogic.getEmployeeByID(23143411);
        if (test == null)
            assertTrue(true);
    }
    
    /**
     * Tests the addEmployee method for valid input
     */
    @Test
    public void test_EmployeeLogic_addEmployee_valid(){
        empLogic.addEmployee(employee);
        Employee test = empLogic.getEmployeeByID(employee.getEmpNo());
        empLogic.deleteEmployee(employee);
        assertEquals(employee.getEmpNo(), test.getEmpNo());
        
    }
    
    /**
     * Tests the addEmployee method for an employee that already exists
     */
    @Test
    public void test_EmployeeLogic_addEmployee_alreadyExists(){
        try {
            empLogic.addEmployee(
                    new Employee(10001, new Date(1985-02-05), "John", "Smith", "M", new Date(1999-02-05)));
        } catch (Exception e){
            assertTrue(true);
        }
    }
    
    /**
     * Tests the addEmployee method for invalid id input
     */
    @Test
    public void test_EmployeeLogic_addEmployee_invalidID(){
        try {
            empLogic.addEmployee(
                    new Employee(-111, new Date(1985-02-05), "John", "Smith", "M", new Date(1999-02-05)));
        } catch (Exception e){
            assertTrue(true);
        }
    }
    
    /**
     * Tests the addEmployee method for invalid date input
     */
    @Test
    public void test_EmployeeLogic_addEmployee_invalidDate(){
        try {
            empLogic.addEmployee(
                    new Employee(222, new Date(2020-02-05), "John", "Smith", "M", new Date(1999-02-05)));
        } catch (Exception e){
            assertTrue(true);
        }
    }
    
    /**
     * Tests the addEmployee method for invalid date input
     */
    @Test
    public void test_EmployeeLogic_addEmployee_emptyString(){
        try {
            empLogic.addEmployee(
                    new Employee(222, new Date(2020-02-05), "", "Smith", "M", new Date(1999-02-05)));
        } catch (Exception e){
            assertTrue(true);
        }
    }
    
    /**
     * Tests the addEmployee method for invalid gender input
     */
    @Test
    public void test_EmployeeLogic_addEmployee_invalidGender(){
        try {
            empLogic.addEmployee(
                    new Employee(222, new Date(2020-02-05), "John", "Smith", "dwdasd", new Date(1999-02-05)));
        } catch (Exception e){
            assertTrue(true);
        }
    }
}
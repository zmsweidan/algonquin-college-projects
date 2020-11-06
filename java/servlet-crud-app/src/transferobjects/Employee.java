package transferobjects;

import java.sql.Date;

/**
 * This class models the information of an employee in the employee table
 * 
 * @author Zaid Sweidan
 */
public class Employee {
    
    /**
     * string representations of all the columns in the employee table
     */
    public static final String 
            EMP_NO="emp_no", BIRTH_DATE="birth_date", FIRST_NAME="first_name", LAST_NAME="last_name", GENDER="gender", HIRE_DATE="hire_date";
    
    /**
     * employee number
     */
    private int emp_no;
    /**
     * string representation of the first name, last name and gender of the employee
     */
    private String  first_name, last_name, gender;
    /**
     * date representations of the birth date and hire date of the employee
     */
    private Date birth_date, hire_date;
    
    /**
     * Employee default constructor
     */
    public Employee(){}
    
    /**
     * Employee initial value constructor
     * @param emp_no integer value of employee number
     * @param birth_date date value of birth date
     * @param first_name string value of first name
     * @param last_name string value of last name
     * @param gender string value of gender
     * @param hire_date date value of hire date
     */
    public Employee( int emp_no, Date birth_date, String first_name, String last_name, String gender, Date hire_date){
        this.emp_no = emp_no;
        this.birth_date = birth_date;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.hire_date = hire_date;
    }
    
    /**
     * Retrieves the employee number of this employee
     * @return integer value of the employee number
     */
    public int getEmpNo() {
        return emp_no;
    }
    /**
     * Sets the employee number of this employee
     * @param emp_no integer value of the employee number
     */
    public void setEmpNo(int emp_no) {
        this.emp_no = emp_no;
    }
    
    /**
     * Retrieves the first name of this employee
     * @return string value of first name
     */
    public String getFirstName() {
        return first_name;
    }
    /**
     * Sets the first name of this employee
     * @param first_name string value of first name
     */
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Retrieves the last name of this employee
     * @return string value of last name
     */
    public String getLastName() {
        return last_name;
    }
    /**
     * Sets the first name of this employee
     * @param last_name string value of last name
     */
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Retrieves the gender of this employee
     * @return string value of gender
     */
    public String getGender() {
        return gender;
    }
    /**
     * Sets the gender of this employee
     * @param gender string value of gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Retrieves the birth date of this employee
     * @return date value of the birth date
     */
    public Date getBirthDate() {
        return birth_date;
    }
    /**
     * Sets the birth date of this employee
     * @param birth_date date value of the birth date
     */
    public void setBirthDate(Date birth_date) {
        this.birth_date = birth_date;
    }

    /**
     * Retrieves the hire date of this employee
     * @return date value of the hire date
     */
    public Date getHireDate() {
        return hire_date;
    }
    /**
     * Sets the birth date of this employee
     * @param hire_date date value of the hire date
     */
    public void setHireDate(Date hire_date) {
        this.hire_date = hire_date;
    }
}
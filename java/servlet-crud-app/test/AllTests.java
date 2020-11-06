package tests;

import hthurow.tomcatjndi.TomcatJNDI;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Launches all JUnit tests for every table in the database
 * @author Zaid Sweidan
 */
@RunWith(Suite.class)
@SuiteClasses({ EmployeeTest.class })
public class AllTests {

    /**
     * TomcatJNDI start object
     */
    private static TomcatJNDI tomcatJNDI;

    /**
     * Initializes TomcatJNDI for testing
     */
    @BeforeClass
    public static void setUpClass() {
        tomcatJNDI = new TomcatJNDI();
        tomcatJNDI.processContextXml(new File("web\\META-INF\\context.xml"));
        tomcatJNDI.processWebXml(new File("web\\WEB-INF\\web.xml"));
        tomcatJNDI.start();
    }

    /**
     * Closes TomcatJNDI after testing completes
     */
    @AfterClass
    public static void tearDownClass() {
        tomcatJNDI.tearDown();
    }
}
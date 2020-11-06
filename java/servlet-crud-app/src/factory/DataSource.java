/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author giang
 */
public class DataSource {
    
    private static DataSource singleton;
    private javax.sql.DataSource ds;
    
    private DataSource() { }
    
    private static DataSource getDataSource() {
        if (singleton == null) {
            singleton = new DataSource(); 
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                singleton.ds = (javax.sql.DataSource) envContext.lookup("jdbc/Employees");
            } catch (NamingException ex) {
                Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        return singleton;
    }
    
    private Connection createConnection() {
        try {
            return getDataSource().ds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Connection getConnection() {
        return getDataSource().createConnection();
    }
}

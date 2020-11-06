/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Shariar
 */
public class DataSource {

    private javax.sql.DataSource ds;
    private static DataSource singleton;

    public DataSource() {
    }

    /**
     * Only use one connection for this application, prevent memory leaks.
     *
     * @return
     */
    private void getDataSource() {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (javax.sql.DataSource) envCtx.lookup("jdbc/employees");

        } catch (NamingException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection createConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static Connection getConnection() {
        if (singleton == null) {
            singleton = new DataSource();
            singleton.getDataSource();
        }
        return singleton.createConnection();
    }

}
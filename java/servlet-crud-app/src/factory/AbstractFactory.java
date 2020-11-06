/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giang
 */
public abstract class AbstractFactory<T> implements Factory<T>{

    @Override
    public abstract T createFromResultSet(ResultSet rs);

    @Override
    public List<T> createListResultSet(ResultSet rs) {
        List<T> list = new LinkedList();
        try {
            while(rs.next()) {
                list.add(createFromResultSet(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AbstractFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public abstract T createFromMap(Map<String, String[]> map);
}

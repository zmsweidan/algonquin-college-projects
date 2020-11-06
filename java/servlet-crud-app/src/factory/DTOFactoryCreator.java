/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shariar
 */
public final class DTOFactoryCreator {

    private static final String PACKAGE = "factory.";
    private static final String FACTORY = "Factory";

    private DTOFactoryCreator() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Factory<T> getFactory(String factoryName) {
        Factory<T> factory = null;
        try {
            factory = (Factory< T>) Class.forName(PACKAGE + factoryName + FACTORY).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(DTOFactoryCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return factory;
    }

    public static <T> Factory<T> createBuilder(Class<T> type) {
        return getFactory(type.getSimpleName());
    }
}

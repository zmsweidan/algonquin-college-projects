package com.algonquincollege.cst8277.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.h2.tools.Server;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.algonquincollege.cst8277.models.*;

/**
 * Performs various unit tests on the Cart table/entity 
 * 
 * Date : April 12, 2019 
 * 
 * @author Tony Geara 
 * @author Zaid Sweidan
 * @author Elias Khoury 
 * @author Jon Brownridge 
 * @author Sharon Odongo
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartTestSuite {


    // test fixture(s)
    public static EntityManagerFactory emf;
    public static Server server;
    
    /**
     * entity list
     */
    private List<Cart> entityList;
    
    /**
     * initial set up
     */
    @BeforeClass
    public static void oneTimeSetUp() {
        try {
            
            // create in-process H2 server so we can 'see' into database
            // use "jdbc:h2:tcp://localhost:9092/mem:assignment3-testing" in Db Perspective
            // (connection in .dbeaver-data-sources.xml so should be immediately useable
            server = Server.createTcpServer().start();
            emf = Persistence.createEntityManagerFactory("assignment4-testing");
                
        }
        catch (Exception e) {
            //logger.error("something went wrong building EntityManagerFactory", e);
        }
    
    }
    
    
    /**
     * test batch creation
     */
    @Test
    public void test01_createBatch() {
        EntityManager em = emf.createEntityManager();
        entityList = new ArrayList<Cart>();
        em.getTransaction().begin();
        try {
            for (int i = 1; i < 11; i++ ) {
                Cart temp = new Cart();
                temp.setId(i);
                entityList.add(temp);
                em.persist(temp);
            }
            em.getTransaction().commit();
            em.close();
        } catch(Exception e) {
            fail();
        }
    }
    
    
    /**
     * test get all products
     */
    @Test
    public void test02_getAll() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select e from Cart e");
        List<Cart> results =  query.getResultList();
        for (Cart result : results) {
            assertEquals(result.getClass(), Cart.class);
        }
        em.close();
    }
    
    /**
     * test get item by id
     */
    @Test
    public void test03_getById() {
        EntityManager em = emf.createEntityManager();
        Cart entityFound = em.find(Cart.class, 1);
        assertEquals(1, entityFound.getId());
        em.close();
    }
    
    /**
     * test creating a cart
     */
    @Test
    public void test04_createCart() {
        EntityManager em = emf.createEntityManager();
        Cart newEntity = new Cart();
        newEntity.setId(11);
        em.getTransaction().begin();
        em.persist(newEntity);
        em.getTransaction().commit();
        Cart persistedEntity = em.find(Cart.class, 11);
        assertEquals(newEntity, persistedEntity);
        em.close();
    }
    
    /**
     * test updating a cart
     */
    @Test
    public void test05_updateCart() {
        EntityManager em = emf.createEntityManager();
        Cart updatedEntity = em.find(Cart.class, 1);
        updatedEntity.setTotalAmount(100.0);
        em.getTransaction().begin();
        em.merge(updatedEntity);
        em.getTransaction().commit();
        Cart persistedEntity = em.find(Cart.class, 1);
        assertEquals(100.0, persistedEntity.getTotalAmount(), 0);
        em.close();
    }
    
    /**
     * test deleting a cart
     */
    @Test
    public void test05_deleteCart() {
        EntityManager em = emf.createEntityManager();
        Cart entityToDelete = em.find(Cart.class, 11);
        em.getTransaction().begin();
        em.remove(entityToDelete);
        em.getTransaction().commit();
        Cart persistedEntity = em.find(Cart.class, 11);
        assertNull(persistedEntity);
        em.close();
    }
    
    /**
     * test adding a cart to a cart-item
     */
    @Test
    public void test07_addCartItemsToCart() {
        EntityManager em = emf.createEntityManager();
        Cart cart = em.find(Cart.class, 1);
        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();
        cartItem1.setId(1);
        cartItem2.setId(2);
        List<CartItem> cartList =  new ArrayList<CartItem>();
        cartList.add(cartItem1);
        cartList.add(cartItem2);
        cart.setItems(cartList);
        em.getTransaction().begin();
        em.persist(cartItem1);
        em.persist(cartItem2);
        em.merge(cart);
        em.getTransaction().commit();
        Cart persistedCart = em.find(Cart.class, 1);
        assertEquals(cartList, persistedCart.getItems());
        em.close();
    }
    
    /**
     * test updating a cart with a cart-item
     */
    @Test
    public void test08_updateCartWithCartItem() {
        EntityManager em = emf.createEntityManager();
        Cart cart = em.find(Cart.class, 1);
        cart.setTotalDiscount(50.0);
        em.getTransaction().begin();
        em.merge(cart);
        em.getTransaction().commit();
        CartItem persistedCartItem = em.find(CartItem.class, 1);
        Cart persistedCart = em.find(Cart.class, 1);
        assertEquals(persistedCart.getTotalDiscount(), persistedCartItem.getCart().getTotalDiscount());
        em.close();
    }
    
    /**
     * test deleting a cart with cart-items
     */
    @Test
    public void test09_deleteCartWithCartItems() {
        EntityManager em = emf.createEntityManager();
        Cart cart = em.find(Cart.class, 1);
        try {
            em.getTransaction().begin();
            em.remove(cart);
            em.getTransaction().commit();
            fail();
        } catch (Exception re) {
           // cart has cart-item child records: referential integrity constraint
        }
        em.close();
    }
    
    
}

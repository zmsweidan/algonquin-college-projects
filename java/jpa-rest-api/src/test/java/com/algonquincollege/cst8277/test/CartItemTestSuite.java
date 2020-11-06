package com.algonquincollege.cst8277.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
 * Performs various unit tests on the CartItem table/entity 
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
public class CartItemTestSuite {


    // test fixture(s)
    public static EntityManagerFactory emf;
    public static Server server;
    
    /**
     * entity list
     */
    private List<CartItem> entityList;

    
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
        entityList = new ArrayList<CartItem>();
        em.getTransaction().begin();
        try {
            for (int i = 1; i < 11; i++ ) {
                CartItem temp = new CartItem();
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
     * test get all cartItems
     */
    @Test
    public void test02_getAll() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("Select e from CartItem e");
        List<CartItem> results =  query.getResultList();
        for (CartItem result : results) {
            assertEquals(result.getClass(), CartItem.class);
        }
        em.close();
    }
    
    /**
     * test get item by id
     */
    @Test
    public void test03_getById() {
        EntityManager em = emf.createEntityManager();
        CartItem entityFound = em.find(CartItem.class, 1);
        assertEquals(1, entityFound.getId());
        em.close();
    }
    
    /**
     * test creating a cartItem
     */
    @Test
    public void test04_createCartItem() {
        EntityManager em = emf.createEntityManager();
        CartItem newEntity = new CartItem();
        newEntity.setId(11);
        em.getTransaction().begin();
        em.persist(newEntity);
        em.getTransaction().commit();
        CartItem persistedEntity = em.find(CartItem.class, 11);
        assertEquals(newEntity, persistedEntity);
        em.close();
    }
    
    /**
     * test updating a cartItem
     */
    @Test
    public void test05_updateCartItem() {
        EntityManager em = emf.createEntityManager();
        CartItem updatedEntity = em.find(CartItem.class, 11);
        updatedEntity.setQuantity(5);
        em.getTransaction().begin();
        em.merge(updatedEntity);
        em.getTransaction().commit();
        CartItem persistedEntity = em.find(CartItem.class, 11);
        assertEquals(5, persistedEntity.getQuantity(), 0);
        em.close();
    }
    
    /**
     * test deleting a cartItem
     */
    @Test
    public void test06_deleteCartItem() {
        EntityManager em = emf.createEntityManager();
        CartItem entityToDelete = em.find(CartItem.class, 11);
        em.getTransaction().begin();
        em.remove(entityToDelete);
        em.getTransaction().commit();
        CartItem persistedEntity = em.find(CartItem.class, 11);
        assertNull(persistedEntity);
        em.close();
    }
    
    /**
     * test adding a cartItem to a Cart
     */
    @Test
    public void test07_addCartItemToCartAndProduct() {
        EntityManager em = emf.createEntityManager();
        Product product = new Product();
        Cart cart = new Cart();
        CartItem cartItem = em.find(CartItem.class, 1);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        em.getTransaction().begin();
        em.persist(product);
        em.persist(cart);
        em.merge(cartItem);
        em.getTransaction().commit();
        cartItem = em.find(CartItem.class, 1);
        assertEquals(product, cartItem.getProduct());
        assertEquals(cart, cartItem.getCart());
        em.close();
    }
    
    /**
     * test updating a cartItem in a cart-item
     */
    @Test
    public void test08_updateCartItemInCart() {
        EntityManager em = emf.createEntityManager();
        CartItem cartItem = em.find(CartItem.class, 1);
        Cart cart = em.find(Cart.class, 1);
        List<CartItem> carts =  new ArrayList<CartItem>();
        carts.add(cartItem);
        cart.setItems(carts);
        em.getTransaction().begin();
        em.merge(cartItem);
        em.merge(cart);
        cart.setTotalAmount(1111.0);
        em.merge(cart);
        cart = em.find(Cart.class, 1);
        carts = cart.getItems();
        System.out.println(carts.size());
        Boolean cartFound = false;
        for (CartItem c : carts) {
            if (c.getTotalAmount() == cartItem.getTotalAmount()) {
                cartFound = true;
                break;
            };
        }
        assertTrue(cartFound);
        em.close();
    }
    
    /**
     * test deleting a cartItem in a cart-item
     */
    @Test
    public void test09_deleteCartItemInCart() {
        EntityManager em = emf.createEntityManager();
        CartItem cartItem = em.find(CartItem.class, 1);
        try {
            em.getTransaction().begin();
            em.detach(cartItem);
            em.getTransaction().commit();
        } catch (Exception e) {
            fail();
         }
        em.close();
    }
    
    
}

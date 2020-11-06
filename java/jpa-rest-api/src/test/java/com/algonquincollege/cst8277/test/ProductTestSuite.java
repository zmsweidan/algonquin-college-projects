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
 * Performs various unit tests on the Product table/entity 
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
public class ProductTestSuite {


    // test fixture(s)
    public static EntityManagerFactory emf;
    public static Server server;
    
    /**
     * entity list
     */
    private List<Product> entityList;

    
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
        entityList = new ArrayList<Product>();
        em.getTransaction().begin();
        try {
            for (int i = 1; i < 11; i++ ) {
                Product temp = new Product();
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
        Query query = em.createQuery("Select e from Product e");
        List<Product> results =  query.getResultList();
        for (Product result : results) {
            assertEquals(result.getClass(), Product.class);
        }
        em.close();
    }
    
    /**
     * test get item by id
     */
    @Test
    public void test03_getById() {
        EntityManager em = emf.createEntityManager();
        Product entityFound = em.find(Product.class, 1);
        assertEquals(1, entityFound.getId());
        em.close();
    }
    
    /**
     * test creating a product
     */
    @Test
    public void test04_createProduct() {
        EntityManager em = emf.createEntityManager();
        Product newEntity = new Product();
        newEntity.setId(11);
        em.getTransaction().begin();
        em.persist(newEntity);
        em.getTransaction().commit();
        Product persistedEntity = em.find(Product.class, 11);
        assertEquals(newEntity, persistedEntity);
        em.close();
    }
    
    /**
     * test updating a product
     */
    @Test
    public void test05_updateProduct() {
        EntityManager em = emf.createEntityManager();
        Product updatedEntity = em.find(Product.class, 11);
        updatedEntity.setProductName("test");
        em.getTransaction().begin();
        em.merge(updatedEntity);
        em.getTransaction().commit();
        Product persistedEntity = em.find(Product.class, 11);
        assertEquals("test", persistedEntity.getProductName());
        em.close();
    }
    
    /**
     * test deleting a product
     */
    @Test
    public void test06_deleteProduct() {
        EntityManager em = emf.createEntityManager();
        Product entityToDelete = em.find(Product.class, 11);
        em.getTransaction().begin();
        em.remove(entityToDelete);
        em.getTransaction().commit();
        Product persistedEntity = em.find(Product.class, 11);
        assertNull(persistedEntity);
        em.close();
    }
    
    /**
     * test adding a product to a cart-item
     */
    @Test
    public void test07_addProductToCartItem() {
        EntityManager em = emf.createEntityManager();
        Product product = em.find(Product.class, 1);
        //Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setProduct(product);
        em.getTransaction().begin();
        em.persist(cartItem);
        em.getTransaction().commit();
        CartItem persistedCartItem = em.find(CartItem.class, 1);
        assertEquals(product, persistedCartItem.getProduct());
        em.close();
    }
    
    /**
     * test updating a product in a cart-item
     */
    @Test
    public void test08_updateProductInCartItem() {
        EntityManager em = emf.createEntityManager();
        CartItem cartItem = em.find(CartItem.class, 1);
        Product product = cartItem.getProduct();
        product.setProductName("test name");
        cartItem.setProduct(product);
        em.getTransaction().begin();
        em.merge(cartItem);
        em.getTransaction().commit();
        CartItem persistedCartItem = em.find(CartItem.class, 1);
        assertEquals(product.getProductName(), persistedCartItem.getProduct().getProductName());
        em.close();
    }
    
    /**
     * test deleting a product in a cart-item
     */
    @Test
    public void test09_deleteProductInCartItem() {
        EntityManager em = emf.createEntityManager();
        Product product = em.find(Product.class, 1);
        try {
            em.getTransaction().begin();
            em.remove(product);
            em.getTransaction().commit();
            fail();
        } catch (Exception re) {
           // cart-item has an existing product: referential integrity constraint
        }
        em.close();
    }
    
    
}

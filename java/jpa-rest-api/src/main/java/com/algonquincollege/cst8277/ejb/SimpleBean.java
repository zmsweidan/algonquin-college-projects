package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.utils.RestDemoConstants.PU_NAME;
import com.algonquincollege.cst8277.models.*;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.algonquincollege.cst8277.utils.RestDemoConstants;


@Stateless
public class SimpleBean {

    @PersistenceContext(unitName = PU_NAME)
    protected EntityManager em;
    

    /********************************************
     * 
     *        Employee Example 
     * 
     * ******************************************/

    public List<Employee> getEmployeeList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        cq.select(cq.from(Employee.class));
        return em.createQuery(cq).getResultList();
    }

    public void updateEmployee(Employee empWithUpdatedFields) {
        em.merge(empWithUpdatedFields);
        
    }

    public Employee getEmployeeById(int id) {
        return em.find(Employee.class, id);
    }


    /********************************************
     * 
     *        Products 
     * 
     * ******************************************/

    public List<Product> getProducts() {
        List<Product> products = em.createQuery("Select p From Product p").getResultList();
        return products;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addProduct(Product prod) {
        if(prod != null)
            em.persist(prod);
        
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean  deleteProduct(Integer id) {
        boolean isDel = false;
        Product p;
        try {
            p  = em.find(Product.class, id);
            em.remove(p);
            isDel = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            p = null;
            isDel = false;
        }finally {
            
        }
        return isDel;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean updateProduct(Product p, Long id) {
        boolean isUpdated = false;
        
        try {
            em.merge(p);
            isUpdated = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            p = null;
            isUpdated = false;
        }finally {
            
        }
        return isUpdated;
    }
    
    public Product getProductById(int id) {
        return em.find(Product.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Product productPurchased(Long productId, Integer count) {
        Product product = null;
        try {
            product = em.find(Product.class, productId);
            product.setInventoryCount(product.getInventoryCount() - count);
            em.persist(product);
        } catch (Exception ex) {

        }finally {
            
        }

        return product;
    }


    /********************************************
     * 
     *        Cart Item 
     * 
     * ******************************************/

    public List<CartItem> listItems(){
        return em.createQuery("select p from CartItem p")
                .getResultList();
    }

    public CartItem getCartItemById(int id) {
        return em.find(CartItem.class, id);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CartItem updateCartItem(CartItem item, Integer itemId) {
        
        if(itemId != null) {
            System.out.println("item id " + itemId);
            em.merge(item);
            int i = em.createQuery("Update CartItem p Set p.quantity = p.quantity, p.totalAmount = p.totalAmount "
                    + " Where p.id = :id").setParameter("id", itemId).executeUpdate();
            System.out.println("res " + i );
            if(i >0) {
                em.merge(temp);
            }
        }
        return item;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public CartItem addItem(Long productId, Long userId) {
        Product p;
        CartItem item = null;
        try {
            p = em.find(Product.class, productId.intValue());
            if (p != null) {
                System.out.println("made it here " + userId.intValue());
                PlatformUser userCart = em.find(PlatformUser.class, userId.intValue());
                item = new CartItem();
                item.setQuantity(1);
                item.setProduct(p);
                item.setCart(userCart.getCart());
                item.calculateTotal();
                userCart.getCart().getItems().add(item);    
                em.merge(userCart);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            item = null;
        }finally {
            
        }
        return item;
    }

    public List<CartItem> getCartItems(Long userId) {
        List<CartItem> items;
        try {
            Cart cart;
            cart = (Cart)em.createQuery("Select u From " + RestDemoConstants.PLATFORM_USER_TABLE_NAME+ " u "
                    + "LEFT JOIN FETCH u.cart where u.id = :userId")
                    .setParameter("userId", userId)
                    .getSingleResult();
            items = em.createNamedQuery(CartItem.CART_ITEMS, CartItem.class)
                    .setParameter("cartId", cart.getId())
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            items = null;
        }finally {
            
        }
        return items;
    }

    public boolean deleteCartItemsByCartId(Integer id) {
        boolean isDel = false;
        PlatformUser user = null;
        try {
            user = em.find(PlatformUser.class,id);
            em.remove(user.getCart().getItems());
            isDel = true;
        } catch(Exception e) {
            user = null;
        }
        return isDel;
    }


    /********************************************
     * 
     *        Cart 
     * 
     * ******************************************/
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PlatformUser createCart(PlatformUser cart) {
        try {
            if (cart !=null)
                em.merge(cart);
            
        } catch(Exception e) {
            
        }
        return cart;

    }

    public Cart findUserCart(Long userId) {
        Cart cart = null;
        PlatformUser user = em.find(PlatformUser.class, userId.intValue());
        try {
            if (user != null) {
                cart = (Cart)em.createQuery("Select u From " + RestDemoConstants.PLATFORM_USER_TABLE_NAME+ " u "
                        + "LEFT JOIN FETCH u.cart where u.id = :userId")
                        .setParameter("userId", userId)
                        .getSingleResult();
            }
        } catch (Exception ex) {
            cart = null;
            user = null;
        } finally {
            
        }
        return cart;
    }

    public List<Cart> findAllCarts(){
        return em.createNamedQuery("findAllCarts", Cart.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteCart(Long id) {
        boolean isDel = false;
        Cart c=null;
        PlatformUser u;
        try {

            u = em.find(PlatformUser.class, id.intValue()); 
            if(u.getCart() != null) {
                c = u.getCart();
            }
            u.setCart(null);
            em.merge(u);
            em.remove(c);
            isDel = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            c = null;
            isDel = false;
        } finally {
            
        }

        return isDel;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Cart updateCart(Cart cart) {
        if(cart != null) {
            try {
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                em.merge(cart);
                tx.commit();
            } catch(Exception e) {
                cart = null;
            }
        }
        return cart;
    }

}
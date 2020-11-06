package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.json.bind.annotation.JsonbTransient;


/**
 * Date: April 02, 2019 
 * 
 * @author Tony Geara
 * @author Sweidan, Zaid
 * @author Elias Khoury 
 * @author Jon Brownridge
 */
@Entity
@Table(name = "CART_ITEM")
@NamedQuery(name=CartItem.CART_ITEMS,query = "SELECT i from CartItem i where i.cart.id=:cartId")
@EntityListeners(AuditListener.class)
public class CartItem extends ModelBase implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String CART_ITEMS="CartItem.findAllInCart";
    
  
    protected Product product;
    protected Cart cart;
    protected Integer quantity;
    protected Double totalAmount;

    @OneToOne
    @JoinColumn(name="PROUCT_ID")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

 
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "CART_ID")
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cart == null) ? 0 : cart.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CartItem other = (CartItem) obj;
        if (cart == null) {
            if (other.cart != null)
                return false;
        } else if (!cart.equals(other.cart))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (totalAmount == null) {
            if (other.totalAmount != null)
                return false;
        } else if (!totalAmount.equals(other.totalAmount))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "com.shop.shopping.orm.CartItem[ id=" + id + " ]";
    }

    public Double calculateTotal() {
        return this.totalAmount=this.quantity*this.product.getUnitPrice();
    }
//    
}

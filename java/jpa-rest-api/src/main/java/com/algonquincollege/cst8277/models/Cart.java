package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Sales order cart
 * 
 * Date: April 02, 2019 
 * 
 * @author Tony Geara
 * @author Sweidan, Zaid
 * @author Elias Khoury 
 * @author Jon Brownridge
 */
@Entity
@Table(name = "CART")
@NamedQuery(name = "findAllCarts", query = "Select c From Cart c")
@EntityListeners({AuditListener.class})
public class Cart extends ModelBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
   // private Long userId;
    private List<CartItem> items;
    private Double totalAmount;
    private Double totalDiscount;
    
    public Cart() {
        super();
        setItems(new ArrayList<>());
    }
    

    

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }


    @OneToMany(mappedBy="cart", cascade = CascadeType.ALL)
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
  

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((totalDiscount == null) ? 0 : totalDiscount.hashCode());
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
        Cart other = (Cart) obj;

        if (totalAmount == null) {
            if (other.totalAmount != null)
                return false;
        } else if (!totalAmount.equals(other.totalAmount))
            return false;
        
        
        if(totalDiscount == null) {
            if(other.totalDiscount != null) {
                return false;
            }
        }else if(!totalDiscount.equals(other.totalDiscount))
            return false;
        return true;
    }

    

    public void calculateTotal() {
        for(CartItem i : items) {
            this.totalAmount+= i.getTotalAmount();
        }
        this.totalAmount-=this.totalDiscount;
        if(this.totalAmount<0){
            this.totalAmount=0.0;
        }
    }
   
}

package com.algonquincollege.cst8277.models;


import java.io.Serializable;
import javax.persistence.*;

/**
 * Date: April 02, 2019 
 * 
 * @author Tony Geara
 * @author Sweidan, Zaid
 * @author Elias Khoury 
 * @author Jon Brownridge
 */
@Entity
@Table(name = "product")
@NamedQuery(name = "findAll",query = "SELECT p FROM Product p")
@NamedQuery(name = "updateProduct",query = "Update Product p Set p.productName = p.productName, p.inventoryCount = p.inventoryCount, "
        + "p.unitPrice = p.unitPrice where p.id = :id")
@EntityListeners(AuditListener.class)
public class Product extends ModelBase implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String productName;
    protected Integer inventoryCount;
    protected Double unitPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }


   

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((inventoryCount == null) ? 0 : inventoryCount.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((unitPrice == null) ? 0 : unitPrice.hashCode());
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
        Product other = (Product) obj;
        if (inventoryCount == null) {
            if (other.inventoryCount != null)
                return false;
        } else if (!inventoryCount.equals(other.inventoryCount))
            return false;
        if (productName == null) {
            if (other.productName != null)
                return false;
        } else if (!productName.equals(other.productName))
            return false;
        if (unitPrice == null) {
            if (other.unitPrice != null)
                return false;
        } else if (!unitPrice.equals(other.unitPrice))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "com.shop.shopping.orm.Product[ id=" + id + " ]";
    }

}

package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.rest.CartConstants.GET_CARTS_OP_403_DESC_JSON_MSG;
import static com.algonquincollege.cst8277.rest.ProductConstants.*;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.ADMIN_ROLENAME;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.algonquincollege.cst8277.ejb.SimpleBean;
import com.algonquincollege.cst8277.models.Product;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * * Date: April 02, 2019 
 * 
 * @author Tony Geara
 * @author Sweidan, Zaid
 * @author Elias Khoury 
 * @author Jon Brownridge
 * @author Sharon Odongo
 */

@Path("product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    @EJB
    protected SimpleBean simpleBean;
    
    @Inject
    protected SecurityContext sc;
    /**
     * Get list of product
     * 
     * @return array of products 
     */
    @GET
    @Path("/getAll")
    @Operation(description = GET_PRODUCTS_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_PRODUCTS_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_PRODUCTS_OP_403_DESC),
        @APIResponse(responseCode = "404", description = GET_PRODUCTS_OP_404_DESC)
    })
    public Response getProducts(){
        Response message = null;
//        List<Product> products = simpleBean.getProducts();
//        System.out.println(products.size());
        for(Product p : simpleBean.getProducts()) {
            message.ok(p).build();

        }
        return message;
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = GET_PRODUCT_BY_ID_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_PRODUCT_BY_ID_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_PRODUCT_BY_ID_OP_403_DESC),
        @APIResponse(responseCode = "404", description = GET_PRODUCT_BY_ID_OP_404_DESC)
    })
    @Path(PRODUCT_RESOURCE_PATH_ID_PATH)
    public Response getProductById(@Parameter(description = PRIMARY_KEY_DESC, required = true)
        @PathParam(PRODUCT_RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        Product emp = simpleBean.getProductById(id);
        if (emp == null) {
            response = Response.status(NOT_FOUND).build();
        }
        else {
            response = Response.ok(emp).build();
        }
        return response;
    }
    
    /**
     * Add product to store
     * @param p Product details
     * @return 
     */
    @POST
    @Operation(description = "Create new product")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "product created successfully"),
        @APIResponse(responseCode = "403", description = "only admins can create new product"),
        @APIResponse(responseCode = "404", description = "product creation not found")
    })
    @Path("/addProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLENAME)
    public Response createProducts(Product p){
        Response message = null;
        if (!sc.isCallerInRole(ADMIN_ROLENAME)) {
            message = Response.status(Status.FORBIDDEN).entity(GET_PRODUCTS_OP_403_DESC_JSON_MSG).build();
        }
        else {
            simpleBean.addProduct(p);
            message.ok(p).build();
        }
        return message;
    }
    
    
    
    @PUT
    @Operation(description = "Updating product")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Update product success"),
        @APIResponse(responseCode = "403", description = "only admins can update products"),
        @APIResponse(responseCode = "404", description = "product update not found")
    })
    @Path("/update/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLENAME)
    public Response updateProduct(@PathParam("productId")Long productId, Product p) {
        Response message = null;
        if (!sc.isCallerInRole(ADMIN_ROLENAME)) {
            message = Response.status(Status.FORBIDDEN).entity(GET_PRODUCTS_OP_403_DESC_JSON_MSG).build();
        }
        else {
            if(simpleBean.updateProduct(p, productId)) {
                message.ok("Product: " + productId +  " updated").build();
            }else {
                message.ok("Error: Product not updated").build();
            }
        }
        return message;
    }
    
    @DELETE
    @Operation(description = "Delete product")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product delete successful"),
        @APIResponse(responseCode = "403", description = "only admins can delete products"),
        @APIResponse(responseCode = "404", description = "product delete not found")
    })
    @Path("/deleteProduct/{productId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLENAME)
    public Response deleteProduct(@PathParam("productId") Integer productId) {
        Response message = null;
        
            if(simpleBean.deleteProduct(productId)) {
                message.ok(productId + " has been deleted").build();
            }else {
                message.ok("No products deleted").build(); 
            }
        
        return message;
    }
    
}

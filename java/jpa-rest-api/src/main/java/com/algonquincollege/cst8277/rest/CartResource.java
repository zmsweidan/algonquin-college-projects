
package com.algonquincollege.cst8277.rest;

import com.algonquincollege.cst8277.ejb.SimpleBean;
import com.algonquincollege.cst8277.models.*;

import static com.algonquincollege.cst8277.rest.CartConstants.*;
import static com.algonquincollege.cst8277.rest.EmployeeConstants.GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.ADMIN_ROLENAME;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.USER_ROLENAME;

import java.security.Principal;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * Date: April 02, 2019 
 * 
 * @author Tony Geara
 * @author Sweidan, Zaid
 * @author Elias Khoury 
 * @author Jon Brownridge
 * @author Sharon Odongo
 */

@Path(CART_RESOURCE_NAME)
@Produces({MediaType.APPLICATION_JSON})
public class CartResource {

    @EJB
    protected SimpleBean simpleBean;

    @Inject
    protected SecurityContext sc;
    
    /**
     * Add item to cart
     *
     * @param item item details
     * @param userId cart to add item
     * @param productId product to add
     * @return
     */
    @PUT
    @Operation(description = "Updating user cart")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Update successful"),
        @APIResponse(responseCode = "403", description = "Only users can delete"),
        @APIResponse(responseCode = "404", description = "Update not found")
    })
    @Path("/{cartId}/addToCart/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response addProductToCart(@PathParam("cartId") Long cartId, @PathParam("productId") Long productId) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getCart().getId() != cartId) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_CART_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                CartItem addItem = simpleBean.addItem(productId, cartId);
                if (addItem != null) {
                    response.ok(addItem).build();
                } else {
                    response.ok("Product was not added").build();
                }
            }
        }

        return response;
    }
    
    /**
     * Create a cart or retrieve active one
     *
     * @param userId user to create cart for
     * @return
     */
    @GET
    @Operation(description = GET_CART_BY_ID_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_CART_BY_ID_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_CART_BY_ID_OP_403_DESC),
        @APIResponse(responseCode = "404", description = GET_CART_BY_ID_OP_404_DESC)
    })
    @Path("/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response findCartbyUser(@PathParam("cartId") Long cartId) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getCart().getId() != cartId) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                //TODO make sure this works
                response = Response.ok(platformUser.getCart()).build();
            }
        }
        return response;

    }

    /**
     * Get items in this user cart
     *
     * @param userId user
     * @return
     */
    @GET
    @Path("/active")
    @Operation(description = GET_CARTS_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_CARTS_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_CARTS_OP_403_DESC),
        @APIResponse(responseCode = "404", description = GET_CARTS_OP_404_DESC)
    })
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLENAME)
    public Response activeCarts() {
        Response response = null;
        if (!sc.isCallerInRole(ADMIN_ROLENAME)) {
            response = Response.status(Status.FORBIDDEN).entity(GET_CARTS_OP_403_DESC_JSON_MSG).build();
        }
        else {
            List<Cart> item = simpleBean.findAllCarts();
            response.ok(item).build();
        }
        return response;
    }
    
    @DELETE
    @Operation(description = "Deleting user cart")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Delete successful"),
        @APIResponse(responseCode = "403", description = "Only admins can delete"),
        @APIResponse(responseCode = "404", description = "Delete cart not found")
    })
    @Path("/deleteCart/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response deleteCart(@PathParam("id") Long id) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null && platformUser.getId() != id) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                //TODO check todo in bean
                response = Response.ok(simpleBean.deleteCart(id)).build();
            }
        }
        return response;
        

    }
    
    @POST
    @Operation(description = "Creating user cart")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Create successful"),
        @APIResponse(responseCode = "403", description = "Only users can delete"),
        @APIResponse(responseCode = "404", description = "cart create not found")
    })
    @Path("/createCart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response createCart(Cart cart) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() != null && platformUser.getCart().getId() != cart.getId()) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                platformUser.setCart(cart);
                response = Response.ok( simpleBean.createCart(platformUser)).build();
            }
        }
        return response;
        
    }

}

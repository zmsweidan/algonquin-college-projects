package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.rest.CartItemConstants.*;
import static com.algonquincollege.cst8277.rest.EmployeeConstants.GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG;
import static com.algonquincollege.cst8277.rest.EmployeeConstants.PRIMARY_KEY_DESC;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.*;

import java.security.Principal;

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
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import com.algonquincollege.cst8277.ejb.SimpleBean;
import com.algonquincollege.cst8277.models.CartItem;
import com.algonquincollege.cst8277.models.PlatformUser;

/**
*
* Date: April 02, 2019 
* 
* @author Tony Geara
* @author Sweidan, Zaid
* @author Elias Khoury 
* @author Jon Brownridge
*/

@Path(CART_ITEM_RESOURCE_NAME)
public class CartItemResource {
    @EJB
    protected SimpleBean simpleBean;
    
    @Inject
    protected SecurityContext sc;
    

    //TODO add path
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = GET_CART_ITEMS_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_CART_ITEMS_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_CART_ITEMS_OP_403_DESC),
        @APIResponse(responseCode = "404", description = GET_CART_ITEMS_OP_404_DESC)
    })
    @RolesAllowed(ADMIN_ROLENAME)
    public Response getAll(){
        Response message = null;
        message.ok(simpleBean.listItems()).build();
        return message;
    }
    
    
    @GET
    @Operation(description = GET_CART_ITEM_BY_ID_OP_DESC)
    @APIResponses({
        @APIResponse(responseCode = "200", description = GET_CART_ITEM_BY_ID_OP_200_DESC),
        @APIResponse(responseCode = "403", description = GET_CART_ITEM_BY_ID_OP_403_DESC)
    })
    @RolesAllowed(USER_ROLENAME)
    @Path(CART_ITEM_RESOURCE_PATH_ID_PATH)
    public Response getCartItemsByUser(@Parameter(description = PRIMARY_KEY_DESC, required = true)
        @PathParam(CART_ITEM_RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getCart().getId() != id) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_CART_ITEM_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                //TODO check if this works
                response = Response.ok(platformUser.getCart().getItems()).build();
            }
        }
        return response;
    }
    
    @DELETE
    @Operation(description = "Clear items in users cart")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "cart items cleared"),
        @APIResponse(responseCode = "403", description = "only users can clear their own items"),
        @APIResponse(responseCode = "404", description = "items not found")
    })
    @Path("/clearCartItems/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response deleteCartItemsByCartId(@PathParam("userId") Integer userId) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getCart().getId() != userId) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                simpleBean.deleteCartItemsByCartId(userId);
                response = Response.ok("all items cleared").build();
            }
        }
        return response;
    }
    
    @POST
    @Operation(description = "Add cart item to user cart")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "cart item added succesfully"),
        @APIResponse(responseCode = "403", description = "only users can add cart items"),
        @APIResponse(responseCode = "404", description = "cart item addition not found")
    })
    @Path("/addItemToCart/{productId}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response addCartItem(@PathParam("productId") Long productId, @PathParam("userId") Long userId) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getId() != userId) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                System.out.println("made it here rest");
                CartItem item = simpleBean.addItem(productId, userId);
                response = Response.ok(item).build();
            }
        }
        return response;
     
    }
    
    @PUT
    @Operation(description = "Update cart item")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Updating cart item"),
        @APIResponse(responseCode = "403", description = "Only users can update cart items"),
        @APIResponse(responseCode = "404", description = "cart items update not found")
    })
    @Path("/updateItem/{userId}/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(USER_ROLENAME)
    public Response editItemInCart(@PathParam("userId")Integer userId, @PathParam("itemId") Integer itemId, CartItem item) {
        Response response = null;

        Principal principal = sc.getCallerPrincipal();
        if (principal == null) {
            //because of @RolesAllowed(USER_ROLENAME) this should never happen!
            response = Response.serverError().entity("{\"message\":\"missing principal\"}").build();
        }
        else {
            PlatformUser platformUser = (PlatformUser)principal;
            if (platformUser.getCart() == null || platformUser.getCart().getId() != userId) {
                //if the PlatformUser has no relationship to an Employee, or has a relationship to a different
                //Employee, then someone is trying to 'get' information they are not entitled to
                response = Response.status(Status.UNAUTHORIZED).entity(GET_EMPLOYEE_BY_ID_OP_403_DESC_JSON_MSG).build();
            }
            else {
                System.out.println("made it");
                response = Response.ok(simpleBean.updateCartItem(item, itemId)).build();
            }
        }
        return response;
    }

}

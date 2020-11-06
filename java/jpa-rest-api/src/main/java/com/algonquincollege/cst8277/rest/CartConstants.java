package com.algonquincollege.cst8277.rest;
/**
*
* Date: April 02, 2019 
* 
* @author Tony Geara 
* @author Sweidan, Zaid
* @author Elias Khoury 
* @author Jon Brownridge
*/
public interface CartConstants {
    
    public static final String PREFIX_JSON_MSG = "{\"message\":\"";
    public static final String SUFFIX_JSON_MSG = "\"}";

    public static final String PRIMARY_KEY_DESC = "primary key";
    public static final String CART_RESOURCE_NAME =  "cart";
    public static final String CART_RESOURCE_PATH_ID_ELEMENT =  "id";
    public static final String CART_RESOURCE_PATH_ID_PATH =  "/{" + CART_RESOURCE_PATH_ID_ELEMENT + "}";

    public static final String GET_CARTS_OP_DESC = "Retrieves list of Carts";
    public static final String GET_CARTS_OP_200_DESC = "Successful, returning carts";
    public static final String GET_CARTS_OP_403_DESC = "Only admin's can list all carts";
    public static final String GET_CARTS_OP_404_DESC = "Could not find carts";
    public static final String GET_CARTS_OP_403_JSON_MSG =
        PREFIX_JSON_MSG + GET_CARTS_OP_403_DESC + SUFFIX_JSON_MSG;

    public static final String GET_CART_BY_ID_OP_DESC = "Retrieve specific Cart";
    public static final String GET_CART_BY_ID_OP_200_DESC = "Successful, returning requested cart";
    public static final String GET_CART_BY_ID_OP_403_DESC = "Only user's can retrieve a specific cart";
    public static final String GET_CART_BY_ID_OP_404_DESC = "Requested cart not found";
    public static final String GET_CARTS_OP_403_DESC_JSON_MSG =
        PREFIX_JSON_MSG + GET_CART_BY_ID_OP_403_DESC + SUFFIX_JSON_MSG;
    public static final String GET_CART_BY_ID_OP_403_DESC_JSON_MSG = "Employee by id JSON Msg";

}

package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.RestDemoConstants.ADMIN_ROLENAME;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.SERVER_API_DESC;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.SERVER_URL;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.USER_ROLENAME;

import java.util.HashSet;
import java.util.Set;

//import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@ApplicationPath("/api/v1")
@OpenAPIDefinition(info = @Info(
    title = "Rest'ful Demo API",
    version = "1.0.0",
    description = SERVER_API_DESC
    ),
    servers = {
        @Server(url = SERVER_URL)
    }
)
//this used to be in web.xml
@DeclareRoles({USER_ROLENAME, ADMIN_ROLENAME})
public class RestDemoConfig extends Application {

    //default behaviour of 'empty' Application is to scan for all Resource classes annotated with @Path

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes=new HashSet<>();
        classes.add(CartResource.class);
        classes.add(CartItemResource.class);
        classes.add(EmployeeResource.class);
        classes.add(ProductResource.class);
        return classes;
    }
}
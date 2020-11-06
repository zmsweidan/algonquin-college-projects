package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.utils.RestDemoConstants.ADMIN_ROLENAME;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_ADMIN_USER_PASSWORD_PROPNAME;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_ADMIN_USER_PROPNAME;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.RestDemoConstants.PROPERTY_SALTSIZE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.algonquincollege.cst8277.models.PlatformRole;
import com.algonquincollege.cst8277.models.PlatformUser;
import com.algonquincollege.cst8277.security.CustomIdentityStoreJPAHelper;

@Startup
@Singleton
public class BuildDefaultAdminUser {

    @Inject
    protected CustomIdentityStoreJPAHelper jpaHelper;

    @Inject
    @ConfigProperty(name = DEFAULT_ADMIN_USER_PROPNAME, defaultValue = DEFAULT_ADMIN_USER)
    private String defaultAdminUsername;

    //TODO - encrypt value inside microprofile-config.properties
    @Inject
    @ConfigProperty(name = DEFAULT_ADMIN_USER_PASSWORD_PROPNAME)
    private String defaultAdminUserPassword;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @PostConstruct
    public void init() {
        // build default admin user (if needed)
        PlatformUser defaultAdminUser = jpaHelper.findUserByName(defaultAdminUsername);
        if (defaultAdminUser == null) {
            defaultAdminUser = new PlatformUser();
            defaultAdminUser.setUsername(defaultAdminUsername);
            Map<String, String> pbAndjProperties = new HashMap<>();
            pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
            pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
            pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
            pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
            pbAndjPasswordHash.initialize(pbAndjProperties);
            String pwHash = pbAndjPasswordHash.generate(defaultAdminUserPassword.toCharArray());
            defaultAdminUser.setPwHash(pwHash);

            PlatformRole platformRole = new PlatformRole();
            platformRole.setRoleName(ADMIN_ROLENAME);
            Set<PlatformRole> platformRoles = defaultAdminUser.getPlatformRoles();
            platformRoles.add(platformRole);
            jpaHelper.savePlatformUser(defaultAdminUser);
        }
    }
}
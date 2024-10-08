package com.lunatech;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.AccessTokenCredential;
import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
@Authenticated
public class BaseResource {

    @IdToken
    JsonWebToken idToken;

    @Inject
    JsonWebToken accessToken;

    @Inject
    AccessTokenCredential opaqueAccessToken;

    @Inject
    RefreshToken refreshToken;

    @Inject
    UserInfo userInfo;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public String admin() {
        return "Hello: " + idToken.getName() + " & roles: " + securityIdentity.getRoles();
    }

    @GET
    @Path("/user")
    @RolesAllowed("user")
    public String user() {
        return "Hello: " + idToken.getName() + " roles: " + accessToken.getGroups().toString();
    }

    @GET
    @Path("/idtoken")
    public String idToken() {
        return idToken.toString();
    }

    @GET
    @Path("/accesstoken")
    public String accessToken() {
        return accessToken.toString();
    }

    @GET
    @Path("/refreshtoken")
    public String refreshToken() {
        return refreshToken.getToken();
    }

    @GET
    @Path("/opaque")
    public String opaque() {
        return "Is opaque? : " + opaqueAccessToken.isOpaque();
    }

    @GET
    @Path("/userinfo")
    public String userInfo() {
        return userInfo.getUserInfoString();
    }
}

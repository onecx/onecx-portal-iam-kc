package io.github.onecx.portal.iam.kc.rs.v1.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;

import io.quarkus.keycloak.admin.client.common.KeycloakAdminClientConfig;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.Authenticated;

@Path("/v1/iam")
@RequestScoped
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KeycloakAdminRestController {

    @Inject
    Keycloak keycloak;

    @Inject
    JsonWebToken jwt;

    @PUT
    @Path("/reset-password")
    @Operation(operationId = "resetPassword", summary = "Reset Keycloak User's password", description = "The password of user registered in keycloak is set to the new one.")
    @APIResponse(responseCode = "204", description = "Password reset successful")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "404", description = "Not Found")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response resetPassword(ResetPasswordRequestDTO request) {
        if (request == null || request.password() == null || request.password().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String userId = jwt.getSubject();
        if (userId == null || userId.isEmpty()) {
            return Response.status(401).build();
        }
        String issuer = jwt.getIssuer();
        if (issuer == null || issuer.isEmpty()) {
            return Response.status(401).build();
        }
        int index = issuer.lastIndexOf("/");
        if (index < 0) {
            return Response.status(401).build();
        }
        String realmName = issuer.substring(index + 1);
        if (realmName.isEmpty()) {
            return Response.status(401).build();
        }

        CredentialRepresentation resetPassword = new CredentialRepresentation();
        resetPassword.setValue(request.password());
        resetPassword.setType(KeycloakAdminClientConfig.GrantType.PASSWORD.asString());
        resetPassword.setTemporary(false);

        keycloak.realm(realmName).users().get(userId).resetPassword(resetPassword);
        return Response.noContent().build();
    }

    @RegisterForReflection
    public record ResetPasswordRequestDTO(String password) {
    }

}

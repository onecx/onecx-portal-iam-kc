package io.github.onecx.portal.iam.kc.rs.v1.controllers;

import java.util.Map;

import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.smallrye.jwt.auth.cdi.NullJsonWebToken;

public class KeycloakAdminRestControllerTokenTest {

    @Test
    public void resetPasswordTokenValidationTest() {

        JsonWebTokenImpl token = new JsonWebTokenImpl();

        KeycloakAdminRestController.ResetPasswordRequestDTO dto = new KeycloakAdminRestController.ResetPasswordRequestDTO(
                "test");
        KeycloakAdminRestController controller = new KeycloakAdminRestController();
        controller.jwt = token;

        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

        token.claims = Map.of(Claims.sub.name(), "");
        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

        token.claims = Map.of(Claims.sub.name(), "userId");
        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

        token.claims = Map.of(Claims.sub.name(), "userId", Claims.iss.name(), "");
        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

        token.claims = Map.of(Claims.sub.name(), "userId", Claims.iss.name(), "wrong-link");
        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

        token.claims = Map.of(Claims.sub.name(), "userId", Claims.iss.name(), "wrong-link/");
        try (Response response = controller.resetPassword(dto)) {
            Assertions.assertEquals(401, response.getStatus());
        }

    }

    public static class JsonWebTokenImpl extends NullJsonWebToken {

        Map<String, String> claims = Map.of();

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getClaim(String claimName) {
            return (T) claims.get(claimName);
        }
    }
}

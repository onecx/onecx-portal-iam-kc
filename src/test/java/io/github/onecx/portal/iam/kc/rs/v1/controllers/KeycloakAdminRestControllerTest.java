package io.github.onecx.portal.iam.kc.rs.v1.controllers;

import static io.github.onecx.portal.iam.test.KeycloakAminUtil.PASSWORD_ALICE;
import static io.github.onecx.portal.iam.test.KeycloakAminUtil.USER_ALICE;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.github.onecx.portal.iam.test.KeycloakAminUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class KeycloakAdminRestControllerTest {

    @Test
    public void resetPasswordTest() {
        String newPassword = "changedPassword";

        String accessToken = KeycloakAminUtil.getAccessToken(USER_ALICE, PASSWORD_ALICE);

        given().auth()
                .oauth2(accessToken)
                .body(new KeycloakAdminRestController.ResetPasswordRequestDTO(newPassword))
                .contentType(ContentType.JSON)
                .put("/v1/iam/reset-password")
                .then().statusCode(204);

        accessToken = KeycloakAminUtil.getAccessToken(USER_ALICE, newPassword);

        given().auth()
                .oauth2(accessToken)
                .body(new KeycloakAdminRestController.ResetPasswordRequestDTO(PASSWORD_ALICE))
                .contentType(ContentType.JSON)
                .put("/v1/iam/reset-password")
                .then().statusCode(204);

        KeycloakAminUtil.getAccessToken(USER_ALICE, PASSWORD_ALICE);
    }
}

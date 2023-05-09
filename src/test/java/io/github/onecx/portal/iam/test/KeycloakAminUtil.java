package io.github.onecx.portal.iam.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import io.restassured.http.ContentType;

public class KeycloakAminUtil {

    private static final Config CONFIG = ConfigProvider.getConfig();
    public static String AUTH_SERVER_URL = CONFIG.getValue("client.quarkus.oidc.auth-server-url", String.class);
    public static String CLIENT_ID = "quarkus-app";
    public static String SECRET = "secret";
    public static String USER_BOB = "bob";
    public static String PASSWORD_BOB = "bob";
    public static String USER_ALICE = "alice";
    public static String PASSWORD_ALICE = "alice";
    public static String REALM = "quarkus";

    public static String getAccessToken(String user, String password) {
        return given()
                .auth()
                .preemptive()
                .basic(CLIENT_ID, SECRET)
                .contentType(ContentType.URLENC)
                .baseUri(AUTH_SERVER_URL)
                .body("username=" + user + "&password=" + password + "&grant_type=password")
                .post("/protocol/openid-connect/token")
                .then()
                .statusCode(200)
                .body("$", hasKey("access_token"))
                .extract().response().jsonPath().getString("access_token");
    }
}

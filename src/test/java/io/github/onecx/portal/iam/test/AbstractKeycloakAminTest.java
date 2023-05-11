package io.github.onecx.portal.iam.test;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.keycloak.client.KeycloakTestClient;

public class AbstractKeycloakAminTest {

    DevServicesContext testContext;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    public static String CLIENT_ID_PROP = "quarkus.oidc.client-id";

    public static String USER_BOB = "bob";
    public static String USER_ALICE = "alice";
    public static String PASSWORD_ALICE = "alice";

    public String getAccessToken(String user) {
        return keycloakClient.getAccessToken(user);
    }

    public String getAccessToken(String user, String password) {
        return keycloakClient.getAccessToken(user, password, getClientId());
    }

    protected String getClientId() {
        return getPropertyValue(CLIENT_ID_PROP, "quarkus-app");
    }

    protected String getPropertyValue(String prop, String defaultValue) {
        return ConfigProvider.getConfig().getOptionalValue(prop, String.class)
                .orElseGet(() -> getDevProperty(prop, defaultValue));
    }

    private String getDevProperty(String prop, String defaultValue) {
        String value = testContext == null ? null : testContext.devServicesProperties().get(prop);
        return value == null ? defaultValue : value;
    }

}

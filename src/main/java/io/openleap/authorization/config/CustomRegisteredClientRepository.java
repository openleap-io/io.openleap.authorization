package io.openleap.authorization.config;

import io.openleap.authorization.service.identity.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Set;
import java.util.UUID;

public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    @Autowired
    private IdentityService identityService;

    @Override
    public void save(RegisteredClient registeredClient) {


        Set<String> scopes = (registeredClient.getScopes() == null || registeredClient.getScopes().isEmpty()) ?
                Set.of("openid") :
                registeredClient.getScopes();

        // Disable PKCE & Consent
        RegisteredClient modifiedClient = RegisteredClient.from(registeredClient)
                .scopes(s -> s.addAll(scopes))
                .clientSettings(ClientSettings
                        .withSettings(registeredClient.getClientSettings().getSettings())
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .build();

        identityService.saveClient(modifiedClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        return identityService.findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return identityService.findByClientId(clientId);
    }
}
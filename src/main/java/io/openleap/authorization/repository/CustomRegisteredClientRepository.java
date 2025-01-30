package io.openleap.authorization.repository;

import io.openleap.authorization.service.identity.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Set;

public class CustomRegisteredClientRepository implements RegisteredClientRepository {
    Logger logger = LoggerFactory.getLogger(CustomRegisteredClientRepository.class);

    @Autowired
    private IdentityService identityService;

    @Override
    public void save(RegisteredClient registeredClient) {
        Set<String> scopes = (registeredClient.getScopes() == null || registeredClient.getScopes().isEmpty()) ?
                Set.of("openid") :
                registeredClient.getScopes();

        RegisteredClient modifiedClient = RegisteredClient.from(registeredClient)
                .scopes(s -> s.addAll(scopes))
                .clientSettings(ClientSettings
                        .withSettings(registeredClient.getClientSettings().getSettings())
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .build();
        logger.debug("Registering client with name: {}", modifiedClient.getClientName());
        identityService.saveClient(modifiedClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        logger.debug("Finding client by id: {}", id);
        return identityService.findByClientId(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        logger.debug("Finding client by client id: {}", clientId);
        return identityService.findByClientId(clientId);
    }
}
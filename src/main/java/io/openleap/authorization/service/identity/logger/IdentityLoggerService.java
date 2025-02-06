package io.openleap.authorization.service.identity.logger;

import io.openleap.authorization.service.identity.IdentityService;
import io.openleap.authorization.service.identity.dto.UserPrincipalDto;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile("identity-logger")
public class IdentityLoggerService implements IdentityService {


    @Override
    public UserPrincipalDto getUserPrincipal(String username) {
        return new UserPrincipalDto("username", "password", false, false,
                Set.of(new UserPrincipalDto.Profile("name", true, true, Set.of(new UserPrincipalDto.Profile.Role("name")))));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        if (clientId.equals("registrar-client")) {
            return RegisteredClient.withId("client-id")
                    .clientId("client-id")
                    .clientSecret("{bcrypt}$2a$10$07vsZjPQz5Oc4HsMXWdxxOv7/CRh6dJBeSABX4BiXmZAowT7dajwK")
                    .id("17d0d75d-4350-47ee-a198-4c734ba85546")
                    .clientName("17d0d75d-4350-47ee-a198-4c734ba85546")
                    .clientAuthenticationMethods(s ->
                            s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    )
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("client.create")
                    .scope("client.read")
                    .clientSettings(ClientSettings.builder()
                            .requireAuthorizationConsent(false) //No authorization required
                            .requireProofKey(false)
                            .build())
                    .build();
        }
        return null;

    }


    @Override
    public void saveClient(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unregister(String instanceId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
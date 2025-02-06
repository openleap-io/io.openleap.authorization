package io.openleap.authorization.service.identity;

import io.openleap.authorization.service.identity.dto.UserPrincipalDto;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface IdentityService {
    UserPrincipalDto getUserPrincipal(String username);

    RegisteredClient findByClientId(String clientId);

    void saveClient(RegisteredClient registeredClient);

    void unregister(String instanceId);
}

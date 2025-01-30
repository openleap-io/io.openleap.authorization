package io.openleap.authorization.service.identity;

import io.openleap.authorization.service.identity.dto.UserPrincipalDto;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface IdentityService {
    UserPrincipalDto getUserPrincipal(String username);
    RegisteredClient findByClientId(String clientId);
    RegisteredClient findById(String id);
    RegisteredClient findByClientName(String clientName);
    void saveClient(RegisteredClient registeredClient);
    void unregisterClient(String clientId, String instanceId);
}

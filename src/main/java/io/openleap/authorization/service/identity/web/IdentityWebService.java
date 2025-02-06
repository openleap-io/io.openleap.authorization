package io.openleap.authorization.service.identity.web;

import io.openleap.authorization.controller.dto.UnregisterRequest;
import io.openleap.authorization.service.identity.IdentityService;
import io.openleap.authorization.service.identity.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("identity-web")
public class IdentityWebService implements IdentityService {
    Logger logger = LoggerFactory.getLogger(IdentityWebService.class);

    private final IdentityClient identityClient;

    public IdentityWebService(IdentityClient identityClient) {
        this.identityClient = identityClient;
    }

    @Override
    public UserPrincipalDto getUserPrincipal(String username) {
        logger.info("Fetching user principal for username: {}", username);
        UserPrincipalResponseDto userPrincipalResponse = identityClient.getUserPrincipalByUsername(username);
        ProfileResponseDto profileResponseDto = identityClient.getUserPrincipalProfilesByUserPrincipalId(userPrincipalResponse.id());
        return new UserPrincipalDto(
                userPrincipalResponse.username(),
                userPrincipalResponse.password(),
                userPrincipalResponse.disabled(),
                userPrincipalResponse.locked(),
                profileResponseDto._embedded().profiles().stream().map(
                        profile -> new UserPrincipalDto.Profile(
                                profile.name(),
                                profile.enabled(),
                                profile.defaultProfile(),
                                profile.roles().stream().map(
                                        role -> new UserPrincipalDto.Profile.Role(
                                                role.name())).collect(Collectors.toSet())
                        )).collect(Collectors.toSet()));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        logger.info("Fetching client with client id: {}", clientId);
        ClientPrincipalResponseDto clientPrincipalResponseDto = identityClient.getClientPrincipalByClientId(clientId);
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.clientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.authorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.redirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.postLogoutRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.scopes());


        return RegisteredClient.withId(clientPrincipalResponseDto.id())
                .clientId(clientPrincipalResponseDto.clientId())
                .clientIdIssuedAt(Instant.now())
                .clientSecret(clientPrincipalResponseDto.clientSecret())
                .clientName(clientPrincipalResponseDto.clientName())
                .clientAuthenticationMethods(authenticationMethods -> clientAuthenticationMethods.forEach(authenticationMethod -> authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes(grantTypes -> authorizationGrantTypes.forEach(grantType -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(scopes -> scopes.addAll(clientScopes)).build();


    }

    @Override
    public void saveClient(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            logger.warn("Client is null, cannot save client");
            return;
        }
        logger.warn("Saving client with client id: {}", registeredClient.getClientId());
        identityClient.saveClient(new ClientPrincipalRequestDto(
                registeredClient.getClientId(),
                registeredClient.getClientIdIssuedAt() == null ? "" : registeredClient.getClientIdIssuedAt().toString(),
                registeredClient.getClientSecret(),
                registeredClient.getClientSecretExpiresAt() == null ? "" : registeredClient.getClientSecretExpiresAt().toString(),
                registeredClient.getClientName(),
                registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).reduce((a, b) -> a + "," + b).orElse(""),
                registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).reduce((a, b) -> a + "," + b).orElse(""),
                registeredClient.getRedirectUris().stream().reduce((a, b) -> a + "," + b).orElse(""),
                registeredClient.getPostLogoutRedirectUris().stream().reduce((a, b) -> a + "," + b).orElse(""),
                registeredClient.getScopes().stream().reduce((a, b) -> a + "," + b).orElse(""),
                registeredClient.getClientSettings().toString(),
                registeredClient.getTokenSettings().toString())
        );
    }

    @Override
    public void unregister(String instanceId) {
        logger.info("Unregistering client with instance id: {}", instanceId);
        identityClient.unregisterClient(new UnregisterRequest(instanceId));
    }


    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else {
            return ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod) ? ClientAuthenticationMethod.NONE : new ClientAuthenticationMethod(clientAuthenticationMethod);
        }
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else {
            return AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType) ? AuthorizationGrantType.REFRESH_TOKEN : new AuthorizationGrantType(authorizationGrantType);
        }
    }
}

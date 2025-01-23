package io.openleap.authorization.service.identity.web;

import io.openleap.authorization.service.identity.IdentityService;
import io.openleap.authorization.service.identity.dto.ClientPrincipalRequestDto;
import io.openleap.authorization.service.identity.dto.ClientPrincipalResponseDto;
import io.openleap.authorization.service.identity.dto.UserPrincipalDto;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Set;

@Service
@Profile("identity-web")
public class IdentityWebService implements IdentityService {

    private final IdentityHttpClient identityHttpClient;

    public IdentityWebService(IdentityHttpClient identityHttpClient) {
        this.identityHttpClient = identityHttpClient;
    }

    @Override
    public UserPrincipalDto getUserPrincipal(String username) {
        return identityHttpClient.findByUsername(username);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {


        ClientPrincipalResponseDto clientPrincipalResponseDto = identityHttpClient.getClientPrincipalResponseDto(clientId);
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.clientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.authorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.redirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.postLogoutRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.scopes());


        return RegisteredClient.withId(clientPrincipalResponseDto.id())
                .clientId(clientPrincipalResponseDto.clientId())
//                .clientIdIssuedAt(clientPrincipalResponseDto.clientIdIssuedAt())
                .clientSecret(clientPrincipalResponseDto.clientSecret())
//                .clientSecretExpiresAt(clientPrincipalResponseDto.clientSecretExpiresAt())
                .clientName(clientPrincipalResponseDto.clientName())
                .clientAuthenticationMethods((authenticationMethods) -> clientAuthenticationMethods.forEach((authenticationMethod) -> authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes.forEach((grantType) -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes)).build();


    }

    @Override
    public RegisteredClient findById(String id) {
        ClientPrincipalResponseDto clientPrincipalResponseDto = identityHttpClient.getClientById(id);
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.clientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.authorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.redirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.postLogoutRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(clientPrincipalResponseDto.scopes());


        return RegisteredClient.withId(clientPrincipalResponseDto.id())
                .clientId(clientPrincipalResponseDto.clientId())
//                .clientIdIssuedAt(clientPrincipalResponseDto.clientIdIssuedAt())
                .clientSecret(clientPrincipalResponseDto.clientSecret())
//                .clientSecretExpiresAt(clientPrincipalResponseDto.clientSecretExpiresAt())
                .clientName(clientPrincipalResponseDto.clientName())
                .clientAuthenticationMethods((authenticationMethods) -> clientAuthenticationMethods.forEach((authenticationMethod) -> authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes.forEach((grantType) -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes)).build();
    }

    @Override
    public void saveClient(RegisteredClient registeredClient) {
        identityHttpClient.saveClient(new ClientPrincipalRequestDto(
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
                registeredClient.getTokenSettings().toString()));

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

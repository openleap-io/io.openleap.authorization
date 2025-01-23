package io.openleap.authorization.service.identity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientPrincipalRequestDto(@JsonProperty("clientId") String clientId,
                                        @JsonProperty("clientIdIssuedAt") String clientIdIssuedAt,
                                        @JsonProperty("clientSecret") String clientSecret,
                                        @JsonProperty("clientSecretExpiresAt") String clientSecretExpiresAt,
                                        @JsonProperty("clientName") String clientName,
                                        @JsonProperty("clientAuthenticationMethods") String clientAuthenticationMethods,
                                        @JsonProperty("authorizationGrantTypes") String authorizationGrantTypes,
                                        @JsonProperty("redirectUris") String redirectUris,
                                        @JsonProperty("postLogoutRedirectUris") String postLogoutRedirectUris,
                                        @JsonProperty("scopes") String scopes,
                                        @JsonProperty("clientSettings") String clientSettings,
                                        @JsonProperty("tokenSettings") String tokenSettings


) {

}
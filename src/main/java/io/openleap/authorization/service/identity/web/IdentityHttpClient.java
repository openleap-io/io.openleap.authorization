package io.openleap.authorization.service.identity.web;


import io.openleap.authorization.exception.FailedConnectionException;
import io.openleap.authorization.service.BaseHttpClient;
import io.openleap.authorization.service.identity.dto.*;
import io.openleap.authorization.util.CheckedExceptionHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;


public class IdentityHttpClient extends BaseHttpClient {

    public IdentityHttpClient(String baseUrl) {
        super(baseUrl);
    }

    public IdentityHttpClient(String baseUrl, HttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public UserPrincipalDto findByUsername(String username) {
        UserPrincipalResponseDto userPrincipalResponse = getUserPrincipalResponseDto(username);
        ProfileResponseDto profileResponseDto = getProfileResponseDto(userPrincipalResponse);

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



    private ProfileResponseDto getProfileResponseDto(UserPrincipalResponseDto userPrincipalResponse) {
        HttpRequest profilesRequest =
                HttpRequest.newBuilder(URI.create(userPrincipalResponse._links().profiles().href()))
                        .headers(
                                HttpUtil.ACCEPT_HEADER_NAME,
                                HttpUtil.ACCEPT_HEADER_VALUE,
                                HttpUtil.CONTENT_TYPE_HEADER_NAME,
                                HttpUtil.CONTENT_TYPE_HEADER_VALUE)
                        .GET()
                        .build();
        HttpResponse<String> profilesResponse =
                CheckedExceptionHandler.handleCheckedException(
                        client::send,
                        profilesRequest,
                        HttpResponse.BodyHandlers.ofString(),
                        FailedConnectionException::new);
        ProfileResponseDto profileResponseDto = handleResponse(profilesResponse, ProfileResponseDto.class);
        return profileResponseDto;
    }

    private UserPrincipalResponseDto getUserPrincipalResponseDto(String username) {
        HttpRequest request =
                HttpRequest.newBuilder(URI.create(baseUrl.concat("/userPrincipals/search/findByUsername?username=" + username)))
                        .headers(
                                HttpUtil.ACCEPT_HEADER_NAME,
                                HttpUtil.ACCEPT_HEADER_VALUE,
                                HttpUtil.CONTENT_TYPE_HEADER_NAME,
                                HttpUtil.CONTENT_TYPE_HEADER_VALUE)
                        .GET()
                        .build();
        HttpResponse<String> response =
                CheckedExceptionHandler.handleCheckedException(
                        client::send,
                        request,
                        HttpResponse.BodyHandlers.ofString(),
                        FailedConnectionException::new);


        UserPrincipalResponseDto userPrincipalResponse = handleResponse(response, UserPrincipalResponseDto.class);
        return userPrincipalResponse;
    }

    public ClientPrincipalResponseDto getClientPrincipalResponseDto(String clientId) {
        HttpRequest request =
                HttpRequest.newBuilder(URI.create(baseUrl.concat("/clientPrincipals/search/findByClientId?clientId=" + clientId)))
                        .headers(
                                HttpUtil.ACCEPT_HEADER_NAME,
                                HttpUtil.ACCEPT_HEADER_VALUE,
                                HttpUtil.CONTENT_TYPE_HEADER_NAME,
                                HttpUtil.CONTENT_TYPE_HEADER_VALUE)
                        .GET()
                        .build();
        HttpResponse<String> response =
                CheckedExceptionHandler.handleCheckedException(
                        client::send,
                        request,
                        HttpResponse.BodyHandlers.ofString(),
                        FailedConnectionException::new);


        return handleResponse(response, ClientPrincipalResponseDto.class);
    }

    public ClientPrincipalResponseDto getClientById(String id) {
        HttpRequest request =
                HttpRequest.newBuilder(URI.create(baseUrl.concat("/clientPrincipals/" + id)))
                        .headers(
                                HttpUtil.ACCEPT_HEADER_NAME,
                                HttpUtil.ACCEPT_HEADER_VALUE,
                                HttpUtil.CONTENT_TYPE_HEADER_NAME,
                                HttpUtil.CONTENT_TYPE_HEADER_VALUE)
                        .GET()
                        .build();
        HttpResponse<String> response =
                CheckedExceptionHandler.handleCheckedException(
                        client::send,
                        request,
                        HttpResponse.BodyHandlers.ofString(),
                        FailedConnectionException::new);


        return handleResponse(response, ClientPrincipalResponseDto.class);
    }

    public ClientPrincipalResponseDto saveClient(ClientPrincipalRequestDto clientPrincipalRequestDto) {

        String body = CheckedExceptionHandler.handleCheckedException(objectMapper::writeValueAsString, clientPrincipalRequestDto);

        HttpRequest request =
                HttpRequest.newBuilder(
                                URI.create(
                                        baseUrl.concat(
                                                "/clientPrincipals")))
                        .headers(
                                HttpUtil.ACCEPT_HEADER_NAME,
                                HttpUtil.ACCEPT_HEADER_VALUE,
                                HttpUtil.CONTENT_TYPE_HEADER_NAME,
                                HttpUtil.CONTENT_TYPE_HEADER_VALUE)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
        HttpResponse<String> response =
                CheckedExceptionHandler.handleCheckedException(
                        client::send,
                        request,
                        HttpResponse.BodyHandlers.ofString(),
                        FailedConnectionException::new);

        return handleResponse(response, ClientPrincipalResponseDto.class);
    }

}

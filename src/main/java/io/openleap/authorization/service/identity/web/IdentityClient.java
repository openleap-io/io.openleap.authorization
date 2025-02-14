package io.openleap.authorization.service.identity.web;

import io.openleap.authorization.controller.dto.UnregisterRequest;
import io.openleap.authorization.service.identity.FeignConfig;
import io.openleap.authorization.service.identity.dto.ClientPrincipalRequestDto;
import io.openleap.authorization.service.identity.dto.ClientPrincipalResponseDto;
import io.openleap.authorization.service.identity.dto.ProfileResponseDto;
import io.openleap.authorization.service.identity.dto.UserPrincipalResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "identity-service", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface IdentityClient {
    @GetMapping("/identity/userPrincipals/search/findByUsername")
    UserPrincipalResponseDto getUserPrincipalByUsername(@RequestParam String username);

    @GetMapping("/identity/userPrincipals/{profileId}/profiles")
    ProfileResponseDto getUserPrincipalProfilesByUserPrincipalId(@PathVariable String profileId);

    @GetMapping("/identity/clientPrincipals/search/findByClientId")
    ClientPrincipalResponseDto getClientPrincipalByClientId(@RequestParam String clientId);

    @GetMapping("/identity/clientPrincipals/{id}")
    ClientPrincipalResponseDto getClientPrincipalById(@PathVariable String id);

    @PostMapping("/identity/registration")
    ClientPrincipalResponseDto saveClient(@RequestBody ClientPrincipalRequestDto clientPrincipalRequestDto);

    @GetMapping("/identity/clientPrincipals/search/findByClientName")
    ClientPrincipalResponseDto getClientPrincipalByClientName(@RequestParam String clientName);

    @PostMapping("/identity/registration/unregister")
    void unregisterClient(@RequestBody UnregisterRequest unregisterRequest);

}

package io.openleap.authorization.service.identity.web;

import io.openleap.authorization.service.identity.dto.ClientPrincipalRequestDto;
import io.openleap.authorization.service.identity.dto.ClientPrincipalResponseDto;
import io.openleap.authorization.service.identity.dto.ProfileResponseDto;
import io.openleap.authorization.service.identity.dto.UserPrincipalResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "identity-service")
public interface IdentityClient {
    @GetMapping("/userPrincipals/search/findByUsername")
    UserPrincipalResponseDto getUserPrincipal(@RequestParam String username);

    @GetMapping("/userPrincipals/{profileId}/profiles")
    ProfileResponseDto getProfileResponseDto(@PathVariable String profileId);

    @GetMapping("/clientPrincipals/search/findByClientId")
    ClientPrincipalResponseDto getClientPrincipalResponseDto(@RequestParam String clientId);

    @GetMapping("/clientPrincipals/{id}")
    ClientPrincipalResponseDto getClientById(@PathVariable String id);

    @PostMapping("/clientPrincipals")
    ClientPrincipalResponseDto saveClient(@RequestBody ClientPrincipalRequestDto clientPrincipalRequestDto);
}

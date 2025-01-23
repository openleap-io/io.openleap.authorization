package io.openleap.authorization.service.identity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserPrincipalDto(String username, String password, Boolean disabled, Boolean locked,
                               Set<Profile> profiles) {
    public record Profile(String name, Boolean enabled, Boolean defaultProfile, Set<Role> roles) {
        public record Role(String name) {
        }
    }

}

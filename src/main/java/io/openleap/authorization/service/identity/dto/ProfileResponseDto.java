package io.openleap.authorization.service.identity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileResponseDto(@JsonProperty("_embedded") Embedded _embedded) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Embedded(Set<Profile> profiles) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Profile(String name, Boolean enabled, @JsonProperty("defaultProfile") Boolean defaultProfile, Set<Role> roles) {
            public record Role(String name) {
            }
        }
    }
}

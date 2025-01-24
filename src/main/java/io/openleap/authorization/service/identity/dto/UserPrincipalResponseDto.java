package io.openleap.authorization.service.identity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserPrincipalResponseDto(String id, String username, String password, Boolean disabled, Boolean locked,
                                       @JsonProperty("_links")Links _links) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Links(Profiles profiles) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Profiles(String href) {
        }
    }
}
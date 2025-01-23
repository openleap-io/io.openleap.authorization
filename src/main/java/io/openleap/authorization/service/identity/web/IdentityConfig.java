package io.openleap.authorization.service.identity.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("identity-web")
class IdentityConfig {

    @Bean
    IdentityHttpClient signingModuleHttpClient() {
        return new IdentityHttpClient("http://localhost:8081");
    }
}

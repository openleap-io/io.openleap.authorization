package io.openleap.authorization.config;

import io.openleap.authorization.provider.CustomAuthenticationProvider;
import io.openleap.authorization.provider.UserDetailsService;
import io.openleap.authorization.repository.CustomRegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer ->
                        authorizationServer
                                .oidc(oidc ->
                                        oidc.clientRegistrationEndpoint(clientRegistrationEndpoint ->
                                                clientRegistrationEndpoint
                                                        .authenticationProviders(CustomClientMetadataConfig.configureCustomClientMetadataConverters())
                                        )
                                )
                                .clientAuthentication(clientAuthentication ->
                                        clientAuthentication
                                                .authenticationProvider(customAuthenticationProvider())
                                )
                );
        return http.build();
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/internal/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasAuthority("SCOPE_client.create")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public SecurityFilterChain formLoginSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(Customizer.withDefaults());


        return http.build();
    }

    @Bean
    CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new CustomRegisteredClientRepository();
    }

}

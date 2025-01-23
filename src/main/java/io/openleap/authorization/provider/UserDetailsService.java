package io.openleap.authorization.provider;

import io.openleap.authorization.service.identity.IdentityService;
import io.openleap.authorization.service.identity.dto.UserPrincipalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private IdentityService identityService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserPrincipalDto userResponse;
        try {
            userResponse = findUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername(userResponse.username())
                .password(passwordEncoder.encode(userResponse.password()))
                .roles(userResponse.profiles().stream().filter(profile -> profile.enabled())
                        .map(profile -> profile.name()).toArray(String[]::new)
                )
                .authorities(
                        userResponse.profiles().stream().filter(profile -> profile.enabled())
                                .map(profile -> profile.roles().stream().map(role -> role.name()).toArray(String[]::new)).flatMap(Stream::of).toArray(String[]::new)
                )
                .build();
    }


    public UserPrincipalDto findUserByUsername(String username) {
        return identityService.getUserPrincipal(username);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

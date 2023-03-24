package org.scortesc.springcloud.msvc.oauth.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private WebClient.Builder client;

    private Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            org.scortesc.springcloud.msvc.oauth.models.entity.User user = client.build().get()
                    .uri("http://msvc-users/login", uri -> uri.queryParam("email", email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(org.scortesc.springcloud.msvc.oauth.models.entity.User.class)
                    .block();
            log.info (String.format("Usuario Logado: %s, %s, %s", user.getEmail(), user.getName(), user.getPassword()));
            return new User(email, user.getPassword(), true, true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        } catch (RuntimeException e) {
            String error = String.format("Error en el login. No existe el usuario %s en el sistema", email);
            log.error(error, e.getMessage());
            throw new UsernameNotFoundException(error);
        }
    }
}

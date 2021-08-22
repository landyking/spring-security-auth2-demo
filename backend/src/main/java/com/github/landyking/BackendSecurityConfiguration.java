package com.github.landyking;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity
public class BackendSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
//    String jwkSetUri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
//
//    @Bean
//    JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
//    }
}
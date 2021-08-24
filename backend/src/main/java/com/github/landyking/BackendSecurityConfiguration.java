package com.github.landyking;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BackendSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties, LoadBalancerInterceptor loadBalancerInterceptor) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(loadBalancerInterceptor));
        return NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri())
                .restOperations(restTemplate)
                .build();
    }
}
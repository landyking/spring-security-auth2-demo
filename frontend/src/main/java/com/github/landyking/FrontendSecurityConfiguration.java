package com.github.landyking;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
public class FrontendSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().permitAll()
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

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientRepository,
            LoadBalancerInterceptor loadBalancerInterceptor) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials(builder -> {
                            RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                                    new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
                            restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
                            restTemplate.setInterceptors(Collections.singletonList(loadBalancerInterceptor));
                            final DefaultClientCredentialsTokenResponseClient client = new DefaultClientCredentialsTokenResponseClient();
                            client.setRestOperations(restTemplate);
                            builder.accessTokenResponseClient(client);
                        })
                        .build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
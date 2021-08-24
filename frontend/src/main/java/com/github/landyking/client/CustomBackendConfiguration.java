package com.github.landyking.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

public class CustomBackendConfiguration {
    @Bean
    RequestInterceptor clientCredentialsRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return template -> {
            final OAuth2AuthorizedClient authorize = authorizedClientManager.authorize(OAuth2AuthorizeRequest
                    .withClientRegistrationId("my-client-credentials")
                    .principal(SecurityContextHolder.getContext().getAuthentication())
                    .build());
            final OAuth2AccessToken accessToken = authorize.getAccessToken();
            if (accessToken != null) {
                template.header("Authorization", accessToken.getTokenType().getValue() + " " + accessToken.getTokenValue());
            }
        };
    }
}

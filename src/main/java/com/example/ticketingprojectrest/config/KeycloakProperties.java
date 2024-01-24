package com.example.ticketingprojectrest.config;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class KeycloakProperties {
    Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${jwt.auth.converter.resource-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.external.client-secret}")
    private String clientSecret;
    @Value("${master.user}")
    private String masterUser;
    @Value("${master.user.password}")
    private String masterUserPswd;
    @Value("${master.realm}")
    private String masterRealm;
    @Value("${master.client}")
    private String masterClient;

//    public Keycloak getKeycloakInstance(){
//        if (keycloak==null){
//            keycloak = KeycloakBuilder
//                   .builder().serverUrl(authServerUrl).realm(realm).clientId(clientId).
//            Keycloak.getInstance()
//        }
    }


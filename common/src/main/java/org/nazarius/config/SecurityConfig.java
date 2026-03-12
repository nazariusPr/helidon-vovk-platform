package org.nazarius.config;

import io.helidon.config.Config;
import io.helidon.security.Security;
import io.helidon.security.providers.jwt.JwtProvider;
import io.helidon.security.util.TokenHandler;
import io.helidon.common.configurable.Resource;

public class SecurityConfig {

    public static Security createSecurity() {
        Config config = Config.create();

        // Load verification key
        String verifyKeyResource = config.get("security.providers.jwt.verify-key-resource")
                .asString()
                .orElseThrow(() -> new IllegalStateException("JWT verifying key resource not configured"));

        // Load issuer
        String issuer = config.get("security.providers.jwt.issuer")
                .asString()
                .orElse("helidon-jwt-demo");

        // Explicit Bearer token handler so JWT is taken from "Authorization: Bearer <token>"
        TokenHandler bearerHandler = TokenHandler.builder()
                .tokenHeader("Authorization")
                .tokenPrefix("Bearer ")
                .build();

        JwtProvider jwtProvider = JwtProvider.builder()
                .verifyJwk(Resource.create(verifyKeyResource))
                .verifySignature(true)
                .issuer(issuer)
                .atnTokenHandler(bearerHandler)
                .authenticate(true)
                .build();

        return Security.builder()
                .addProvider(jwtProvider)
                .authenticationProvider(jwtProvider)
                .build();
    }
}
package org.nazarius.service;

import io.helidon.config.Config;
import io.helidon.security.jwt.Jwt;
import io.helidon.security.jwt.SignedJwt;
import io.helidon.security.jwt.jwk.JwkKeys;
import io.helidon.common.configurable.Resource;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.nazarius.config.GeneralConfig.getConfig;


public class JwtService {

    private final JwkKeys signingKeys;
    private final String algorithm;
    private final String issuer;
    private final long expirationSeconds;

    /**
     * Load JWT configuration from application.properties and JWK resource
     */
    public JwtService() {
        Config config = getConfig();

        String signingKeyResource = config.get("security.providers.jwt.signing-key-resource")
                .asString()
                .orElseThrow(() -> new IllegalStateException("JWT signing key resource not configured"));

        this.signingKeys = JwkKeys.builder()
                .resource(Resource.create(signingKeyResource))
                .build();

        this.algorithm = config.get("security.providers.jwt.algorithm")
                .asString()
                .orElseThrow(() -> new IllegalStateException("Algorithm not configured"));

        this.issuer = config.get("security.providers.jwt.issuer")
                .asString()
                .orElse("helidon-jwt-demo");

        this.expirationSeconds = config.get("security.providers.jwt.expiration-seconds")
                .asLong()
                .orElse(3600L);
    }

    /**
     * Generate JWT with username only.
     */
    public String generateToken(String username) {
        return generateToken(username, Collections.emptyMap());
    }

    /**
     * Generate JWT with roles.
     */
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("groups", roles);

        return generateToken(username, claims);
    }

    /**
     * Generate JWT with extra claims.
     */
    public String generateToken(String username, Map<String, Object> extraClaims) {
        Jwt.Builder builder = Jwt.builder()
                .algorithm(algorithm)
                .keyId(signingKeys.keys().getFirst().keyId())
                .subject(username)
                .issuer(issuer)
                .expirationTime(Instant.now().plusSeconds(expirationSeconds));

        if (extraClaims != null) {
            extraClaims.forEach(builder::addPayloadClaim);
        }

        // Use the first key from the JWK set
        return SignedJwt.sign(builder.build(), signingKeys.keys().getFirst())
                .tokenContent();
    }
}
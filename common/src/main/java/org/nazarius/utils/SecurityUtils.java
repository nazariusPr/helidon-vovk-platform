package org.nazarius.utils;

import io.helidon.common.security.SecurityContext;
import io.helidon.security.Principal;
import io.helidon.webserver.http.ServerRequest;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
        // private constructor to prevent instantiation
    }

    /**
     * Returns the username of the authenticated user.
     *
     * @param req the server request
     * @return the authenticated username
     * @throws IllegalStateException if the principal is missing
     */
    public static String getUsername(ServerRequest req) {
        SecurityContext securityContext = req.context().get(SecurityContext.class).orElse(null);

        if (securityContext == null || !securityContext.isAuthenticated()) {
            throw new IllegalStateException("SecurityContext is not available");
        }

        // Get the authenticated user's principal safely
        Optional<Principal> optionalPrincipal = securityContext.userPrincipal();
        if (optionalPrincipal.isEmpty()) {
            throw new IllegalStateException("Principal name is empty");
        }

        Principal userPrincipal = optionalPrincipal.get();
        return userPrincipal.getName();
    }
}
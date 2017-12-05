package com.decipherzone.dropwizard.auth;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.util.CredentialsManager;
import io.dropwizard.auth.AuthFilter;

import javax.annotation.Priority;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Priority(Priorities.AUTHENTICATION)
public class AppAuthenticationFilter extends AuthFilter<String, Principal>{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!requestContext.getMethod().equals(HttpMethod.OPTIONS) && !isExcludedFromAuth(requestContext.getUriInfo().getPath())) {
                String token = requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (token == null) {
                    throw new BadRequestException("Authorization token not found");
                }

                if (requestContext.getUriInfo().getPath().equals("auth/logout")) {
                    if (CredentialsManager.INSTANCE.isTokenAbsent(token)) {
                        throw new BadRequestException("Token Not Found On Server");
                    }
                    CredentialsManager.INSTANCE.removeToken(token);
                } else {
                    if (!authenticate(requestContext, token, "CUSTOM")) {
                        throw new NotAuthorizedException("Not Authorized to access resource");
                    }
                }
        }
    }


    @Override
    public boolean authenticate(ContainerRequestContext requestContext, String token, String scheme) {
        if (CredentialsManager.INSTANCE.isTokenAbsent(token)) {
            throw new BadRequestException("Token Not Found On Server");
        }

        if (CredentialsManager.INSTANCE.isTokenTimedOut(token)) {
            CredentialsManager.INSTANCE.removeToken(token);
            throw new BadRequestException("Token Timed Out");
        }

        Optional<UserDetails> principal = CredentialsManager.INSTANCE.getLoggedInUser(token);

        if (!principal.isPresent()) {
            return false;
        }

        UserDetails user = principal.get();

        final SecurityContext securityContext = requestContext.getSecurityContext();
        final boolean secure = securityContext != null && securityContext.isSecure();

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return user;
            }

            @Override
            public boolean isUserInRole(String role) {
                return authorizer.authorize(user, role);
            }

            @Override
            public boolean isSecure() {
                return secure;
            }

            @Override
            public String getAuthenticationScheme() {
                return scheme;
            }
        });

        CredentialsManager.INSTANCE.updateLastAccessedTime(token);
        return true;
    }

    /**
     * This method contains url that don't need to authenticate
     * @param url : request url
     * @return : if url is in method then it will return true otherwise it will return false
     */
    private boolean isExcludedFromAuth(String url) {
        Set<String> staticExURLs = new HashSet<>();

        staticExURLs.add("swagger");
        staticExURLs.add("swagger.json");
        staticExURLs.add("auth/login");
        staticExURLs.add("auth/signup");

        return staticExURLs.contains(url)|| url.contains("/mapfeeds");
    }
}

package com.decipherzone.dropwizard.util;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum CredentialsManager {
    INSTANCE;

    private static final int TIME_OUT_MINUTES = 600;
    private static final Logger LOGGER = Logger.getLogger(CredentialsManager.class.getName());
    private ConcurrentHashMap<String, LoggedInUserDetails> tokenUserMap = new ConcurrentHashMap<>();

    public void addToken(String token, UserDetails user) {
        tokenUserMap.put(token, new LoggedInUserDetails(user));
    }

    public void updateLastAccessedTime(String token) {
        tokenUserMap.get(token).setTime(new DateTime());
    }

    public boolean isTokenAbsent(String token) {
        return tokenUserMap.get(token) == null;
    }

    /**
     * Getting token for already logged in user
     * @param email
     * @return token
     */
    public String getTokenIfUserAlreadyLoggedIn(String email) {
        String token = tokenUserMap.entrySet().stream().filter(loggedInUser -> loggedInUser.getValue().getUser().getEmail().equals(email)).map(entry -> entry.getKey()).collect(Collectors.joining(""));
        if (!token.isEmpty() && isTokenTimedOut(token)) {
            removeToken(token);
            token = "";
        }
        return token;
    }

    /**
     * Check token validation time
     * @param token
     * @return
     */
    public boolean isTokenTimedOut(String token) {
        if (tokenUserMap.get(token) == null) {
            return true;
        }
        DateTime currentTime = new DateTime();
        DateTime lastAccessedTime = tokenUserMap.get(token).getTime();
        return Minutes.minutesBetween(lastAccessedTime, currentTime).getMinutes() > TIME_OUT_MINUTES;
    }

    /**
     * Getting logged in user details
     * @param token
     * @return
     */
    public Optional<UserDetails> getLoggedInUser(String token) {
        if (tokenUserMap.get(token) != null) {
            return Optional.of(tokenUserMap.get(token).getUser());
        }
        return Optional.empty();
    }

    /**
     * Remove token
     * @param token
     */
    public void removeToken(String token) {
        tokenUserMap.remove(token);
    }

    private class LoggedInUserDetails {
        private final UserDetails user;
        private DateTime time;

        public LoggedInUserDetails(UserDetails user) {
            this.user = user;
            this.setTime(new DateTime());
        }

        public DateTime getTime() {
            return time;
        }

        public void setTime(DateTime time) {
            this.time = time;
        }

        public UserDetails getUser() {
            return user;
        }
    }

}

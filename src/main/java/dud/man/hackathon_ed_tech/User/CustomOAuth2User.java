package dud.man.hackathon_ed_tech.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final User user; // your DB User entity
    private final Map<String, Object> attributes; // OAuth2 attributes from GitHub

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // Expose the database User so you can generate JWT later
    public User getUser() {
        return user;
    }

    // OAuth2 attributes like "id", "login", "email"
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // Everyone gets ROLE_USER for simplicity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // This is used by Spring Security for the principal name
    @Override
    public String getName() {
        return user.getUsername();
    }
}
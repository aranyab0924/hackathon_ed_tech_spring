package dud.man.hackathon_ed_tech.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthorizationCodeStore authorizationCodeStore;

    public OAuth2SuccessHandler(AuthorizationCodeStore authorizationCodeStore) {
        this.authorizationCodeStore = authorizationCodeStore;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("githubId", oauthUser.getAttribute("id"));
        claims.put("username", oauthUser.getAttribute("login"));
        claims.put("email", oauthUser.getAttribute("email"));

        // Generate a short-lived authorization code
        String code = authorizationCodeStore.generateCode(claims);
        
        System.out.println("OAuth2 Success - Generated code: " + code);
        System.out.println("Redirecting to: https://aranyab0924.github.io/hackathon_ed_tech/?code=" + code);

        // Redirect to frontend with authorization code
        String redirectUrl = "http://127.0.0.1:4000/hackathon_ed_tech/?code=" + code;
        response.sendRedirect(redirectUrl);
    }
}
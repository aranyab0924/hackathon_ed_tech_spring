package dud.man.hackathon_ed_tech.security;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final Duration ttl;

    public OAuth2SuccessHandler(JwtService jwtService, @Value("${app.jwt.ttl-seconds:3600}") long ttlSeconds) {
        this.jwtService = jwtService;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("JWT", token)
            .httpOnly(true)
            .secure(request.isSecure())
            .path("/")
            .maxAge(ttl)
            .sameSite("Lax")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());

        response.sendRedirect("/");
    }
}

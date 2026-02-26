package dud.man.hackathon_ed_tech.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final Duration ttl;

    public JwtService(JwtEncoder jwtEncoder, @Value("${app.jwt.ttl-seconds:3600}") long ttlSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    public String generateToken(OAuth2User user) {
        Instant now = Instant.now();
        Map<String, Object> attributes = user.getAttributes();
        String name = (String) attributes.getOrDefault("name", attributes.getOrDefault("login", user.getName()));

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
            .issuer("hackathon_ed_tech")
            .issuedAt(now)
            .expiresAt(now.plus(ttl))
            .subject(user.getName())
            .claim("name", name);

        Object login = attributes.get("login");
        if (login != null) {
            builder.claim("login", login);
        }
        Object email = attributes.get("email");
        if (email != null) {
            builder.claim("email", email);
        }

        JwtClaimsSet claims = builder.build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}

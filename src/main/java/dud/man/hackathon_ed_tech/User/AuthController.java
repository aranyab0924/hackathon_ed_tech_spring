package dud.man.hackathon_ed_tech.User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthorizationCodeStore authorizationCodeStore;

    public AuthController(AuthorizationCodeStore authorizationCodeStore) {
        this.authorizationCodeStore = authorizationCodeStore;
    }

    @PostMapping("/api/auth/exchange")
    public Map<String, Object> exchangeCodeForToken(@RequestParam String code) {
        try {
            System.out.println("Received code: " + code);
            System.out.println("Available codes in store: " + authorizationCodeStore.getAllCodes().size());
            
            // Exchange authorization code for claims
            Map<String, Object> claims = authorizationCodeStore.exchangeCode(code);

            // Generate JWT token from claims
            String token = JwtUtil.generateToken(claims);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("success", true);
            return response;
        } catch (IllegalArgumentException e) {
            System.err.println("Error exchanging code: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
}

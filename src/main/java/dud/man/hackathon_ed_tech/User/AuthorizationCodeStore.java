package dud.man.hackathon_ed_tech.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationCodeStore {

    private static final long CODE_EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes
    private final Map<String, AuthorizationCode> store = new ConcurrentHashMap<>();

    public String generateCode(Map<String, Object> claims) {
        String code = UUID.randomUUID().toString();
        AuthorizationCode authCode = new AuthorizationCode(code, claims, System.currentTimeMillis() + CODE_EXPIRATION_TIME);
        store.put(code, authCode);
        System.out.println("Generated code: " + code + ", total codes: " + store.size());
        return code;
    }

    public Map<String, Object> exchangeCode(String code) {
        System.out.println("Exchanging code: " + code + ", available codes: " + store.keySet());
        
        AuthorizationCode authCode = store.get(code);
        
        if (authCode == null) {
            throw new IllegalArgumentException("Invalid authorization code");
        }
        
        if (System.currentTimeMillis() > authCode.expiresAt) {
            store.remove(code);
            throw new IllegalArgumentException("Authorization code expired");
        }
        
        // Code can only be used once
        store.remove(code);
        return authCode.claims;
    }

    public Map<String, AuthorizationCode> getAllCodes() {
        return store;
    }

    public static class AuthorizationCode {
        public String code;
        public Map<String, Object> claims;
        public long expiresAt;

        public AuthorizationCode(String code, Map<String, Object> claims, long expiresAt) {
            this.code = code;
            this.claims = claims;
            this.expiresAt = expiresAt;
        }
    }
}

package dud.man.hackathon_ed_tech;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HackathonEdTechApplication {

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		if (principal == null) {
			return Collections.singletonMap("error", "Not authenticated");
		}
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}

	@GetMapping("/csrf")
	public Map<String, String> csrf(CsrfToken token) {
		if (token == null) {
			return Collections.singletonMap("error", "CSRF token not available");
		}
		return Collections.singletonMap("token", token.getToken());
	}


	public static void main(String[] args) {
		SpringApplication.run(HackathonEdTechApplication.class, args);
	}

}

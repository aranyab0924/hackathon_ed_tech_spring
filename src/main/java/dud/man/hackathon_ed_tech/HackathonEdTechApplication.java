package dud.man.hackathon_ed_tech;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HackathonEdTechApplication {

	@GetMapping("/user")
		public Map<String, Object> user(Authentication authentication) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof OAuth2User oauth2User) {
				System.out.println("User info: " + oauth2User.getAttributes());
				String name = oauth2User.getAttribute("name");
				if (name == null) {
					name = oauth2User.getAttribute("login");
				}
				return Collections.singletonMap("name", name);
			}
			if (principal instanceof Jwt jwt) {
				String name = jwt.getClaimAsString("name");
				if (name == null) {
					name = jwt.getSubject();
				}
				return Collections.singletonMap("name", name);
			}
			return Collections.singletonMap("name", authentication.getName());
		}

	@GetMapping("/csrf")
	public Map<String, String> csrf(CsrfToken token) {
		return Collections.singletonMap("token", token.getToken());
	}


	public static void main(String[] args) {
		SpringApplication.run(HackathonEdTechApplication.class, args);
	}

}

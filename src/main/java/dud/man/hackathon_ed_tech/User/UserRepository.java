package dud.man.hackathon_ed_tech.User;

public interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
    java.util.Optional<User> findByGithubId(String githubId);
    
}

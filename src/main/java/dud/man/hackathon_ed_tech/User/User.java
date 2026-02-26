package dud.man.hackathon_ed_tech.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String githubId;
    private String username;

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    public String getGithubId() {
        return githubId;
    }
    public Object getId() {
        return id;
    }
}
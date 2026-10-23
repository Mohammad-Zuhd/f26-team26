package _Project.RecLeague_API.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Represents a Team Captain account in RecLeague.
// Team Captains can manage teams, and SysAdmin can manage their account access.
@Entity
@Table(name = "team_captains")
public class TeamCaptain {

    // The database automatically creates the ID.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Captain's full name.
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    // Email must be valid and unique.
    @NotBlank
    @Email
    @Size(max = 254)
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    // Password is write-only so it is not returned in API JSON responses.
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Name of the team this captain manages.
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String teamName;

    // ACTIVE or SUSPENDED.
    // New captains start as ACTIVE.
    // SysAdmin can later change this value to control account access.
    @Column(nullable = false, length = 30)
    private String accountStatus;

    // JPA needs an empty constructor.
    public TeamCaptain() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
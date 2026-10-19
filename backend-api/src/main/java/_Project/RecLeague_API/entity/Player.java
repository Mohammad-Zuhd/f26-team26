package _Project.RecLeague_API.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// A Player is the "customer" in our app, someone looking to join a rec team.
// @Entity tells Spring to save Player objects in the database,
// and @Table names the table "players".
@Entity
@Table(name = "players")
public class Player {

    // The database picks the ID automatically (1, 2, 3...), so we never set it ourselves
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank = the API sends back a 400 error if this is missing or empty
    // @Column(nullable = false) = the database column can't be empty either
    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    // unique = true means two players can't sign up with the same email
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    // These next fields come from US-1 in our SRS (location, sports, skill level, availability)
    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 100)
    private String location;

    // Stored as one string, like "Soccer, Basketball, etc"
    @NotBlank(message = "Preferred sports are required")
    @Column(nullable = false)
    private String preferredSports;

    // Beginner, Intermediate, or Advanced
    @NotBlank(message = "Skill level is required")
    @Column(nullable = false, length = 30)
    private String skillLevel;

    // Optional, like "Weeknights after 6pm"
    private String availability;

    // ACTIVE or SUSPENDED. The service sets this to ACTIVE when a player signs up,
    // and only the SysAdmin side should change it (for banning/suspending accounts)
    @Column(nullable = false, length = 30)
    private String accountStatus;

    // JPA needs an empty constructor so it can build Player objects from database rows
    public Player() {
    }

    // Getters and setters so Spring can read and write each field
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPreferredSports() { return preferredSports; }
    public void setPreferredSports(String preferredSports) { this.preferredSports = preferredSports; }

    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
}

package _Project.RecLeague_API.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a scheduled recreational sports game.
 */
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Team participating in the game.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @Column(nullable = false)
    private LocalDate gameDate;

    @NotNull
    @Column(nullable = false)
    private LocalTime gameTime;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String location;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String status = "SCHEDULED";

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public LocalTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(LocalTime gameTime) {
        this.gameTime = gameTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

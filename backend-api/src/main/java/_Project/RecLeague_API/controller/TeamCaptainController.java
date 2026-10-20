
package _Project.RecLeague_API.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.dto.TeamCaptainRegistrationRequest;
import _Project.RecLeague_API.dto.TeamCaptainResponse;
import _Project.RecLeague_API.dto.TeamCaptainUpdateRequest;
import _Project.RecLeague_API.dto.TeamRequest;
import _Project.RecLeague_API.dto.TeamResponse;
import _Project.RecLeague_API.service.TeamCaptainService;
import _Project.RecLeague_API.service.TeamService;
import jakarta.validation.Valid;

/**
 * Handles API requests for Team Captain accounts and teams.
 */
@RestController
@RequestMapping("/api/team-captains")
public class TeamCaptainController {

    private final TeamCaptainService captainService;
    private final TeamService teamService;

    public TeamCaptainController(TeamCaptainService captainService,
                                 TeamService teamService) {
        this.captainService = captainService;
        this.teamService = teamService;
    }

    // Register a new Team Captain.
    @PostMapping
    public ResponseEntity<TeamCaptainResponse> register(
            @Valid @RequestBody TeamCaptainRegistrationRequest request) {

        TeamCaptainResponse captain = captainService.register(request);
        return ResponseEntity.created(
                URI.create("/api/team-captains/" + captain.id())).body(captain);
    }

    // Get a Team Captain profile.
    @GetMapping("/{captainId}")
    public TeamCaptainResponse getProfile(@PathVariable Long captainId) {
        return captainService.getProfile(captainId);
    }

    // Update a Team Captain profile.
    @PutMapping("/{captainId}")
    public TeamCaptainResponse updateProfile(
            @PathVariable Long captainId,
            @Valid @RequestBody TeamCaptainUpdateRequest request) {

        return captainService.updateProfile(captainId, request);
    }

    // Create a team for a Team Captain.
    @PostMapping("/{captainId}/teams")
    public ResponseEntity<TeamResponse> createTeam(
            @PathVariable Long captainId,
            @Valid @RequestBody TeamRequest request) {

        TeamResponse team = teamService.create(captainId, request);

        return ResponseEntity.created(
                URI.create("/api/team-captains/" + captainId
                        + "/teams/" + team.id())).body(team);
    }

    // View all teams managed by a Team Captain.
    @GetMapping("/{captainId}/teams")
    public List<TeamResponse> getTeams(@PathVariable Long captainId) {
        return teamService.listTeams(captainId);
    }

    // Update a team managed by a Team Captain.
    @PutMapping("/{captainId}/teams/{teamId}")
    public TeamResponse updateTeam(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRequest request) {

        return teamService.update(captainId, teamId, request);
    }
}

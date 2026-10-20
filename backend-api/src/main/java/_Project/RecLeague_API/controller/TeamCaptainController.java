
package _Project.RecLeague_API.controller;

import java.net.URI;

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
import _Project.RecLeague_API.service.TeamCaptainService;
import jakarta.validation.Valid;

/**
 * Handles API requests for Team Captain accounts and profiles.
 */
@RestController
@RequestMapping("/api/team-captains")
public class TeamCaptainController {

    private final TeamCaptainService captainService;

    public TeamCaptainController(TeamCaptainService captainService) {
        this.captainService = captainService;
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
}

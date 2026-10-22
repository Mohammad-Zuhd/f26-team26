
package _Project.RecLeague_API.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.dto.JoinRequestResponse;
import _Project.RecLeague_API.dto.OpenSpotRequest;
import _Project.RecLeague_API.dto.OpenSpotResponse;
import _Project.RecLeague_API.dto.TeamCaptainRegistrationRequest;
import _Project.RecLeague_API.dto.TeamCaptainResponse;
import _Project.RecLeague_API.dto.TeamCaptainUpdateRequest;
import _Project.RecLeague_API.dto.TeamRequest;
import _Project.RecLeague_API.dto.TeamResponse;
import _Project.RecLeague_API.service.OpenSpotService;
import _Project.RecLeague_API.service.RosterService;
import _Project.RecLeague_API.service.TeamCaptainService;
import _Project.RecLeague_API.service.TeamService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/team-captains")
public class TeamCaptainController {

    private final TeamCaptainService captainService;
    private final TeamService teamService;
    private final OpenSpotService openSpotService;
    private final RosterService rosterService;

    public TeamCaptainController(
            TeamCaptainService captainService,
            TeamService teamService,
            OpenSpotService openSpotService,
            RosterService rosterService) {

        this.captainService = captainService;
        this.teamService = teamService;
        this.openSpotService = openSpotService;
        this.rosterService = rosterService;
    }

    // Register a Team Captain.
    @PostMapping
    public ResponseEntity<TeamCaptainResponse> register(
            @Valid @RequestBody TeamCaptainRegistrationRequest request) {

        TeamCaptainResponse captain = captainService.register(request);

        return ResponseEntity.created(
                URI.create("/api/team-captains/" + captain.id()))
                .body(captain);
    }

    // View Team Captain profile.
    @GetMapping("/{captainId}")
    public TeamCaptainResponse getProfile(
            @PathVariable Long captainId) {

        return captainService.getProfile(captainId);
    }

    // Update Team Captain profile.
    @PutMapping("/{captainId}")
    public TeamCaptainResponse updateProfile(
            @PathVariable Long captainId,
            @Valid @RequestBody TeamCaptainUpdateRequest request) {

        return captainService.updateProfile(captainId, request);
    }

    // Create a team.
    @PostMapping("/{captainId}/teams")
    public ResponseEntity<TeamResponse> createTeam(
            @PathVariable Long captainId,
            @Valid @RequestBody TeamRequest request) {

        TeamResponse team = teamService.create(captainId, request);

        return ResponseEntity.created(
                URI.create("/api/team-captains/" + captainId
                        + "/teams/" + team.id()))
                .body(team);
    }

    // View the captain's teams.
    @GetMapping("/{captainId}/teams")
    public List<TeamResponse> getTeams(
            @PathVariable Long captainId) {

        return teamService.listTeams(captainId);
    }

    // Update a team.
    @PutMapping("/{captainId}/teams/{teamId}")
    public TeamResponse updateTeam(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRequest request) {

        return teamService.update(captainId, teamId, request);
    }

    // Post an open roster spot.
    @PostMapping("/{captainId}/teams/{teamId}/spots")
    public ResponseEntity<OpenSpotResponse> createOpenSpot(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @Valid @RequestBody OpenSpotRequest request) {

        OpenSpotResponse spot =
                openSpotService.create(captainId, teamId, request);

        return ResponseEntity.created(
                URI.create("/api/team-captains/" + captainId
                        + "/teams/" + teamId
                        + "/spots/" + spot.id()))
                .body(spot);
    }

    // View a team's open spot postings.
    @GetMapping("/{captainId}/teams/{teamId}/spots")
    public List<OpenSpotResponse> getOpenSpots(
            @PathVariable Long captainId,
            @PathVariable Long teamId) {

        return openSpotService.listSpots(captainId, teamId);
    }

    // Update an open spot.
    @PutMapping("/{captainId}/teams/{teamId}/spots/{spotId}")
    public OpenSpotResponse updateOpenSpot(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long spotId,
            @Valid @RequestBody OpenSpotRequest request) {

        return openSpotService.update(
                captainId, teamId, spotId, request);
    }

    // Close an open spot.
    @PatchMapping("/{captainId}/teams/{teamId}/spots/{spotId}/close")
    public OpenSpotResponse closeOpenSpot(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long spotId) {

        return openSpotService.close(captainId, teamId, spotId);
    }

    // View pending join requests for a team.
    @GetMapping("/{captainId}/teams/{teamId}/join-requests")
    public List<JoinRequestResponse> getPendingRequests(
            @PathVariable Long captainId,
            @PathVariable Long teamId) {

        return rosterService.pendingRequests(captainId, teamId);
    }

    // View approved team members.
    @GetMapping("/{captainId}/teams/{teamId}/roster")
    public List<JoinRequestResponse> getRoster(
            @PathVariable Long captainId,
            @PathVariable Long teamId) {

        return rosterService.roster(captainId, teamId);
    }

    // Approve a player's request.
    @PatchMapping("/{captainId}/teams/{teamId}/join-requests/{requestId}/approve")
    public JoinRequestResponse approveJoinRequest(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long requestId) {

        return rosterService.approve(captainId, teamId, requestId);
    }

    // Decline a player's request.
    @PatchMapping("/{captainId}/teams/{teamId}/join-requests/{requestId}/decline")
    public JoinRequestResponse declineJoinRequest(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long requestId) {

        return rosterService.decline(captainId, teamId, requestId);
    }
}

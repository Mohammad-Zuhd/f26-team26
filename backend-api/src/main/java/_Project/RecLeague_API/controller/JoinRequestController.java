package _Project.RecLeague_API.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.dto.JoinRequestRequest;
import _Project.RecLeague_API.dto.JoinRequestResponse;
import _Project.RecLeague_API.service.RosterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/players")
public class JoinRequestController {

    private final RosterService rosterService;

    public JoinRequestController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    // the Player submits a request to join a team.
    @PostMapping("/{playerId}/teams/{teamId}/join-requests")
    public ResponseEntity<JoinRequestResponse> requestToJoin(
            @PathVariable Long playerId,
            @PathVariable Long teamId,
            @Valid @RequestBody JoinRequestRequest request) {

        JoinRequestResponse response =
                rosterService.requestToJoin(playerId, teamId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Player views their own join requests which would be --> 200
    // (and the approved ones are the teams they're on)
    @GetMapping("/{playerId}/join-requests")
    public List<JoinRequestResponse> getMyRequests(
            @PathVariable Long playerId) {

        return rosterService.playerRequests(playerId);
    }

    // when the Player cancels a pending join request -> 204
    @DeleteMapping("/{playerId}/join-requests/{requestId}")
    public ResponseEntity<Void> cancelRequest(
            @PathVariable Long playerId,
            @PathVariable Long requestId) {

        rosterService.cancelRequest(playerId, requestId);
        return ResponseEntity.noContent().build();
    }
}
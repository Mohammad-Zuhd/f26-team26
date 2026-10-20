
package _Project.RecLeague_API.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Player submits a request to join a team.
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
}

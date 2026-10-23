
package _Project.RecLeague_API.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.service.TeamService;

/**
 * Handles team deletion for Team Captains.
 */
@RestController
@RequestMapping("/api/team-captains")
public class TeamDeletionController {

    private final TeamService teamService;

    public TeamDeletionController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Delete a team managed by the specified Team Captain.
    @DeleteMapping("/{captainId}/teams/{teamId}")
    public ResponseEntity<Void> deleteTeam(
            @PathVariable Long captainId,
            @PathVariable Long teamId) {

        teamService.delete(captainId, teamId);

        return ResponseEntity.noContent().build();
    }
}

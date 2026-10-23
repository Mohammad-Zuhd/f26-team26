package _Project.RecLeague_API.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.service.SysAdminService;

// This controller creates the API endpoints used by the SysAdmin.
// The controller receives requests and sends the work to SysAdminService.
@RestController
@RequestMapping("/api/sysadmin")
public class SysAdminController {

    private final SysAdminService sysAdminService;

    // Spring automatically gives this controller access to SysAdminService.
    // This is called constructor injection.
    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    // ---------------- MANAGE USER ACCESS ----------------

    // Return all Player accounts.
    // GET /api/sysadmin/players
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return sysAdminService.getAllPlayers();
    }

    // Return all Team Captain accounts.
    // GET /api/sysadmin/captains
    @GetMapping("/captains")
    public List<TeamCaptain> getAllTeamCaptains() {
        return sysAdminService.getAllTeamCaptains();
    }

    // Change a Player account status to ACTIVE or SUSPENDED.
    // Example:
    // PUT /api/sysadmin/players/1/status?status=SUSPENDED
    @PutMapping("/players/{id}/status")
    public Player updatePlayerStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return sysAdminService.updatePlayerStatus(id, status);
    }

    // ---------------- MODERATE SERVICES ----------------

    // Return all team listings for the SysAdmin to review.
    // GET /api/sysadmin/teams
    @GetMapping("/teams")
    public List<Team> getAllTeams() {
        return sysAdminService.getAllTeams();
    }

    // Remove a team listing.
    // DELETE /api/sysadmin/teams/{id}
    // A successful delete returns HTTP 204 No Content.
    @DeleteMapping("/teams/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        sysAdminService.deleteTeam(id);
    }

    // ---------------- VIEW USAGE STATISTICS ----------------

    // Return counts of players, captains, total users, and teams.
    // GET /api/sysadmin/statistics
    @GetMapping("/statistics")
    public Map<String, Long> getUsageStatistics() {
        return sysAdminService.getUsageStatistics();
    }
}
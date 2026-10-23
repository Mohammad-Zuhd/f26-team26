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
import _Project.RecLeague_API.entity.Review;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.service.SysAdminService;

// Handles all SysAdmin API requests.
@RestController
@RequestMapping("/api/sysadmin")
public class SysAdminController {

    private final SysAdminService sysAdminService;

    // Spring gives the controller the SysAdmin service automatically.
    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    // ---------------- MANAGE USER ACCESS ----------------

    // View all Player accounts.
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return sysAdminService.getAllPlayers();
    }

    // View all Team Captain accounts.
    @GetMapping("/captains")
    public List<TeamCaptain> getAllTeamCaptains() {
        return sysAdminService.getAllTeamCaptains();
    }

    // Activate or suspend a Player account.
    // Example:
    // PUT /api/sysadmin/players/1/status?status=SUSPENDED
    @PutMapping("/players/{id}/status")
    public Player updatePlayerStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return sysAdminService.updatePlayerStatus(id, status);
    }

    // Activate or suspend a Team Captain account.
    // Example:
    // PUT /api/sysadmin/captains/1/status?status=SUSPENDED
    @PutMapping("/captains/{id}/status")
    public TeamCaptain updateCaptainStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return sysAdminService.updateCaptainStatus(id, status);
    }

    // Remove a Player account.
    @DeleteMapping("/players/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        sysAdminService.deletePlayer(id);
    }

    // Remove a Team Captain account.
    // Any teams owned by the captain are safely removed first.
    @DeleteMapping("/captains/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCaptain(@PathVariable Long id) {
        sysAdminService.deleteCaptain(id);
    }

    // ---------------- MODERATE SERVICES ----------------

    // View all team listings.
    @GetMapping("/teams")
    public List<Team> getAllTeams() {
        return sysAdminService.getAllTeams();
    }

    // Remove a team listing.
    @DeleteMapping("/teams/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        sysAdminService.deleteTeam(id);
    }

    // ---------------- MODERATE REVIEWS ----------------

    // View all reviews.
    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return sysAdminService.getAllReviews();
    }

    // Remove an inappropriate or misleading review.
    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        sysAdminService.deleteReview(id);
    }

    // ---------------- VIEW USAGE STATISTICS ----------------

    // View basic RecLeague usage statistics.
    @GetMapping("/statistics")
    public Map<String, Long> getUsageStatistics() {
        return sysAdminService.getUsageStatistics();
    }
}
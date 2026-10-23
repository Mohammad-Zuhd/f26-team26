package _Project.RecLeague_API.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.Review;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.repository.JoinRequestRepository;
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.ReviewRepository;
import _Project.RecLeague_API.repository.TeamCaptainRepository;
import _Project.RecLeague_API.repository.TeamRepository;

// This class contains the backend logic for SysAdmin features.
// The controller calls this service, and this service uses
// the repositories and existing services to manage RecLeague data.
@Service
@Transactional
public class SysAdminService {

    private final PlayerRepository playerRepository;
    private final TeamCaptainRepository captainRepository;
    private final TeamRepository teamRepository;
    private final ReviewRepository reviewRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final TeamService teamService;

    // Spring automatically gives this service everything it needs.
    public SysAdminService(
            PlayerRepository playerRepository,
            TeamCaptainRepository captainRepository,
            TeamRepository teamRepository,
            ReviewRepository reviewRepository,
            JoinRequestRepository joinRequestRepository,
            TeamService teamService) {

        this.playerRepository = playerRepository;
        this.captainRepository = captainRepository;
        this.teamRepository = teamRepository;
        this.reviewRepository = reviewRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.teamService = teamService;
    }

    // ---------------- MANAGE USER ACCESS ----------------

    // Get all Player accounts so the SysAdmin can view and manage them.
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Get all Team Captain accounts so the SysAdmin can view and manage them.
    @Transactional(readOnly = true)
    public List<TeamCaptain> getAllTeamCaptains() {
        return captainRepository.findAll();
    }

    // Change a Player account status.
    // Only ACTIVE and SUSPENDED are allowed.
    public Player updatePlayerStatus(Long playerId, String status) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player " + playerId + " was not found."));

        String newStatus = validateAccountStatus(status);

        player.setAccountStatus(newStatus);

        return playerRepository.save(player);
    }

    // Change a Team Captain account status.
    // SysAdmin can activate or suspend a captain account.
    public TeamCaptain updateCaptainStatus(Long captainId, String status) {

        TeamCaptain captain = captainRepository.findById(captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team Captain " + captainId + " was not found."));

        String newStatus = validateAccountStatus(status);

        captain.setAccountStatus(newStatus);

        return captainRepository.save(captain);
    }

    // Remove a Player account.
    // Reviews and join requests reference the Player,
    // so those records must be removed first.
    public void deletePlayer(Long playerId) {

        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Player " + playerId + " was not found.");
        }

        // Remove records that depend on this Player.
        reviewRepository.deleteByPlayerId(playerId);
        joinRequestRepository.deleteByPlayerId(playerId);

        // Now the Player can safely be removed.
        playerRepository.deleteById(playerId);
    }

    // Remove a Team Captain account.
    // A captain may own one or more teams, so each team must be
    // safely removed before deleting the captain account.
    public void deleteCaptain(Long captainId) {

        if (!captainRepository.existsById(captainId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Team Captain " + captainId + " was not found.");
        }

        // Get all teams managed by this captain.
        List<Team> captainTeams =
                teamRepository.findAllByCaptainIdOrderById(captainId);

        // Use the existing TeamService deletion logic.
        // It already removes reviews, join requests,
        // open spots, and games before deleting a team.
        for (Team team : captainTeams) {
            teamService.delete(captainId, team.getId());
        }

        // After the captain's teams are removed,
        // the captain account can safely be deleted.
        captainRepository.deleteById(captainId);
    }

    // Make sure an account status is valid.
    private String validateAccountStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Account status is required.");
        }

        String newStatus = status.trim().toUpperCase();

        if (!newStatus.equals("ACTIVE")
                && !newStatus.equals("SUSPENDED")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Account status must be ACTIVE or SUSPENDED.");
        }

        return newStatus;
    }

    // ---------------- MODERATE SERVICES ----------------

    // Get all team listings so the SysAdmin can review them.
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Remove a team listing safely.
    // TeamService already handles deleting all related records.
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team " + teamId + " was not found."));

        Long captainId = team.getCaptain().getId();

        teamService.delete(captainId, teamId);
    }

    // ---------------- MODERATE REVIEWS ----------------

    // Get all reviews so the SysAdmin can review reported
    // or inappropriate content.
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Remove a review by ID.
    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Review " + reviewId + " was not found.");
        }

        reviewRepository.deleteById(reviewId);
    }

    // ---------------- VIEW USAGE STATISTICS ----------------

    // Count the current users, teams, and reviews in the database.
    @Transactional(readOnly = true)
    public Map<String, Long> getUsageStatistics() {

        long playerCount = playerRepository.count();
        long captainCount = captainRepository.count();
        long teamCount = teamRepository.count();
        long reviewCount = reviewRepository.count();

        long totalUsers = playerCount + captainCount;

        Map<String, Long> statistics = new LinkedHashMap<>();

        statistics.put("players", playerCount);
        statistics.put("teamCaptains", captainCount);
        statistics.put("totalUsers", totalUsers);
        statistics.put("teams", teamCount);
        statistics.put("reviews", reviewCount);

        return statistics;
    }
}
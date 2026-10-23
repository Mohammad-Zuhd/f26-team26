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
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.ReviewRepository;
import _Project.RecLeague_API.repository.TeamCaptainRepository;
import _Project.RecLeague_API.repository.TeamRepository;

// This class contains the backend logic for SysAdmin features.
// The controller calls this service, and this service uses
// the repositories to read or change data in the database.
@Service
@Transactional
public class SysAdminService {

    // These repositories let the SysAdmin service access
    // players, team captains, teams, and reviews stored in PostgreSQL.
    private final PlayerRepository playerRepository;
    private final TeamCaptainRepository captainRepository;
    private final TeamRepository teamRepository;
    private final ReviewRepository reviewRepository;

    // Spring automatically gives this service the repositories it needs.
    // This is called constructor injection.
    public SysAdminService(
            PlayerRepository playerRepository,
            TeamCaptainRepository captainRepository,
            TeamRepository teamRepository,
            ReviewRepository reviewRepository) {

        this.playerRepository = playerRepository;
        this.captainRepository = captainRepository;
        this.teamRepository = teamRepository;
        this.reviewRepository = reviewRepository;
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
    // SysAdmin can use this to activate or suspend an account.
    // Only ACTIVE and SUSPENDED are allowed.
    public Player updatePlayerStatus(Long playerId, String status) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player " + playerId + " was not found."));

        String newStatus = status.trim().toUpperCase();

        if (!newStatus.equals("ACTIVE") && !newStatus.equals("SUSPENDED")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Account status must be ACTIVE or SUSPENDED.");
        }

        player.setAccountStatus(newStatus);

        return playerRepository.save(player);
    }

    // ---------------- MODERATE SERVICES ----------------

    // Get all team listings so the SysAdmin can review them.
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Remove a team listing by ID.
    // If the team does not exist, return a 404 error.
    public void deleteTeam(Long teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Team " + teamId + " was not found.");
        }

        teamRepository.deleteById(teamId);
    }

    // ---------------- MODERATE REVIEWS ----------------

    // Get all reviews so the SysAdmin can review reported
    // or inappropriate content.
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Remove a review by ID.
    // If the review does not exist, return a 404 error.
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
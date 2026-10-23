package _Project.RecLeague_API.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.TeamCaptainRepository;
import _Project.RecLeague_API.repository.TeamRepository;

// This class contains the backend logic for SysAdmin features.
// The controller calls this service, and this service uses
// the repositories to read or change data in the database.
@Service
@Transactional
public class SysAdminService {

    // These repositories let the SysAdmin service access
    // players, team captains, and teams stored in PostgreSQL.
    private final PlayerRepository playerRepository;
    private final TeamCaptainRepository captainRepository;
    private final TeamRepository teamRepository;

    // Spring automatically gives this service the repositories it needs.
    // This is called constructor injection.
    public SysAdminService(
            PlayerRepository playerRepository,
            TeamCaptainRepository captainRepository,
            TeamRepository teamRepository) {

        this.playerRepository = playerRepository;
        this.captainRepository = captainRepository;
        this.teamRepository = teamRepository;
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

    // ---------------- VIEW USAGE STATISTICS ----------------

    // Count the current users and teams in the database.
    // LinkedHashMap keeps the statistics in the same order
    // when they are returned as JSON.
    @Transactional(readOnly = true)
    public Map<String, Long> getUsageStatistics() {

        long playerCount = playerRepository.count();
        long captainCount = captainRepository.count();
        long teamCount = teamRepository.count();
        long totalUsers = playerCount + captainCount;

        Map<String, Long> statistics = new LinkedHashMap<>();

        statistics.put("players", playerCount);
        statistics.put("teamCaptains", captainCount);
        statistics.put("totalUsers", totalUsers);
        statistics.put("teams", teamCount);

        return statistics;
    }
}
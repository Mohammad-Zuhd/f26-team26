package _Project.RecLeague_API.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.TeamCaptainRepository;
import _Project.RecLeague_API.repository.TeamRepository;

// This class contains the backend logic for SysAdmin features.
// The controller will call this service, and this service will
// use the repositories to read or change data in the database.
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
}
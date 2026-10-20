
package _Project.RecLeague_API.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.TeamRequest;
import _Project.RecLeague_API.dto.TeamResponse;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.repository.TeamCaptainRepository;
import _Project.RecLeague_API.repository.TeamRepository;

/**
 * Handles team creation and management for Team Captains.
 */
@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamCaptainRepository captainRepository;

    public TeamService(TeamRepository teamRepository,
                       TeamCaptainRepository captainRepository) {
        this.teamRepository = teamRepository;
        this.captainRepository = captainRepository;
    }

    // Create a new team for a Team Captain.
    public TeamResponse create(Long captainId, TeamRequest request) {

        TeamCaptain captain = findCaptain(captainId);

        Team team = new Team();
        team.setCaptain(captain);
        apply(team, request);

        return TeamResponse.from(teamRepository.save(team));
    }

    // View all teams managed by a Team Captain.
    @Transactional(readOnly = true)
    public List<TeamResponse> listTeams(Long captainId) {

        findCaptain(captainId);

        return teamRepository.findAllByCaptainIdOrderById(captainId).stream().map(TeamResponse::from).toList();
    }

    // Update an existing team belonging to a Team Captain.
    public TeamResponse update(Long captainId, Long teamId,
                               TeamRequest request) {

        Team team = teamRepository.findByIdAndCaptainId(teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team " + teamId + " was not found for this captain."));

        apply(team, request);

        return TeamResponse.from(teamRepository.save(team));
    }

    // Apply team information from a request.
    private void apply(Team team, TeamRequest request) {

        team.setTeamName(request.teamName().trim());
        team.setSport(request.sport().trim());
        team.setSkillLevel(request.skillLevel().trim());
        team.setLocation(request.location().trim());
        team.setSchedule(request.schedule().trim());
        team.setDescription(request.description());
    }

    // Find the Team Captain who manages the team.
    private TeamCaptain findCaptain(Long captainId) {

        return captainRepository.findById(captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team Captain " + captainId + " was not found."));
    }
}

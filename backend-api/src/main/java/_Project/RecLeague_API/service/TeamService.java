
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
import _Project.RecLeague_API.repository.JoinRequestRepository;
import _Project.RecLeague_API.repository.OpenSpotRepository;
import _Project.RecLeague_API.repository.ReviewRepository;
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
    private final OpenSpotRepository openSpotRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final ReviewRepository reviewRepository;

    public TeamService(
            TeamRepository teamRepository,
            TeamCaptainRepository captainRepository,
            OpenSpotRepository openSpotRepository,
            JoinRequestRepository joinRequestRepository,
            ReviewRepository reviewRepository) {

        this.teamRepository = teamRepository;
        this.captainRepository = captainRepository;
        this.openSpotRepository = openSpotRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.reviewRepository = reviewRepository;
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

        return teamRepository.findAllByCaptainIdOrderById(captainId)
                .stream()
                .map(TeamResponse::from)
                .toList();
    }

    // Update an existing team belonging to a Team Captain.
    public TeamResponse update(
            Long captainId,
            Long teamId,
            TeamRequest request) {

        Team team = teamRepository.findByIdAndCaptainId(
                        teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team " + teamId
                                + " was not found for this captain."));

        apply(team, request);

        return TeamResponse.from(teamRepository.save(team));
    }

    // Delete a team belonging to a Team Captain.
    public void delete(Long captainId, Long teamId) {

        // Verify that the team belongs to the specified captain.
        Team team = teamRepository.findByIdAndCaptainId(
                        teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team " + teamId
                                + " was not found for this captain."));

        // Remove related records before deleting the team.
        reviewRepository.deleteByTeamId(teamId);

        joinRequestRepository.deleteByTeamId(teamId);

        openSpotRepository.deleteByTeamId(teamId);

        // Finally, delete the team itself.
        teamRepository.delete(team);

        // Execute the delete before the transaction completes.
        teamRepository.flush();
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
                        "Team Captain " + captainId
                                + " was not found."));
    }
}

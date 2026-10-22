
package _Project.RecLeague_API.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.JoinRequestRequest;
import _Project.RecLeague_API.dto.JoinRequestResponse;
import _Project.RecLeague_API.entity.JoinRequest;
import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.repository.JoinRequestRepository;
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.TeamRepository;

@Service
@Transactional
public class RosterService {

    private final JoinRequestRepository requestRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public RosterService(
            JoinRequestRepository requestRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {

        this.requestRepository = requestRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // Player submits a request to join a team.
    public JoinRequestResponse requestToJoin(
            Long playerId, Long teamId, JoinRequestRequest request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found."));

        if (!"ACTIVE".equals(player.getAccountStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Player account is not active.");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found."));

        boolean alreadyRequested =
                requestRepository.existsByPlayerIdAndTeamIdAndStatusIn(
                        playerId, teamId, List.of("PENDING", "APPROVED"));

        if (alreadyRequested) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Player already has a pending request or membership.");
        }

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setPlayer(player);
        joinRequest.setTeam(team);
        joinRequest.setPreferredPosition(
                request.preferredPosition().trim());
        joinRequest.setStatus("PENDING");

        return JoinRequestResponse.from(
                requestRepository.save(joinRequest));
    }

    // View players waiting for approval.
    @Transactional(readOnly = true)
    public List<JoinRequestResponse> pendingRequests(
            Long captainId, Long teamId) {

        findTeam(captainId, teamId);

        return requestRepository
                .findAllByTeamIdAndStatusOrderById(teamId, "PENDING")
                .stream()
                .map(JoinRequestResponse::from)
                .toList();
    }

    // View approved team members.
    @Transactional(readOnly = true)
    public List<JoinRequestResponse> roster(
            Long captainId, Long teamId) {

        findTeam(captainId, teamId);

        return requestRepository
                .findAllByTeamIdAndStatusOrderById(teamId, "APPROVED")
                .stream()
                .map(JoinRequestResponse::from)
                .toList();
    }

    // Captain approves a player's request.
    public JoinRequestResponse approve(
            Long captainId, Long teamId, Long requestId) {

        JoinRequest request = findRequest(
                captainId, teamId, requestId);

        checkPending(request);

        if (!"ACTIVE".equals(request.getPlayer().getAccountStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Player account is not active.");
        }

        request.setStatus("APPROVED");

        return JoinRequestResponse.from(
                requestRepository.save(request));
    }

    // Captain declines a player's request.
    public JoinRequestResponse decline(
            Long captainId, Long teamId, Long requestId) {

        JoinRequest request = findRequest(
                captainId, teamId, requestId);

        checkPending(request);

        request.setStatus("DECLINED");

        return JoinRequestResponse.from(
                requestRepository.save(request));
    }

    // Verify the team belongs to the captain.
    private Team findTeam(Long captainId, Long teamId) {

        return teamRepository.findByIdAndCaptainId(
                teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team was not found for this captain."));
    }

    // Find a request associated with the captain's team.
    private JoinRequest findRequest(
            Long captainId, Long teamId, Long requestId) {

        findTeam(captainId, teamId);

        return requestRepository.findByIdAndTeamId(
                requestId, teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Join request not found."));
    }

    // Only pending requests can be approved or declined.
    private void checkPending(JoinRequest request) {

        if (!"PENDING".equals(request.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending requests can be approved or declined.");
        }
    }
}

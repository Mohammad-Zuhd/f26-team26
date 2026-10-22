
package _Project.RecLeague_API.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.OpenSpotRequest;
import _Project.RecLeague_API.dto.OpenSpotResponse;
import _Project.RecLeague_API.entity.OpenSpot;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.repository.OpenSpotRepository;
import _Project.RecLeague_API.repository.TeamRepository;

@Service
@Transactional
public class OpenSpotService {

    private final OpenSpotRepository spotRepository;
    private final TeamRepository teamRepository;

    public OpenSpotService(OpenSpotRepository spotRepository,
                           TeamRepository teamRepository) {
        this.spotRepository = spotRepository;
        this.teamRepository = teamRepository;
    }

    // Post a new open spot for a team.
    public OpenSpotResponse create(Long captainId, Long teamId,
                                   OpenSpotRequest request) {

        Team team = findTeam(captainId, teamId);

        if (request.availableSpots() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A new posting must have at least one open spot.");
        }

        OpenSpot spot = new OpenSpot();
        spot.setTeam(team);
        apply(spot, request);
        spot.setStatus("OPEN");

        return OpenSpotResponse.from(spotRepository.save(spot));
    }

    // View all postings for a team.
    @Transactional(readOnly = true)
    public List<OpenSpotResponse> listSpots(Long captainId, Long teamId) {

        findTeam(captainId, teamId);

        return spotRepository.findAllByTeamIdOrderById(teamId)
                .stream()
                .map(OpenSpotResponse::from)
                .toList();
    }

    // Update an existing posting.
    public OpenSpotResponse update(Long captainId, Long teamId,
                                   Long spotId, OpenSpotRequest request) {

        OpenSpot spot = findSpot(captainId, teamId, spotId);

        if ("CLOSED".equals(spot.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot edit a closed posting.");
        }

        apply(spot, request);

        return OpenSpotResponse.from(spotRepository.save(spot));
    }

    // Close a posting without deleting it.
    public OpenSpotResponse close(Long captainId, Long teamId, Long spotId) {

        OpenSpot spot = findSpot(captainId, teamId, spotId);

        spot.setStatus("CLOSED");
        spot.setAvailableSpots(0);

        return OpenSpotResponse.from(spotRepository.save(spot));
    }

    // Apply and validate posting details.
    private void apply(OpenSpot spot, OpenSpotRequest request) {

        String type = request.spotType().trim();

        if (!type.equals("Permanent Player")
                && !type.equals("Substitute Player")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Spot type must be Permanent Player or Substitute Player.");
        }

        spot.setSpotType(type);
        spot.setAvailableSpots(request.availableSpots());
        spot.setGameDate(request.gameDate());
        spot.setGameTime(request.gameTime());
        spot.setLocation(request.location().trim());
        spot.setDescription(request.description());
    }

    // Make sure the team belongs to this captain.
    private Team findTeam(Long captainId, Long teamId) {

        return teamRepository.findByIdAndCaptainId(teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team was not found for this captain."));
    }

    // Find a posting belonging to the captain's team.
    private OpenSpot findSpot(Long captainId, Long teamId, Long spotId) {

        findTeam(captainId, teamId);

        return spotRepository.findByIdAndTeamId(spotId, teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Open spot posting was not found."));
    }
}

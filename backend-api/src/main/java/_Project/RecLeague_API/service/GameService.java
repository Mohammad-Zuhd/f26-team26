package _Project.RecLeague_API.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.OpenSpotRequest;
import _Project.RecLeague_API.dto.OpenSpotResponse;
import _Project.RecLeague_API.entity.Game;
import _Project.RecLeague_API.entity.OpenSpot;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.repository.GameRepository;
import _Project.RecLeague_API.repository.OpenSpotRepository;
import _Project.RecLeague_API.repository.TeamRepository;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final OpenSpotRepository spotRepository;
    private final OpenSpotService openSpotService;

    public GameService(GameRepository gameRepository,
                       TeamRepository teamRepository,
                       OpenSpotRepository spotRepository,
                       OpenSpotService openSpotService) {
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.spotRepository = spotRepository;
        this.openSpotService = openSpotService;
    }

    public void create(Long captainId, Long teamId, OpenSpotRequest request) {
        Team team = teamRepository.findByIdAndCaptainId(teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found."));

        Game game = new Game();
        game.setTeam(team);
        game.setGameDate(request.gameDate());
        game.setGameTime(request.gameTime());
        game.setLocation(request.location().trim());
        game = gameRepository.save(game);

        OpenSpotResponse response = openSpotService.create(captainId, teamId, request);
        OpenSpot spot = spotRepository.findById(response.id()).orElseThrow();
        spot.setGame(game);
        spotRepository.save(spot);
    }
}

package _Project.RecLeague_API.controller;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// The controller in short is the "front door" of the API.
// It bascially receives HTTP requests, hands the work to the service, and sends back JSON. Every endpoint here starts with /api/players.
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    // The controller only talks to the service, never directly to the repository
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // GET /api/players -> list all players (200)
    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    // GET /api/players/{id} > get one player (200, or 404 if not found)
    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    // POST /api/players -> create a player profile (201)
    // @Valid checks the @NotBlank/@Email rules in Player first, and sends back 400 if they fail
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player createPlayer(@Valid @RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    // PUT /api/players/{id} -> update a player profile (200, 400, 404, or 409)
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @Valid @RequestBody Player player) {
        return playerService.updatePlayer(id, player);
    }
}
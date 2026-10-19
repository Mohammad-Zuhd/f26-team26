package _Project.RecLeague_API.service;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// The service holds the actual logic, the "rules" for players.
// The controller bascially asks the service for things, and the service talks to the repository
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    // Spring passes in the repository for us automatically
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Returns every player in the database
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Finds one player by ID. If that ID doesn't exist, the API sends back a 404.
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found with id " + id));
    }

    // Thiss Creates a new player profile 
    public Player createPlayer(Player player) {
        // it Block duplicate emails with a 409 Conflict
        if (playerRepository.existsByEmailIgnoreCase(player.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A player with email " + player.getEmail() + " already exists");
        }
        // Clear any ID sent in the request so we always create a new row
        player.setId(null);
        // Every new player starts out as ACTIVE as they just signed up
        player.setAccountStatus("ACTIVE");
        return playerRepository.save(player);
    }

    // Updates an existing player's profile
    public Player updatePlayer(Long id, Player updated) {
        // Makes sure the player exists first, or 404 if not 
        Player existing = getPlayerById(id);

        // If they changed their email, make sure no one else already has it
        if (playerRepository.existsByEmailIgnoreCaseAndIdNot(updated.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A player with email " + updated.getEmail() + " already exists");
        }

        // Copy the new values onto the existing player
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setLocation(updated.getLocation());
        existing.setPreferredSports(updated.getPreferredSports());
        existing.setSkillLevel(updated.getSkillLevel());
        existing.setAvailability(updated.getAvailability());

        // accountStatus is not copied on purpose, so a suspended player can't un-suspend themselves by editing their profile

        return playerRepository.save(existing);
    }
}
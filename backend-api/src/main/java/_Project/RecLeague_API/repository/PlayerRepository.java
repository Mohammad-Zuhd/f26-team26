package _Project.RecLeague_API.repository;

import _Project.RecLeague_API.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// The repository is how we talk to the "players" table.
// By extending JpaRepository we get save, findAll, findById, and delete for free, without writing any SQL ourselves.
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Spring builds the query from the method name:
    // "does a player with this email already exist?", ignoring upper/lowercase
    // We use this to stop two players from signing up with the same email.
    boolean existsByEmailIgnoreCase(String email);

    // Same check, but it skips the player we're updating.
    // Otherwise, saving your own profile would count your own email as a duplicate.
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // for Milestone 4: this finds the player who owns this email so they can get back into their profile.
    Optional<Player> findByEmailIgnoreCase(String email);
}
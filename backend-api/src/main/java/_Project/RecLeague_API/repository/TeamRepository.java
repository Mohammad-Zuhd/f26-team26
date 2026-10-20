
package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import _Project.RecLeague_API.entity.Team;

/**
 * Handles database operations for teams.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {

    // Find all teams managed by a Team Captain.
    List<Team> findAllByCaptainIdOrderById(Long captainId);

    // Find a specific team belonging to a Team Captain.
    Optional<Team> findByIdAndCaptainId(Long id, Long captainId);
}

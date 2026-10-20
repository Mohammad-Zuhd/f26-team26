
package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import _Project.RecLeague_API.entity.OpenSpot;

/**
 * Handles database operations for open roster spots.
 */
public interface OpenSpotRepository extends JpaRepository<OpenSpot, Long> {

    // Find all open spot postings for a team.
    List<OpenSpot> findAllByTeamIdOrderById(Long teamId);

    // Find a specific posting belonging to a team.
    Optional<OpenSpot> findByIdAndTeamId(Long id, Long teamId);
}

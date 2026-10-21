
package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import _Project.RecLeague_API.entity.Review;

/**
 * Handles database operations for team reviews.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find all reviews for a team.
    List<Review> findAllByTeamIdOrderById(Long teamId);

    // Find a specific review belonging to a team.
    Optional<Review> findByIdAndTeamId(Long id, Long teamId);
}

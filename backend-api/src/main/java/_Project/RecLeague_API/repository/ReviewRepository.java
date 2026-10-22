package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import _Project.RecLeague_API.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByTeamIdOrderById(Long teamId);

    Optional<Review> findByIdAndTeamId(
            Long id, Long teamId);

    // Delete all reviews belonging to a team.
    @Modifying
    @Query("DELETE FROM Review r WHERE r.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);
    
// --- Goran: player-side lookups (#31) ---

    // All reviews written by one player
    List<Review> findAllByPlayerIdOrderById(Long playerId);

    // Find a review only if this player wrote it,
    // so players can't edit or delete other people's reviews
    Optional<Review> findByIdAndPlayerId(Long id, Long playerId);
}

package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import _Project.RecLeague_API.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for one team.
    List<Review> findAllByTeamIdOrderById(Long teamId);

    // Find one review only if it belongs to the specified team.
    Optional<Review> findByIdAndTeamId(Long id, Long teamId);

    // Delete all reviews connected to a team.
    // This is used before a team is deleted.
    @Modifying
    @Query("DELETE FROM Review r WHERE r.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);

    // Get all reviews written by one player.
    List<Review> findAllByPlayerIdOrderById(Long playerId);

    // Find one review only if it belongs to the specified player.
    Optional<Review> findByIdAndPlayerId(Long id, Long playerId);

    // Delete all reviews written by a player.
    // SysAdmin can use this before removing the Player account.
    @Modifying
    @Query("DELETE FROM Review r WHERE r.player.id = :playerId")
    void deleteByPlayerId(@Param("playerId") Long playerId);
}
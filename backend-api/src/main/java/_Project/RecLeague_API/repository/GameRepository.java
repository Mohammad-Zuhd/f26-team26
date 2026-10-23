
package _Project.RecLeague_API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import _Project.RecLeague_API.entity.Game;

/**
 * Repository for managing Game records.
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    // Find all games belonging to a team.
    List<Game> findAllByTeamIdOrderById(Long teamId);

    // Remove a team's games before deleting that team.
    @Modifying
    @Query("DELETE FROM Game g WHERE g.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);
}

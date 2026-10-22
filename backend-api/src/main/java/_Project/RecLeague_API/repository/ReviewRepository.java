
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
}

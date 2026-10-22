
package _Project.RecLeague_API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import _Project.RecLeague_API.entity.OpenSpot;

public interface OpenSpotRepository extends JpaRepository<OpenSpot, Long> {

    List<OpenSpot> findAllByTeamIdOrderById(Long teamId);

    Optional<OpenSpot> findByIdAndTeamId(Long id, Long teamId);

    // Delete all open spots belonging to a team.
    @Modifying
    @Query("DELETE FROM OpenSpot o WHERE o.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);
}

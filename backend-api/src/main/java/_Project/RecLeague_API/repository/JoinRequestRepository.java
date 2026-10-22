
package _Project.RecLeague_API.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import _Project.RecLeague_API.entity.JoinRequest;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findAllByTeamIdAndStatusOrderById(
            Long teamId, String status);

    Optional<JoinRequest> findByIdAndTeamId(
            Long id, Long teamId);

    boolean existsByPlayerIdAndTeamIdAndStatusIn(
            Long playerId,
            Long teamId,
            Collection<String> statuses);

    List<JoinRequest> findAllByPlayerIdOrderById(Long playerId);

    // Delete all join requests and roster memberships for a team.
    @Modifying
    @Query("DELETE FROM JoinRequest j WHERE j.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);

        // Finding a request only if it belongs to this player,
    // so a player can't cancel someone else's request
    Optional<JoinRequest> findByIdAndPlayerId(Long id, Long playerId);
}

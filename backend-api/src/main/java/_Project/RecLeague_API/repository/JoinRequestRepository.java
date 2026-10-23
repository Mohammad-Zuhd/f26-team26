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

    // Get all requests for a team with a certain status.
    List<JoinRequest> findAllByTeamIdAndStatusOrderById(
            Long teamId,
            String status);

    // Find one request only if it belongs to the specified team.
    Optional<JoinRequest> findByIdAndTeamId(
            Long id,
            Long teamId);

    // Check whether a player already has a request for a team
    // with one of the provided statuses.
    boolean existsByPlayerIdAndTeamIdAndStatusIn(
            Long playerId,
            Long teamId,
            Collection<String> statuses);

    // Get all join requests belonging to a player.
    List<JoinRequest> findAllByPlayerIdOrderById(Long playerId);

    // Delete all join requests and roster memberships for a team.
    @Modifying
    @Query("DELETE FROM JoinRequest j WHERE j.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);

    // Delete all join requests and roster memberships for a player.
    // SysAdmin can use this before removing the Player account.
    @Modifying
    @Query("DELETE FROM JoinRequest j WHERE j.player.id = :playerId")
    void deleteByPlayerId(@Param("playerId") Long playerId);

    // Find a request only if it belongs to this player,
    // so a player cannot cancel someone else's request.
    Optional<JoinRequest> findByIdAndPlayerId(
            Long id,
            Long playerId);
}
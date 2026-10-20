
package _Project.RecLeague_API.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import _Project.RecLeague_API.entity.JoinRequest;

/**
 * Handles database operations for team join requests.
 */
public interface JoinRequestRepository
        extends JpaRepository<JoinRequest, Long> {

    // Find pending requests or approved roster members.
    List<JoinRequest> findAllByTeamIdAndStatusOrderById(
            Long teamId, String status);

    // Find a specific request belonging to a team.
    Optional<JoinRequest> findByIdAndTeamId(
            Long id, Long teamId);

    // Check whether a player already has an active request or membership.
    boolean existsByPlayerIdAndTeamIdAndStatusIn(
            Long playerId,
            Long teamId,
            Collection<String> statuses);

    // Find all requests submitted by a player.
    List<JoinRequest> findAllByPlayerIdOrderById(Long playerId);
}

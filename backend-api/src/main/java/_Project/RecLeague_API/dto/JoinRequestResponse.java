
package _Project.RecLeague_API.dto;

import _Project.RecLeague_API.entity.JoinRequest;

/**
 * Represents a player's join request or roster membership.
 */
public record JoinRequestResponse(
        Long id,
        Long playerId,
        String playerName,
        String preferredPosition,
        String skillLevel,
        Long teamId,
        String status) {

    public static JoinRequestResponse from(JoinRequest request) {
        return new JoinRequestResponse(
                request.getId(),
                request.getPlayer().getId(),
                request.getPlayer().getName(),
                request.getPreferredPosition(),
                request.getPlayer().getSkillLevel(),
                request.getTeam().getId(),
                request.getStatus());
    }
}

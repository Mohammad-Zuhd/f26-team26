
package _Project.RecLeague_API.dto;

import _Project.RecLeague_API.entity.TeamCaptain;

/**
 * Represents the Team Captain information returned by the API.
 * The password is excluded for security.
 */
public record TeamCaptainResponse(
        Long id,
        String name,
        String email,
        String teamName) {

    public static TeamCaptainResponse from(TeamCaptain captain) {
        return new TeamCaptainResponse(
                captain.getId(),
                captain.getName(),
                captain.getEmail(),
                captain.getTeamName());
    }
}
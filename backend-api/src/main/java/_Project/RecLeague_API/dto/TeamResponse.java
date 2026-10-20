
package _Project.RecLeague_API.dto;

import _Project.RecLeague_API.entity.Team;

/**
 * Represents the team information returned by the API.
 */
public record TeamResponse(
        Long id,
        String teamName,
        String sport,
        String skillLevel,
        String location,
        String schedule,
        String description,
        Long captainId) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(),team.getTeamName(),team.getSport(),team.getSkillLevel(),team.getLocation(),team.getSchedule(),team.getDescription(),team.getCaptain().getId());
    }
}

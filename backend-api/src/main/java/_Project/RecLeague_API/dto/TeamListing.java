package _Project.RecLeague_API.dto;

import _Project.RecLeague_API.entity.Team;

// A TeamListing is what a PLAYER sees when browsing teams.
// It's different from TeamResponse (the captain's version) because a player also needs
// to know how many open spots the team has and who the captain is.
public record TeamListing(
        Long id,
        String teamName,
        String sport,
        String skillLevel,
        String location,
        String schedule,
        String description,
        String captainName,
        int availableSpots) {

    // this Turns a Team from the database into a TeamListing.
    // availableSpots gets passed in because it's not stored on the Team itself,
    // it's added up from the team's open spots in TeamBrowseService.
    public static TeamListing from(Team team, int availableSpots) {
        return new TeamListing(
                team.getId(),
                team.getTeamName(),
                team.getSport(),
                team.getSkillLevel(),
                team.getLocation(),
                team.getSchedule(),
                team.getDescription(),
                team.getCaptain().getName(),
                availableSpots);
    }
}
package _Project.RecLeague_API.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.TeamListing;
import _Project.RecLeague_API.entity.OpenSpot;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.repository.OpenSpotRepository;
import _Project.RecLeague_API.repository.TeamRepository;

// This service is for User Story 2, "View available services".
// For our app the "service" is a team with open roster spots, so this is where
// we figure out which teams a player can actually join, and apply the search filters.
// We need that because Team -> captain and OpenSpot -> team are LAZY, meaning
// Hibernate only loads them when we touch them, and it can only do that inside a transaction.
// readOnly = true just tells Spring we're only reading, never saving, in here.
@Service
@Transactional(readOnly = true)
public class TeamBrowseService {

    private final TeamRepository teamRepository;
    private final OpenSpotRepository openSpotRepository;

    // Spring passes both repositories in for us (constructor injection, same as PlayerService)
    public TeamBrowseService(TeamRepository teamRepository, OpenSpotRepository openSpotRepository) {
        this.teamRepository = teamRepository;
        this.openSpotRepository = openSpotRepository;
    }

    // Returns every team that has open spots AND matches the filters the player picked.
    // and Every filter is optional, if it's null or empty, we just don't filter on it.
    // Sport and skill level come from dropdowns so they have to match exactly.
    // Location and schedule are typed in, so We use "contains" (typing "greens" finds "Greensboro").
    public List<TeamListing> browse(String sport, String skillLevel, String location, String schedule) {
        // Step 1, add up how many open spots each team has, like {teamId 3 -> 4 spots}
        Map<Long, Integer> spotsPerTeam = countOpenSpotsPerTeam();

        List<TeamListing> results = new ArrayList<>();

        // Step 2, only load the teams that showed up in that map (teams with no spots are of course skipped)
        for (Team team : teamRepository.findAllById(spotsPerTeam.keySet())) {
            // Step 3, hide teams whose captain was suspended by the SysAdmin
            if ("SUSPENDED".equals(team.getCaptain().getAccountStatus())) {
                continue;
            }
            // Step 4, check the filters, and keep the team only if all of them pass
            if (matchesExactly(team.getSport(), sport)
                    && matchesExactly(team.getSkillLevel(), skillLevel)
                    && containsText(team.getLocation(), location)
                    && containsText(team.getSchedule(), schedule)) {
                results.add(TeamListing.from(team, spotsPerTeam.get(team.getId())));
            }
        }

        // Step 5, sort A to Z by team name so the page always shows them in the same order
        results.sort(Comparator.comparing(TeamListing::teamName, String.CASE_INSENSITIVE_ORDER));
        return results;
    }

    // Gets one team for the "View Team" page.
    // If the ID doesn't exist we throw a 404 with a clear message, which the UI
    // turns into a friendly error page instead of a crash.
    public TeamListing getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team " + teamId + " was not found."));

        // A team can exist but have 0 spots right now, getOrDefault gives 0 in that case
        int spots = countOpenSpotsPerTeam().getOrDefault(teamId, 0);
        return TeamListing.from(team, spots);
    }

    // The open spots for one team (the game date, time, position etc.) shown on the team page.
    // We only show spots that are still OPEN and still have room.
    public List<OpenSpot> getOpenSpotsForTeam(Long teamId) {
        List<OpenSpot> openOnes = new ArrayList<>();
        for (OpenSpot spot : openSpotRepository.findAllByTeamIdOrderById(teamId)) {
            if (isStillOpen(spot)) {
                openOnes.add(spot);
            }
        }
        return openOnes;
    }

    // The list of sports for the filter dropdown, built from the real data in the database.
    // That way we never hardcode "Soccer, Basketball..." and new sports show up automatically.
    public List<String> getSportsWithOpenSpots() {
        TreeSet<String> sports = new TreeSet<>();
        for (TeamListing team : browse(null, null, null, null)) {
            sports.add(team.sport());
        }
        return new ArrayList<>(sports);
    }

    // Goes through every open spot in the database and adds up the available
    // spots per team. 
    private Map<Long, Integer> countOpenSpotsPerTeam() {
        Map<Long, Integer> spotsPerTeam = new HashMap<>();
        for (OpenSpot spot : openSpotRepository.findAll()) {
            if (isStillOpen(spot)) {
                Long teamId = spot.getTeam().getId();
                int soFar = spotsPerTeam.getOrDefault(teamId, 0);
                spotsPerTeam.put(teamId, soFar + spot.getAvailableSpots());
            }
        }
        return spotsPerTeam;
    }

    // A spot counts only if the captain hasn't closed it and there's at least 1 place left
    private boolean isStillOpen(OpenSpot spot) {
        return "OPEN".equals(spot.getStatus()) && spot.getAvailableSpots() > 0;
    }

    // For dropdown filters, empty filter = match everything, otherwise exact match (ignoring caps)
    private boolean matchesExactly(String value, String filter) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        return value != null && value.equalsIgnoreCase(filter.trim());
    }

    // For typed filters, empty filter = match everything, otherwise "does it contain this text"
    private boolean containsText(String value, String filter) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        return value != null && value.toLowerCase().contains(filter.trim().toLowerCase());
    }
}
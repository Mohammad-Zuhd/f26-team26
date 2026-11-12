package _Project.RecLeague_API.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.dto.TeamListing;
import _Project.RecLeague_API.service.TeamBrowseService;

// REST (JSON) version of User Story 2, so it can be tested in Postman like our other endpoints.
@RestController
@RequestMapping("/api/teams")
public class TeamBrowseController {

    private final TeamBrowseService teamBrowseService;

    public TeamBrowseController(TeamBrowseService teamBrowseService) {
        this.teamBrowseService = teamBrowseService;
    }
    // All 4 filters are optional (required = false). Returns 200 and a list, which can be empty.
    @GetMapping
    public List<TeamListing> browseTeams(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String skillLevel,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String schedule) {
        return teamBrowseService.browse(sport, skillLevel, location, schedule);
    }

    // GET /api/teams/{teamId} -> one team (200), or 404 if the ID doesn't exist
    @GetMapping("/{teamId}")
    public TeamListing getTeam(@PathVariable Long teamId) {
        return teamBrowseService.getTeam(teamId);
    }
}
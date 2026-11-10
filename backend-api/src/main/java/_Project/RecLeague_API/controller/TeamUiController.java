package _Project.RecLeague_API.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import _Project.RecLeague_API.service.TeamService;

@Controller
public class TeamUiController {

    private final TeamService teamService;

    public TeamUiController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Display all teams belonging to a Team Captain.
    @GetMapping("/captains/{captainId}/teams")
    public String manageTeams(
            @PathVariable Long captainId,
            Model model) {

        model.addAttribute("captainId", captainId);
        model.addAttribute("teams", teamService.listTeams(captainId));

        return "manage-teams";
    }
}

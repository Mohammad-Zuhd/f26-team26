
package _Project.RecLeague_API.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import _Project.RecLeague_API.dto.TeamRequest;
import _Project.RecLeague_API.service.TeamService;
import jakarta.validation.Valid;

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

    // Create a new team.
    @PostMapping("/captains/{captainId}/teams")
    public String createTeam(
            @PathVariable Long captainId,
            @Valid @ModelAttribute TeamRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("captainId", captainId);
            model.addAttribute("teams", teamService.listTeams(captainId));
            model.addAttribute("error", "Please enter valid team information.");
            return "manage-teams";
        }

        teamService.create(captainId, request);

        return "redirect:/captains/" + captainId + "/teams";
    }

    // Remove a team.
    @PostMapping("/captains/{captainId}/teams/delete")
    public String deleteTeam(
            @PathVariable Long captainId,
            @RequestParam Long teamToRemove,
            RedirectAttributes redirectAttributes) {

        try {
            teamService.delete(captainId, teamToRemove);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute(
                    "error", "The selected team could not be found.");
        }

        return "redirect:/captains/" + captainId + "/teams";
    }
}

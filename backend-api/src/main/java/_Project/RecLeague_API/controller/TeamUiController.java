package _Project.RecLeague_API.controller;

import java.util.ArrayList;
import java.util.List;

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

import _Project.RecLeague_API.dto.OpenSpotRequest;
import _Project.RecLeague_API.dto.OpenSpotResponse;
import _Project.RecLeague_API.dto.TeamRequest;
import _Project.RecLeague_API.dto.TeamResponse;
import _Project.RecLeague_API.service.GameService;
import _Project.RecLeague_API.service.OpenSpotService;
import _Project.RecLeague_API.service.TeamService;
import jakarta.validation.Valid;

@Controller
public class TeamUiController {

    private final TeamService teamService;
    private final OpenSpotService openSpotService;
    private final GameService gameService;

    public TeamUiController(TeamService teamService,
                            OpenSpotService openSpotService,
                            GameService gameService) {
        this.teamService = teamService;
        this.openSpotService = openSpotService;
        this.gameService = gameService;
    }

    @GetMapping("/captains/{captainId}/teams")
    public String manageTeams(@PathVariable Long captainId, Model model) {
        model.addAttribute("captainId", captainId);
        model.addAttribute("teams", teamService.listTeams(captainId));
        return "manage-teams";
    }

    @PostMapping("/captains/{captainId}/teams")
    public String createTeam(@PathVariable Long captainId,
                             @Valid @ModelAttribute TeamRequest request,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("captainId", captainId);
            model.addAttribute("teams", teamService.listTeams(captainId));
            model.addAttribute("error", "Please enter valid team information.");
            return "manage-teams";
        }
        teamService.create(captainId, request);
        return "redirect:/captains/" + captainId + "/teams";
    }

    @PostMapping("/captains/{captainId}/teams/{teamId}/edit")
    public String updateTeam(@PathVariable Long captainId,
                             @PathVariable Long teamId,
                             @Valid @ModelAttribute TeamRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter valid team information.");
            return "redirect:/captains/" + captainId + "/teams";
        }
        try {
            teamService.update(captainId, teamId, request);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "The selected team could not be found.");
        }
        return "redirect:/captains/" + captainId + "/teams";
    }

    @PostMapping("/captains/{captainId}/teams/delete")
    public String deleteTeam(@PathVariable Long captainId,
                             @RequestParam Long teamToRemove,
                             RedirectAttributes redirectAttributes) {
        try {
            teamService.delete(captainId, teamToRemove);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "The selected team could not be found.");
        }
        return "redirect:/captains/" + captainId + "/teams";
    }

    @GetMapping("/captains/{captainId}/spots")
    public String manageSpots(@PathVariable Long captainId, Model model) {
        List<TeamResponse> teams = teamService.listTeams(captainId);
        List<OpenSpotResponse> spots = new ArrayList<>();

        for (TeamResponse team : teams) {
            spots.addAll(openSpotService.listSpots(captainId, team.id()));
        }

        model.addAttribute("captainId", captainId);
        model.addAttribute("teams", teams);
        model.addAttribute("spots", spots);
        return "post-open-spots";
    }

    @PostMapping("/captains/{captainId}/spots")
    public String createSpot(@PathVariable Long captainId,
                             @RequestParam Long teamId,
                             @Valid @ModelAttribute OpenSpotRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter valid open spot information.");
            return "redirect:/captains/" + captainId + "/spots";
        }
        try {
            openSpotService.create(captainId, teamId, request);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/captains/" + captainId + "/spots";
    }

    @PostMapping("/captains/{captainId}/spots/{teamId}/{spotId}/edit")
    public String updateSpot(@PathVariable Long captainId,
                             @PathVariable Long teamId,
                             @PathVariable Long spotId,
                             @Valid @ModelAttribute OpenSpotRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter valid open spot information.");
            return "redirect:/captains/" + captainId + "/spots";
        }
        try {
            openSpotService.update(captainId, teamId, spotId, request);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/captains/" + captainId + "/spots";
    }

    @PostMapping("/captains/{captainId}/spots/{teamId}/{spotId}/close")
    public String closeSpot(@PathVariable Long captainId,
                            @PathVariable Long teamId,
                            @PathVariable Long spotId,
                            RedirectAttributes redirectAttributes) {
        try {
            openSpotService.close(captainId, teamId, spotId);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/captains/" + captainId + "/spots";
    }

    @PostMapping("/captains/{captainId}/games")
    public String createGame(@PathVariable Long captainId,
                             @RequestParam Long teamId,
                             @Valid @ModelAttribute OpenSpotRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please enter valid game information.");
            return "redirect:/captains/" + captainId + "/spots";
        }
        try {
            gameService.create(captainId, teamId, request);
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/captains/" + captainId + "/spots";
    }
}

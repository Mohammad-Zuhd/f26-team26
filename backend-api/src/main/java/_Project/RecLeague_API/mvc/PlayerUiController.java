package _Project.RecLeague_API.mvc;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.service.PlayerService;
import _Project.RecLeague_API.service.TeamBrowseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

// The web pages for the Player (customer) side.
// User Story 1: create / modify my player profile  -> /player/signup, /player/profile, /player/profile/edit
// User Story 2: view teams with open roster spots  -> /player/teams, /player/teams/{teamId}
//
// How it works: the browser asks for a page, this controller asks the SERVICE for real data
// from Neon, puts that data in the "Model" (basically a box of values), and returns a template
// name. FreeMarker fills the template with the data and sends finished HTML back to the browser.
// The controller never talks to the repositories directly, only to the services,
// same layering as our REST controllers.
@Controller
@RequestMapping("/player")
public class PlayerUiController {

    // The name we store the logged-in player's ID under in the session.

    static final String SESSION_PLAYER_ID = "playerId";

    // The 3 skill levels the dropdown shows. Same values our API and SRS use.
    private static final List<String> SKILL_LEVELS = List.of("Beginner", "Intermediate", "Advanced");

    private final PlayerService playerService;
    private final TeamBrowseService teamBrowseService;

    // Spring hands us both services (constructor injection)
    public PlayerUiController(PlayerService playerService, TeamBrowseService teamBrowseService) {
        this.playerService = playerService;
        this.teamBrowseService = teamBrowseService;
    }

    // this is for US 1 -- Create / modify customer profile

    // GET /player/signup -> shows an empty profile form
    @GetMapping("/signup")
    public String signupForm(Model model) {
        // An empty Player so the form's fields have something to read from
        return showProfileForm(model, new Player(), "Create Your Player Profile", "/player/signup");
    }

    // POST /player/signup -> the form was submitted, try to create the player

    @PostMapping("/signup")
    public String createProfile(@Valid @ModelAttribute("player") Player player,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirect) {
        // Bad input (blank name, bad email...) -> same form again, with what they typed still in it
        if (result.hasErrors()) {
            return showProfileForm(model, player, "Create Your Player Profile", "/player/signup");
        }
        try {
            // PlayerService saves it to Neon (and sets accountStatus to ACTIVE for us)
            Player saved = playerService.createPlayer(player);
            // "Log them in" by remembering their new ID in the session
            session.setAttribute(SESSION_PLAYER_ID, saved.getId());
            redirect.addFlashAttribute("message", "Welcome to RecLeague, " + saved.getName() + "! Your profile was created.");
            return "redirect:/player/profile";
        } catch (ResponseStatusException e) {
            // The service throws 409 CONFLICT when the email is already taken.
            // We show that message on the form instead of an error page.
            model.addAttribute("formError", e.getReason());
            return showProfileForm(model, player, "Create Your Player Profile", "/player/signup");
        }
    }

    // GET /player/login -> "Find my profile" page (type your email)
    @GetMapping("/login")
    public String loginForm() {
        return "player/login";
    }

    // POST /player/login -> look the email up, and if it exists, remember that player
    @PostMapping("/login")
    public String login(@RequestParam(defaultValue = "") String email,
                        HttpSession session,
                        Model model) {
        if (email.isBlank()) {
            model.addAttribute("formError", "Please enter the email you signed up with.");
            return "player/login";
        }
        try {
            Player player = playerService.getPlayerByEmail(email);
            // A SysAdmin can suspend a player, so we don't let suspended accounts in
            if ("SUSPENDED".equals(player.getAccountStatus())) {
                model.addAttribute("formError", "This account is suspended. Please contact RecLeague support.");
                model.addAttribute("email", email);
                return "player/login";
            }
            session.setAttribute(SESSION_PLAYER_ID, player.getId());
            return "redirect:/player/profile";
        } catch (ResponseStatusException e) {
            // 404 from the service = no one has that email. Show the message on the page.
            model.addAttribute("formError", e.getReason());
            model.addAttribute("email", email);
            return "player/login";
        }
    }

    // POST /player/logout -> forget who's logged in and go back home
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_PLAYER_ID);
        return "redirect:/";
    }

    // GET /player/profile -> "My Profile" page, showing the real data from Neon
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long playerId = currentPlayerId(session);
        if (playerId == null) {
            // Not logged in -> send them to find their profile first
            return "redirect:/player/login";
        }
        // If this ID was deleted by a SysAdmin, getPlayerById throws a 404
        // and PlayerUiExceptionHandler shows a friendly error page.
        model.addAttribute("player", playerService.getPlayerById(playerId));
        return "player/profile";
    }

    // GET /player/profile/edit -> same form as signup, but filled in with their current info
    @GetMapping("/profile/edit")
    public String editForm(HttpSession session, Model model) {
        Long playerId = currentPlayerId(session);
        if (playerId == null) {
            return "redirect:/player/login";
        }
        Player player = playerService.getPlayerById(playerId);
        return showProfileForm(model, player, "Edit My Profile", "/player/profile/edit");
    }

    // POST /player/profile/edit -> save the changes
    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("player") Player player,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirect) {
        Long playerId = currentPlayerId(session);
        if (playerId == null) {
            return "redirect:/player/login";
        }
        if (result.hasErrors()) {
            return showProfileForm(model, player, "Edit My Profile", "/player/profile/edit");
        }
        try {
            // We use the ID from the SESSION, not from the form,
            // so nobody can edit someone else's profile by changing a hidden field.
            playerService.updatePlayer(playerId, player);
            redirect.addFlashAttribute("message", "Your profile was saved.");
            return "redirect:/player/profile";
        } catch (ResponseStatusException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw e; // player was deleted -> let the error page handle it
            }
            // 409: they changed their email to one another player already uses
            model.addAttribute("formError", e.getReason());
            return showProfileForm(model, player, "Edit My Profile", "/player/profile/edit");
        }
    }

    // this is the US 2 -- View available services (teams with open roster spots)


    // GET /player/teams?sport=...&skillLevel=...&location=...&schedule=...
    // That means you can refresh or share the link and get the same results.
    @GetMapping("/teams")
    public String browseTeams(@RequestParam(required = false) String sport,
                              @RequestParam(required = false) String skillLevel,
                              @RequestParam(required = false) String location,
                              @RequestParam(required = false) String schedule,
                              Model model) {
        // The actual list of teams, straight from the database through the service
        model.addAttribute("teams", teamBrowseService.browse(sport, skillLevel, location, schedule));

        // Options for the dropdowns (sports come from the database, not hardcoded)
        model.addAttribute("sports", teamBrowseService.getSportsWithOpenSpots());
        model.addAttribute("skillLevels", SKILL_LEVELS);

        // Send the filters back so the form remembers what the player searched for.
        // We turn null into "" because FreeMarker complains about missing values.
        model.addAttribute("sport", sport == null ? "" : sport);
        model.addAttribute("skillLevel", skillLevel == null ? "" : skillLevel);
        model.addAttribute("location", location == null ? "" : location);
        model.addAttribute("schedule", schedule == null ? "" : schedule);
        return "player/browse-teams";
    }

    // GET /player/teams/{teamId} -> "View Team" page
    @GetMapping("/teams/{teamId}")
    public String teamDetails(@PathVariable Long teamId, Model model) {
        model.addAttribute("team", teamBrowseService.getTeam(teamId));
        model.addAttribute("openSpots", teamBrowseService.getOpenSpotsForTeam(teamId));
        return "player/team-detail";
    }


    // One place that sets up the profile form, used for both "create" and "edit"
    private String showProfileForm(Model model, Player player, String formTitle, String formAction) {
        model.addAttribute("player", player);
        model.addAttribute("formTitle", formTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("skillLevels", SKILL_LEVELS);
        return "player/profile-form";
    }

    // Reads the logged-in player's ID out of the session, or null if nobody is logged in
    private Long currentPlayerId(HttpSession session) {
        Object id = session.getAttribute(SESSION_PLAYER_ID);
        return (id instanceof Long playerId) ? playerId : null;
    }
}
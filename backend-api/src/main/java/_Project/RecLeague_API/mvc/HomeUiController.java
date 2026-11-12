package _Project.RecLeague_API.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import _Project.RecLeague_API.service.TeamCaptainService;

// Shared RecLeague home page (http://localhost:8080/).
// It links to both sides of the app: the Player pages and the Team Captain pages.
@Controller
public class HomeUiController {

    private final TeamCaptainService teamCaptainService;

    // Spring passes in the captain service (constructor injection)
    public HomeUiController(TeamCaptainService teamCaptainService) {
        this.teamCaptainService = teamCaptainService;
    }

    // GET / -> the home page with a Player card and a Team Captain card
    @GetMapping("/")
    public String home() {
        return "player/home";
    }

    // GET /captain?captainId=1 -> sends the captain to their teams page (/captains/1/teams).
    // Prototype step until captain login exists: getProfile throws a 404 if the ID doesn't exist,
    // and PlayerUiExceptionHandler turns that into a friendly error page.
    @GetMapping("/captain")
    public String goToCaptainPages(@RequestParam Long captainId) {
        teamCaptainService.getProfile(captainId);
        return "redirect:/captains/" + captainId + "/teams";
    }
}

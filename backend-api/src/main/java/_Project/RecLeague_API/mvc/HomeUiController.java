package _Project.RecLeague_API.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// this is The home page 
@Controller
public class HomeUiController {

    // @Controller (not @RestController) means the String we return is the NAME of a
    // FreeMarker template, not JSON. "player/home" -> templates/player/home.ftlh
    @GetMapping("/")
    public String home() {
        return "player/home";
    }
}
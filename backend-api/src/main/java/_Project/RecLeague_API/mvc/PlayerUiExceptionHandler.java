package _Project.RecLeague_API.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// catches errors on the player pages and shows a friendly page instead of a stack trace
@ControllerAdvice(assignableTypes = {PlayerUiController.class, HomeUiController.class})
public class PlayerUiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PlayerUiExceptionHandler.class);

    // runs before every page so the navbar knows if someone is logged in
    @ModelAttribute("playerLoggedIn")
    public boolean playerLoggedIn(HttpSession session) {
        return session.getAttribute(PlayerUiController.SESSION_PLAYER_ID) != null;
    }

    // things like "Team 99 was not found" (404) from our services
    @ExceptionHandler(ResponseStatusException.class)
    public String handleServiceError(ResponseStatusException e, Model model, HttpServletResponse response) {
        response.setStatus(e.getStatusCode().value());
        model.addAttribute("errorMessage", e.getReason());
        return "player/error";
    }

    // when the URL has text instead of a number, like /player/teams/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleBadId(MethodArgumentTypeMismatchException e, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("errorMessage", "\"" + e.getValue() + "\" is not a valid ID. Please use the links on the site.");
        return "player/error";
    }

    // backup for anything unexpected. logs the real error in the terminal for us
    @ExceptionHandler(Exception.class)
    public String handleEverythingElse(Exception e, Model model, HttpServletResponse response) {
        log.error("Unexpected error on a player page", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", "Something went wrong on our end. Please try again.");
        return "player/error";
    }
}


package _Project.RecLeague_API.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.TeamCaptainRegistrationRequest;
import _Project.RecLeague_API.dto.TeamCaptainResponse;
import _Project.RecLeague_API.dto.TeamCaptainUpdateRequest;
import _Project.RecLeague_API.entity.TeamCaptain;
import _Project.RecLeague_API.repository.TeamCaptainRepository;

/**
 * Handles Team Captain registration and profile management.
 */
@Service
@Transactional
public class TeamCaptainService {

    private final TeamCaptainRepository captainRepository;
    private final PasswordEncoder passwordEncoder;

    public TeamCaptainService(TeamCaptainRepository captainRepository,
                              PasswordEncoder passwordEncoder) {
        this.captainRepository = captainRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new Team Captain.
    public TeamCaptainResponse register(TeamCaptainRegistrationRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        ensureEmailAvailable(email);

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be no more than 72 UTF-8 bytes.");
        }

        TeamCaptain captain = new TeamCaptain();
        captain.setName(request.name().trim());
        captain.setEmail(email);
        captain.setPassword(passwordEncoder.encode(request.password()));
        captain.setTeamName(request.teamName().trim());

        return TeamCaptainResponse.from(captainRepository.save(captain));
    }

    // Get a Team Captain profile by ID.
    @Transactional(readOnly = true)
    public TeamCaptainResponse getProfile(Long captainId) {
        return TeamCaptainResponse.from(findCaptain(captainId));
    }

    // Update a Team Captain profile.
    public TeamCaptainResponse updateProfile(
            Long captainId, TeamCaptainUpdateRequest request) {

        TeamCaptain captain = findCaptain(captainId);
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (!captain.getEmail().equalsIgnoreCase(email)
                && captainRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with that email already exists.");
        }

        captain.setName(request.name().trim());
        captain.setEmail(email);
        captain.setTeamName(request.teamName().trim());

        return TeamCaptainResponse.from(captainRepository.save(captain));
    }

    // Find a Team Captain or report that the account does not exist.
    private TeamCaptain findCaptain(Long captainId) {
        return captainRepository.findById(captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team Captain " + captainId + " was not found."));
    }

    // Prevent registration with an email that already exists.
    private void ensureEmailAvailable(String email) {
        if (captainRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with that email already exists.");
        }
    }
}

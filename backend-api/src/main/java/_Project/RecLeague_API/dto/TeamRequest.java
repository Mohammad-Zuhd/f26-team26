
package _Project.RecLeague_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Holds and validates the information needed
 * to create or update a recreational team.
 */
public record TeamRequest(
        @NotBlank @Size(max = 100) String teamName,
        @NotBlank @Size(max = 100) String sport,
        @NotBlank @Size(max = 100) String skillLevel,
        @NotBlank @Size(max = 150) String location,
        @NotBlank @Size(max = 255) String schedule,
        @Size(max = 2000) String description) {
}

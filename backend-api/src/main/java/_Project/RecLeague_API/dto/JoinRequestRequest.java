
package _Project.RecLeague_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Holds the information needed to request joining a team.
 */
public record JoinRequestRequest(

        @NotBlank(message = "Preferred position is required")
        @Size(max = 100)
        String preferredPosition

) {
}

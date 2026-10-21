
package _Project.RecLeague_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Information submitted when a captain replies to a review.
 */
public record ReviewReplyRequest(

        @NotBlank
        @Size(max = 2000)
        String captainReply

) {
}

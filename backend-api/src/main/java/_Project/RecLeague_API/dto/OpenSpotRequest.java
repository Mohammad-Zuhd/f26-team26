
package _Project.RecLeague_API.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Holds and validates information needed
 * to create or update an open roster spot.
 */
public record OpenSpotRequest(
        @NotBlank @Size(max = 50) String spotType,
        @NotNull @Min(0) Integer availableSpots,
        @NotNull LocalDate gameDate,
        @NotNull LocalTime gameTime,
        @NotBlank @Size(max = 150) String location,
        @Size(max = 2000) String description) {
}

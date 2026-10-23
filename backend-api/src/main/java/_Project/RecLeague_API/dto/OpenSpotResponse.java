
package _Project.RecLeague_API.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import _Project.RecLeague_API.entity.OpenSpot;

/**
 * Represents the open spot information returned by the API.
 */
public record OpenSpotResponse(
        Long id,
        Long teamId,
        String spotType,
        Integer availableSpots,
        LocalDate gameDate,
        LocalTime gameTime,
        String location,
        String description,
        String status) {

    public static OpenSpotResponse from(OpenSpot spot) {
        return new OpenSpotResponse(spot.getId(), spot.getTeam().getId(),spot.getSpotType(), spot.getAvailableSpots(), spot.getGameDate(), spot.getGameTime(),spot.getLocation(),  spot.getDescription(),  spot.getStatus());
    }
}

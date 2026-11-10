package _Project.RecLeague_API.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import _Project.RecLeague_API.entity.OpenSpot;

public record OpenSpotResponse(
        Long id,
        Long teamId,
        String spotType,
        Integer availableSpots,
        LocalDate gameDate,
        LocalTime gameTime,
        String location,
        String description,
        String status,
        Long gameId) {

    public static OpenSpotResponse from(OpenSpot spot) {
        return new OpenSpotResponse(
                spot.getId(),
                spot.getTeam().getId(),
                spot.getSpotType(),
                spot.getAvailableSpots(),
                spot.getGameDate(),
                spot.getGameTime(),
                spot.getLocation(),
                spot.getDescription(),
                spot.getStatus(),
                spot.getGame() == null ? null : spot.getGame().getId()
        );
    }
}


package _Project.RecLeague_API.dto;

import java.time.LocalDate;

import _Project.RecLeague_API.entity.Review;

/**
 * Review information returned by the API.
 */
public record ReviewResponse(
        Long id,
        Long playerId,
        String playerName,
        Long teamId,
        Integer rating,
        String comment,
        LocalDate reviewDate,
        String captainReply) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getPlayer().getId(),
                review.getPlayer().getName(),
                review.getTeam().getId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate(),
                review.getCaptainReply());
    }
}

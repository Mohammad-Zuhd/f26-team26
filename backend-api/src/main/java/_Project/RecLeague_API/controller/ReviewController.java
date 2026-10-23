package _Project.RecLeague_API.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import _Project.RecLeague_API.dto.ReviewReplyRequest;
import _Project.RecLeague_API.dto.ReviewRequest;
import _Project.RecLeague_API.dto.ReviewResponse;
import _Project.RecLeague_API.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Player submits a review for a team.
    @PostMapping("/players/{playerId}/teams/{teamId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long playerId,
            @PathVariable Long teamId,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse review =
                reviewService.create(playerId, teamId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(review);
    }

    // Players and captains can view a team's reviews.
    @GetMapping("/teams/{teamId}/reviews")
    public List<ReviewResponse> getReviews(
            @PathVariable Long teamId) {

        return reviewService.listReviews(teamId);
    }

    // Team Captain replies to a review.
    @PatchMapping(
            "/team-captains/{captainId}/teams/{teamId}/reviews/{reviewId}/reply")
    public ReviewResponse replyToReview(
            @PathVariable Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReplyRequest request) {

        return reviewService.reply(
                captainId, teamId, reviewId, request);
    }

    // Goran: player side review endpoints (#31) 

    // Player views reviews they wrote -> 200
    @GetMapping("/players/{playerId}/reviews")
    public List<ReviewResponse> getMyReviews(
            @PathVariable Long playerId) {

        return reviewService.playerReviews(playerId);
    }

    // when the Player edits their review -> 200
    @PutMapping("/players/{playerId}/reviews/{reviewId}")
    public ReviewResponse updateReview(
            @PathVariable Long playerId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {

        return reviewService.updateReview(playerId, reviewId, request);
    }

    // when the Player deletes their review -> 204
    @DeleteMapping("/players/{playerId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long playerId,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(playerId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
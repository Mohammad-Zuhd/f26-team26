package _Project.RecLeague_API.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import _Project.RecLeague_API.dto.ReviewReplyRequest;
import _Project.RecLeague_API.dto.ReviewRequest;
import _Project.RecLeague_API.dto.ReviewResponse;
import _Project.RecLeague_API.entity.Player;
import _Project.RecLeague_API.entity.Review;
import _Project.RecLeague_API.entity.Team;
import _Project.RecLeague_API.repository.JoinRequestRepository;
import _Project.RecLeague_API.repository.PlayerRepository;
import _Project.RecLeague_API.repository.ReviewRepository;
import _Project.RecLeague_API.repository.TeamRepository;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final JoinRequestRepository joinRequestRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository,
            JoinRequestRepository joinRequestRepository) {

        this.reviewRepository = reviewRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.joinRequestRepository = joinRequestRepository;
    }

    // Player writes a review for a team.
    public ReviewResponse create(
            Long playerId,
            Long teamId,
            ReviewRequest request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found."));

        // Only active players can submit reviews.
        if (!"ACTIVE".equals(player.getAccountStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Player account is not active.");
        }

        // Player must belong to the team's approved roster.
        boolean isMember =
                joinRequestRepository.existsByPlayerIdAndTeamIdAndStatusIn(
                        playerId,
                        teamId,
                        List.of("APPROVED"));

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only approved team members can write reviews.");
        }

        Review review = new Review();
        review.setPlayer(player);
        review.setTeam(team);
        review.setRating(request.rating());
        review.setComment(request.comment().trim());

        return ReviewResponse.from(
                reviewRepository.save(review));
    }

    // View all reviews for a team.
    @Transactional(readOnly = true)
    public List<ReviewResponse> listReviews(Long teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Team not found.");
        }

        return reviewRepository.findAllByTeamIdOrderById(teamId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // Team Captain replies to a review.
    public ReviewResponse reply(
            Long captainId,
            Long teamId,
            Long reviewId,
            ReviewReplyRequest request) {

        // Verify that the team belongs to this captain.
        teamRepository.findByIdAndCaptainId(teamId, captainId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team was not found for this captain."));

        Review review = reviewRepository
                .findByIdAndTeamId(reviewId, teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found for this team."));

        review.setCaptainReply(request.captainReply().trim());

        return ReviewResponse.from(
                reviewRepository.save(review));
    }

    //  Goran: player-side review management (#31)

    // the PLAYER: view the reviews I've written.
    @Transactional(readOnly = true)
    public List<ReviewResponse> playerReviews(Long playerId) {

        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Player not found.");
        }

        return reviewRepository.findAllByPlayerIdOrderById(playerId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // PLAYER: edit my review. The captain's reply (if any) stays.
    public ReviewResponse updateReview(
            Long playerId, Long reviewId, ReviewRequest request) {

        Review review = findPlayerReview(playerId, reviewId);
        review.setRating(request.rating());
        review.setComment(request.comment().trim());

        return ReviewResponse.from(reviewRepository.save(review));
    }

    // PLAYER: delete my review.
    public void deleteReview(Long playerId, Long reviewId) {
        reviewRepository.delete(findPlayerReview(playerId, reviewId));
    }

    // Finds a review only if this player wrote it, otherwise itll be 404.
    private Review findPlayerReview(Long playerId, Long reviewId) {
        return reviewRepository.findByIdAndPlayerId(reviewId, playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found for this player."));
    }
}
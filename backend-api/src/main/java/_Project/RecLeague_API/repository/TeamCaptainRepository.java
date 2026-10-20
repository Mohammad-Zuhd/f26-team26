

package _Project.RecLeague_API.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import _Project.RecLeague_API.entity.TeamCaptain;

/**
 * Handles database operations for Team Captain accounts.
 */
public interface TeamCaptainRepository extends JpaRepository<TeamCaptain, Long> {

    boolean existsByEmailIgnoreCase(String email);
}

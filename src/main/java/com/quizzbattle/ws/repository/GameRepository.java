package com.quizzbattle.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quizzbattle.ws.model.Game;
import com.quizzbattle.ws.model.Game.Status;
import com.quizzbattle.ws.model.Player;

public interface GameRepository extends JpaRepository<Game, Long> {

	@Query("""
			    SELECT g FROM Game g
			    WHERE (g.player1 = :player OR g.player2 = :player)
			    AND (g.status = :status)
			    ORDER BY
			        CASE
			            WHEN g.turn = :player THEN 0
			            WHEN g.turn IS NOT NULL THEN 1
			            ELSE 2
			        END
			""")
	List<Game> findAllByPlayerOrderedByTurn(Player player, Status status);

	List<Game> findByPlayer1Id(Long playerId);

	List<Game> findByPlayer2Id(Long playerId);

}

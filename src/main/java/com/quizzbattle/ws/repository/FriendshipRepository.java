package com.quizzbattle.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quizzbattle.ws.model.Friendship;
import com.quizzbattle.ws.model.Friendship.Status;
import com.quizzbattle.ws.model.Player;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	@Query("SELECT f FROM Friendship f WHERE (f.sender = ?1 OR ?1 IS NULL)"
			+ " OR (f.receiver = ?1 OR ?1 IS NULL) AND f.status = 'ACCEPTED'")
	List<Friendship> findAllByFilters(Player player);

	List<Friendship> findAllByReceiverAndStatus(Player player, Status status);

	void deleteBySenderId(Long playerId);

	void deleteByReceiverId(Long playerId);
}

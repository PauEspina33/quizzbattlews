package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.Friendship;
import com.quizzbattle.ws.model.Friendship.Status;
import com.quizzbattle.ws.model.Player;

public interface FriendshipService {

	List<Friendship> findAll(Player player);

	List<Friendship> findAllByReceiverAndStatus(Player player, Status status);

	void deleteById(Long id);

	Friendship update(Long id);

	Friendship createFriendship(Player sender, Player receiver);
}

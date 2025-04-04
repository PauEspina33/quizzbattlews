package com.quizzbattle.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.quizzbattle.ws.exception.NotFoundException;
import com.quizzbattle.ws.model.Friendship;
import com.quizzbattle.ws.model.Friendship.Status;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.repository.FriendshipRepository;
import com.quizzbattle.ws.service.FriendshipService;

@Validated
@Service
public class FriendshipServiceImpl implements FriendshipService {

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Override
	public List<Friendship> findAll(Player player) {
		return friendshipRepository.findAllByFilters(player);
	}

	@Override
	public List<Friendship> findAllByReceiverAndStatus(Player player, Status status) {
		return friendshipRepository.findAllByReceiverAndStatus(player, status);
	}

	@Override
	public void deleteById(Long Id) {
		friendshipRepository.deleteById(Id);
	}

	@Override
	public Friendship update(Long id) {
		// Lógica para actualizar la amistad con el id recibido
		Friendship existingFriendship = friendshipRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Friendship not found"));

		existingFriendship.setStatus(Friendship.Status.ACCEPTED);

		return friendshipRepository.save(existingFriendship);
	}

	@Override
	public Friendship createFriendship(Player sender, Player receiver) {
		if (sender == null) {
			throw new IllegalArgumentException("Sender must be provided to create a friendship.");
		}
		if (receiver == null) {
			throw new IllegalArgumentException("Receiver must be provided to create a friendship.");
		}

		Friendship friendship = new Friendship();
		friendship.setSender(sender);
		friendship.setReceiver(receiver);
		friendship.setStatus(Friendship.Status.PENDING);
		return friendshipRepository.saveAndFlush(friendship);
	}

}

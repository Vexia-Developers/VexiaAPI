package fr.vexia.api.friends;

import java.util.UUID;

public class Friendship {

	private final UUID player;
	private final UUID anotherPlayer;

	public Friendship(UUID player, UUID anotherPlayer) {
		this.player = player;
		this.anotherPlayer = anotherPlayer;
	}

	public UUID getPlayer() {
		return player;
	}

	public UUID getAnotherPlayer() {
		return anotherPlayer;
	}
}

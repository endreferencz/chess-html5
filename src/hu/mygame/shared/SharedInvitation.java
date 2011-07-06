package hu.mygame.shared;

import hu.mygame.shared.jdo.Player;

import java.io.Serializable;

public class SharedInvitation implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long invitationId;
	private Player player;
	public SharedInvitation() {
	}
	public SharedInvitation(Long invitationId, Player player) {
		super();
		this.invitationId = invitationId;
		this.player = player;
	}
	public Long getInvitationId() {
		return invitationId;
	}
	public Player getPlayer() {
		return player;
	}
	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}

package hu.mygame.server.jdo;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.shared.Board;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;

@PersistenceCapable
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	@Persistent
	String blackUser;
	@Persistent(serialized = "true")
	Board board;
	@Persistent
	Boolean finished;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	String whiteUser;

	public Game(String whiteUser, String blackUser, Board board) {
		super();
		this.whiteUser = whiteUser;
		this.blackUser = blackUser;
		this.board = board;
		this.finished = false;
	}

	public String getBlackUser() {
		return blackUser;
	}

	public Board getBoard() {
		return board;
	}

	public Boolean getFinished() {
		return finished;
	}

	public Long getId() {
		return id;
	}

	public String getWhiteUser() {
		return whiteUser;
	}

	public void notifyPlayers(ChannelService channelService) {
		ChannelMessage message = new ChannelMessage(whiteUser, ChessGameService.REFRESH_BOARD + id);
		channelService.sendMessage(message);
		message = new ChannelMessage(blackUser, ChessGameService.REFRESH_BOARD + id);
		channelService.sendMessage(message);
	}

	public void setBlackUser(String blackUser) {
		this.blackUser = blackUser;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setWhiteUser(String whiteUser) {
		this.whiteUser = whiteUser;
	}
}

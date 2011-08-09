package hu.mygame.server.rpc;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.server.PMF;
import hu.mygame.server.jdo.GameJDO;
import hu.mygame.server.jdo.InvitationJDO;
import hu.mygame.server.jdo.PlayerJDO;
import hu.mygame.shared.Board;
import hu.mygame.shared.Player;
import hu.mygame.shared.SharedInvitation;
import hu.mygame.shared.Side;
import hu.mygame.shared.State;
import hu.mygame.shared.moves.Move;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ChessGameServiceImpl extends RemoteServiceServlet implements ChessGameService {
	private static final long serialVersionUID = 1L;
	ChannelService channelService = ChannelServiceFactory.getChannelService();
	UserService userService = UserServiceFactory.getUserService();

	void notifyRefreshPlayers(PersistenceManager pm) {

	}

	@Override
	public String connect() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String userId = null;
		try {
			PlayerJDO currentPlayer = getCurrentPlayer(pm);
			userId = currentPlayer.getUser();
		} finally {
			pm.close();
		}
		if (userId == null) {
			return null;
		} else {
			return channelService.createChannel(userId);
		}
	}

	@Override
	public void declineInvitation(Long invitationId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			InvitationJDO invitation = pm.getObjectById(InvitationJDO.class, invitationId);
			pm.deletePersistent(invitation);
		} finally {
			pm.close();
		}
	}

	@Override
	public Board getBoard(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Board board = null;
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			board = game.getBoard();
		} finally {
			pm.close();
		}
		if (board == null)
			return null;
		if (userService.getCurrentUser().getUserId().equals(game.getWhiteUser())) {
			return board;
		} else {
			board.setSide(Side.BLACK);
			return board;
		}
	}

	private PlayerJDO getCurrentPlayer(PersistenceManager pm) {
		PlayerJDO player = null;
		User user = userService.getCurrentUser();
		try {
			player = pm.getObjectById(PlayerJDO.class, user.getUserId());
		} catch (Exception e) {

		}
		if (player == null) {
			player = new PlayerJDO(0L, user.getEmail(), 0L, null, user.getUserId(), false, false, 0L);
			pm.makePersistent(player);
		}
		return player;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<SharedInvitation> getMyInvitations() {
		List<SharedInvitation> ret = new ArrayList<SharedInvitation>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query invitationQuery = pm.newQuery(InvitationJDO.class);
			invitationQuery.setFilter("target == targetParam");
			invitationQuery.declareParameters("java.lang.String targetParam");
			List<InvitationJDO> invitations = (List<InvitationJDO>) invitationQuery.execute(userService
					.getCurrentUser().getUserId());
			for (InvitationJDO i : invitations) {
				PlayerJDO player = pm.getObjectById(PlayerJDO.class, i.getInitiator());
				ret.add(new SharedInvitation(i.getId(), player.toPlayer()));
			}
		} finally {
			pm.close();
		}
		return ret;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Player> getPlayers() {
		List<Player> ret = new ArrayList<Player>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PlayerJDO currentPlayer = getCurrentPlayer(pm);
			Query query = pm.newQuery(PlayerJDO.class);
			List<PlayerJDO> players = (List<PlayerJDO>) query.execute();
			ret.add(currentPlayer.toPlayer());
			for (PlayerJDO p : players) {
				if (!p.equals(currentPlayer)) {
					ret.add(p.toPlayer());
				}
			}
		} finally {
			pm.close();
		}
		return ret;
	}

	private PlayerJDO getWaitingPlayer(PersistenceManager pm) {
		Query query = pm.newQuery(PlayerJDO.class, "waiting == true");
		@SuppressWarnings("unchecked")
		List<PlayerJDO> players = (List<PlayerJDO>) query.execute();
		if (players.size() > 0) {
			return players.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<String> getWaitingPlayers() {
		ArrayList<String> ret = new ArrayList<String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(PlayerJDO.class, "waiting == true");
			@SuppressWarnings("unchecked")
			List<PlayerJDO> players = (List<PlayerJDO>) query.execute();
			for (PlayerJDO p : players) {
				ret.add(p.getUser());
			}
		} finally {
			pm.close();
		}
		return ret;
	}
	@Override
	public void invite(String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PlayerJDO initiator = getCurrentPlayer(pm);
			PlayerJDO target = pm.getObjectById(PlayerJDO.class, userId);
			InvitationJDO invitation = new InvitationJDO(initiator.getUser(), target.getUser());
			pm.makePersistent(invitation);
			ChannelMessage message = new ChannelMessage(userId, ChessGameService.INVITATION);
			channelService.sendMessage(message);
		} finally {
			pm.close();
		}
	}

	@Override
	public boolean move(Long gameId, Move move) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Boolean ret = false;
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			game.getBoard().movePiece(move);
			manageScore(game, pm);
			JDOHelper.makeDirty(game, "board");
			ret = true;
		} finally {
			pm.close();
		}

		User user = userService.getCurrentUser();
		ChannelMessage message;
		if (user.getUserId().equals(game.getWhiteUser())) {
			message = new ChannelMessage(game.getBlackUser(), ChessGameService.REFRESH_BOARD + game.getId());
		} else {
			message = new ChannelMessage(game.getWhiteUser(), ChessGameService.REFRESH_BOARD + game.getId());
		}
		channelService.sendMessage(message);
		return ret;
	}

	private void manageScore(GameJDO game, PersistenceManager pm) {
		if (game.getBoard().getState() == State.WHITE_WIN) {
			PlayerJDO player1 = pm.getObjectById(PlayerJDO.class, game.getWhiteUser());
			player1.setWin(player1.getWin() + 1);
			PlayerJDO player2 = pm.getObjectById(PlayerJDO.class, game.getBlackUser());
			player2.setLost(player2.getLost() + 1);
			game.setFinished(true);
		}
		if (game.getBoard().getState() == State.BLACK_WIN) {
			PlayerJDO player1 = pm.getObjectById(PlayerJDO.class, game.getBlackUser());
			player1.setWin(player1.getWin() + 1);
			PlayerJDO player2 = pm.getObjectById(PlayerJDO.class, game.getWhiteUser());
			player2.setLost(player2.getLost() + 1);
			game.setFinished(true);
		}
		if (game.getBoard().getState() == State.DRAW) {
			PlayerJDO player1 = pm.getObjectById(PlayerJDO.class, game.getWhiteUser());
			player1.setDraw(player1.getDraw() + 1);
			PlayerJDO player2 = pm.getObjectById(PlayerJDO.class, game.getBlackUser());
			player2.setDraw(player2.getDraw() + 1);
			game.setFinished(true);
		}
	}

	@Override
	public void startGame() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PlayerJDO waitingPlayer = getWaitingPlayer(pm);
			PlayerJDO currentPlayer = getCurrentPlayer(pm);
			if (waitingPlayer != null && waitingPlayer != currentPlayer) {
				waitingPlayer.setWaiting(false);
				Board board = new Board(true);
				board.setWhitePlayer(waitingPlayer.getUser());
				board.setBlackPlayer(currentPlayer.getUser());
				GameJDO game = new GameJDO(waitingPlayer.getUser(), currentPlayer.getUser(), board);
				pm.makePersistent(game);
				game.notifyPlayers(channelService);
			} else {
				currentPlayer.setWaiting(true);
			}
		} finally {
			pm.close();
		}
	}

	@Override
	public void startGame(Long invitationId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			InvitationJDO invitation = pm.getObjectById(InvitationJDO.class, invitationId);
			PlayerJDO otherPlayer = pm.getObjectById(PlayerJDO.class, invitation.getTarget());
			PlayerJDO currentPlayer = pm.getObjectById(PlayerJDO.class, invitation.getInitiator());
			if (!otherPlayer.getUser().equals(currentPlayer.getUser())) {
				Board board = new Board(true);
				board.setWhitePlayer(otherPlayer.getUser());
				board.setBlackPlayer(currentPlayer.getUser());
				GameJDO game = new GameJDO(otherPlayer.getUser(), currentPlayer.getUser(), board);
				pm.makePersistent(game);
				game.notifyPlayers(channelService);
			}
			pm.deletePersistent(invitation);
		} finally {
			pm.close();
		}
	}

	@Override
	public void changeName(String name) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PlayerJDO currentPlayer = getCurrentPlayer(pm);
			currentPlayer.setName(name);
		} finally {
			pm.close();
		}
	}

	@Override
	public void undo(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			game.getBoard().undoLastMove();
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}

		ChannelMessage message;
		message = new ChannelMessage(game.getBlackUser(), ChessGameService.REFRESH_BOARD + game.getId());
		channelService.sendMessage(message);
		message = new ChannelMessage(game.getWhiteUser(), ChessGameService.REFRESH_BOARD + game.getId());
		channelService.sendMessage(message);
	}

	@Override
	public void requestUndo(Long gameId) {
		User user = userService.getCurrentUser();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			if (user.getUserId().equals(game.getWhiteUser())) {
				game.getBoard().requestUndo(Side.WHITE);
			} else {
				game.getBoard().requestUndo(Side.BLACK);
			}
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}

		game.notifyPlayers(channelService);
	}

	@Override
	public void refuseUndo(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			game.getBoard().refuseUndo();
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}
		game.notifyPlayers(channelService);
	}

	@Override
	public void resign(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user = userService.getCurrentUser();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			if (user.getUserId().equals(game.getWhiteUser())) {
				game.getBoard().setState(State.BLACK_WIN);
			} else {
				game.getBoard().setState(State.WHITE_WIN);
			}
			manageScore(game, pm);
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}
		game.notifyPlayers(channelService);
	}

	@Override
	public void requestDraw(Long gameId) {
		User user = userService.getCurrentUser();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			if (user.getUserId().equals(game.getWhiteUser())) {
				game.getBoard().requestDraw(Side.WHITE);
			} else {
				game.getBoard().requestDraw(Side.BLACK);
			}
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}

		game.notifyPlayers(channelService);
	}

	@Override
	public void refuseDraw(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			game.getBoard().refuseDraw();
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}
		game.notifyPlayers(channelService);
	}

	@Override
	public void draw(Long gameId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO game = null;
		try {
			game = pm.getObjectById(GameJDO.class, gameId);
			game.getBoard().draw();
			manageScore(game, pm);
			JDOHelper.makeDirty(game, "board");
		} finally {
			pm.close();
		}
		game.notifyPlayers(channelService);
	}
}

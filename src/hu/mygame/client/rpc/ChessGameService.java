package hu.mygame.client.rpc;

import hu.mygame.shared.Board;
import hu.mygame.shared.SharedInvitation;
import hu.mygame.shared.jdo.Player;
import hu.mygame.shared.moves.Move;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chessGame")
public interface ChessGameService extends RemoteService {

	public static String INVITATION = "1:";
	public static String REFRESH_BOARD = "2:";
	public static String PLAYER_WENT_ONLINE = "3:";
	public static String PLAYER_WENT_OFFLINE = "4:";

	public void changeName(String name);

	/**
	 * At startup for channel connection.
	 * 
	 * @return ID for channel
	 */
	public String connect();

	public void declineInvitation(Long invitationId);

	public Board getBoard(Long boardId);

	/**
	 * At startup for invitations.
	 * 
	 * @return List of invitations
	 */
	public List<SharedInvitation> getMyInvitations();

	/**
	 * Get all players.
	 * 
	 * @return List of players. First player is the current player.
	 */
	public List<Player> getPlayers();

	public List<String> getWaitingPlayers();

	public void invite(String userId);

	public boolean move(Long gameId, Move move);

	public void startGame();

	public void startGame(Long invitationId);
}

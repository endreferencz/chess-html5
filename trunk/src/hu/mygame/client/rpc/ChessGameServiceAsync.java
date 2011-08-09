package hu.mygame.client.rpc;

import hu.mygame.shared.Board;
import hu.mygame.shared.SharedInvitation;
import hu.mygame.shared.Player;
import hu.mygame.shared.moves.Move;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChessGameServiceAsync {

	void connect(AsyncCallback<String> callback);

	void declineInvitation(Long invitationId, AsyncCallback<Void> callback);

	void getBoard(Long boardId, AsyncCallback<Board> callback);

	void getMyInvitations(AsyncCallback<List<SharedInvitation>> callback);

	void getPlayers(AsyncCallback<List<Player>> callback);

	void getWaitingPlayers(AsyncCallback<List<String>> callback);

	void invite(String userId, AsyncCallback<Void> callback);

	void move(Long gameId, Move move, AsyncCallback<Boolean> callback);

	void startGame(AsyncCallback<Void> callback);

	void startGame(Long invitationId, AsyncCallback<Void> callback);

	void changeName(String name, AsyncCallback<Void> callback);

	void undo(Long gameId, AsyncCallback<Void> callback);

	void requestUndo(Long gameId, AsyncCallback<Void> callback);

	void refuseUndo(Long gameId, AsyncCallback<Void> callback);

	void resign(Long gameId, AsyncCallback<Void> callback);

	void requestDraw(Long gameId, AsyncCallback<Void> callback);

	void refuseDraw(Long gameId, AsyncCallback<Void> callback);

	void draw(Long gameId, AsyncCallback<Void> callback);

}

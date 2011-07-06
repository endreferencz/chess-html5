package hu.mygame.client.rpc;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.SharedInvitation;
import hu.mygame.shared.jdo.Player;

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

	void move(Long gameId, Position position, Position target, PromotionPiece promotionPiece,
			AsyncCallback<Boolean> callback);

	void startGame(AsyncCallback<Void> callback);

	void startGame(Long invitationId, AsyncCallback<Void> callback);

	void changeName(String name, AsyncCallback<Void> callback);

}

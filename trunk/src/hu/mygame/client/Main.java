package hu.mygame.client;

import hu.mygame.client.chessboard.ChessBoard;
import hu.mygame.client.dialog.IncomingInvitationDialog;
import hu.mygame.client.players.PlayersPanel;
import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.Board;
import hu.mygame.shared.SharedInvitation;
import hu.mygame.shared.Side;
import hu.mygame.shared.jdo.Player;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class Main extends Composite implements AsyncCallback<List<SharedInvitation>> {

	interface MainUiBinder extends UiBinder<Widget, Main> {
	}
	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);
	private HashMap<String, ChessBoard> boards = new HashMap<String, ChessBoard>();

	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	@UiField
	PlayersPanel playersPanel;

	@UiField
	TabLayoutPanel tabLayoutPanel;
	public Main() {
		initWidget(uiBinder.createAndBindUi(this));
		new MyChannel(this);
		chessGameService.getMyInvitations(this);
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO
	}

	@Override
	public void onSuccess(List<SharedInvitation> result) {
		for (SharedInvitation s : result) {
			new IncomingInvitationDialog(s.getInvitationId(), s.getPlayer());
		}
	}
	public void processMessage(String message) {
		if (message.startsWith(ChessGameService.REFRESH_BOARD)) {
			String gameId = message.substring(ChessGameService.REFRESH_BOARD.length());
			refreshBoard(gameId.trim());
		} else if (message.startsWith(ChessGameService.INVITATION)) {
			chessGameService.getMyInvitations(this);
		} else if (message.startsWith(ChessGameService.PLAYER_WENT_ONLINE)) {
			String playerId = message.substring(ChessGameService.PLAYER_WENT_ONLINE.length());
			playersPanel.playerOnline(playerId.trim());
		} else if (message.startsWith(ChessGameService.PLAYER_WENT_OFFLINE)) {
			String playerId = message.substring(ChessGameService.PLAYER_WENT_OFFLINE.length());
			playersPanel.playerOffline(playerId.trim());
		}
	}

	private void refreshBoard(final String gameId) {
		chessGameService.getBoard(Long.valueOf(gameId), new AsyncCallback<Board>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Board result) {
				if (boards.containsKey(gameId)) {
					ChessBoard chessBoard = boards.get(gameId);
					chessBoard.setBoard(result);
				} else {
					ChessBoard chessBoard = new ChessBoard(Long.valueOf(gameId), result);
					String title = "";
					if (result.getSide() == Side.WHITE) {
						title = getName(result.getBlackPlayer());
					} else {
						title = getName(result.getWhitePlayer());
					}
					tabLayoutPanel.add(chessBoard, title);
					tabLayoutPanel.selectTab(chessBoard);
					boards.put(gameId, chessBoard);
				}
			}
		});
	}

	public String getName(String userId) {
		List<Player> players = playersPanel.getPlayers();
		if (players == null)
			return null;
		for (Player p : players) {
			if (p.getUser().equals(userId)) {
				return p.getDisplayName();
			}
		}
		return null;
	}
}

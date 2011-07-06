package hu.mygame.client;

import hu.mygame.client.chessboard.ChessBoard;
import hu.mygame.client.dialog.IncomingInvitationDialog;
import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.Board;
import hu.mygame.shared.SharedInvitation;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class Main extends Composite implements AsyncCallback<List<SharedInvitation>> {

	interface MainUiBinder extends UiBinder<Widget, Main> {
	}
	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);
	private HashMap<String, ChessBoard> boards = new HashMap<String, ChessBoard>();

	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	@UiField
	Button startButton;

	@UiField
	Label statusLabel;

	@UiField
	TabLayoutPanel tabLayoutPanel;
	public Main() {
		initWidget(uiBinder.createAndBindUi(this));
		new MyChannel(this);
		chessGameService.getMyInvitations(this);
	}

	@UiHandler("startButton")
	void handleClick(ClickEvent e) {
		chessGameService.startGame(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
				statusLabel.setText("Waiting for opponent...");
			}
		});
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
		}
	}

	private void refreshBoard(final String gameId) {
		chessGameService.getBoard(Long.valueOf(gameId), new AsyncCallback<Board>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Board result) {
				statusLabel.setText("");
				if (boards.containsKey(gameId)) {
					ChessBoard chessBoard = boards.get(gameId);
					chessBoard.setBoard(result);
				} else {
					ChessBoard chessBoard = new ChessBoard(Long.valueOf(gameId), result);
					tabLayoutPanel.add(chessBoard, "Game");
					tabLayoutPanel.selectTab(chessBoard);
					boards.put(gameId, chessBoard);
				}
			}
		});
	}

}

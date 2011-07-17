package hu.mygame.client.chessboard;

import hu.mygame.client.dialog.CanvasNotSupportedDialog;
import hu.mygame.client.dialog.ConfirmationCallback;
import hu.mygame.client.dialog.ConfirmationDialog;
import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.Board;
import hu.mygame.shared.Side;
import hu.mygame.shared.State;
import hu.mygame.shared.moves.Move;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ChessBoard extends Composite implements AsyncCallback<Void>, ConfirmationCallback {

	interface ChessUiBinder extends UiBinder<Widget, ChessBoard> {
	}
	private static ImageLoader imageLoader = null;
	private static ChessUiBinder uiBinder = GWT.create(ChessUiBinder.class);
	private ActionHandler actionHandler = null;
	private Board board = null;
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);
	private Long gameId = null;
	private BoardDrawer boardDrawer = null;

	@UiField
	Canvas canvas;

	@UiField
	Label label;

	@UiField
	Label undoLabel;
	
	@UiField
	Label drawLabel;

	@UiField
	Button undoButton;

	@UiField
	Button undoAcceptButton;

	@UiField
	Button undoRefuseButton;
	
	@UiField
	Button drawAcceptButton;

	@UiField
	Button drawRefuseButton;


	@UiField
	Button resignButton;

	@UiField
	Button drawButton;

	@UiHandler("resignButton")
	void resign(ClickEvent e) {
		new ConfirmationDialog(this);
	}

	@UiHandler("drawButton")
	void requestDraw(ClickEvent e) {
		chessGameService.requestDraw(gameId, this);
	}

	@UiHandler("undoButton")
	void undo(ClickEvent e) {
		chessGameService.requestUndo(gameId, this);
		undoButton.setEnabled(false);
	}

	@UiHandler("undoAcceptButton")
	void undoAccept(ClickEvent e) {
		chessGameService.undo(gameId, this);
		undoAcceptButton.setEnabled(false);
		undoRefuseButton.setEnabled(false);
	}

	@UiHandler("undoRefuseButton")
	void undoRefuse(ClickEvent e) {
		chessGameService.refuseUndo(gameId, this);
		undoAcceptButton.setEnabled(false);
		undoRefuseButton.setEnabled(false);
	}
	
	@UiHandler("drawAcceptButton")
	void drawAccept(ClickEvent e) {
		chessGameService.draw(gameId, this);
		drawAcceptButton.setEnabled(false);
		drawRefuseButton.setEnabled(false);
	}

	@UiHandler("drawRefuseButton")
	void drawRefuse(ClickEvent e) {
		chessGameService.refuseDraw(gameId, this);
		drawAcceptButton.setEnabled(false);
		drawRefuseButton.setEnabled(false);
	}

	public ChessBoard(Long gameId, Board board) {
		initWidget(uiBinder.createAndBindUi(this));
		this.board = board;
		this.gameId = gameId;
		if (ChessBoard.imageLoader == null) {
			ChessBoard.imageLoader = new ImageLoader(this);
		}
		boardDrawer = new BoardDrawer(canvas.getContext2d(), imageLoader);
		this.actionHandler = new ActionHandler(gameId, this, board);
		refresh();
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void highlite(ArrayList<Move> available) {
		boardDrawer.highlite(available, board);
	}

	@UiFactory
	Canvas makeCanvas() {
		Canvas ret = Canvas.createIfSupported();
		if (ret == null) {
			new CanvasNotSupportedDialog();
			return null;
		}
		ret.setCoordinateSpaceHeight(BoardDrawer.height * 8);
		ret.setCoordinateSpaceWidth(BoardDrawer.width * 8);
		ret.setSize(BoardDrawer.width * 8 + "PX", BoardDrawer.height * 8 + "PX");
		return ret;
	}

	public void refresh() {
		boardDrawer.drawTable(board);
		if (board.getStep() > 0 && board.getSide() == Side.BLACK
				&& (board.getState() == State.WHITE_TURN || board.getState() == State.WHITE_TURN_CHESS)) {
			undoButton.setEnabled(true);
			undoButton.setVisible(true);
		} else if (board.getStep() > 0 && board.getSide() == Side.WHITE
				&& (board.getState() == State.BLACK_TURN || board.getState() == State.BLACK_TURN_CHESS)) {
			undoButton.setEnabled(true);
			undoButton.setVisible(true);
		} else {
			undoButton.setVisible(false);
		}

		if (board.getSide() == Side.BLACK && board.getState() == State.WHITE_REQUESTED_UNDO) {
			undoLabel.setText("White player requested undo. Do you accept?");
			undoLabel.setVisible(true);
			undoAcceptButton.setEnabled(true);
			undoRefuseButton.setEnabled(true);
			undoAcceptButton.setVisible(true);
			undoRefuseButton.setVisible(true);
		} else if (board.getSide() == Side.WHITE && board.getState() == State.BLACK_REQUESTED_UNDO) {
			undoLabel.setText("Black player requested undo. Do you accept?");
			undoLabel.setVisible(true);
			undoAcceptButton.setEnabled(true);
			undoRefuseButton.setEnabled(true);
			undoAcceptButton.setVisible(true);
			undoRefuseButton.setVisible(true);
		} else {
			undoLabel.setVisible(false);
			undoAcceptButton.setVisible(false);
			undoRefuseButton.setVisible(false);
		}

		if (board.getSide() == Side.BLACK && board.getState() == State.WHITE_REQUESTED_DRAW) {
			drawLabel.setText("White player offered DRAW. Do you accept?");
			drawLabel.setVisible(true);
			drawAcceptButton.setEnabled(true);
			drawRefuseButton.setEnabled(true);
			drawAcceptButton.setVisible(true);
			drawRefuseButton.setVisible(true);
		} else if (board.getSide() == Side.WHITE && board.getState() == State.BLACK_REQUESTED_DRAW) {
			drawLabel.setText("Black player offered DRAW. Do you accept?");
			drawLabel.setVisible(true);
			drawAcceptButton.setEnabled(true);
			drawRefuseButton.setEnabled(true);
			drawAcceptButton.setVisible(true);
			drawRefuseButton.setVisible(true);
		} else {
			drawLabel.setVisible(false);
			drawAcceptButton.setVisible(false);
			drawRefuseButton.setVisible(false);
		}
		
		if (board.getState() != State.WHITE_WIN && board.getState() != State.BLACK_WIN) {
			resignButton.setVisible(true);
			drawButton.setVisible(true);
		} else {
			resignButton.setVisible(false);
			drawButton.setVisible(false);
		}
		label.setText(board.getState().toString());
	}

	public void setBoard(Board result) {
		this.board = result;
		actionHandler.setBoard(result);
		refresh();
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO
	}

	@Override
	public void onSuccess(Void result) {
	}

	@Override
	public void accept() {
		chessGameService.resign(gameId, this);
	}

	@Override
	public void refuse() {
	}

}

package hu.mygame.client.chessboard;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.Side;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ActionHandler implements MouseDownHandler {
	private ArrayList<Position> available = null;

	private ClientBoard board = null;
	private ChessBoard chessBoard = null;
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);
	private Long gameId;

	private Position newSelected = new Position(-1, -1);
	private Position selected = new Position(-1, -1);

	public ActionHandler(Long gameId, ChessBoard chessBoard, Board board) {
		this.board = new ClientBoard(board, this);
		this.gameId = gameId;
		this.chessBoard = chessBoard;
		chessBoard.getCanvas().addMouseDownHandler(this);
	}

	public void makeMove(Position selected, Position newSelected, PromotionPiece promotionPiece) {
		promotionPiece = board.movePiece(selected, newSelected, promotionPiece);
		if (promotionPiece != PromotionPiece.REPEAT) {
			chessBoard.refresh();
			available = null;
			chessGameService.move(gameId, selected, newSelected, promotionPiece, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Boolean result) {
				}
			});
		}

	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (board != null && board.isMyTurn()) {
			if (board.getSide() == Side.WHITE) {
				newSelected.setRow(7 - event.getY() / BoardDrawer.height);
				newSelected.setColumn(event.getX() / BoardDrawer.width);
			} else {
				newSelected.setRow(event.getY() / BoardDrawer.height);
				newSelected.setColumn(7 - event.getX() / BoardDrawer.width);
			}

			if (available != null && available.contains(newSelected)) {
				makeMove(selected, newSelected, null);
			} else {
				selected.setColumn(newSelected.getColumn());
				selected.setRow(newSelected.getRow());
				available = board.getAvailableMoves(selected);
				if (available != null) {
					chessBoard.highlite(available);
				} else {
					chessBoard.refresh();
				}
			}
		}
	}
	public void setBoard(Board board) {
		this.board = new ClientBoard(board, this);
	}
}

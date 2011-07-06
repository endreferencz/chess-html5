package hu.mygame.client.chessboard;

import hu.mygame.client.dialog.CanvasNotSupportedDialog;
import hu.mygame.shared.Board;
import hu.mygame.shared.Position;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ChessBoard extends Composite {

	interface ChessUiBinder extends UiBinder<Widget, ChessBoard> {
	}
	private static ImageLoader imageLoader = null;
	private static ChessUiBinder uiBinder = GWT.create(ChessUiBinder.class);
	private ActionHandler actionHandler = null;
	private Board board = null;

	private BoardDrawer boardDrawer = null;

	@UiField
	Canvas canvas;

	@UiField
	Label label;

	public ChessBoard(Long gameId, Board board) {
		initWidget(uiBinder.createAndBindUi(this));
		this.board = board;
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

	public void highlite(ArrayList<Position> available) {
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
		label.setText(board.getState().toString());
	}

	public void setBoard(Board result) {
		this.board = result;
		actionHandler.setBoard(result);
		refresh();
	}

}

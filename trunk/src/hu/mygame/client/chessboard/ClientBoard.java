package hu.mygame.client.chessboard;

import hu.mygame.client.dialog.PromotionDialog;
import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.pieces.Piece;

public class ClientBoard extends Board {
	private static final long serialVersionUID = 1L;
	private ActionHandler actionHandler = null;

	public ClientBoard(Board board, ActionHandler actionHandler) {
		super();
		this.actionHandler = actionHandler;
		pieces = board.getPieces();
		for (Piece p : pieces) {
			p.setBoard(this);
		}
		savedPiece = null;
		redoPiece = null;
		redoPosition = new Position();
		state = board.getState();
		side = board.getSide();
		step = board.getStep();
	}

	@Override
	public void promote(Position selected, Position target) {
		new PromotionDialog(selected, target, actionHandler);
	}
}

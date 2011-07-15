package hu.mygame.shared.moves;

import java.io.Serializable;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Piece;

public abstract class Move implements Serializable {
	private static final long serialVersionUID = 1L;
	private int lastMoved = -1;
	private int lastMovedStep = -1;

	public abstract Position getHighlitePosition();
	public abstract void makeMove(Board board);
	public abstract void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException;
	public abstract boolean isPromotion();
	public abstract Position getFrom();
	public abstract void undoMove(Board board);

	protected void makeMoved(Piece piece, Board board) {
		lastMoved = piece.getMoved();
		lastMovedStep = piece.getMovedStep();
		piece.makeMoved(board.getStep());
	}

	protected void undoMakeMoved(Piece piece) {
		piece.setMoved(lastMoved);
		piece.setMovedStep(lastMovedStep);
	}
}

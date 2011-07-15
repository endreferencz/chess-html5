package hu.mygame.shared.moves;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Piece;

public class SimpleMove extends Move {
	private static final long serialVersionUID = 1L;
	protected Position from = null;
	protected Position to = null;

	public SimpleMove() {

	}
	public SimpleMove(Position from, Position to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public Position getHighlitePosition() {
		return to;
	}

	@Override
	public void makeMove(Board board) {
		Piece piece = board.getPiece(from);
		makeMoved(piece, board);
		piece.setPosition(to);
	}

	@Override
	public void undoMove(Board board) {
		Piece piece = board.getPiece(to);
		undoMakeMoved(piece);
		piece.setPosition(from);
	}

	@Override
	public void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException {
		throw new IllegalOperationException("SimpleMove >> No promotionpiece.");
	}

	@Override
	public boolean isPromotion() {
		return false;
	}
	@Override
	public Position getFrom() {
		return from;
	}
}

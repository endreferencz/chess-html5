package hu.mygame.shared.moves;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Piece;

public class CastleMove implements Move {
	private static final long serialVersionUID = 1L;
	Position fromKing = null;
	Position toKing = null;
	Position fromRook = null;
	Position toRook = null;

	public CastleMove() {

	}
	public CastleMove(Position fromKing, Position toKing, Position fromRook, Position toRook) {
		this.fromKing = fromKing;
		this.toKing = toKing;
		this.fromRook = fromRook;
		this.toRook = toRook;
	}

	@Override
	public Position getHighlitePosition() {
		return toKing;
	}

	@Override
	public void makeMove(Board board) {
		Piece king = board.getPiece(fromKing);
		Piece rook = board.getPiece(fromRook);
		king.setPosition(toKing);
		rook.setPosition(toRook);
	}

	@Override
	public void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException {
		throw new IllegalOperationException("CastleMove >> No promotionpiece.");
	}

	@Override
	public boolean isPromotion() {
		return false;
	}
	@Override
	public Position getFrom() {
		return fromKing;
	}
}

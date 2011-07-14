package hu.mygame.shared.moves;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Piece;

public class AttackMove extends SimpleMove {
	private static final long serialVersionUID = 1L;

	public AttackMove() {
	}

	public AttackMove(Position from, Position to) {
		super(from, to);
	}

	@Override
	public void makeMove(Board board) {
		Piece piece = board.getPiece(from);
		Piece attackedPiece = board.getPiece(to);

		board.remove(attackedPiece);
		piece.setPosition(to);
	}

	@Override
	public void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException {
		throw new IllegalOperationException("AttackMove >> No promotionpiece.");
	}
}

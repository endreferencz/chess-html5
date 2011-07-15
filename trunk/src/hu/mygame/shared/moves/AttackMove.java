package hu.mygame.shared.moves;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Piece;

public class AttackMove extends SimpleMove {
	private static final long serialVersionUID = 1L;
	private Piece removedPiece = null;
	private Position remove = null;

	public AttackMove() {
	}

	public AttackMove(Position from, Position to, Position remove) {
		super(from, to);
		this.remove = remove;
	}

	@Override
	public void makeMove(Board board) {
		Piece piece = board.getPiece(from);
		makeMoved(piece, board);
		
		Piece attackedPiece = board.getPiece(remove);
		removedPiece = attackedPiece;
		board.remove(attackedPiece);
		
		piece.setPosition(to);
	}
	@Override
	public void undoMove(Board board) {
		Piece piece = board.getPiece(to);
		piece.setPosition(from);
		undoMakeMoved(piece);

		removedPiece.setPosition(remove);
		board.add(removedPiece);
	}

	@Override
	public void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException {
		throw new IllegalOperationException("AttackMove >> No promotionpiece.");
	}
}

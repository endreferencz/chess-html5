package hu.mygame.shared.moves;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.pieces.Bishop;
import hu.mygame.shared.pieces.Knight;
import hu.mygame.shared.pieces.Piece;
import hu.mygame.shared.pieces.Queen;
import hu.mygame.shared.pieces.Rook;

public class AttackMoveWithPromotion extends SimpleMoveWithPromotion {
	private static final long serialVersionUID = 1L;

	Piece removedPiece = null;
	Piece promotedPiece = null;

	public AttackMoveWithPromotion() {

	}
	public AttackMoveWithPromotion(Position from, Position to, PromotionPiece promotionPiece) {
		super(from, to, promotionPiece);
	}

	@Override
	public void makeMove(Board board) {
		Piece piece = board.getPiece(from);
		makeMoved(piece, board);
		promotedPiece = piece;
		board.remove(piece);

		Piece attackedPiece = board.getPiece(to);
		removedPiece = attackedPiece;
		board.remove(attackedPiece);

		Piece newPiece = null;
		switch (promotionPiece) {
			case QUEEN :
				newPiece = new Queen(to, piece.isWhite());
				break;
			case ROOK :
				newPiece = new Rook(to, piece.isWhite());
				break;
			case KNIGHT :
				newPiece = new Knight(to, piece.isWhite());
				break;
			case BISHOP :
				newPiece = new Bishop(to, piece.isWhite());
				break;
		}
		board.add(newPiece);
	}

	@Override
	public void undoMove(Board board) {
		Piece piece = board.getPiece(to);
		board.remove(piece);

		promotedPiece.setPosition(from);
		undoMakeMoved(promotedPiece);
		board.add(promotedPiece);

		removedPiece.setPosition(to);
		board.add(removedPiece);
	}

	@Override
	public void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException {
		this.promotionPiece = promotionPiece;
	}

	@Override
	public boolean isPromotion() {
		return true;
	}
}

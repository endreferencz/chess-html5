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

public class SimpleMoveWithPromotion extends SimpleMove {
	private static final long serialVersionUID = 1L;
	protected PromotionPiece promotionPiece = null;
	protected Piece promotedPiece = null;
	public SimpleMoveWithPromotion() {

	}
	public SimpleMoveWithPromotion(Position from, Position to, PromotionPiece promotionPiece) {
		super(from, to);
		this.promotionPiece = promotionPiece;
	}

	@Override
	public void makeMove(Board board) {
		Piece piece = board.getPiece(from);
		makeMoved(piece, board);
		promotedPiece = piece;
		board.remove(piece);
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

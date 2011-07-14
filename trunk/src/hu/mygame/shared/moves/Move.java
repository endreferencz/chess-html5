package hu.mygame.shared.moves;

import java.io.Serializable;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;

public interface Move extends Serializable {
	Position getHighlitePosition();
	void makeMove(Board board);
	void setPromotionPiece(PromotionPiece promotionPiece) throws IllegalOperationException;
	boolean isPromotion();
	Position getFrom();
}

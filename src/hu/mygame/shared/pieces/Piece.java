package hu.mygame.shared.pieces;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.moves.AttackMove;
import hu.mygame.shared.moves.AttackMoveWithPromotion;
import hu.mygame.shared.moves.CastleMove;
import hu.mygame.shared.moves.Move;
import hu.mygame.shared.moves.SimpleMove;
import hu.mygame.shared.moves.SimpleMoveWithPromotion;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable {
	private static ArrayList<ArrayList<Position>> allDeltasBlack = null;
	private static ArrayList<ArrayList<Position>> allDeltasWhite = null;
	private static final long serialVersionUID = 1L;
	public static ArrayList<ArrayList<Position>> getAllDeltas(boolean white) {
		if (white) {
			if (allDeltasWhite == null) {
				Queen queen = new Queen();
				Knight knight = new Knight();
				Pawn pawn = new Pawn();
				pawn.setWhite(white);
				allDeltasWhite = new ArrayList<ArrayList<Position>>();
				allDeltasWhite.addAll(queen.getAttackDeltas());
				allDeltasWhite.addAll(knight.getAttackDeltas());
				allDeltasWhite.addAll(pawn.getAttackDeltas());
			}
			return allDeltasWhite;
		} else {
			if (allDeltasBlack == null) {
				Queen queen = new Queen();
				Knight knight = new Knight();
				Pawn pawn = new Pawn();
				pawn.setWhite(white);
				allDeltasBlack = new ArrayList<ArrayList<Position>>();
				allDeltasBlack.addAll(queen.getAttackDeltas());
				allDeltasBlack.addAll(knight.getAttackDeltas());
				allDeltasBlack.addAll(pawn.getAttackDeltas());
			}
			return allDeltasBlack;
		}
	}

	protected int moved = 0;
	protected int movedStep = -1;
	protected Position position;

	protected boolean white;
	public Piece() {
	}
	public Piece(Position position, boolean white) {
		this.position = position;
		this.white = white;
	}
	public boolean enPassant(int step) {
		return false;
	}

	public ArrayList<ArrayList<Position>> getAttackDeltas() {
		return getDeltas();
	}

	public ArrayList<Move> getAvailableMoves(Board board) {
		ArrayList<Move> ret = new ArrayList<Move>();
		Position temp = new Position();
		King king = board.getKing(isWhite());

		for (ArrayList<Position> array : getDeltas()) {
			for (Position d : array) {
				temp.copy(position);
				temp.add(d);
				if (temp.isValid()) {
					Piece piece = board.getPiece(temp);
					if (piece == null) {
						board.moveWithUndo(this, temp);
						if (!board.isPositionAttackedBy(king.getPosition(), !isWhite())) {
							Move move;
							if (isPawn() && (temp.getRow() == 0 || temp.getRow() == 7)) {
								move = new SimpleMoveWithPromotion(this.getPosition(), new Position(temp.getRow(),
										temp.getColumn()), null);
							} else {
								move = new SimpleMove(this.getPosition(), new Position(temp.getRow(), temp.getColumn()));
							}
							ret.add(move);
						}
						board.undo();
					}
					if (piece != null) {
						break;
					}
				}
			}
		}

		for (ArrayList<Position> array : getAttackDeltas()) {
			for (Position d : array) {
				temp.copy(position);
				temp.add(d);
				if (temp.isValid()) {
					Piece piece = board.getPiece(temp);
					if ((piece != null) && (piece.isWhite() != isWhite())) {
						board.moveWithUndo(this, temp);
						if (!board.isPositionAttackedBy(king.getPosition(), !isWhite())) {
							Move move;
							if (isPawn() && (temp.getRow() == 0 || temp.getRow() == 7)) {
								move = new AttackMoveWithPromotion(this.getPosition(), new Position(temp.getRow(),
										temp.getColumn()), null);
							} else {
								move = new AttackMove(this.getPosition(), new Position(temp.getRow(), temp.getColumn()));
							}
							ret.add(move);
						}
						board.undo();
					}
					if (piece != null) {
						break;
					}
				}
			}
		}

		// Castling
		if (isKing() && (moved == 0)) {
			if (white) {
				Piece piece = board.getPiece(new Position(0, 7));
				if (piece != null && piece.isRookAndNotMoved() && board.free(0, 5) && board.free(0, 6)
						&& !board.isPositionAttackedBy(new Position(0, 5), !white)
						&& !board.isPositionAttackedBy(new Position(0, 6), !white)) {
					Move move = new CastleMove(this.getPosition(), new Position(0, 6), piece.getPosition(),
							new Position(0, 5));
					ret.add(move);
				}
				piece = board.getPiece(new Position(0, 0));
				if (piece != null && piece.isRookAndNotMoved() && board.free(0, 1) && board.free(0, 2)
						&& board.free(0, 3) && !board.isPositionAttackedBy(new Position(0, 2), !white)
						&& !board.isPositionAttackedBy(new Position(0, 3), !white)) {
					Move move = new CastleMove(this.getPosition(), new Position(0, 2), piece.getPosition(),
							new Position(0, 3));
					ret.add(move);
				}
			} else {
				Piece piece = board.getPiece(new Position(7, 7));
				if (piece != null && piece.isRookAndNotMoved() && board.free(7, 5) && board.free(7, 6)
						&& !board.isPositionAttackedBy(new Position(7, 5), !white)
						&& !board.isPositionAttackedBy(new Position(7, 6), !white)) {
					Move move = new CastleMove(this.getPosition(), new Position(7, 6), piece.getPosition(),
							new Position(7, 5));
					ret.add(move);
				}
				piece = board.getPiece(new Position(7, 0));
				if (piece != null && piece.isRookAndNotMoved() && board.free(7, 1) && board.free(7, 2)
						&& board.free(7, 3) && !board.isPositionAttackedBy(new Position(7, 2), !white)
						&& !board.isPositionAttackedBy(new Position(7, 3), !white)) {
					Move move = new CastleMove(this.getPosition(), new Position(7, 2), piece.getPosition(),
							new Position(7, 3));
					ret.add(move);
				}
			}
		}

		// en passant
		// TODO nem kerul sakkba a kiraly?!
		if (isPawn()) {
			Piece piece = board.getPiece(new Position(position.getRow(), position.getColumn() - 1));
			if ((piece != null) && (piece.isWhite() != isWhite()) && piece.enPassant(board.getStep())) {
				if (isWhite()) {
					Move move = new AttackMove(this.getPosition(), new Position(position.getRow() + 1,
							position.getColumn() - 1));
					ret.add(move);
				} else {
					Move move = new AttackMove(this.getPosition(), new Position(position.getRow() - 1,
							position.getColumn() - 1));
					ret.add(move);
				}
			}
			piece = board.getPiece(new Position(position.getRow(), position.getColumn() + 1));
			if ((piece != null) && (piece.isWhite() != isWhite()) && piece.enPassant(board.getStep())) {
				if (isWhite()) {
					Move move = new AttackMove(this.getPosition(), new Position(position.getRow() + 1,
							position.getColumn() + 1));
					ret.add(move);
				} else {
					Move move = new AttackMove(this.getPosition(), new Position(position.getRow() - 1,
							position.getColumn() + 1));
					ret.add(move);
				}
			}
		}

		if (ret.isEmpty())
			return null;
		else
			return ret;
	}

	public abstract ArrayList<ArrayList<Position>> getDeltas();
	public String getImageName() {
		return "";
	}

	public Position getPosition() {
		return position;
	}

	public boolean isBishop() {
		return false;
	}

	public boolean isBlack() {
		return !white;
	}

	public boolean isKing() {
		return false;
	}

	public boolean isKnight() {
		return false;
	}

	public boolean isPawn() {
		return false;
	}

	public boolean isRookAndNotMoved() {
		return false;
	}

	public boolean isWhite() {
		return white;
	}

	public void makeMoved(int step) {
		moved++;
		this.movedStep = step;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}
}

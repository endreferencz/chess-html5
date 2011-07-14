package hu.mygame.shared;

import hu.mygame.shared.moves.Move;
import hu.mygame.shared.pieces.Bishop;
import hu.mygame.shared.pieces.King;
import hu.mygame.shared.pieces.Knight;
import hu.mygame.shared.pieces.Pawn;
import hu.mygame.shared.pieces.Piece;
import hu.mygame.shared.pieces.Queen;
import hu.mygame.shared.pieces.Rook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

	private String whitePlayer = null;
	private String blackPlayer = null;
	protected ArrayList<Piece> pieces = new ArrayList<Piece>();
	protected Piece redoPiece = null;
	protected Position redoPosition = new Position();
	protected Piece savedPiece = null;
	protected Side side = Side.WHITE;
	protected State state = State.WHITE_TURN;
	protected int step = 0;

	public Board() {
	}

	public Board(boolean init) {
		if (init) {
			pieces.add(new Rook(new Position(0, 0), true));
			pieces.add(new Knight(new Position(0, 1), true));
			pieces.add(new Bishop(new Position(0, 2), true));
			pieces.add(new Queen(new Position(0, 3), true));
			pieces.add(new King(new Position(0, 4), true));
			pieces.add(new Bishop(new Position(0, 5), true));
			pieces.add(new Knight(new Position(0, 6), true));
			pieces.add(new Rook(new Position(0, 7), true));

			for (int i = 0; i < 8; i++) {
				pieces.add(new Pawn(new Position(1, i), true));
			}

			for (int i = 0; i < 8; i++) {
				pieces.add(new Pawn(new Position(6, i), false));
			}

			pieces.add(new Rook(new Position(7, 0), false));
			pieces.add(new Knight(new Position(7, 1), false));
			pieces.add(new Bishop(new Position(7, 2), false));
			pieces.add(new Queen(new Position(7, 3), false));
			pieces.add(new King(new Position(7, 4), false));
			pieces.add(new Bishop(new Position(7, 5), false));
			pieces.add(new Knight(new Position(7, 6), false));
			pieces.add(new Rook(new Position(7, 7), false));
		}
	}

	public void add(Piece piece) {
		if (piece != null) {
			pieces.add(piece);
		}
	}

	public boolean free(int row, int column) {
		// TODO optimize
		Position position = new Position(row, column);
		return free(position);
	}

	public boolean free(Position position) {
		return (getPiece(position) == null);
	}

	public ArrayList<Move> getAvailableMoves(Piece piece) {
		if (piece == null) {
			return null;
		} else {
			return piece.getAvailableMoves(this);
		}
	}

	public ArrayList<Move> getAvailableMoves(Position position) {
		Piece piece = getPiece(position);
		if (piece != null) {
			if ((piece.isWhite() && side == Side.WHITE) || (piece.isBlack() && side == Side.BLACK)) {
				return getAvailableMoves(piece);
			}
		}
		return null;
	}

	public King getKing(boolean white) {
		for (Piece p : pieces) {
			if (p.isKing() && (p.isWhite() == white)) {
				return (King) p;
			}
		}
		return null;
	}

	public Piece getPiece(Position position) {
		for (Piece p : pieces) {
			if (p.getPosition().equals(position)) {
				return p;
			}
		}
		return null;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public Piece getRedoPiece() {
		return redoPiece;
	}

	public Position getRedoPosition() {
		return redoPosition;
	}
	public Piece getSavedPiece() {
		return savedPiece;
	}

	public Side getSide() {
		return side;
	}

	public State getState() {
		return state;
	}

	public int getStep() {
		return step;
	}

	public boolean isMyTurn() {
		if (side == Side.WHITE && state == State.WHITE_TURN)
			return true;
		if (side == Side.WHITE && state == State.WHITE_TURN_CHESS)
			return true;
		if (side == Side.BLACK && state == State.BLACK_TURN)
			return true;
		if (side == Side.BLACK && state == State.BLACK_TURN_CHESS)
			return true;
		return false;
	}

	public boolean isPositionAttackedBy(Position position, boolean white) {
		Position temp = new Position();
		temp.copy(position);
		Piece piece = null;

		for (ArrayList<Position> array : Piece.getAllDeltas(white)) {
			for (Position delta : array) {
				temp.copy(position);
				temp.add(delta);
				if (temp.isValid()) {
					if ((piece = getPiece(temp)) != null) {
						if (piece.isWhite() != white) {
							break;
						}
						for (ArrayList<Position> array2 : piece.getAttackDeltas()) {
							if (array2.contains(delta.getInvert())) {
								return true;
							}
						}
						break;
					}
				} else {
					break;
				}
			}
		}
		return false;
	}

	boolean isStaleMate(King king) {
		List<Move> available = king.getAvailableMoves(this);
		if (available != null)
			return false;
		List<Piece> copy = new ArrayList<Piece>();
		for (Piece p : pieces) {
			copy.add(p);
		}
		for (Piece p : copy) {
			if (p.isWhite() == king.isWhite()) {
				available = p.getAvailableMoves(this);
				if (available != null)
					return false;
			}
		}
		return true;
	}

	public void movePiece(Move move) {
		// TODO ELLENORZES

		Piece piece = getPiece(move.getFrom());

		if (piece.isWhite()) {
			if (state == State.WHITE_TURN || state == State.WHITE_TURN_CHESS) {
				state = State.BLACK_TURN;
			} else {
				return;
			}
		} else {
			if (state == State.BLACK_TURN || state == State.BLACK_TURN_CHESS) {
				state = State.WHITE_TURN;
			} else {
				return;
			}
		}

		piece.makeMoved(step);
		step++;
		move.makeMove(this);

		Boolean white;
		if (state == State.BLACK_TURN) {
			white = false;
		} else {
			white = true;
		}

		King king = getKing(white);
		if (isPositionAttackedBy(king.getPosition(), !white)) {
			if (white) {
				if (isStaleMate(king)) {
					state = State.BLACK_WIN;
					return;
				} else {
					state = State.WHITE_TURN_CHESS;
					return;
				}
			} else {
				if (isStaleMate(king)) {
					state = State.WHITE_WIN;
					return;
				} else {
					state = State.BLACK_TURN_CHESS;
					return;
				}
			}
		}
		if ((pieces.size() == 2) || isStaleMate(king)) {
			state = State.DRAW;
			return;
		}

		if (pieces.size() == 3) {
			for (Piece p : pieces) {
				if (p.isBishop() || p.isKnight()) {
					state = State.DRAW;
					return;
				}
			}
		}
		if (pieces.size() == 4) {
			Position pos1 = null;
			Position pos2 = null;
			for (Piece p : pieces) {
				if (!(p.isBishop() || p.isKing())) {
					return;
				} else if (p.isBishop()) {
					if (pos1 != null) {
						pos2 = p.getPosition();
					} else {
						pos1 = p.getPosition();
					}
				}
			}
			if (((pos1.getColumn() + pos1.getRow()) % 2) == ((pos2.getColumn() + pos2.getRow()) % 2)) {
				state = State.DRAW;
				return;
			}
		}
		return;
	}

	public void moveWithUndo(Piece piece, Position target) {
		redoPiece = piece;
		Piece targetPiece = getPiece(target);
		if (targetPiece != null) {
			savedPiece = targetPiece;
			remove(targetPiece);
		}
		redoPosition.copy(piece.getPosition());
		piece.getPosition().setColumn(target.getColumn());
		piece.getPosition().setRow(target.getRow());
	}

	protected void promote(Position selected, Position target) {
		// ERROR
	}

	public void remove(Piece piece) {
		if (piece != null) {
			pieces.remove(piece);
		}
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public void undo() {
		if (redoPiece != null) {
			redoPiece.getPosition().copy(redoPosition);
			if (savedPiece != null) {
				add(savedPiece);
			}
			redoPiece = null;
			savedPiece = null;
		}
	}

	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public String getWhitePlayer() {
		return whitePlayer;
	}

	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public String getBlackPlayer() {
		return blackPlayer;
	}
}

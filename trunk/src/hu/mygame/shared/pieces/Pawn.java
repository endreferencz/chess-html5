package hu.mygame.shared.pieces;

import hu.mygame.shared.Position;

import java.util.ArrayList;

public class Pawn extends Piece {
	private static final String blackImage = "black_pawn.png";

	public static ArrayList<ArrayList<Position>> deltasBlack = null;
	public static ArrayList<ArrayList<Position>> deltasBlackAttack = null;

	public static ArrayList<ArrayList<Position>> deltasBlackFirst = null;
	public static ArrayList<ArrayList<Position>> deltasWhite = null;
	public static ArrayList<ArrayList<Position>> deltasWhiteAttack = null;
	public static ArrayList<ArrayList<Position>> deltasWhiteFirst = null;
	private static final long serialVersionUID = 1L;
	private static final String whiteImage = "white_pawn.png";

	public Pawn() {
	}

	public Pawn(Position position, boolean white) {
		super(position, white);
	}
	@Override
	public boolean enPassant(int step) {
		if (step != (movedStep + 1)) {
			return false;
		}
		if (moved != 1) {
			return false;
		}
		if (isWhite() && (position.getRow() != 3)) {
			return false;
		}
		if (isBlack() && (position.getRow() != 4)) {
			return false;
		}
		return true;
	}
	@Override
	public ArrayList<ArrayList<Position>> getAttackDeltas() {
		if (isWhite()) {
			if (deltasWhiteAttack == null) {
				deltasWhiteAttack = new ArrayList<ArrayList<Position>>();
				deltasWhiteAttack.add(new ArrayList<Position>());
				deltasWhiteAttack.add(new ArrayList<Position>());
				deltasWhiteAttack.get(0).add(new Position(1, 1));
				deltasWhiteAttack.get(1).add(new Position(1, -1));
			}
			return deltasWhiteAttack;
		} else {
			if (deltasBlackAttack == null) {
				deltasBlackAttack = new ArrayList<ArrayList<Position>>();
				deltasBlackAttack.add(new ArrayList<Position>());
				deltasBlackAttack.add(new ArrayList<Position>());
				deltasBlackAttack.get(0).add(new Position(-1, -1));
				deltasBlackAttack.get(1).add(new Position(-1, 1));
			}
			return deltasBlackAttack;
		}
	}

	@Override
	public ArrayList<ArrayList<Position>> getDeltas() {
		if (isWhite()) {
			if (getPosition().getRow() == 1) {
				return getDeltasWhiteFirst();
			} else {
				return getDeltasWhite();
			}
		} else {
			if (getPosition().getRow() == 6) {
				return getDeltasBlackFirst();
			} else {
				return getDeltasBlack();
			}
		}
	}

	private ArrayList<ArrayList<Position>> getDeltasBlack() {
		if (deltasBlack == null) {
			deltasBlack = new ArrayList<ArrayList<Position>>();
			deltasBlack.add(new ArrayList<Position>());
			deltasBlack.get(0).add(new Position(-1, 0));
		}
		return deltasBlack;
	}

	private ArrayList<ArrayList<Position>> getDeltasBlackFirst() {
		if (deltasBlackFirst == null) {
			deltasBlackFirst = new ArrayList<ArrayList<Position>>();
			deltasBlackFirst.add(new ArrayList<Position>());
			deltasBlackFirst.get(0).add(new Position(-1, 0));
			deltasBlackFirst.get(0).add(new Position(-2, 0));
		}
		return deltasBlackFirst;
	}

	private ArrayList<ArrayList<Position>> getDeltasWhite() {
		if (deltasWhite == null) {
			deltasWhite = new ArrayList<ArrayList<Position>>();
			deltasWhite.add(new ArrayList<Position>());
			deltasWhite.get(0).add(new Position(1, 0));
		}
		return deltasWhite;
	}

	private ArrayList<ArrayList<Position>> getDeltasWhiteFirst() {
		if (deltasWhiteFirst == null) {
			deltasWhiteFirst = new ArrayList<ArrayList<Position>>();
			deltasWhiteFirst.add(new ArrayList<Position>());
			deltasWhiteFirst.get(0).add(new Position(1, 0));
			deltasWhiteFirst.get(0).add(new Position(2, 0));
		}
		return deltasWhiteFirst;
	}

	@Override
	public String getImageName() {
		return white ? whiteImage : blackImage;
	}

	@Override
	public boolean isPawn() {
		return true;
	}

}

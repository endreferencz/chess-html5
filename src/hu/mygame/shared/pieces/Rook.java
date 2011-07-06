package hu.mygame.shared.pieces;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;

import java.util.ArrayList;

public class Rook extends Piece {
	private static final String blackImage = "black_rook.png";

	public static ArrayList<ArrayList<Position>> deltas = null;
	private static final long serialVersionUID = 1L;
	private static final String whiteImage = "white_rook.png";

	public Rook() {

	}
	public Rook(Position position, boolean white, Board board) {
		super(position, white, board);
	}
	@Override
	public ArrayList<ArrayList<Position>> getDeltas() {
		if (deltas == null) {
			deltas = new ArrayList<ArrayList<Position>>();
			for (int i = 0; i < 4; i++) {
				deltas.add(new ArrayList<Position>());
			}
			for (int i = 1; i <= 7; i++) {
				deltas.get(0).add(new Position(i, 0));
				deltas.get(1).add(new Position(0, i));
				deltas.get(2).add(new Position(-i, 0));
				deltas.get(3).add(new Position(0, -i));
			}
		}
		return deltas;
	}

	@Override
	public String getImageName() {
		return white ? whiteImage : blackImage;
	}
	@Override
	public boolean isRookAndNotMoved() {
		return (moved == 0);
	}
}

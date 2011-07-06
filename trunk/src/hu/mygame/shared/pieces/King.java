package hu.mygame.shared.pieces;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;

import java.util.ArrayList;

public class King extends Piece {
	private static final String blackImage = "black_king.png";

	public static ArrayList<ArrayList<Position>> deltas = null;
	private static final long serialVersionUID = 1L;

	private static final String whiteImage = "white_king.png";
	public King() {

	}
	public King(Position position, boolean white, Board board) {
		super(position, white, board);
	}

	@Override
	public ArrayList<ArrayList<Position>> getDeltas() {
		if (deltas == null) {
			deltas = new ArrayList<ArrayList<Position>>();

			deltas.add(new ArrayList<Position>());
			deltas.get(0).add(new Position(-1, -1));

			deltas.add(new ArrayList<Position>());
			deltas.get(1).add(new Position(0, -1));

			deltas.add(new ArrayList<Position>());
			deltas.get(2).add(new Position(1, -1));

			deltas.add(new ArrayList<Position>());
			deltas.get(3).add(new Position(-1, 0));

			deltas.add(new ArrayList<Position>());
			deltas.get(4).add(new Position(1, 0));

			deltas.add(new ArrayList<Position>());
			deltas.get(5).add(new Position(-1, 1));

			deltas.add(new ArrayList<Position>());
			deltas.get(6).add(new Position(0, 1));

			deltas.add(new ArrayList<Position>());
			deltas.get(7).add(new Position(1, 1));
		}
		return deltas;
	}

	@Override
	public String getImageName() {
		return white ? whiteImage : blackImage;
	}

	@Override
	public boolean isKing() {
		return true;
	}
}

package hu.mygame.shared.pieces;

import hu.mygame.shared.Position;

import java.util.ArrayList;

public class Queen extends Piece {
	private static final String blackImage = "black_queen.png";

	public static ArrayList<ArrayList<Position>> deltas = null;
	private static final long serialVersionUID = 1L;
	private static final String whiteImage = "white_queen.png";

	public Queen() {
	}

	public Queen(Position position, boolean white) {
		super(position, white);
	}

	@Override
	public ArrayList<ArrayList<Position>> getDeltas() {
		if (deltas == null) {
			deltas = new ArrayList<ArrayList<Position>>();
			for (int i = 0; i < 8; i++) {
				deltas.add(new ArrayList<Position>());
			}
			for (int i = 1; i <= 7; i++) {
				deltas.get(0).add(new Position(i, i));
				deltas.get(1).add(new Position(-i, i));
				deltas.get(2).add(new Position(i, -i));
				deltas.get(3).add(new Position(-i, -i));
				deltas.get(4).add(new Position(i, 0));
				deltas.get(5).add(new Position(0, i));
				deltas.get(6).add(new Position(-i, 0));
				deltas.get(7).add(new Position(0, -i));
			}
		}
		return deltas;
	}

	@Override
	public String getImageName() {
		return white ? whiteImage : blackImage;
	}
}

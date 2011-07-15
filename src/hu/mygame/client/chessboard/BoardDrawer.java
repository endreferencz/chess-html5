package hu.mygame.client.chessboard;

import hu.mygame.shared.Board;
import hu.mygame.shared.Position;
import hu.mygame.shared.Side;
import hu.mygame.shared.State;
import hu.mygame.shared.moves.Move;
import hu.mygame.shared.pieces.Piece;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;

public class BoardDrawer {
	private static CssColor blackColor = CssColor.make("rgba(100,100,100,1)");
	public static int height = 45;
	private static CssColor highlite = CssColor.make("rgba(255,0,0,0.2)");
	private static CssColor whiteColor = CssColor.make("rgba(250,250,250,1)");
	private static CssColor greyColor = CssColor.make("rgba(0,0,0,0.5)");
	private static CssColor greenColor = CssColor.make("rgba(0,255,0,0.2)");
	public static int width = 45;
	private Context2d context = null;
	private ImageLoader imageLoader = null;

	private Position lastHighlite = new Position(-1, -1);

	public BoardDrawer(Context2d context, ImageLoader imageLoader) {
		this.context = context;
		this.imageLoader = imageLoader;
	}

	public void clear() {
		context.save();

		context.setFillStyle(whiteColor);
		context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());

		context.restore();
	}
	public void drawPieces(Board board) {
		context.save();
		for (Piece p : board.getPieces()) {
			ImageElement image = imageLoader.getImage(p.getImageName());
			if (image != null) {
				if (board.getSide() == Side.WHITE)
					context.drawImage(image, p.getPosition().getColumn() * width, 7 * height - p.getPosition().getRow()
							* height);
				else
					context.drawImage(image, 7 * width - p.getPosition().getColumn() * width, p.getPosition().getRow()
							* height);
			}

		}
		context.restore();
	}

	public void drawGrid() {
		context.save();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (((i + j) % 2) == 0) {
					context.setFillStyle(whiteColor);
				} else {
					context.setFillStyle(blackColor);
				}
				context.fillRect(i * width, j * height, width, height);
			}
		}
		context.restore();
	}

	public void highLiteLastMove(Board board) {
		context.save();
		if (!board.getMoveHistory().isEmpty()) {
			Position p = board.getMoveHistory().getLast().getHighlitePosition();
			context.setFillStyle(greenColor);
			if (board.getSide() == Side.WHITE)
				context.fillRect(p.getColumn() * width, (7 - p.getRow()) * height, width, height);
			else
				context.fillRect((7 - p.getColumn()) * width, (p.getRow()) * height, width, height);
		}
		context.restore();
	}

	public void hideIfUndo(Board board) {
		context.save();
		if (board.getState() == State.WHITE_REQUESTED_UNDO || board.getState() == State.BLACK_REQUESTED_UNDO) {
			context.setFillStyle(greyColor);
			context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
		}
		context.restore();
	}

	public void drawTable(Board board) {
		clear();
		drawGrid();
		highLiteLastMove(board);
		drawPieces(board);
		hideIfUndo(board);
	}

	public void highlite(ArrayList<Move> positions, Board board) {
		if (!lastHighlite.equals(positions)) {
			clear();
			drawGrid();
			highLiteLastMove(board);
			context.save();
			context.setFillStyle(highlite);
			for (Move m : positions) {
				Position p = m.getHighlitePosition();
				if (board.getSide() == Side.WHITE)
					context.fillRect(p.getColumn() * width, (7 - p.getRow()) * height, width, height);
				else
					context.fillRect((7 - p.getColumn()) * width, (p.getRow()) * height, width, height);

			}
			context.restore();
			drawPieces(board);
		}
	}

	public void refresh() {

	}
}

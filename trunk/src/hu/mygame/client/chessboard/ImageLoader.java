package hu.mygame.client.chessboard;


import java.util.HashMap;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class ImageLoader {

	private static String[] images = {"black_bishop.png", "black_king.png", "black_knight.png", "black_pawn.png",
			"black_queen.png", "black_rook.png", "white_bishop.png", "white_king.png", "white_knight.png",
			"white_pawn.png", "white_queen.png", "white_rook.png"};
	private static HashMap<String, ImageElement> map = new HashMap<String, ImageElement>();
	private int loaded = 0;

	public ImageLoader(final ChessBoard chessBoard) {
		loaded = 0;
		for (final String imageName : images) {
			if (map.get(imageName) == null) {
				final Image image = new Image(imageName);
				image.addLoadHandler(new LoadHandler() {
					@Override
					public void onLoad(LoadEvent event) {
						map.put(imageName, (ImageElement) image.getElement().cast());
						loaded++;
						if (loaded == 12) {
							chessBoard.refresh();
						}
					}
				});
				image.setVisible(false);
				RootPanel.get().add(image);
			}
		}
	}

	public ImageElement getImage(String imageName) {
		return map.get(imageName);
	}
}

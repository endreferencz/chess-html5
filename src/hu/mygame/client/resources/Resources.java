package hu.mygame.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	@Source("top_image.png")
	ImageResource top_image();
	@Source("white_bishop.png")
	ImageResource white_bishop();
	@Source("white_knight.png")
	ImageResource white_knight();
	@Source("white_queen.png")
	ImageResource white_queen();
	@Source("white_rook.png")
	ImageResource white_rook();
}

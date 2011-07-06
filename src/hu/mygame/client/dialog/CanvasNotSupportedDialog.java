package hu.mygame.client.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CanvasNotSupportedDialog extends DialogBox{

	interface CanvasNotSupportedDialogUiBinder extends UiBinder<Widget, CanvasNotSupportedDialog> {
	}
	private static final String CANVAS_NOT_SUPPORTED = "Your browser does not support the HTML5 Canvas.";

	
	private static final CanvasNotSupportedDialogUiBinder uiBinder = GWT.create(CanvasNotSupportedDialogUiBinder.class);

	@UiField
	Button button;

	@UiField
	DialogBox dialog;
	
	@UiField
	Label label;
	
	public CanvasNotSupportedDialog() {
        setWidget(uiBinder.createAndBindUi(this));
		label.setText(CANVAS_NOT_SUPPORTED);
		button.setText("Ok");
		dialog.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				dialog.setPopupPosition((Window.getClientWidth() - offsetWidth) / 2,
						(Window.getClientHeight() - offsetHeight) / 2);
			}
		});
	}	

	@Override
	public String getText() {
		return button.getText();
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		dialog.hide();
	}

	@Override
	public void setText(String text) {
		button.setText(text);
	}

}

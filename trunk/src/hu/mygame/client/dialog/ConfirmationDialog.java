package hu.mygame.client.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmationDialog extends DialogBox implements PositionCallback {

	private static ConfirmationDialogUiBinder uiBinder = GWT.create(ConfirmationDialogUiBinder.class);

	interface ConfirmationDialogUiBinder extends UiBinder<Widget, ConfirmationDialog> {
	}

	@UiField
	Button acceptButton;

	@UiField
	Button refuseButton;

	@UiField
	DialogBox dialog;

	ConfirmationCallback callback;

	public ConfirmationDialog(ConfirmationCallback callback) {
		setWidget(uiBinder.createAndBindUi(this));
		this.callback = callback;
		dialog.setPopupPositionAndShow(this);
	}

	@Override
	public void setPosition(int offsetWidth, int offsetHeight) {
		dialog.setPopupPosition((Window.getClientWidth() - offsetWidth) / 2,
				(Window.getClientHeight() - offsetHeight) / 2);
	}

	@UiHandler("acceptButton")
	void acceptButton(ClickEvent e) {
		dialog.hide();
		callback.accept();
	}

	@UiHandler("refuseButton")
	void refuseButton(ClickEvent e) {
		dialog.hide();
		callback.refuse();
	}

}

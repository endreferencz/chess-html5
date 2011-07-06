package hu.mygame.client.dialog;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.jdo.Player;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.Widget;

public class IncomingInvitationDialog extends DialogBox implements PositionCallback, AsyncCallback<Void> {

	interface IncomingInvitationDialogUiBinder extends UiBinder<Widget, IncomingInvitationDialog> {
	}
	private static IncomingInvitationDialogUiBinder uiBinder = GWT.create(IncomingInvitationDialogUiBinder.class);
	@UiField
	Button acceptButton;

	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	@UiField
	Button declineButton;

	@UiField
	DialogBox dialog;

	private Long invitationId = null;

	@UiField
	Label label;
	public IncomingInvitationDialog(Long invitationId, Player player) {
		setWidget(uiBinder.createAndBindUi(this));
		this.invitationId = invitationId;
		label.setText("Incoming invitation from " + player.getEmail() + ".");
		dialog.setPopupPositionAndShow(this);
	}

	@UiHandler("acceptButton")
	void onAccept(ClickEvent e) {
		dialog.hide();
		chessGameService.startGame(invitationId, this);
	}

	@UiHandler("declineButton")
	void onDecline(ClickEvent e) {
		dialog.hide();
		chessGameService.declineInvitation(invitationId, this);
	}
	
	@Override
	public void onFailure(Throwable caught) {
		// TODO
	}

	@Override
	public void onSuccess(Void result) {
	}

	@Override
	public void setPosition(int offsetWidth, int offsetHeight) {
		dialog.setPopupPosition((Window.getClientWidth() - offsetWidth) / 2,
				(Window.getClientHeight() - offsetHeight) / 2);
	}

}

package hu.mygame.client.dialog;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.Player;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CurrentPlayerHoverPopupPanel extends PopupPanel implements AsyncCallback<Void> {

	interface CurrentPlayerHoverPopupPanelUiBinder extends UiBinder<Widget, CurrentPlayerHoverPopupPanel> {
	}
	private static CurrentPlayerHoverPopupPanelUiBinder uiBinder = GWT
			.create(CurrentPlayerHoverPopupPanelUiBinder.class);
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	@UiField
	Button nameButton;

	@UiField
	TextBox nameTextBox;

	@UiField
	Label scoreLabel;

	public CurrentPlayerHoverPopupPanel(Player player) {
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		if (player.getName() != null) {
			nameTextBox.setText(player.getName());
		}
		scoreLabel.setText(player.getWin() + " - " + player.getDraw() + " - " + player.getLost());
	}

	@UiHandler("nameButton")
	void onNickNameButton(ClickEvent clickEvent) {
		chessGameService.changeName(nameTextBox.getText(), this);
	}
	@Override
	public void onFailure(Throwable caught) {
		// TODO
		
	}
	@Override
	public void onSuccess(Void result) {
	}
}

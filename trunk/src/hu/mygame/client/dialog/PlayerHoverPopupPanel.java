package hu.mygame.client.dialog;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.jdo.Player;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlayerHoverPopupPanel extends PopupPanel implements AsyncCallback<Void> {

	interface PlayerHoverPopupPanelUiBinder extends UiBinder<Widget, PlayerHoverPopupPanel> {
	}
	private static PlayerHoverPopupPanelUiBinder uiBinder = GWT.create(PlayerHoverPopupPanelUiBinder.class);
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	@UiField
	Label invitationState;

	@UiField
	Button inviteButton;

	private Player player = null;

	@UiField
	Label playerName;

	@UiField
	Label score;

	public PlayerHoverPopupPanel(Player player) {
		setWidget(uiBinder.createAndBindUi(this));
		sinkEvents(Event.MOUSEEVENTS);
		setAutoHideEnabled(true);
		this.player = player;
		if (player.getName() == null) {
			playerName.setText(player.getEmail());
		} else {
			playerName.setText(player.getName());
		}
		if (player.getInvited()) {
			invitationState.setText("invited");
		} else {
			invitationState.setText("");
		}
		score.setText(player.getWin() + " - " + player.getDraw() + " - " + player.getLost());
	}
	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEOUT :
				int x = event.getClientX();
				int y = event.getClientY();
				if ((x < getPopupLeft())
						|| (x > (getPopupLeft() + getOffsetWidth()) || (y < getPopupTop()) || (y > (getPopupTop() + getOffsetHeight())))) {
					hide();
				}
				break;
			default :
				super.onBrowserEvent(event);
		}
	}

	@Override
	public void onFailure(Throwable caught) {
		invitationState.setText("error");
	}
	@UiHandler("inviteButton")
	void onInvite(ClickEvent e) {
		invitationState.setText("inviting...");
		chessGameService.invite(player.getUser(), this);
	}
	@Override
	public void onSuccess(Void result) {
		invitationState.setText("invited");
	}
}

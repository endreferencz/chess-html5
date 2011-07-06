package hu.mygame.client.players;

import hu.mygame.client.dialog.CurrentPlayerHoverPopupPanel;
import hu.mygame.client.dialog.PlayerHoverPopupPanel;
import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.jdo.Player;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlayersPanel extends Composite implements AsyncCallback<List<Player>> {

	interface MyStyle extends CssResource {
		String player();
	}
	private class PlayerLabel extends Label {
		private PopupPanel hoverPanel = null;
		private Player player = null;
		private boolean currentPlayer = false;

		public PlayerLabel(Player player, boolean currentPlayer) {
			super();
			getElement().addClassName(style.player());
			sinkEvents(Event.MOUSEEVENTS);
			this.setWidth("200px");
			this.player = player;
			this.currentPlayer = currentPlayer;
			this.setText(player.getEmail());
		}

		@Override
		public void onBrowserEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN :
				case Event.ONMOUSEOVER :
					if (hoverPanel == null) {
						if (currentPlayer)
							hoverPanel = new CurrentPlayerHoverPopupPanel(player);
						else
							hoverPanel = new PlayerHoverPopupPanel(player);
					}
					hoverPanel.setPopupPosition(160, this.getAbsoluteTop());
					if (showingHoverPanel != null) {
						showingHoverPanel.hide();
					}
					showingHoverPanel = hoverPanel;
					hoverPanel.show();
					break;
				default :
					super.onBrowserEvent(event);
			}
		}
	}
	interface PlayersPanelUiBinder extends UiBinder<Widget, PlayersPanel> {
	}

	private static PlayersPanelUiBinder uiBinder = GWT.create(PlayersPanelUiBinder.class);
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	private PopupPanel showingHoverPanel = null;

	@UiField
	MyStyle style;
	@UiField
	VerticalPanel verticalPanel;

	public PlayersPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		sinkEvents(Event.MOUSEEVENTS);
		chessGameService.getPlayers(this);
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEOUT :
				int x = event.getClientX();
				int y = event.getClientY();
				if (showingHoverPanel != null
						&& ((x < getAbsoluteLeft()) || (x > (getAbsoluteLeft() + getOffsetWidth())
								|| (y < getAbsoluteTop()) || (y > (getAbsoluteTop() + getOffsetHeight()))))) {
					showingHoverPanel.hide();
					showingHoverPanel = null;
				}
				break;
			default :
				super.onBrowserEvent(event);
		}
	}
	@Override
	public void onFailure(Throwable caught) {
		// TODO
	}

	@Override
	public void onSuccess(List<Player> players) {
		if (players != null && players.size() > 0) {
			Iterator<Player> iterator = players.iterator();
			if (iterator.hasNext()) {
				verticalPanel.add(new PlayerLabel(iterator.next(), true));
			}
			while (iterator.hasNext()) {
				verticalPanel.add(new PlayerLabel(iterator.next(), false));
			}
		}
	}
}

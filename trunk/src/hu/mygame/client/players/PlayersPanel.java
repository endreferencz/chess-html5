package hu.mygame.client.players;

import hu.mygame.client.dialog.CurrentPlayerHoverPopupPanel;
import hu.mygame.client.dialog.PlayerHoverPopupPanel;
import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;
import hu.mygame.shared.jdo.Player;

import java.util.HashMap;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlayersPanel extends Composite implements AsyncCallback<List<Player>> {

	private List<Player> players = null;

	interface MyStyle extends CssResource {
		String player();
	}
	private class PlayerWidget extends HorizontalPanel {
		private PopupPanel hoverPanel = null;
		private Player player = null;
		private boolean currentPlayer = false;

		public PlayerWidget(Player player, boolean currentPlayer) {
			super();
			sinkEvents(Event.MOUSEEVENTS);
			// this.setWidth("200px");
			this.player = player;
			this.currentPlayer = currentPlayer;
			if (player.isOnline() || currentPlayer) {
				add(new Image("icons.png", 0, 80, 20, 20));
			} else {
				add(new Image("icons.png", 20, 80, 20, 20));
			}
			Label label = new Label(player.getEmail());
			label.setWidth("200px");
			label.getElement().addClassName(style.player());
			add(label);
		}

		@Override
		public void onBrowserEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN :
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

		public void goOnline() {
			player.setOnline(true);
			Image image = (Image) getWidget(0);
			image.setUrlAndVisibleRect("icons.png", 0, 80, 20, 20);
		}

		public void goOffline() {
			player.setOnline(false);
			Image image = (Image) getWidget(0);
			image.setUrlAndVisibleRect("icons.png", 20, 80, 20, 20);
		}
	}
	interface PlayersPanelUiBinder extends UiBinder<Widget, PlayersPanel> {
	}

	private static PlayersPanelUiBinder uiBinder = GWT.create(PlayersPanelUiBinder.class);
	private ChessGameServiceAsync chessGameService = GWT.create(ChessGameService.class);

	private HashMap<String, PlayerWidget> playerWidgets = null;
	private PopupPanel showingHoverPanel = null;

	@UiField
	MyStyle style;
	@UiField
	VerticalPanel verticalPanel;

	public PlayersPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		sinkEvents(Event.MOUSEEVENTS);
		playerWidgets = new HashMap<String, PlayerWidget>();
		chessGameService.getPlayers(this);
	}

	public void refresh() {
		chessGameService.getPlayers(this);
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO
	}

	@Override
	public void onSuccess(List<Player> players) {
		this.players = players;
		verticalPanel.clear();
		if (players != null && players.size() > 0) {
			Iterator<Player> iterator = players.iterator();
			if (iterator.hasNext()) {
				Player player = iterator.next();
				PlayerWidget playerWidget = new PlayerWidget(player, true);
				playerWidgets.put(player.getUser(), playerWidget);
				verticalPanel.add(playerWidget);
			}

			verticalPanel.add(new Label("Players:"));
			while (iterator.hasNext()) {
				Player player = iterator.next();
				PlayerWidget playerWidget = new PlayerWidget(player, false);
				playerWidgets.put(player.getUser(), playerWidget);
				verticalPanel.add(playerWidget);
			}
		}
	}

	public void playerOnline(String playerId) {
		playerWidgets.get(playerId).goOnline();
	}

	public void playerOffline(String playerId) {
		playerWidgets.get(playerId).goOffline();
	}

	public List<Player> getPlayers() {
		return players;
	}
}

package hu.mygame.server.servlet;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.server.PMF;
import hu.mygame.server.jdo.Game;
import hu.mygame.shared.jdo.Player;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChannelConnectedServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ChannelConnectedServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	// Handler for _ah/channel/connected/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(req);
		String clientId = presence.clientId();

		log.info("Channel connected: " + clientId);
		System.out.println("Channel connected: " + clientId);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Player player = pm.getObjectById(Player.class, clientId);
			player.setOnline(true);
		} finally {
			pm.close();
		}

		pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Game.class);
			query.setFilter("(whiteUser == userParam) && (finished == false)");
			query.declareParameters("String userParam");
			List<Game> games = (List<Game>) query.execute(clientId);
			for (Game g : games) {
				ChannelMessage message = new ChannelMessage(clientId, ChessGameService.REFRESH_BOARD + g.getId());
				channelService.sendMessage(message);
			}

			query = pm.newQuery(Game.class);
			query.setFilter("(blackUser == userParam) && (finished == false)");
			query.declareParameters("String userParam");
			games = (List<Game>) query.execute(clientId);
			for (Game g : games) {
				ChannelMessage message = new ChannelMessage(clientId, ChessGameService.REFRESH_BOARD + g.getId());
				channelService.sendMessage(message);
			}

			query = pm.newQuery(Player.class);
			query.setFilter("online == true");
			List<Player> players = (List<Player>) query.execute();
			for (Player p : players) {
				ChannelMessage message = new ChannelMessage(p.getUser(), ChessGameService.PLAYER_WENT_ONLINE + clientId);
				channelService.sendMessage(message);
			}
		} finally {
			pm.close();
		}
	}
}
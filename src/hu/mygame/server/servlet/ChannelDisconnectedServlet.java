package hu.mygame.server.servlet;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.server.PMF;
import hu.mygame.server.jdo.PlayerJDO;

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

public class ChannelDisconnectedServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ChannelConnectedServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	// Handler for _ah/channel/disconnected/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(req);
		String clientId = presence.clientId();
		log.info("Channel disconnected: " + clientId);
		System.out.println("Channel disconnected: " + clientId);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PlayerJDO player = pm.getObjectById(PlayerJDO.class, clientId);
			player.setOnline(false);
		} finally {
			pm.close();
		}

		pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(PlayerJDO.class);
			query.setFilter("online == true");
			List<PlayerJDO> players = (List<PlayerJDO>) query.execute();
			for (PlayerJDO p : players) {
				ChannelMessage message = new ChannelMessage(p.getUser(), ChessGameService.PLAYER_WENT_OFFLINE
						+ clientId);
				channelService.sendMessage(message);
			}
		} finally {
			pm.close();
		}
	}
}

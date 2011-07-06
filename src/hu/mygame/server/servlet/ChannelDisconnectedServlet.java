package hu.mygame.server.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChannelDisconnectedServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ChannelConnectedServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Override
	// Handler for _ah/channel/disconnected/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(req);
		String clientId = presence.clientId();
		log.info("Channel disconnected: " + clientId);
	}
}

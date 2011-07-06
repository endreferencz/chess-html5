package hu.mygame.client;

import hu.mygame.client.rpc.ChessGameService;
import hu.mygame.client.rpc.ChessGameServiceAsync;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MyChannel implements AsyncCallback<String>, SocketListener,
		ChannelCreatedCallback {

	private ChessGameServiceAsync chessGameService = GWT
			.create(ChessGameService.class);
	private Main main;
	private boolean open = false;

	public MyChannel(Main main) {
		this.main = main;
		chessGameService.connect(this);
	}

	public boolean isOpen() {
		return open;
	}

	@Override
	public void onChannelCreated(Channel channel) {
		channel.open(this);
	}

	@Override
	public void onClose() {
		chessGameService.connect(this);
	}

	@Override
	public void onError(SocketError error) {
	}

	@Override
	public void onFailure(Throwable caught) {
	}

	@Override
	public void onMessage(String message) {
		main.processMessage(message);
	}

	@Override
	public void onOpen() {
		open = true;
	}

	@Override
	public void onSuccess(String result) {
		ChannelFactory.createChannel(result, this);
	}

}

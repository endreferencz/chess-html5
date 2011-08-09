package hu.mygame.server.jdo;

import hu.mygame.shared.Player;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PlayerJDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Persistent
	private long draw;
	@Persistent
	private String email;
	@Persistent
	private long lost;
	@Persistent
	private String name;
	@PrimaryKey
	private String user;
	@Persistent
	private boolean waiting;
	@Persistent
	private boolean online;
	@Persistent
	private long win;
	public long getDraw() {
		return draw;
	}
	public void setDraw(long draw) {
		this.draw = draw;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getLost() {
		return lost;
	}
	public void setLost(long lost) {
		this.lost = lost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isWaiting() {
		return waiting;
	}
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public long getWin() {
		return win;
	}
	public void setWin(long win) {
		this.win = win;
	}
	public PlayerJDO(long draw, String email, long lost, String name, String user, boolean waiting, boolean online,
			long win) {
		super();
		this.draw = draw;
		this.email = email;
		this.lost = lost;
		this.name = name;
		this.user = user;
		this.waiting = waiting;
		this.online = online;
		this.win = win;
	}

	public PlayerJDO() {

	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (draw ^ (draw >>> 32));
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (int) (lost ^ (lost >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (online ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + (waiting ? 1231 : 1237);
		result = prime * result + (int) (win ^ (win >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerJDO other = (PlayerJDO) obj;
		if (draw != other.draw)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (lost != other.lost)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (online != other.online)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (waiting != other.waiting)
			return false;
		if (win != other.win)
			return false;
		return true;
	}
	public Player toPlayer() {
		return new Player(draw, email, lost, name, user, online, win);
	}
}
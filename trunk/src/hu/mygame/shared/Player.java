package hu.mygame.shared;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	private long draw;
	private String email;
	private long lost;
	private String name;
	private String user;
	private boolean online;
	private long win;

	public Player() {

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
		Player other = (Player) obj;
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
		if (win != other.win)
			return false;
		return true;
	}

	public Player(long draw, String email, long lost, String name, String user, boolean online, long win) {
		super();
		this.draw = draw;
		this.email = email;
		this.lost = lost;
		this.name = name;
		this.user = user;
		this.online = online;
		this.win = win;
	}

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

	public String getDisplayName() {
		if (name == null)
			return email;
		return name;
	}

}

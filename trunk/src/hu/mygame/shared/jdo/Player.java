package hu.mygame.shared.jdo;

import java.io.Serializable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Player implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	private static final long serialVersionUID = 1L;
	@Persistent
	private Long draw;
	@Persistent
	private String email;
	@NotPersistent
	private boolean invited = false;
	@Persistent
	private Long lost;
	@Persistent
	private String name;
	@PrimaryKey
	private String user;
	@Persistent
	private Boolean waiting;

	@Persistent
	private Long win;

	public Player() {
	}
	public Player(String user, String email, String name, Long win, Long draw, Long lost, Boolean waiting) {
		super();
		this.user = user;
		this.email = email;
		this.setName(name);
		this.win = win;
		this.draw = draw;
		this.lost = lost;
		this.waiting = waiting;
	}

	public Long getDraw() {
		return draw;
	}
	public String getEmail() {
		return email;
	}
	public Boolean getInvited() {
		return invited;
	}
	public Long getLost() {
		return lost;
	}
	public String getName() {
		return name;
	}
	public String getUser() {
		return user;
	}
	public Boolean getWaiting() {
		return waiting;
	}
	public Long getWin() {
		return win;
	}

	public void setDraw(Long draw) {
		this.draw = draw;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setInvited(Boolean invited) {
		this.invited = invited;
	}
	public void setLost(Long lost) {
		this.lost = lost;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setWaiting(Boolean waiting) {
		this.waiting = waiting;
	}
	public void setWin(Long win) {
		this.win = win;
	}
}

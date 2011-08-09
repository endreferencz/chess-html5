package hu.mygame.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class InvitationJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String initiator;

	@Persistent
	private String target;

	public InvitationJDO() {

	}

	public InvitationJDO(String initiator, String target) {
		super();
		this.initiator = initiator;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public String getInitiator() {
		return initiator;
	}

	public String getTarget() {
		return target;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}

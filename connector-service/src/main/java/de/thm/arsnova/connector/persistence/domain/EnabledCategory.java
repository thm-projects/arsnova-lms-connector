package de.thm.arsnova.connector.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

@Entity
@Table(name = "enabled_category")
public class EnabledCategory {

	@Id
	@Unique
	@Column(name = "ref_id")
	int refId;

	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
}

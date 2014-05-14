package de.thm.arsnova.connector.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class Configuration {

	@Id
	private String key;
	private String value;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return (this.key + this.value).hashCode();
	};

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Configuration) {
			Configuration other = (Configuration) obj;
			if (
					(this.key != null && this.key.equals(other.getKey()))
					&& (this.value != null && this.value.equals(other.getValue()))
					) {
				return true;
			}
		}
		return false;
	}
}

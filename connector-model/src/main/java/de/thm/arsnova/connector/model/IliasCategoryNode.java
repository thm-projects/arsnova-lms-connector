package de.thm.arsnova.connector.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.hateoas.ResourceSupport;

public class IliasCategoryNode extends ResourceSupport {

	private int child;
	private int parent;
	private String title;
	private String type;
	private List<Short> children;

	public IliasCategoryNode(int child, int parent, String title, String type) {
		this.child = child;
		this.parent = parent;
		this.title = title;
		this.type = type;
	}

	public int getChild() {
		return child;
	}
	public void setChild(int child) {
		this.child = child;
	}

	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<Short> getChildren() {
		return children;
	}
	public void addChild(int id) {
		if (this.children == null) {
			this.children = new ArrayList<Short>();
		}
		this.children.add(new Short(id, this.child));
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public static class Short extends IliasCategoryNode {
		public Short(int id, int parent) {
			super(id, parent, null, null);
		}
	}
}

package de.thm.arsnova.connector.core;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.hateoas.ResourceSupport;

import de.thm.arsnova.connector.web.UniRepController;

public class IliasCategoryNode extends ResourceSupport {

	protected int child;
	protected int parent;
	protected String title;
	protected String type;
	protected List<Short> children;
	
	public IliasCategoryNode(int child, int parent, String title, String type) {
		this.children = new ArrayList<Short>();
		this.child = child;
		this.parent = parent;
		this.title = title;
		this.type = type;
		this.add(linkTo(UniRepController.class).slash(String.valueOf(child)).withSelfRel());
		
		if (type.equals("qpl")) {
			this.add(linkTo(UniRepController.class).slash("question").slash(String.valueOf(child)).withRel("questions"));
		}
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
		children.add(new Short(id, this.child));
	}
	
	@JsonSerialize(include=Inclusion.NON_NULL)
	public static class Short extends IliasCategoryNode {
		public Short(int id, int parent) {
			super(id,parent,null,null);
			this.children = null;
		}
	}
}

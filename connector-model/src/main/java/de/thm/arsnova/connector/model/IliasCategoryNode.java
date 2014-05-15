package de.thm.arsnova.connector.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class IliasCategoryNode {

	private int id;
	private int parent;
	private String title;
	private String type;
	private Boolean leaf;
	private int questionCount;
	private List<IliasCategoryNode> children;

	public IliasCategoryNode() {}

	public IliasCategoryNode(int id, int parent, String title, String type, int questionCount) {
		this.id = id;
		this.parent = parent;
		this.title = title;
		this.type = type;
		this.questionCount = questionCount;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	
	public Boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public List<IliasCategoryNode> getChildren() {
		return children;
	}
	public void addChild(IliasCategoryNode node) {
		if (node == null) throw new IllegalArgumentException();
		if (this.children == null) {
			this.children = new ArrayList<IliasCategoryNode>();
		}
		this.children.add(node);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		if (obj instanceof IliasCategoryNode) {
			IliasCategoryNode other = (IliasCategoryNode) obj;
			return this.id == other.getId() && this.parent == other.getParent();
		}
		return false;
	}
}

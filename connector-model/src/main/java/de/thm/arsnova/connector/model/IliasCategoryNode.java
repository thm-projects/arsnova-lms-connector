package de.thm.arsnova.connector.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IliasCategoryNode {

	private int id;
	private int parent;
	private String title;
	private String type;
	private Boolean leaf;
	private int questionCount;
	private Boolean isRandomTest;
	private Integer randomQuestionAmount;
	private List<IliasCategoryNode> children;

	public IliasCategoryNode() {}

	public IliasCategoryNode(final int id, final int parent, final String title, final String type, final int questionCount) {
		this.id = id;
		this.parent = parent;
		this.title = title;
		this.type = type;
		this.questionCount = questionCount;
	}

	public int getId() {
		return id;
	}
	public void setId(final int id) {
		this.id = id;
	}

	public int getParent() {
		return parent;
	}
	public void setParent(final int parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}

	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(final int questionCount) {
		this.questionCount = questionCount;
	}

	public Boolean getIsRandomTest() {
		return isRandomTest;
	}
	public void setIsRandomTest(final Boolean isRandomTest) {
		this.isRandomTest = isRandomTest;
	}

	public Integer getRandomQuestionCount() {
		return randomQuestionAmount;
	}
	public void setRandomQuestionCount(final Integer randomQuestionAmount) {
		this.randomQuestionAmount = randomQuestionAmount;
	}

	public Boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(final Boolean leaf) {
		this.leaf = leaf;
	}

	public List<IliasCategoryNode> getChildren() {
		return children;
	}
	public void addChild(final IliasCategoryNode node) {
		if (node == null) {
			throw new IllegalArgumentException();
		}
		if (children == null) {
			children = new ArrayList<IliasCategoryNode>();
		}
		children.add(node);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof IliasCategoryNode) {
			final IliasCategoryNode other = (IliasCategoryNode) obj;
			return id == other.getId() && parent == other.getParent();
		}
		return false;
	}
}

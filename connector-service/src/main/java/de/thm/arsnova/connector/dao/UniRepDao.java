package de.thm.arsnova.connector.dao;

import java.util.List;

import de.thm.arsnova.connector.core.IliasAnswer;
import de.thm.arsnova.connector.core.IliasCategoryNode;
import de.thm.arsnova.connector.core.IliasQuestion;

public interface UniRepDao {
	List<IliasCategoryNode> getTreeObjects(int refId);
	List<IliasQuestion> getQuestion(int refId);
	List<IliasAnswer> getAnswers(int questionId);
}

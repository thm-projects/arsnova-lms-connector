package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import de.thm.arsnova.connector.core.IliasCategoryNode;
import de.thm.arsnova.connector.core.IliasQuestion;

public interface UniRepService {
	@PreAuthorize("isAuthenticated()")
	List<IliasCategoryNode> getTreeObjects(int refId);
	@PreAuthorize("isAuthenticated()")
	List<IliasQuestion> getQuestions(int refId);
}

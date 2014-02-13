package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.core.IliasCategoryNode;
import de.thm.arsnova.connector.core.IliasQuestion;
import de.thm.arsnova.connector.dao.UniRepDao;

@Service
public class UniRepServiceImpl implements UniRepService {

	@Autowired
	private UniRepDao uniRepDao;

	@Override
	public List<IliasCategoryNode> getTreeObjects(int refId) {
		return uniRepDao.getTreeObjects(refId);
	}

	@Override
	public List<IliasQuestion> getQuestions(int refId) {
		return uniRepDao.getQuestion(refId);
	}
}

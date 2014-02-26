package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.dao.UniRepDao;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

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

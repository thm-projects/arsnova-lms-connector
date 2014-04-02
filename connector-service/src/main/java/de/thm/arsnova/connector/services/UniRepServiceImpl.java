package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.dao.UniRepDao;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

@Service
public class UniRepServiceImpl implements UniRepService {

	@Autowired(required = false)
	private UniRepDao uniRepDao;

	@Override

	public List<IliasCategoryNode> getTreeObjects(int refId) throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}
		return uniRepDao.getTreeObjects(refId);
	}

	@Override
	public List<IliasQuestion> getQuestions(int refId) throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}
		return uniRepDao.getQuestion(refId);
	}
}

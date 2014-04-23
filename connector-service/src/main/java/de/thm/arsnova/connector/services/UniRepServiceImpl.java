package de.thm.arsnova.connector.services;

import java.util.Iterator;
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
	public List<IliasCategoryNode> getTreeObjects(int refId)
			throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}

		List<IliasCategoryNode> result = uniRepDao.getTreeObjects(refId);
		while(removeBranchesWithoutQuestionPools(result));

		return this.removeNodesNotMarked(result);
	}

	@Override
	public List<IliasQuestion> getQuestions(int refId)
			throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}
		return uniRepDao.getQuestion(refId);
	}

	private boolean removeBranchesWithoutQuestionPools( List<IliasCategoryNode> nodes ) throws ServiceUnavailableException {

		if (nodes == null) return false;

		Iterator<IliasCategoryNode> it = nodes.iterator();

		boolean hasRemovedNodes = false;

		while (it.hasNext()) {
			IliasCategoryNode node = it.next();

			if (node == null) continue;

			if (node.getQuestionCount() == 0 && (node.getChildren() == null || node.getChildren().size() == 0)) {
				it.remove();
				hasRemovedNodes = true;
				continue;
			}

			removeBranchesWithoutQuestionPools(node.getChildren());
		}

		return hasRemovedNodes;

	}

	private List<IliasCategoryNode> removeNodesNotMarked(List<IliasCategoryNode> nodes) {
		List<String> disabledRefIds = uniRepDao.getReferenceIdsWithMetaDataFlagDisabled("UniRepApp");

		if (nodes == null)return null;

		Iterator<IliasCategoryNode> it = nodes.iterator();

		while (it.hasNext()) {
			IliasCategoryNode node = it.next();

			if (disabledRefIds.contains(String.valueOf(node.getId()))) {
				it.remove();
				continue;
			}

			if ("cat".equals(node.getType()) || "root".equals(node.getType())) {
				removeNodesNotMarked(node.getChildren());
			}
		}

		return nodes;
	}
}

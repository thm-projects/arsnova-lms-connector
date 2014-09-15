package de.thm.arsnova.connector.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.core.NotFoundException;
import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.dao.UniRepDao;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

@Service
public class UniRepServiceImpl implements UniRepService {

	@Autowired(required = false)
	private UniRepDao uniRepDao;

	@Override
	public IliasCategoryNode getTreeObjects(final int refId)
			throws ServiceUnavailableException, NotFoundException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}

		List<IliasCategoryNode> result = uniRepDao.getTreeObjects(refId);
		result = removeNotMarkedNodes(result, false);

		while (removeBranchesWithoutQuestionPools(result)) {
		}

		if (result.size() == 1) {
			return result.get(0);
		}
		throw new NotFoundException();
	}

	@Override
	public List<IliasQuestion> getQuestions(final int refId,
			final boolean noRandomQuestions) throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}

		if (uniRepDao.isRandomQuestionSet(refId) && !noRandomQuestions) {
			return getRandomQuestions(refId);
		} else {
			return uniRepDao.getQuestion(refId);
		}
	}

	@Override
	public List<IliasQuestion> getRandomQuestions(final int refId) {
		final int amountOfQuestions = uniRepDao.getQuestionAmountPerTest(refId);
		final List<IliasQuestion> allQuestions = uniRepDao
				.getRandomTestQuestions(refId);

		final Set<Integer> randomIds = new HashSet<>();
		while (randomIds.size() < amountOfQuestions) {
			randomIds
			.add((int) Math.floor(Math.random() * allQuestions.size()));
		}

		final List<IliasQuestion> result = new ArrayList<>();
		for (final int id : randomIds) {
			result.add(allQuestions.get(id));
		}

		return result;
	}

	private boolean removeBranchesWithoutQuestionPools(
			final List<IliasCategoryNode> nodes)
					throws ServiceUnavailableException {

		if (nodes == null) {
			return false;
		}

		final Iterator<IliasCategoryNode> it = nodes.iterator();

		boolean hasRemovedNodes = false;

		while (it.hasNext()) {
			final IliasCategoryNode node = it.next();

			if (node == null) {
				continue;
			}

			if (node.getChildren() == null || node.getChildren().size() == 0) {
				if (node.getQuestionCount() == 0 || !uniRepDao.isTestOnline(node.getId())) {
					it.remove();
					hasRemovedNodes = true;
					continue;
				} else {
					node.setLeaf(true);
				}
			}

			if (removeBranchesWithoutQuestionPools(node.getChildren())) {
				hasRemovedNodes = true;
			}
		}

		return hasRemovedNodes;
	}

	private List<IliasCategoryNode> removeNotMarkedNodes(
			final List<IliasCategoryNode> nodes, final boolean isParentMarked) {
		final Map<String, String> categoryMetaTagRefIds = uniRepDao
				.getReferenceIdsWithMetaDataFlag("UniRepApp");
		final Map<String, String> courseMetaTagRefIds = uniRepDao
				.getReferenceIdsWithMetaDataFlag("UniRepCourse");

		if (nodes == null) {
			return null;
		}

		final Iterator<IliasCategoryNode> it = nodes.iterator();

		while (it.hasNext()) {
			final IliasCategoryNode node = it.next();
			final String nodeId = String.valueOf(node.getId());

			if ("crs".equals(node.getType())) {
				if ("yes".equals(courseMetaTagRefIds.get(nodeId))) {
					continue;
				} else {
					it.remove();
					continue;
				}
			}

			else {
				if (categoryMetaTagRefIds.containsKey(nodeId)) {
					if ("no".equals(categoryMetaTagRefIds.get(nodeId))) {
						it.remove();
						continue;
					}

					else if ("yes".equals(categoryMetaTagRefIds.get(nodeId))) {
						removeNotMarkedNodes(node.getChildren(), true);
					}
				}

				else {
					if (isParentMarked) {
						removeNotMarkedNodes(node.getChildren(), true);
					} else {
						removeNotMarkedNodes(node.getChildren(), false);
						if (
								node.getChildren() == null
								|| node.getChildren().size() == 0
								) {
							it.remove();
						}
					}
				}
			}
		}

		return nodes;
	}
}

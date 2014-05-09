package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.persistence.domain.EnabledCategory;
import de.thm.arsnova.connector.persistence.repository.EnabledCategoryRepository;

@Service
public class EnabledCategoryServiceImpl implements EnabledCategoryService {

	@Autowired
	EnabledCategoryRepository repo;

	@Override
	public void enableCategory(int refId) {
		EnabledCategory cat = new EnabledCategory();
		cat.setRefId(refId);
		repo.save(cat);
	}

	@Override
	public void disableCategory(int refId) {
		repo.delete(refId);
	}

	@Override
	public List<EnabledCategory> getEnabledCategories() {
		return repo.findAll();
	}
}

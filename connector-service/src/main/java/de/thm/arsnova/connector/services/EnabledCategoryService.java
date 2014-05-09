package de.thm.arsnova.connector.services;

import java.util.List;

import de.thm.arsnova.connector.persistence.domain.EnabledCategory;

public interface EnabledCategoryService {
	public void enableCategory(int refId);
	public void disableCategory(int refId);
	public List<EnabledCategory> getEnabledCategories();
}

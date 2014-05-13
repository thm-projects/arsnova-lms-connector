package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import de.thm.arsnova.connector.persistence.domain.EnabledCategory;

public interface EnabledCategoryService {
	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'enabled_category', 'write')")
	public void enableCategory(int refId);

	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'enabled_category', 'write')")
	public void disableCategory(int refId);

	public List<EnabledCategory> getEnabledCategories();

	public boolean isEnabledCategory(int refId);
}

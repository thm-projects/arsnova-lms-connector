package de.thm.arsnova.connector.services;

import org.springframework.security.access.prepost.PreAuthorize;

import de.thm.arsnova.connector.persistence.domain.User;

public interface InternalUserService {

	User getUser(String userid);

	@PreAuthorize(value = "hasRole('ADMIN')")
	void saveUser(User user);

	@PreAuthorize(value = "hasRole('ADMIN')")
	void makeAdmin(String userid);

	@PreAuthorize(value = "hasRole('ADMIN')")
	void unmakeAdmin(String userid);
}

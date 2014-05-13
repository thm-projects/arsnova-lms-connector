package de.thm.arsnova.connector.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.persistence.domain.User;
import de.thm.arsnova.connector.persistence.repository.UserRepository;

@Service
public class InternalUserServiceImpl implements InternalUserService {

	@Autowired
	UserRepository repo;

	@Override
	public User getUser(String userid) {
		return repo.findOne(userid);
	}

	@Override
	public void saveUser(User user) {
		if (! user.getPassword().startsWith("{SHA512}")) {
			String password = user.getPassword();
			user.setPassword("{SHA512}" + Sha512DigestUtils.shaHex(password));
		}
		repo.save(user);
	}

	@Override
	public void makeAdmin(String userid) {
		User u = getUser(userid);
		u.setAdmin(true);
		saveUser(u);
	}

	@Override
	public void unmakeAdmin(String userid) {
		User u = getUser(userid);
		u.setAdmin(false);
		saveUser(u);
	}
}

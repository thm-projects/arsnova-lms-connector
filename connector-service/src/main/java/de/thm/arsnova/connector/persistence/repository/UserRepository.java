package de.thm.arsnova.connector.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.thm.arsnova.connector.persistence.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("select u from User u where u.authToken like %?1")
	User findByToken(String authToken);
}

package de.thm.arsnova.connector.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.thm.arsnova.connector.persistence.domain.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}

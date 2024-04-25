package cl.sermaluc.gestion.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sermaluc.gestion.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByName(String name);
	Boolean existsByName(String name);
	Boolean existsByEmail(String email);
}

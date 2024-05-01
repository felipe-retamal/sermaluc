package cl.sermaluc.gestion.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sermaluc.gestion.model.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

}

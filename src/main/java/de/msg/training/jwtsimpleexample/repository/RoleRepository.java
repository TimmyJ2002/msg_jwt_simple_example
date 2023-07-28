package de.msg.training.jwtsimpleexample.repository;

import de.msg.training.jwtsimpleexample.model.ERole;
import de.msg.training.jwtsimpleexample.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}

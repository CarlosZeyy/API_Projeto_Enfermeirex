package dev.carlosmoises.projeto.enferm.repository;

import dev.carlosmoises.projeto.enferm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmailOrCoren(String email, String coren);
}

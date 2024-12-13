package it.exam.ticket.platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}

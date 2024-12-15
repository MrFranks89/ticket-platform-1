package it.exam.ticket.platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
 
	 public Optional<Admin> findByUsername(String username);
	
}

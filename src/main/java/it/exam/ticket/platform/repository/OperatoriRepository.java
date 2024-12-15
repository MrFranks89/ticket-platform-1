package it.exam.ticket.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.Operatori;

public interface OperatoriRepository extends JpaRepository<Operatori, Long> {

	public List<Operatori> findByAttivo(boolean attivo);
	 public List<Operatori> findAll();
	 public List<Operatori> findByUsername(String username);
}

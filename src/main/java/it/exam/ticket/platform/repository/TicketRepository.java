package it.exam.ticket.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	public List<Ticket> findByTitoloContaining(String titolo);
	
	public List<Ticket> findByOperatoreId(Long id);
	
	List<Ticket> findByCategoria(String categoria);

    List<Ticket> findByStato(String stato);
    
    List<Ticket> findByCategoriaStato(String categoria, String stato);
	
}

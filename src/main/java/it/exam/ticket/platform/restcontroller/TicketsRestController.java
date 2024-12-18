package it.exam.ticket.platform.restcontroller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.TicketRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketsRestController {

	private static final Logger logger = LoggerFactory.getLogger(TicketsRestController.class);

	@Autowired
	private TicketRepository ticketRepo;

	public TicketsRestController() {
		System.out.println("TicketsRestController caricato!");
	}

	@GetMapping("/")
	    public ResponseEntity<List<Ticket>> index(@RequestParam(name = "keyword", required = false) String keyword) {
	        try {
	            if (keyword != null && !keyword.isBlank()) {
	                List<Ticket> tickets = ticketRepo.findByTitoloContaining(keyword);
	                return new ResponseEntity<>(tickets, HttpStatus.OK);
	            } else {
	                List<Ticket> tickets = ticketRepo.findAll();
	                return ResponseEntity.ok(tickets);
	            }
	        } catch (Exception e) {
	            logger.error("Errore durante il recupero del ticket", e);
	            return ResponseEntity.badRequest().build();
	        }
	    }

	@PutMapping("/{id}")
	public ResponseEntity<Ticket> update(@PathVariable Long id, @RequestBody Ticket ticket) {
		try {
			Optional<Ticket> byId = ticketRepo.findById(id);
			if (byId.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Ticket dbTicket = byId.get();
			dbTicket.setDescrizione(ticket.getDescrizione());
			ticketRepo.save(dbTicket);

			return ResponseEntity.ok(dbTicket);
		} catch (Exception e) {
			logger.error("Errore durante l'aggiornamento del ticket", e);
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
		try {
			if (!ticketRepo.existsById(id)) {
				return ResponseEntity.notFound().build();
			}

			ticketRepo.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("Errore durante la cancellazione del ticket", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public Ticket create(@Valid @RequestBody Ticket ticket) {
		return ticketRepo.save(ticket);
	}

	@GetMapping("/categoria")
	public List<Ticket> getTicketsByCategoria(@RequestParam String categoria) {
		return ticketRepo.findByCategoria_nome(categoria);
	}

	@GetMapping("/stato")
	public List<Ticket> getTicketsByStato(@RequestParam String stato) {
		return ticketRepo.findByStato(stato);
	}

	@GetMapping("/filtri")
	public List<Ticket> getTicketsByCategoriaAndStato(@RequestParam String categoria, @RequestParam String stato) {
		return ticketRepo.findByCategoria_NomeAndStato(categoria, stato);
	}
}

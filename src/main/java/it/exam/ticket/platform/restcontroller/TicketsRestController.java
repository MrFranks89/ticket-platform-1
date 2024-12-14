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

	@Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/categoria")
    public List<Ticket> getTicketsByCategoria(@RequestParam String categoria) {
        return ticketRepository.findByCategoria(categoria);
    }

    @GetMapping("/stato")
    public List<Ticket> getTicketsByStato(@RequestParam String stato) {
        return ticketRepository.findByStato(stato);
    }

    @GetMapping("/filtri")
    public List<Ticket> getTicketsByCategoriaAndStato(
            @RequestParam String categoria,
            @RequestParam String stato) {
        return ticketRepository.findByCategoriaStato(categoria, stato);
    }
}

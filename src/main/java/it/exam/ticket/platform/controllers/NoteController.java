package it.exam.ticket.platform.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.exam.ticket.platform.model.Nota;
import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.NoteRepository;
import it.exam.ticket.platform.repository.OperatoriRepository;
import it.exam.ticket.platform.repository.TicketRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private NoteRepository noteRepo;

	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private OperatoriRepository operatoriRepo;

	
	@GetMapping("/create")
    public String showCreateForm(@RequestParam("ticketId") Long ticketId, Model model) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

        List<Operatori> allOperatori = operatoriRepo.findAll();
        model.addAttribute("allOperatori", allOperatori);
        Nota newNota = new Nota();
        newNota.setTicket(ticket);
        newNota.setDataCreazione(LocalDate.now());

        model.addAttribute("ticket", ticket);
        model.addAttribute("newNota", newNota);

        return "tickets/note";
    }

	@PostMapping("/create")
    public String createNota(@ModelAttribute Nota nota, @RequestParam Long ticketId, RedirectAttributes redirectAttributes) {
        
		System.out.println("Titolo: " + nota.getTitolo());
	    System.out.println("Testo: " + nota.getTesto());
	    System.out.println("Ticket ID: " + ticketId);
		Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

        nota.setTicket(ticket);
        nota.setDataCreazione(LocalDate.now());
        noteRepo.save(nota);

        redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta con successo!");
        return "redirect:/tickets/" + ticketId;
    }

	@PostMapping("/delete/{id}")
	public String deleteNota(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		Nota nota = noteRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Nota non trovata"));
		
		Long ticketId = nota.getTicket().getId();
		noteRepo.delete(nota);
		
		redirectAttributes.addFlashAttribute("successMessage", "Nota eliminata con successo!");
		return "redirect:/tickets/" + nota.getTicket().getId();
	}
}

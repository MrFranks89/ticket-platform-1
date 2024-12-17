package it.exam.ticket.platform.controllers;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.exam.ticket.platform.model.Nota;
import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.CategoriaRepository;
import it.exam.ticket.platform.repository.NoteRepository;
import it.exam.ticket.platform.repository.OperatoriRepository;
import it.exam.ticket.platform.repository.TicketRepository;
import it.exam.ticket.platform.security.DatabaseUserDetails;
import it.exam.ticket.platform.model.Categoria;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	private TicketRepository ticketRepo;

	@Autowired
	private OperatoriRepository operatoriRepo;

	@Autowired
	private NoteRepository noteRepo;

	@Autowired
	private CategoriaRepository categoriaRepo;

	@GetMapping
	public String index(Authentication authentication, Model model,
			@RequestParam(name = "keyword", required = false) String keyword) {

		List<Ticket> allTickets = ticketRepo.findAll();

		if (keyword != null && !keyword.isBlank()) {
			allTickets = ticketRepo.findByTitoloContaining(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			allTickets = ticketRepo.findAll();
		}

		if (keyword == null || keyword.isBlank() || keyword.equals("null")) {
			model.addAttribute("ticketUrl", "/tickets");
		} else {
			model.addAttribute("ticketUrl", "/tickets?keyword=" + keyword);
		}

		model.addAttribute("tickets", allTickets);

		return "tickets/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Long id, Model model) {
		Optional<Ticket> ticketOptional = ticketRepo.findById(id);

		if (ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			model.addAttribute("ticket", ticket);

			if (ticket.getOperatore() != null) {
				model.addAttribute("operatore", ticket.getOperatore());
			}

			List<String> statiTicket = Arrays.asList("Da Fare", "In corso", "Completato");
			model.addAttribute("statiTicket", statiTicket);

			return "tickets/show";
		} else {
			model.addAttribute("errorMessage", "Ticket non trovato");
			return "redirect:/tickets";
		}

	}

	@GetMapping("/create")
	public String create(Model model) {

		Ticket ticket = new Ticket();
		ticket.setStato("da fare");
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("allOperatori", operatoriRepo.findAll());
		model.addAttribute("allCategorie", categoriaRepo.findAll());

		return "tickets/create";
	}

	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {

			bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return "tickets/create";
		}

		if (ticket.getDataCreazione() == null) {
			ticket.setDataCreazione(LocalDateTime.now());
		}

		LocalDateTime now = LocalDateTime.now();
		ticket.setDataCreazione(now);
		ticket.setDataModifica(now);

		if (ticket.getStato() == null || ticket.getStato().isEmpty()) {
			ticket.setStato("da fare");
		}

		if (ticket.getCategoria() != null && ticket.getCategoria().getId() != null) {

			Categoria categoria = categoriaRepo.findById(ticket.getCategoria().getId())
					.orElseThrow(() -> new IllegalArgumentException("Categoria non trovata"));

			ticket.setCategoria(categoria);
		} else {
			bindingResult.rejectValue("categoria", "error.categoria", "La categoria non può essere vuota");
			return "tickets/create";
		}

		System.out.println(ticket);

		ticketRepo.save(ticket);

		Operatori operatore = ticket.getOperatore();
		operatoriRepo.save(operatore);

		redirectAttributes.addFlashAttribute("successMessage", "Ticket creato con successo!");
		return "redirect:/tickets";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		Ticket ticket = ticketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket non trovato"));

		model.addAttribute("ticket", ticketRepo.findById(id).get());
		model.addAttribute("allOperatori", operatoriRepo.findAll());
		model.addAttribute("allCategorie", categoriaRepo.findAll());

		return "tickets/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("ticket") Ticket formTicket,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		Ticket ticket = ticketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket non trovato"));

		if (bindingResult.hasErrors()) {
			return "ticket/edit";
		}

		if (!formTicket.getTitolo().equals(ticket.getTitolo())) {
			bindingResult.addError(new ObjectError("Titolo", "Il titolo non può essere cambiato"));
			return "tickets/edit";
		}

		if (!formTicket.getDescrizione().equals(ticket.getDescrizione())) {
			bindingResult.rejectValue("descrizione", "error.descrizione", "La descrizione non può essere cambiata");
		}
		if (formTicket.getDataModifica() == null) {
			formTicket.setDataModifica(LocalDateTime.now());
		}

		if (formTicket.getDataCreazione() == null) {
			formTicket.setDataCreazione(ticket.getDataCreazione());
		}

		if (formTicket.getStato() == null || formTicket.getStato().isEmpty()) {
			formTicket.setStato("da fare");
		}

		ticket.setStato(formTicket.getStato());

		ticketRepo.save(formTicket);

		Operatori operatore = formTicket.getOperatore();
		operatoriRepo.save(operatore);

		redirectAttributes.addFlashAttribute("successMessage", "Ticket Modificato!");

		return "redirect:/tickets/{id}";
	}

	@PostMapping("/{id}")
	public String updateStatoTicket(@PathVariable Long id, @RequestParam("stato") String stato,
			RedirectAttributes redirectAttributes) {

		Ticket ticket = ticketRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Ticket non trovato: " + id));

		ticket.setStato(stato);
		ticket.setDataModifica(LocalDateTime.now());
		ticketRepo.save(ticket);

		redirectAttributes.addFlashAttribute("successMessage", "Stato del ticket aggiornato con successo!");

		return "redirect:/tickets/{id}";
	}

	@PostMapping("/delete/{id}")
	public String deleteTicket(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		Ticket ticket = ticketRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Ticket non trovato: " + id));

		noteRepo.deleteAll(ticket.getNote());

		ticketRepo.deleteById(id);

		redirectAttributes.addFlashAttribute("success", "Ticket eliminato con successo!");
		return "redirect:/tickets";
	}

	@GetMapping("/{id}/note")
	public String nota(@PathVariable Long id, Model model) {

		Ticket ticket = ticketRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

		Nota newNota = new Nota();

		newNota.setTicket(ticket);
		newNota.setDataCreazione(LocalDate.now());

		model.addAttribute("ticket", ticket);
		model.addAttribute("note", ticket.getNote());
		model.addAttribute("newNota", new Nota());

		return "note/create";
	}

}

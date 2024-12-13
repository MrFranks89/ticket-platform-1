package it.exam.ticket.platform.controllers;

import java.time.LocalDate;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.exam.ticket.platform.model.Nota;
import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.NoteRepository;
import it.exam.ticket.platform.repository.TicketRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@GetMapping("/crea")
	public String creaNota(@RequestParam(required = false) Long ticketId, Model model) {
		Nota nuovaNota = new Nota();
		if (ticketId != null) {
			Ticket ticket = ticketRepository.findById(ticketId)
					.orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));
			nuovaNota.setTicket(ticket);
		}
		model.addAttribute("nota", nuovaNota);
		model.addAttribute("ticket", ticketRepository.findAll());
		return "note/create";
	}

	@PostMapping("/crea")
	public String creaNota(@Valid @ModelAttribute("nota") Nota nota, BindingResult bindingResult,
			@RequestParam(required = false) Long ticketId, @RequestParam(required = false) Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("ticket", ticketRepository.findAll());
			return "note/create";
		}

		if (ticketId != null) {
			Ticket ticket = ticketRepository.findById(ticketId)
					.orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));
			nota.setTicket(ticket);
		}

		noteRepository.save(nota);
		return "redirect:/ticket/show/" + nota.getTicket().getId();
	}

	@GetMapping("/edit/{id}")
	public String editNota(@PathVariable Long id, Model model) {
		Nota nota = noteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Nota non trovata"));

		model.addAttribute("nota", nota);
		model.addAttribute("ticket", ticketRepository.findAll());
		return "note/edit";
	}

	@PostMapping("/edit/{id}")
	public String aggiornaNota(@PathVariable Long id, @Valid @ModelAttribute("nota") Nota nota,
			BindingResult bindingResult, @RequestParam(required = false) Long ticketId,
			@RequestParam(required = false) Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("ticket", ticketRepository.findAll());
			return "note/edit";
		}

		if (ticketId != null) {
			Ticket ticket = ticketRepository.findById(ticketId)
					.orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));
			nota.setTicket(ticket);
		}

		noteRepository.save(nota);
		return "redirect:/ticket/show/" + nota.getTicket().getId();
	}

	@PostMapping("/delete/{id}")
	public String deleteNota(@PathVariable Long id) {
		Nota nota = noteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Nota non trovata"));
		noteRepository.delete(nota);
		return "redirect:/ticket/show/" + nota.getTicket().getId();
	}
}

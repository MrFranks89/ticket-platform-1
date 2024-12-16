package it.exam.ticket.platform.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.OperatoriRepository;
import it.exam.ticket.platform.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/operatori")
public class OperatoriController {

	@Autowired
	private OperatoriRepository operatoriRepository;

	@Autowired
	private TicketRepository ticketRepo;

	@GetMapping
	public String index(Model model) {

		List<Operatori> all = operatoriRepository.findAll();

		model.addAttribute("operatori", all);
		model.addAttribute("ope", new Operatori());

		return "operatori/index";

	}

	@GetMapping("/{id}")
	public String showOperatore(@PathVariable Long id, Model model) {
		Operatori operatori = operatoriRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Operatore non trovato con id: " + id));
		model.addAttribute("operatore", operatori);

		List<Ticket> assignedTickets = ticketRepo.findByOperatoreId(id);
		model.addAttribute("assignedTickets", assignedTickets);

		boolean isAttivo = assignedTickets.isEmpty();
	    operatori.setAttivo(isAttivo);
		
	    List<String> statiOperatore = Arrays.asList("Attivo", "Non attivo");
	    model.addAttribute("statiOperatore", statiOperatore);
	    
		return "operatori/show";
	}
	
	@PostMapping("/{id}")
	public String updateOperatore(@PathVariable Long id, RedirectAttributes redirectAttributes) {

	    Operatori operatore = operatoriRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Operatore non trovato con id: " + id));

	    operatoriRepository.save(operatore);
	
	    redirectAttributes.addFlashAttribute("message", "Stato dell'operatore aggiornato con successo!");

	    return "redirect:/operatori/{id}";
	}


	@PostMapping("/create")
	public String create(@Valid @ModelAttribute(name = "ope") Operatori operatori, BindingResult bindingresult,
			Model model) {

		if (bindingresult.hasErrors()) {

			List<Operatori> all = operatoriRepository.findAll();

			model.addAttribute("operatori", all);
			model.addAttribute("ope", new Operatori());

			return "/operatori/index";
		}

		operatoriRepository.save(operatori);

		return "redirect:/operatori";
	}
	
	@PostMapping("/updateStato/{id}")
	public String updateOperatoreStato(@PathVariable Long id, @RequestParam String stato, RedirectAttributes redirectAttributes) {
		System.out.println("ID Operatore: " + id);
	    System.out.println("Stato ricevuto: " + stato);
		
		Operatori operatore = operatoriRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Operatore non trovato con id: " + id));


	    operatore.setAttivo(stato.equals("Attivo"));
	    operatoriRepository.save(operatore);

	    redirectAttributes.addFlashAttribute("message", "Stato dell'operatore aggiornato con successo!");
	    return "redirect:/operatori/{id}";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			Operatori operatore = operatoriRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Operatore non trovato"));

			List<Ticket> ticketsAssociati = new ArrayList<>(operatore.getTicket());
			for (Ticket ticket : ticketsAssociati) {
				ticket.removeOperatore();
				ticketRepo.save(ticket);
			}

			operatoriRepository.delete(operatore);

			redirectAttributes.addFlashAttribute("message", "Operatore eliminato con successo");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error",
					"Si è verificato un errore durante l'eliminazione dell'operatore");
		}

		return "redirect:/operatori";
	}
}

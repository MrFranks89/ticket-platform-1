package it.exam.ticket.platform.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.model.Ticket;
import it.exam.ticket.platform.repository.OperatoriRepository;
import it.exam.ticket.platform.repository.TicketRepository;

@Controller
public class AdminController {

	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private OperatoriRepository operatoreRepo;
	
	@GetMapping("/admin")
    public String home(Model model){
       List<Ticket> tickets = ticketRepo.findAll();
       List<Operatori> operatori = operatoreRepo.findAll();
       
       model.addAttribute("tickets", tickets);
       model.addAttribute("operatori", operatori);
    	
    	return "admin/index";
    }
}

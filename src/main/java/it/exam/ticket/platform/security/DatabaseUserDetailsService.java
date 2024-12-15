package it.exam.ticket.platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.exam.ticket.platform.model.Admin;
import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.repository.AdminRepository;
import it.exam.ticket.platform.repository.OperatoriRepository;


public class DatabaseUserDetailsService implements UserDetailsService{

	@Autowired
	private OperatoriRepository operatoriRepo;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Admin> adminByUsername = adminRepo.findByUsername(username);
		if(adminByUsername.isPresent()) {
			return new DatabaseUserDetails(adminByUsername.get());
		}
		
		Optional<Operatori> userByUsername = operatoriRepo.findOneByUsername(username);
		if (userByUsername.isPresent()) {
			return new DatabaseUserDetails(userByUsername.get());}
		else {
			throw new UsernameNotFoundException("Operatore non trovato");
		}
	}
		
	}
	

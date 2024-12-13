package it.exam.ticket.platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.exam.ticket.platform.model.User;
import it.exam.ticket.platform.repository.UserRepository;

public class DatabaseUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userByUsername = userRepo.findByUsername(username);
		
		if(userByUsername.isPresent()) {
			return new DatabaseUserDetails(userByUsername.get());
		} else {
			throw new UsernameNotFoundException("Username not found");
		}
		
	}
	
	

}
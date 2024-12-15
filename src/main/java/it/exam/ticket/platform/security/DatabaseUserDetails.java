package it.exam.ticket.platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.exam.ticket.platform.model.Admin;
import it.exam.ticket.platform.model.Operatori;
import it.exam.ticket.platform.model.Roles;

public class DatabaseUserDetails implements UserDetails {
	
	private final Long id;
	private final String username;
	private final String password;
	private final Set<GrantedAuthority> authorities;
	
	public DatabaseUserDetails(Operatori operatore) {
		this.id = operatore.getId();
		this.username = operatore.getUsername();
		this.password = operatore.getPassword();
		
		authorities = new HashSet<GrantedAuthority>();
		for (Roles role : operatore.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
	}
	
	public DatabaseUserDetails(Admin admin) {
		this.id = admin.getId();
		this.username = admin.getUsername();
		this.password = admin.getPassword();
		authorities = new HashSet<GrantedAuthority>();
		for (Roles role : admin.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}

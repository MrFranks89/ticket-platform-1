package it.exam.ticket.platform.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Operatori {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank (message = "Il nome non può essere vuoto")
	@NotNull (message = "Il nome non può essere nullo")
	private String nome;

	@NotBlank (message = "Il cognome non può essere vuoto")
	@NotNull (message = "Il cognome non può essere nullo")
	private String cognome;
	
	@NotBlank (message = "L'username non può essere vuoto")
	@NotNull (message = "L'username non può essere nullo")
	private String username;
	
	@NotBlank(message = "La mail non può essere vuota")
	@NotNull(message = "La mail non può essere nulla")
	private String email;
	
	@NotBlank(message = "La password non può essere vuota")
	@NotNull(message = "La password non può essere nulla")
	private String password;

	private boolean attivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public List<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(List<Ticket> ticket) {
		this.ticket = ticket;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Roles> roles;

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	@OneToMany(mappedBy = "operatore", fetch = FetchType.LAZY)
	private List<Ticket> ticket = new ArrayList<>();

	public void removeTicket(Ticket ticket) {
		this.ticket.remove(ticket);
		ticket.setOperatore(null);
	}
}

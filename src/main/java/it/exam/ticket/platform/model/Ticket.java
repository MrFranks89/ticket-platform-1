package it.exam.ticket.platform.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import it.exam.ticket.platform.model.Operatori;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Il titolo non può essere nullo")
	@NotBlank(message = "Il titolo non può essere vuoto")
	private String titolo;

	@NotNull(message = "La descrizione non può essere nulla")
	@NotBlank(message = "La descrizione non può essere vuota")
	private String descrizione;

	private String stato;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataCreazione;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataModifica;
	
	@OneToMany(mappedBy = "ticket")
	private List<Nota> note;
	
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name = "operatori_id", nullable = false)
	private Operatori operatore;

	public List<Nota> getNote() {
		return note;
	}

	public void setNota(List<Nota> note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public LocalDateTime getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(LocalDateTime dataModifica) {
		this.dataModifica = dataModifica;
	}

	
	public Operatori getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatori operatore) {
		this.operatore = operatore;
	}

	public void removeOperatore() {
        if (this.operatore != null) {
            Operatori oldOperatore = this.operatore;
            this.operatore = null;
            oldOperatore.getTicket().remove(this);
        }
    }

	public void setNote(List<Nota> note) {
		this.note = note;
	}
}

package it.exam.ticket.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.Nota;

public interface NoteRepository extends JpaRepository<Nota, Long>{


}

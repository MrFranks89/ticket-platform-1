package it.exam.ticket.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.exam.ticket.platform.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}

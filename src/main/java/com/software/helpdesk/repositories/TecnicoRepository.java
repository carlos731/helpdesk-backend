package com.software.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.software.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
	
	
}

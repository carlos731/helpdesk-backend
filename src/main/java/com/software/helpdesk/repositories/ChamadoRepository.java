package com.software.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.software.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
	
	
}

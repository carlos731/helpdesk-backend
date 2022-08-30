package com.software.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.software.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	
}

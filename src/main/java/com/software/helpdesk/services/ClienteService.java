package com.software.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.software.helpdesk.domain.Cliente;
import com.software.helpdesk.domain.Pessoa;
import com.software.helpdesk.domain.dtos.ClienteDTO;
import com.software.helpdesk.repositories.ClienteRepository;
import com.software.helpdesk.repositories.PessoaRepository;
import com.software.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.software.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = tecnicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Cliente> findAll() {
		return tecnicoRepository.findAll();
	}

	public Cliente create(ClienteDTO objDTO) {//converter ClienteDTO, passando para um novo tecnico
		objDTO.setId(null);
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return tecnicoRepository.save(newObj);
	}
	
	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente oldObj = findById(id);
		validaPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		return tecnicoRepository.save(oldObj);
	}
	
	public void delete(Integer id) {
		Cliente obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		}else {
			tecnicoRepository.deleteById(id);
		}
	}

	//validar cpf e email
	private void validaPorCpfEEmail(ClienteDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("Gmail já cadastrado no sistema!");
		}
		
	}

}

package com.software.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.software.helpdesk.domain.Pessoa;
import com.software.helpdesk.domain.Tecnico;
import com.software.helpdesk.domain.dtos.TecnicoDTO;
import com.software.helpdesk.repositories.PessoaRepository;
import com.software.helpdesk.repositories.TecnicoRepository;
import com.software.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.software.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {//converter TecnicoDTO, passando para um novo tecnico
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return tecnicoRepository.save(newObj);
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico oldObj = findById(id);
		
		if(!objDTO.getSenha().equals(oldObj.getSenha())) //{
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		//}
		
		validaPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return tecnicoRepository.save(oldObj);
	}
	
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}else {
			tecnicoRepository.deleteById(id);
		}
	}

	//validar cpf e email
	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
		
	}

}

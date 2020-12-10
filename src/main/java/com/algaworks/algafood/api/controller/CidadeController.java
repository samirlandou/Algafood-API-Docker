package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private CadastroCidadeService cadastroCidadeService;
    
	/**
	 * Listar Cidade
	 * 
	 * @return
	 */   
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cidade> listar() {
		return cidadeRepository.listar();
		
	}
 
    /**
     * Buscar Cidade
     * 
     * @param cidadeId
     * @return
     */
    @GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeRepository.buscar(cidadeId);
		
		if (cidade != null) {
			return ResponseEntity.ok(cidade);
		}
		
		return ResponseEntity.notFound().build();
	}
    
    /**
     * Adicionar Cidade
     * 
     * @param cidade
     * @return
     */
    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
    	
		try {
			cidade = cadastroCidadeService.salvar(cidade);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    /**
     * Atualizar Cidade
     * 
     * @param cidadeId
     * @param cidade
     * @return
     */
	@PutMapping("/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Restaurante cidade) {

		try {
			Cidade cidadeAtual = cidadeRepository.buscar(cidadeId);
			if (cidadeAtual != null) {
				// cozinhaAtual.setNome(cozinha.getNome());
				BeanUtils.copyProperties(cidade,cidadeAtual, "id");

				cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);

				return ResponseEntity.ok(cidadeAtual);
			}
			// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			return ResponseEntity.notFound().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * Remover Cidade
	 * 
	 * @param cidadeId
	 * @return
	 */
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cidadeId) {

		try {
			cadastroCidadeService.excluir(cidadeId);

			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}

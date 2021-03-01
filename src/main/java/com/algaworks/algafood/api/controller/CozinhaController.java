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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
//@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	/**
	 * Listar Cozinha
	 * 
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar() {
		return cozinhaRepository.listar();
	}	
	
	/**
	 * Buscar Cozinha
	 * 
	 * @param cozinhaId
	 * @return
	 */
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}

		// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.notFound().build();
	}

	/**
	 * Adicionar Cozinha
	 * 
	 * @param cozinha
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinhaService.salvar(cozinha);
	}

	/**
	 * Atualizar Cozinha
	 * 
	 * @param cozinhaId
	 * @param cozinha
	 * @return
	 */
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		Cozinha cozinhaAtual = cozinhaRepository.buscar(cozinhaId);

		if (cozinhaAtual != null) {
			// cozinhaAtual.setNome(cozinha.getNome());
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

			cozinhaAtual = cadastroCozinhaService.salvar(cozinhaAtual);

			return ResponseEntity.ok(cozinhaAtual);
		}

		// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.notFound().build();
	}

	/**
	 * Remover Cozinha
	 * 
	 * @param cozinhaId
	 * @return
	 */
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {

		try {
			cadastroCozinhaService.excluir(cozinhaId);

			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	/*
	 * para tratar o methodo com o xml
	 * 
	 * @GetMapping(produces = MediaType.APPLICATION_XML_VALUE) public
	 * CozinhasXmlWrapper listarXml(){ return new
	 * CozinhasXmlWrapper(cozinhaRepository.listar()); }
	 */

	/*
	 * Ex: how to return result with status and Header
	 */
	/*
	 * @ResponseStatus(HttpStatus.CREATED)
	 * 
	 * @GetMapping("/{cozinhaId}") public ResponseEntity<Cozinha>
	 * buscarWithStatusAndHeader(@PathVariable Long cozinhaId) { //Cozinha cozinha =
	 * cozinhaRepository.buscar(cozinhaId);
	 * 
	 * //return ResponseEntity.status(HttpStatus.OK).body(cozinha); //return
	 * ResponseEntity.ok(cozinha); //return cozinhaRepository.buscar(cozinhaId);
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.LOCATION,
	 * "http://localhost:8080/cozinhas");
	 * 
	 * return ResponseEntity.status(HttpStatus.FOUND) .headers(headers) .build(); }
	 */

	/*
	 * @GetMapping("/{cozinhaId}") public Cozinha buscar(@PathVariable Long
	 * cozinhaId) { return cozinhaRepository.buscar(cozinhaId); }
	 */
}

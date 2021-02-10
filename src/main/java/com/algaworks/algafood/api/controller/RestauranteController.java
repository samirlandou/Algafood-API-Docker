package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

	/**
	 * Listar Restaurante
	 * 
	 * @return
	 */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Restaurante> listar() {
		return restauranteRepository.listar();
		
	}

	/**
	 * Buscar Restaurante
	 * 
	 * @param restauranteId
	 * @return
	 */
    @GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteRepository.buscar(restauranteId);
		
		if (restaurante != null) {
			return ResponseEntity.ok(restaurante);
		}
		
		return ResponseEntity.notFound().build();
	}

    /**
     * Adicionar Restaurante
     * 
     * @param restaurante
     * @return
     */
    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
    	
		try {
			restaurante = cadastroRestauranteService.salvar(restaurante);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    /**
     * Atualizar Restaurante
     * 
     * @param restauranteId
     * @param restaurante
     * @return
     */
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {

		try {
			Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
			if (restauranteAtual != null) {
				// cozinhaAtual.setNome(cozinha.getNome());
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");

				restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);

				return ResponseEntity.ok(restauranteAtual);
			}
			// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			return ResponseEntity.notFound().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/**
	 * Atualizar Parcialmente um Restaurante
	 * 
	 * @param restauranteId
	 * @param restaurante
	 * @return
	 */
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos){
		
		Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
		
		//verificar se o restaurante é nulo ou não
		if(restauranteAtual == null) {
			return ResponseEntity.notFound().build();
		}
		
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino) {
		camposOrigem.forEach((nomePropriedade, valorPropiedade) -> {
			
			//Mapper jackson (convert java object into json and json into java object)
			ObjectMapper objectMapper = new ObjectMapper();
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			
			System.out.println(restauranteOrigem);
			
			//Usa o reflection para atrelar as propriedades do campoOrigem para restauranteDestino
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			
			//Autoriza o acesso das variabeis do restaurante apesar do tipo private dos campos.
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
			//System.out.println(nomePropriedade + "=" + valorPropiedade + "=" + novoValor);
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
	
	
	/**
	 * Remover Restaurante
	 * 
	 * @param restauranteId
	 * @return
	 */
	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long restauranteId) {

		try {
			cadastroRestauranteService.excluir(restauranteId);

			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			// return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}

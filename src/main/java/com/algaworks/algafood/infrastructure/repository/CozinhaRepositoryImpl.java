package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

	@PersistenceContext
	private EntityManager manager;

	/**
	 * Listar Cozinha
	 */
	@Override
	public List<Cozinha> listar() {
		return /* TypedQuery<Cozinha> query = */manager.createQuery("from Cozinha", Cozinha.class).getResultList();
	}

	/**
	 * Buscar Cozinha
	 */
	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}

	/**
	 * Salvar Cozinha
	 */
	@Override
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}

	/**
	 * Remover Cozinha
	 */
	@Override
	@Transactional
	public void remover(Long id) {
		Cozinha cozinha = buscar(id);

		if (cozinha == null) {
			throw new EmptyResultDataAccessException(1);
		}

		manager.remove(cozinha);
	}

}

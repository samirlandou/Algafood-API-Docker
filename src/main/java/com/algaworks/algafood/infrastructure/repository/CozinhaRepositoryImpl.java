package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Repository
//@Component
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
	 * Listar Cozinha por Nome
	 */
	@Override
	public List<Cozinha> consultarPorNome(String nome) {
		return manager.createQuery("from Cozinha where nome like :nome", Cozinha.class)
				.setParameter("nome", "%" + nome + "%")
				.getResultList();
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

package br.com.tg.repositorio;

import java.util.List;

import br.com.tg.entidades.StatusFatura;

public interface RepositorioStatusFatura {
	
	public void inserir(StatusFatura statusFatura);

	public void atualizar(StatusFatura statusFatura);

	public StatusFatura getStatusFatura(Integer idStatusFatura);

	public List<StatusFatura> listar();
	
	public void remover(StatusFatura statusFatura);

}

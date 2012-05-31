package br.com.tg.repositorio;

import java.util.List;

import br.com.tg.entidades.StatusPedido;

public interface RepositorioStatusPedido {
	
	public void inserir(StatusPedido statusPedido);

	public void atualizar(StatusPedido statusPedido);

	public StatusPedido getStatusPedido(Integer idStatusPedido);

	public List<StatusPedido> listar();
	
	public void remover(StatusPedido statusPedido);

}

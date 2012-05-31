package br.com.tg.util;

import java.util.List;

import br.com.tg.entidades.StatusPedido;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.StatusPedidoInexistenteException;
import br.com.tg.repositorio.RepositorioStatusPedido;

public class CadastroStatusPedidos {

	private RepositorioStatusPedido statusPedidos;

	public CadastroStatusPedidos(RepositorioStatusPedido rep) {

		this.statusPedidos = rep;
	}

	public void atualizar(StatusPedido sp)
		throws StatusPedidoInexistenteException, ErroAcessoRepositorioException {
		statusPedidos.atualizar(sp);
	}

	public void cadastrar(StatusPedido sp)
		throws ErroAcessoRepositorioException {
		statusPedidos.inserir(sp);
	}

	public void descadastrar(StatusPedido sp)
		throws StatusPedidoInexistenteException, ErroAcessoRepositorioException {
		statusPedidos.remover(sp);
	}

	public StatusPedido procurar(Integer idStatusPedido)
		throws StatusPedidoInexistenteException, ErroAcessoRepositorioException {
		return statusPedidos.getStatusPedido(idStatusPedido);
	}

	public List<StatusPedido> listar() throws ErroAcessoRepositorioException {
		return statusPedidos.listar();
	}

}

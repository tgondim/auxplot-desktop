package br.com.tg.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import br.com.tg.entidades.ItemPedido;
import br.com.tg.entidades.Pedido;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.PedidoInexistenteException;
import br.com.tg.repositorio.RepositorioPedido;

public class CadastroPedidos {

	private RepositorioPedido pedidos;

	public CadastroPedidos(RepositorioPedido rep) {

		this.pedidos = rep;
	}

	public void atualizar(Pedido p)
		throws PedidoInexistenteException, ErroAcessoRepositorioException {
//		for (ItemPedido ip : p.getItensPedido()) {
//			if (ip.getPlotagem() == null) {
//				p.getItensPedido().remove(ip);
//			}
//		}
		pedidos.atualizar(p);
	}

	public void cadastrar(Pedido p)
		throws ErroAcessoRepositorioException {
		for (ItemPedido ip : p.getItensPedido()) {
			if (ip.getId() == null) {
				p.getItensPedido().remove(ip);
			}
		}
		pedidos.inserir(p);
	}

	public void descadastrar(Pedido p)
		throws PedidoInexistenteException, ErroAcessoRepositorioException {
		pedidos.remover(p);
	}

	public Pedido procurar(Integer idPedido)
		throws PedidoInexistenteException, ErroAcessoRepositorioException {
		return pedidos.getPedido(idPedido);
	}

	public List<Pedido> listarPedidos(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente) throws ErroAcessoRepositorioException {
		return pedidos.listarPedidos(restricoes, ordenar, crescente);
	}
	
	public int getPedidoGenerator(String generator) throws HibernateException, SQLException {
		return pedidos.getGenerator(generator);
	}

}

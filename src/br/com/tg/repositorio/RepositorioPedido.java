package br.com.tg.repositorio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import br.com.tg.entidades.Pedido;

public interface RepositorioPedido {
	
	public void inserir(Pedido pedido);

	public void atualizar(Pedido pedido);

	public Pedido getPedido(Integer idPedido);

	public List<Pedido> listarPedidos(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente);

	public void remover(Pedido pedido);
	
	public int getGenerator(String generator) throws HibernateException, SQLException;

}

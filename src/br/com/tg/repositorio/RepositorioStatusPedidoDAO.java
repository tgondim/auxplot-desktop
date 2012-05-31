package br.com.tg.repositorio;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.tg.entidades.StatusPedido;
import br.com.tg.util.HibernateUtil;

public class RepositorioStatusPedidoDAO implements RepositorioStatusPedido {

	private Session session;
	
	@Override
	public void atualizar(StatusPedido statusPedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(statusPedido);
		session.getTransaction().commit();
	}

	@Override
	public StatusPedido getStatusPedido(Integer idStatusPedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		StatusPedido statusPedido = (StatusPedido)session.load(StatusPedido.class, idStatusPedido);
		session.getTransaction().commit();
		return statusPedido;	
	}

	@Override
	public void inserir(StatusPedido statusPedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(statusPedido);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatusPedido> listar() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(StatusPedido.class);
		List<StatusPedido> listaStatusPedidos = c.list();		
		return listaStatusPedidos;
	}

	@Override
	public void remover(StatusPedido statusPedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		StatusPedido statusPedidoRemover = (StatusPedido)session.load(StatusPedido.class, statusPedido.getId());
		session.delete(statusPedidoRemover);
		session.getTransaction().commit();
	}

}

package br.com.tg.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.tg.entidades.Pedido;
import br.com.tg.util.HibernateUtil;

public class RepositorioPedidoDAO implements RepositorioPedido {

	private Session session;
	
	@Override
	public void inserir(Pedido pedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(pedido);
		session.getTransaction().commit();
	}

	@Override
	public void atualizar(Pedido pedido) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(pedido);
		session.getTransaction().commit();
	}
	
	@Override
	public Pedido getPedido(Integer idPedido) throws ObjectNotFoundException {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Pedido pedido = (Pedido)session.load(Pedido.class, idPedido);
		session.getTransaction().commit();
		return pedido;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pedido> listarPedidos(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(Pedido.class);
		if (!restricoes.isEmpty()) {
			for ( Object[] restr : restricoes ) {
				if (restr.length == 3) {
					if (restr[2].equals("ilike")) {
						c.add(Restrictions.ilike((String) restr[0], restr[1]));
					}
					if (restr[2].equals("like")) {
						c.add(Restrictions.like((String) restr[0], restr[1]));
					}
					if (restr[2].equals("eq")) {
						c.add(Restrictions.eq((String) restr[0], restr[1]));
					}
					if (restr[2].equals("ge")) {
						c.add(Restrictions.ge((String) restr[0], restr[1]));
					}
					if (restr[2].equals("le")) {
						c.add(Restrictions.le((String) restr[0], restr[1]));
					}
 				} else if (restr.length == 4) {
					if (restr[1].equals("between")) {
						c.add(Restrictions.between((String) restr[0], restr[2], restr[3]));
					}
				}
			}
		}
		if (crescente) {
			for (int i = 0; i < ordenar.length; i++) {
				c.addOrder(Order.asc((String)ordenar[i]));
			}
		} else {
			for (int i = 0; i < ordenar.length; i++) {
				c.addOrder(Order.desc((String)ordenar[i]));
			}
		}
		List<Pedido> listaPedidos = c.list();		
		return listaPedidos;
	}

	@Override
	public void remover(Pedido pedido){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Pedido pedidoRemover = (Pedido)session.load(Pedido.class, pedido.getId());
		session.delete(pedidoRemover);
		session.getTransaction().commit();
	}
	
	@Override
	public int getGenerator(String generator) throws HibernateException, SQLException {
		int id = 0;
		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor)HibernateUtil.getSessionFactory();
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		PreparedStatement ps = sessionFactory.getConnectionProvider()
			.getConnection().prepareStatement(
					"SELECT GEN_ID(" + generator + ", 1) FROM RDB$DATABASE");
		ResultSet rs = ps.executeQuery();
		rs.next();
		id = rs.getInt("GEN_ID");
		session.getTransaction().commit();
		return id;
	}
	
}

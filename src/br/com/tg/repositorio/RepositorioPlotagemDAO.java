package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.tg.entidades.Plotagem;
import br.com.tg.util.HibernateUtil;

public class RepositorioPlotagemDAO implements RepositorioPlotagem {

	private Session session;
	
	@Override
	public void atualizar(Plotagem plotagem) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(plotagem);
		session.getTransaction().commit();
	}

	@Override
	public Plotagem getPlotagem(Integer idPlotagem) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Plotagem plotagem = (Plotagem)session.load(Plotagem.class, idPlotagem);
		session.getTransaction().commit();
		return plotagem;
	}

	@Override
	public void inserir(Plotagem plotagem) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(plotagem);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Plotagem> listarPlotagens(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(Plotagem.class);
		if (!restricoes.isEmpty()) {
			for ( Object[] restr : restricoes ) {
				if (restr[2].equals("ilike")) {
					c.add(Restrictions.ilike((String) restr[0], restr[1]));
				}
				if (restr[2].equals("like")) {
					c.add(Restrictions.like((String) restr[0], restr[1]));
				}
				if (restr[2].equals("eq")) {
					c.add(Restrictions.eq((String) restr[0], restr[1]));
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
		List<Plotagem> listaPlotagem = c.list();		
		return listaPlotagem;
	}

	@Override
	public void remover(Plotagem plotagem) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Plotagem plotagemRemover = (Plotagem)session.load(Plotagem.class, plotagem.getId());
		session.delete(plotagemRemover);
		session.getTransaction().commit();
	}

}

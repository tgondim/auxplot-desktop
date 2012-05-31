package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.tg.entidades.Fatura;
import br.com.tg.util.HibernateUtil;

public class RepositorioFaturaDAO implements RepositorioFatura {

	private Session session;
	
	@Override
	public void inserir(Fatura fatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(fatura);
		session.getTransaction().commit();
	}

	@Override
	public void atualizar(Fatura fatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(fatura);
		session.getTransaction().commit();
	}
	
	@Override
	public Fatura getFatura(Integer idFatura) throws ObjectNotFoundException {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Fatura fatura = (Fatura)session.load(Fatura.class, idFatura);
		session.getTransaction().commit();
		return fatura;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Fatura> listarFaturas(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(Fatura.class);
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
		List<Fatura> listaFaturas = c.list();		
		return listaFaturas;
	}

	@Override
	public void remover(Fatura fatura){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Fatura faturaRemover = (Fatura)session.load(Fatura.class, fatura.getId());
		session.delete(faturaRemover);
		session.getTransaction().commit();
	}
	
}

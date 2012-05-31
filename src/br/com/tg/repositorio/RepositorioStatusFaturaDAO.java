package br.com.tg.repositorio;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.tg.entidades.StatusFatura;
import br.com.tg.util.HibernateUtil;

public class RepositorioStatusFaturaDAO implements RepositorioStatusFatura {

	private Session session;
	
	@Override
	public void atualizar(StatusFatura statusFatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(statusFatura);
		session.getTransaction().commit();
	}

	@Override
	public StatusFatura getStatusFatura(Integer idStatusFatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		StatusFatura statusFatura = (StatusFatura)session.load(StatusFatura.class, idStatusFatura);
		session.getTransaction().commit();
		return statusFatura;	
	}

	@Override
	public void inserir(StatusFatura statusFatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(statusFatura);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatusFatura> listar() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(StatusFatura.class);
		List<StatusFatura> listaStatusFaturas = c.list();		
		return listaStatusFaturas;
	}

	@Override
	public void remover(StatusFatura statusFatura) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		StatusFatura statusFaturaRemover = (StatusFatura)session.load(StatusFatura.class, statusFatura.getId());
		session.delete(statusFaturaRemover);
		session.getTransaction().commit();
	}

}

package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.tg.entidades.Pessoa;
import br.com.tg.util.HibernateUtil;

public class RepositorioPessoaDAO implements RepositorioPessoa {

	private Session session;
	
	@Override
	public void inserir(Pessoa pessoa) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.save(pessoa);
		session.getTransaction().commit();
	}

	@Override
	public void atualizar(Pessoa pessoa) {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.flush();
		session.clear();
		session.merge(pessoa);
		session.getTransaction().commit();
	}
	
	@Override
	public Pessoa getPessoa(Integer idPessoa) throws ObjectNotFoundException {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Pessoa pessoa = (Pessoa)session.load(Pessoa.class, idPessoa);
		session.getTransaction().commit();
		return pessoa;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> listarPessoas(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(Pessoa.class);
		Disjunction ou = Restrictions.disjunction();
		boolean disj = false;
		if (!restricoes.isEmpty()) {
			for ( Object[] restr : restricoes ) {
				if (restr[2].equals("ilike")) {
					c.add(Restrictions.ilike((String) restr[0], restr[1]));
				}
				if (restr[2].equals("like")) {
					c.add(Restrictions.like((String) restr[0], restr[1]));
				}
				if (restr[2].equals("eq")) {
					if (restr.length == 4) {
						if (restr[3].equals("or")) {
							ou.add(Restrictions.eq((String) restr[0], restr[1]));
							disj = true;
						}
					} else {
						c.add(Restrictions.eq((String) restr[0], restr[1]));
					}
				}
			}
		}
		if (disj) {
			c.add(ou);
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
		List<Pessoa> listaPessoas = c.list();		
		return listaPessoas;
	}

	@Override
	public void remover(Pessoa pessoa){
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Pessoa pessoaRemover = (Pessoa)session.load(Pessoa.class, pessoa.getId());
		session.delete(pessoaRemover);
		session.getTransaction().commit();
	}
	
}

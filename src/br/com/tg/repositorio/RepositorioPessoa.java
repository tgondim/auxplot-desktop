package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import br.com.tg.entidades.Pessoa;

public interface RepositorioPessoa {
	
	public void inserir(Pessoa pessoa);

	public void atualizar(Pessoa pessoa);

	public Pessoa getPessoa(Integer idPessoa);

	public List<Pessoa> listarPessoas(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente);

	public void remover(Pessoa pessoa);

}

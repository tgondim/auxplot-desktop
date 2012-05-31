package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import br.com.tg.entidades.Fatura;

public interface RepositorioFatura {
	
	public void inserir(Fatura fatura);

	public void atualizar(Fatura fatura);

	public Fatura getFatura(Integer idFatura);

	public List<Fatura> listarFaturas(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente);

	public void remover(Fatura fatura);

}

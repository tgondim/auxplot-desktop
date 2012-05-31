package br.com.tg.repositorio;

import java.util.ArrayList;
import java.util.List;

import br.com.tg.entidades.Plotagem;

public interface RepositorioPlotagem {
	
	public void inserir(Plotagem plotagem);

	public void atualizar(Plotagem plotagem);

	public Plotagem getPlotagem(Integer idPlotagem);

	public List<Plotagem> listarPlotagens(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente);

	public void remover(Plotagem plotagem);
	
}

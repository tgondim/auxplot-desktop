package br.com.tg.util;

import java.util.ArrayList;
import java.util.List;

import br.com.tg.entidades.Plotagem;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.PlotagemInexistenteException;
import br.com.tg.repositorio.RepositorioPlotagem;

public class CadastroPlotagens {

	private RepositorioPlotagem plotagens;

	public CadastroPlotagens(RepositorioPlotagem rep) {

		this.plotagens = rep;
	}

	public void atualizar(Plotagem p)
		throws PlotagemInexistenteException, ErroAcessoRepositorioException {
		plotagens.atualizar(p);
	}

	public void cadastrar(Plotagem p)
		throws ErroAcessoRepositorioException {
		plotagens.inserir(p);
	}

	public void descadastrar(Plotagem p)
		throws PlotagemInexistenteException, ErroAcessoRepositorioException {
		plotagens.remover(p);
	}

	public Plotagem procurar(Integer idPlotagem)
		throws PlotagemInexistenteException, ErroAcessoRepositorioException {
		return plotagens.getPlotagem(idPlotagem);
	}

	public List<Plotagem> listarPlotagens(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente) throws ErroAcessoRepositorioException {
		return plotagens.listarPlotagens(restricoes, ordenar, crescente);
	}

}

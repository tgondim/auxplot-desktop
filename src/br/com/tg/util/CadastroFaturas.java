package br.com.tg.util;

import java.util.ArrayList;
import java.util.List;

import br.com.tg.entidades.Fatura;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.FaturaInexistenteException;
import br.com.tg.repositorio.RepositorioFatura;

public class CadastroFaturas {

	private RepositorioFatura faturas;

	public CadastroFaturas(RepositorioFatura rep) {

		this.faturas = rep;
	}

	public void atualizar(Fatura f)
		throws FaturaInexistenteException, ErroAcessoRepositorioException {
		faturas.atualizar(f);
	}

	public void cadastrar(Fatura f)
		throws ErroAcessoRepositorioException {
		faturas.inserir(f);
	}

	public void descadastrar(Fatura f)
		throws FaturaInexistenteException, ErroAcessoRepositorioException {
		faturas.remover(f);
	}

	public Fatura procurar(Integer idFatura)
		throws FaturaInexistenteException, ErroAcessoRepositorioException {
		return faturas.getFatura(idFatura);
	}

	public List<Fatura> listarFaturas(ArrayList<Object[]> restricoes, Object[] ordenar, boolean crescente) throws ErroAcessoRepositorioException {
		return faturas.listarFaturas(restricoes, ordenar, crescente);
	}

}

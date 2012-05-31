package br.com.tg.util;

import java.util.List;

import br.com.tg.entidades.StatusFatura;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.StatusFaturaInexistenteException;
import br.com.tg.repositorio.RepositorioStatusFatura;

public class CadastroStatusFaturas {

	private RepositorioStatusFatura statusFaturas;

	public CadastroStatusFaturas(RepositorioStatusFatura rep) {

		this.statusFaturas = rep;
	}

	public void atualizar(StatusFatura sf)
		throws StatusFaturaInexistenteException, ErroAcessoRepositorioException {
		statusFaturas.atualizar(sf);
	}

	public void cadastrar(StatusFatura sf)
		throws ErroAcessoRepositorioException {
		statusFaturas.inserir(sf);
	}

	public void descadastrar(StatusFatura sf)
		throws StatusFaturaInexistenteException, ErroAcessoRepositorioException {
		statusFaturas.remover(sf);
	}

	public StatusFatura procurar(Integer idStatusFatura)
		throws StatusFaturaInexistenteException, ErroAcessoRepositorioException {
		return statusFaturas.getStatusFatura(idStatusFatura);
	}

	public List<StatusFatura> listar() throws ErroAcessoRepositorioException {
		return statusFaturas.listar();
	}

}

package br.com.tg.exceptions;

/**
 * Exceção caso a pessoa tratada não exista.
 */
@SuppressWarnings("serial")
public class PessoaInexistenteException extends Exception {
 
	private static final String MSG_PESSOA_INEXISTENTE = "Pessoa não cadastrada.";
	 
	public PessoaInexistenteException() {
		super(MSG_PESSOA_INEXISTENTE);
	}
	 
}
 

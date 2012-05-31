package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class StatusPessoaInexistenteException extends Exception {
	 
	private static final String MSG_STATUS_PESSOA_INEXISTENTE = "Status de Pessoa não cadastrado.";
	 
	public StatusPessoaInexistenteException() {
		super(MSG_STATUS_PESSOA_INEXISTENTE);
	}
	 
}

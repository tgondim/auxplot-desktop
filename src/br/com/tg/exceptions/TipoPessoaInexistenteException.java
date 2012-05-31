package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class TipoPessoaInexistenteException extends Exception {
	 
	private static final String MSG_TIPO_PESSOA_INEXISTENTE = "Status de Pessoa n�o cadastrado.";
	 
	public TipoPessoaInexistenteException() {
		super(MSG_TIPO_PESSOA_INEXISTENTE);
	}
	 
}

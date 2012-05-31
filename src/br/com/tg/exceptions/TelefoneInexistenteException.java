package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class TelefoneInexistenteException extends Exception {
	
	private static final String MSG_TELEFONE_INEXISTENTE = "Telefone n�o cadastrado.";
	 
	public TelefoneInexistenteException() {
		super(MSG_TELEFONE_INEXISTENTE);
	}
	
}

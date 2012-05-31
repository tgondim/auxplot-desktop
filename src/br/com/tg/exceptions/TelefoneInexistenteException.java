package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class TelefoneInexistenteException extends Exception {
	
	private static final String MSG_TELEFONE_INEXISTENTE = "Telefone não cadastrado.";
	 
	public TelefoneInexistenteException() {
		super(MSG_TELEFONE_INEXISTENTE);
	}
	
}

package br.com.tg.exceptions;

/**
 * Exce��o caso a pessoa tratada n�o exista.
 */
@SuppressWarnings("serial")
public class PlotagemInexistenteException extends Exception {
 
	private static final String MSG_PLOTAGEM_INEXISTENTE = "Plotagem n�o cadastrada.";
	 
	public PlotagemInexistenteException() {
		super(MSG_PLOTAGEM_INEXISTENTE);
	}
	 
}
 

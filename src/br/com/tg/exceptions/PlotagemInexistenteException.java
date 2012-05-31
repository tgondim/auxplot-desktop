package br.com.tg.exceptions;

/**
 * Exceção caso a pessoa tratada não exista.
 */
@SuppressWarnings("serial")
public class PlotagemInexistenteException extends Exception {
 
	private static final String MSG_PLOTAGEM_INEXISTENTE = "Plotagem não cadastrada.";
	 
	public PlotagemInexistenteException() {
		super(MSG_PLOTAGEM_INEXISTENTE);
	}
	 
}
 

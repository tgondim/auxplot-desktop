package br.com.tg.exceptions;

/**
 * Exceção caso a pessoa tratada não exista.
 */
@SuppressWarnings("serial")
public class FaturaInexistenteException extends Exception {
 
	private static final String MSG_FATURA_INEXISTENTE = "Fatura não cadastrada.";
	 
	public FaturaInexistenteException() {
		super(MSG_FATURA_INEXISTENTE);
	}
	 
}
 

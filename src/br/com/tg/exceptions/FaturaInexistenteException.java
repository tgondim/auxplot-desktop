package br.com.tg.exceptions;

/**
 * Exce��o caso a pessoa tratada n�o exista.
 */
@SuppressWarnings("serial")
public class FaturaInexistenteException extends Exception {
 
	private static final String MSG_FATURA_INEXISTENTE = "Fatura n�o cadastrada.";
	 
	public FaturaInexistenteException() {
		super(MSG_FATURA_INEXISTENTE);
	}
	 
}
 

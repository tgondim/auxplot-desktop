package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class StatusFaturaInexistenteException extends Exception {
	 
	private static final String MSG_STATUS_FATURA_INEXISTENTE = "Status de Fatura não cadastrado.";
	 
	public StatusFaturaInexistenteException() {
		super(MSG_STATUS_FATURA_INEXISTENTE);
	}
	 
}

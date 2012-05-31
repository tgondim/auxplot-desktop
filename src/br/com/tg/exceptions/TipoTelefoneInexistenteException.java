package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class TipoTelefoneInexistenteException extends Exception {
	
	private static final String MSG_TIPO_TELEFONE_INEXISTENTE = "Tipo de Telefone não cadastrado.";
	 
	public TipoTelefoneInexistenteException() {
		super(MSG_TIPO_TELEFONE_INEXISTENTE);
	}
	
}

package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class EnderecoInexistenteException extends Exception {
	
	private static final String MSG_ENDERECO_INEXISTENTE = "Endereco não cadastrado.";
	 
	public EnderecoInexistenteException() {
		super(MSG_ENDERECO_INEXISTENTE);
	}
	
}

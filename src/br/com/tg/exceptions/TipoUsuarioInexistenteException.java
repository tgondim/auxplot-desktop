package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class TipoUsuarioInexistenteException extends Exception {
	
	private static final String MSG_TIPO_USUARIO_INEXISTENTE = "Tipo de Usuário não cadastrado.";
	 
	public TipoUsuarioInexistenteException() {
		super(MSG_TIPO_USUARIO_INEXISTENTE);
	}
	
}

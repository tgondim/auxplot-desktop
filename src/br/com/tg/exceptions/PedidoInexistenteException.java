package br.com.tg.exceptions;

/**
 * Exce��o caso a pessoa tratada n�o exista.
 */
@SuppressWarnings("serial")
public class PedidoInexistenteException extends Exception {
 
	private static final String MSG_PEDIDO_INEXISTENTE = "Pedido n�o cadastrado.";
	 
	public PedidoInexistenteException() {
		super(MSG_PEDIDO_INEXISTENTE);
	}
	 
}
 

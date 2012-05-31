package br.com.tg.exceptions;

/**
 * Exceção caso a pessoa tratada não exista.
 */
@SuppressWarnings("serial")
public class PedidoInexistenteException extends Exception {
 
	private static final String MSG_PEDIDO_INEXISTENTE = "Pedido não cadastrado.";
	 
	public PedidoInexistenteException() {
		super(MSG_PEDIDO_INEXISTENTE);
	}
	 
}
 

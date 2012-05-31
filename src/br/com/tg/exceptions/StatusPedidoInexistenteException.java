package br.com.tg.exceptions;

@SuppressWarnings("serial")
public class StatusPedidoInexistenteException extends Exception {
	 
	private static final String MSG_STATUS_PEDIDO_INEXISTENTE = "Status de Pedido não cadastrado.";
	 
	public StatusPedidoInexistenteException() {
		super(MSG_STATUS_PEDIDO_INEXISTENTE);
	}
	 
}

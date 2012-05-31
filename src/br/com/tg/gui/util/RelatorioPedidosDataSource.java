package br.com.tg.gui.util;

import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.tg.entidades.Pedido;

public class RelatorioPedidosDataSource implements JRDataSource {

	private Iterator<Pedido> iterator;
	
	private Pedido cursor;
	
	public RelatorioPedidosDataSource(Vector<Pedido> pedidos) {
		super();
		this.iterator = pedidos.iterator();
	}
	
	@Override
	public Object getFieldValue(JRField nome) throws JRException {
		Pedido pedido = this.cursor;
		if (nome.getName().equals("id")) {
			return pedido.getId();
		}
		if (nome.getName().equals("pessoaPai")) {
			return pedido.getPessoaPai().getNome();
		}
		if (nome.getName().equals("nomeProjeto")) {
			return pedido.getNomeProjeto();
		}
		if (nome.getName().equals("statusPedido")) {
			return pedido.getStatusPedido().getDescricao();
		}
		if (nome.getName().equals("dataEmissao")) {
			return pedido.getDataEmissao();
		}
		if (nome.getName().equals("valorTotal")) {
			return pedido.getValorTotal();
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		boolean retorno = this.iterator.hasNext();
		if (retorno) {
			this.cursor = this.iterator.next();
		}
		return retorno;
	}	

}

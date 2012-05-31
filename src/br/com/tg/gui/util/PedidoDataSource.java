package br.com.tg.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.tg.entidades.ItemPedido;
import br.com.tg.entidades.Pedido;
import br.com.tg.entidades.Pessoa;
import br.com.tg.util.Validador;

public class PedidoDataSource implements JRDataSource {

	private Iterator<Pedido> iterator;
	
	private Pedido cursor;
	
	public PedidoDataSource(Vector<Pedido> pedidos) {
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
			return (Pessoa)pedido.getPessoaPai();
		}
		if (nome.getName().equals("nomeProjeto")) {
			return pedido.getNomeProjeto();
		}
		if (nome.getName().equals("nomeSolicitante")) {
			return pedido.getNomeSolicitante();
		}
		if (nome.getName().equals("itensPedido")) {
			return new JRBeanCollectionDataSource(new ArrayList<ItemPedido>(pedido.getItensPedido()));
		}
		if (nome.getName().equals("itensPedido2")) {
			return new JRBeanCollectionDataSource(new ArrayList<ItemPedido>(pedido.getItensPedido()));
		}
		if (nome.getName().equals("taxaEntrega")) {
			return Validador.inserirVirgula(pedido.getTaxaEntrega());
		}
		if (nome.getName().equals("valorTotal")) {
			return Validador.inserirVirgula(pedido.getValorTotal());
		}
		if (nome.getName().equals("dataEmissao")) {
			return pedido.getDataEmissao();
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

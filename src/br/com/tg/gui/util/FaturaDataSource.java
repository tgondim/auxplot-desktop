package br.com.tg.gui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.Pedido;
import br.com.tg.util.Validador;

public class FaturaDataSource implements JRDataSource {

	private Iterator<Fatura> iterator;
	
	private Fatura cursor;
	
	public FaturaDataSource(Vector<Fatura> faturas) {
		super();
		this.iterator = faturas.iterator();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getFieldValue(JRField nome) throws JRException {
		Fatura fatura = this.cursor;
		if (nome.getName().equals("dataEmissao")) {
			return CalendarFormatter.formatDate(fatura.getDataEmissao());
		}
		if (nome.getName().equals("dataVencimento")) {
			return CalendarFormatter.formatDate(fatura.getDataVencimento());
		}
		if (nome.getName().equals("id")) {
			return fatura.getId();
		}
		if (nome.getName().equals("projeto")) {
			return fatura.getProjeto();
		}
		if (nome.getName().equals("notaFiscal")) {
			return fatura.getNotaFiscal();
		}
		if (nome.getName().equals("pedidos")) {
			List<Pedido> listaOrdenada = new ArrayList<Pedido>(fatura.getPedidos());
			Collections.sort(listaOrdenada, new Pedido());
			return new JRBeanCollectionDataSource(listaOrdenada);
		}
		if (nome.getName().equals("valorTotal")) {
			return Validador.inserirVirgula(fatura.getValorTotal());
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

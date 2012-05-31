package br.com.tg.gui.util;

import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.tg.entidades.Fatura;

public class RelatorioFaturasDataSource implements JRDataSource {

	private Iterator<Fatura> iterator;
	
	private Fatura cursor;
	
	public RelatorioFaturasDataSource(Vector<Fatura> faturas) {
		super();
		this.iterator = faturas.iterator();
	}
	
	@Override
	public Object getFieldValue(JRField nome) throws JRException {
		Fatura fatura = this.cursor;
		if (nome.getName().equals("id")) {
			return fatura.getId();
		}
		if (nome.getName().equals("pessoaPai")) {
			return fatura.getPessoaPai().getNome();
		}
		if (nome.getName().equals("statusFatura")) {
			return fatura.getStatusFatura().getDescricao();
		}
		if (nome.getName().equals("dataEmissao")) {
			return fatura.getDataEmissao();
		}
		if (nome.getName().equals("dataVencimento")) {
			return fatura.getDataVencimento();
		}
		if (nome.getName().equals("valorTotal")) {
			return fatura.getValorTotal();
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

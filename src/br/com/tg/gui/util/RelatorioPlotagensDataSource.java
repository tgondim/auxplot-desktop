package br.com.tg.gui.util;

import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.tg.entidades.Plotagem;

public class RelatorioPlotagensDataSource implements JRDataSource {

	private Iterator<Plotagem> iterator;
	
	private Plotagem cursor;
	
	public RelatorioPlotagensDataSource(Vector<Plotagem> plotagens) {
		super();
		this.iterator = plotagens.iterator();
	}
	
	@Override
	public Object getFieldValue(JRField nome) throws JRException {
		Plotagem plotagem = this.cursor;
		if (nome.getName().equals("id")) {
			return plotagem.getId();
		}
		if (nome.getName().equals("descricao")) {
			return plotagem.getDescricao();
		}
		if (nome.getName().equals("unidade")) {
			return plotagem.getUnidade();
		}
		if (nome.getName().equals("valor")) {
			return plotagem.getValor();
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

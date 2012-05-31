package br.com.tg.gui.util;

import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.tg.entidades.Pessoa;

public class RelatorioPessoasDataSource implements JRDataSource {

	private Iterator<Pessoa> iterator;
	
	private Pessoa cursor;
	
	public RelatorioPessoasDataSource(Vector<Pessoa> pessoas) {
		super();
		this.iterator = pessoas.iterator();
	}
	
	@Override
	public Object getFieldValue(JRField nome) throws JRException {
		Pessoa pessoa = this.cursor;
		if (nome.getName().equals("id")) {
			return pessoa.getId();
		}
		if (nome.getName().equals("nome")) {
			return pessoa.getNome();
		}
		if (nome.getName().equals("cidade")) {
			return pessoa.getEndereco().getCidade();
		}
		if (nome.getName().equals("uf")) {
			return pessoa.getEndereco().getUf();
		}
		if (nome.getName().equals("tipoPessoa")) {
			return pessoa.getTipoPessoa().toString();
		}
		if (nome.getName().equals("statusPessoa")) {
			return pessoa.getStatusPessoa().toString();
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

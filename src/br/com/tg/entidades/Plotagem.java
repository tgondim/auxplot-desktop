package br.com.tg.entidades;

import java.util.Calendar;

public class Plotagem {

	private Integer id;
	private String descricao;
	private String unidade;
	private float valor;
	private Calendar dataCadastro;
	private Usuario usuarioCadastro;
	private Calendar dataAlteracao;
	private Usuario usuarioAlteracao;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getUnidade() {
		return unidade;
	}
	
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	
	public float getValor() {
		return valor;
	}
	
	public void setValor(float valor) {
		this.valor = valor;
	}
	
	public Calendar getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Calendar dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Calendar getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Calendar dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((Plotagem)o).getId());
		return false;
	}
	
	@Override 
	public String toString() {
		return this.descricao;
	}
}

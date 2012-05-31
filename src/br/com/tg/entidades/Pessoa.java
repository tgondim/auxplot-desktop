package br.com.tg.entidades;

import java.util.Calendar;
import java.util.Set;


/**
 * Representa todas as pessoas no sistema.
 * Física, jurídica, usuário.
 */

public abstract class Pessoa {
	
	private Integer id;
	private String nome;
	private Endereco endereco;
	private Set<Telefone> telefones;
	private StatusPessoa statusPessoa;
	private TipoPessoa tipoPessoa;
	private Calendar dataCadastro;
	private Usuario usuarioCadastro;
	private Calendar dataAlteracao;
	private Usuario usuarioAlteracao;
	 
	public Integer getId() {
		return id;
	}
	 
	public void setId(Integer newId) {
		this.id = newId;
	}
	 
	public String getNome() {
		return nome;
	}
	 
	public void setNome(String newNome) {
		this.nome = newNome;
	 
	}
	 
	public Endereco getEndereco() {
		return endereco;
	}
	 
	public void setEndereco(Endereco newEndereco) {
		this.endereco = newEndereco;
	 
	}
	 
	public Set<Telefone> getTelefones() {
		return telefones;
	}
	 
	public void setTelefones(Set<Telefone> newTelefones) {
		this.telefones = newTelefones;
	 
	}
	 
	public StatusPessoa getStatusPessoa() {
		return statusPessoa;
	}
	 
	public void setStatusPessoa(StatusPessoa newStatusPessoa) {
		this.statusPessoa = newStatusPessoa;
	 
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
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
			return this.id.equals(((Pessoa)o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
	
	public String getTelefonesString() {
		String retorno = "";
		for (Telefone tel : this.telefones) {
			if (!retorno.equals("")) {
				retorno += " / ";
			}
			retorno += "(" + tel.getCodigoArea() + ") " +
				tel.getNumero(); 
		}
		return retorno;
	}
	
}
 

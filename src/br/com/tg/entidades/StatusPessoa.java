package br.com.tg.entidades;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StatusPessoa implements Serializable{

	public StatusPessoa() {
		
	}
	
	public StatusPessoa(Integer newId) {
		this.id = newId;
	}
	
	private Integer id;
	
	private String descricao;
	
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
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return (this.id.equals(((StatusPessoa)o).getId()));
		return false;
	}

}

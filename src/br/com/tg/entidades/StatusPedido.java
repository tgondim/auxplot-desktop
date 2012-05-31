package br.com.tg.entidades;

import java.io.Serializable;

public class StatusPedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
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
		if (o != null && this.id != null) {
			return (this.id.equals(((StatusPedido)o).getId()));
		}
		return false;
	}

}

package br.com.tg.entidades;

public class TipoPessoa {
 
	private Integer id;
	 
	private String descricao;
	 
	public TipoPessoa() {
	 
	}

	public TipoPessoa(int newId) {
		this.id = newId;
	}
	 
	public Integer getId() {
		return id;
	}
	 
	public void setId(Integer newId) {
		this.id = newId;
	 
	}
	 
	public String getDescricao() {
		return descricao;
	}
	 
	public void setDescricao(String newDescricao) {
		this.descricao = newDescricao;
	 
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((TipoPessoa)o).getId());
		return false;
	}
	
}
 

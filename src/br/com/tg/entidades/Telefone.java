package br.com.tg.entidades;


public class Telefone {
 
	private Integer id;
	private Pessoa pessoaPai;
	private TipoTelefone tipoTelefone;
	private String codigoArea;
	private String numero;
	 
	public Telefone() {
		this.id = 0;
		this.codigoArea = "";
		this.numero = "";
		this.tipoTelefone = new TipoTelefone();
	}
	 
	public Telefone(int newId, String newCodigoArea, String newNumero, TipoTelefone newTipoTelefone) {
		this.id = newId;
		this.codigoArea = newCodigoArea;
		this.numero = newNumero;
		this.tipoTelefone = newTipoTelefone;
	}

	public Integer getId() {
		return id;
	}
	 
	public void setId(Integer newId) {
		this.id = newId;
	 
	}
	 
	public Pessoa getPessoaPai() {
		return pessoaPai;
	}

	public void setPessoaPai(Pessoa pessoa) {
		this.pessoaPai = pessoa;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}
	 
	public void setTipoTelefone(TipoTelefone newTipoTelefone) {
		this.tipoTelefone = newTipoTelefone;
	 
	}
	 
	public String getCodigoArea() {
		return codigoArea;
	}
	 
	public void setCodigoArea(String newCodigoArea) {
		this.codigoArea = newCodigoArea;
	 
	}
	 
	public String getNumero() {
		return numero;
	}
	 
	public void setNumero(String newNumero) {
		this.numero = newNumero;
	 
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((Telefone)o).getId());
		return false;
	}
	
	@Override 
	public String toString() {
		return this.tipoTelefone.getDescricao() + " (" + this.codigoArea + ") " + this.numero;
	}	 
}
 

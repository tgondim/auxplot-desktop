package br.com.tg.entidades;


public class Endereco {
 
	private Integer id;
	private Pessoa pessoaPai;
	private String logradouro;
	private Integer numero;
	private String bairro;
	private String complemento;
	private String cidade;
	private String uf;
	private String cep;
	private String email;
	 
	public Endereco() {
	 
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

	public String getLogradouro() {
		return logradouro;
	}
	 
	public void setLogradouro(String newLogradouro) {
		this.logradouro = newLogradouro;
	 
	}
	 
	public Integer getNumero() {
		return numero;
	}
	 
	public void setNumero(Integer newNumero) {
		this.numero = newNumero;
	 
	}
	 
	public String getBairro() {
		return bairro;
	}
	 
	public void setBairro(String newBairro) {
		this.bairro = newBairro;
	 
	}
	 
	public String getComplemento() {
		return complemento;
	}
	 
	public void setComplemento(String newComplemento) {
		this.complemento = newComplemento;
	 
	}
	 
	public String getCidade() {
		return cidade;
	}
	 
	public void setCidade(String newCidade) {
		this.cidade = newCidade;
	 
	}
	 
	public String getUf() {
		return uf;
	}
	 
	public void setUf(String newUf) {
		this.uf = newUf;
	 
	}
	 
	public String getCep() {
		return cep;
	}
	 
	public void setCep(String newCep) {
		this.cep = newCep;
	 
	}
	 
	public String getEmail() {
		return email;
	}
	 
	public void setEmail(String newEmail) {
		this.email = newEmail;
	 
	}
	
	@Override 
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((Endereco)o).getId());
		return false;
	}
	
	@Override
	public String toString() {
		return this.logradouro + ", " + 
		(this.numero != null ? this.numero : "") + " " + 
		this.complemento + " - " + 
		this.bairro + " - " + 
		this.cidade + " - " + 
		this.uf +
		" CEP: " + this.cep;
	}
	 
}
 

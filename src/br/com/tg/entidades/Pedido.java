package br.com.tg.entidades;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class Pedido implements Comparator {

	private Integer id;
	private Pessoa pessoaPai;
	private String nomeProjeto;
	private String nomeSolicitante;
	private StatusPedido statusPedido;
	private Set<ItemPedido> itensPedido;
	private float taxaEntrega;
	private float valorTotal;
	private Calendar dataEmissao;
	private Calendar dataCadastro;
	private Usuario usuarioCadastro;
	private Calendar dataAlteracao;
	private Usuario usuarioAlteracao;
	
	//estes atributos não são persistidos
	private boolean marcado;
	private boolean novo;
	
	public Pedido() {
	}
	
	public Pedido(Integer id, Pessoa pessoaPai, String nomeProjeto,
			String nomeSolicitante, StatusPedido statusPedido,
			Set<ItemPedido> itensPedido, float taxaEntrega, float valorTotal,
			Calendar dataEmissao, Calendar dataCadastro,
			Usuario usuarioCadastro, Calendar dataAlteracao,
			Usuario usuarioAlteracao) {
		super();
		this.id = id;
		this.pessoaPai = pessoaPai;
		this.nomeProjeto = nomeProjeto;
		this.nomeSolicitante = nomeSolicitante;
		this.statusPedido = statusPedido;
		this.itensPedido = itensPedido;
		this.taxaEntrega = taxaEntrega;
		this.valorTotal = valorTotal;
		this.dataEmissao = dataEmissao;
		this.dataCadastro = dataCadastro;
		this.usuarioCadastro = usuarioCadastro;
		this.dataAlteracao = dataAlteracao;
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Pedido(boolean newNovo) {
		this.novo = newNovo;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Pessoa getPessoaPai() {
		return pessoaPai;
	}

	public void setPessoaPai(Pessoa pessoaPai) {
		this.pessoaPai = pessoaPai;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getNomeSolicitante() {
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}

	public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
	}

	public Set<ItemPedido> getItensPedido() {
		return itensPedido;
	}

	public void setItensPedido(Set<ItemPedido> itensPedido) {
		this.itensPedido = itensPedido;
	}

	public float getTaxaEntrega() {
		return taxaEntrega;
	}

	public void setTaxaEntrega(float taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}

	public float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Calendar getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Calendar dataEmissao) {
		this.dataEmissao = dataEmissao;
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
	
	public boolean isNovo() {
		return novo;
	}

	public void setNovo(boolean novo) {
		this.novo = novo;
	}

	public boolean isMarcado() {
		return marcado;
	}

	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((Pedido)o).getId());
		return false;
	}
	
	@Override 
	public String toString() {
		return this.id + " - " + this.pessoaPai.getNome() + " R$ " + this.valorTotal;
	}
	
	@Override
	public Pedido clone() {
		Pedido pedidoClone = new Pedido(this.id, 
				this.pessoaPai,
				this.nomeProjeto,
				this.nomeSolicitante,
				this.statusPedido,
				this.itensPedido,
				this.taxaEntrega,
				this.valorTotal,
				this.dataEmissao,
				this.dataCadastro,
				this.usuarioCadastro,
				this.dataAlteracao,
				this.usuarioAlteracao);
		return pedidoClone;
	}

//	@Override
//	public int compare(Pedido o1, Pedido o2) {		
//		return o1.getDataEmissao().compareTo(o2.getDataEmissao());
//	}

	@Override
	public int compare(Object o1, Object o2) {
		return ((Pedido)o1).getDataEmissao().compareTo(((Pedido)o2).getDataEmissao());
	}
}

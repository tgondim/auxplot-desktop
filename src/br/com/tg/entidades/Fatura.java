package br.com.tg.entidades;

import java.util.Calendar;
import java.util.Set;

public class Fatura {
	
	private Integer id;
	private Pessoa pessoaPai;
	private StatusFatura statusFatura;
	private Set<Pedido> pedidos;
	private float valorTotal;
	private Calendar dataEmissao;
	private Calendar dataVencimento;
	private String projeto;
	private String notaFiscal;
	private Calendar dataCadastro;
	private Usuario usuarioCadastro;
	private Calendar dataAlteracao;
	private Usuario usuarioAlteracao;
	
	public Fatura() {
	}
	
	public Fatura(Integer id, Pessoa pessoaPai, StatusFatura statusFatura,
			Set<Pedido> pedidos, float valorTotal, Calendar dataEmissao,
			Calendar dataVencimento, String projeto, String notaFiscal,
			Calendar dataCadastro, Usuario usuarioCadastro,
			Calendar dataAlteracao, Usuario usuarioAlteracao) {
		super();
		this.id = id;
		this.pessoaPai = pessoaPai;
		this.statusFatura = statusFatura;
		this.pedidos = pedidos;
		this.valorTotal = valorTotal;
		this.dataEmissao = dataEmissao;
		this.dataVencimento = dataVencimento;
		this.projeto = projeto;
		this.notaFiscal = notaFiscal;
		this.dataCadastro = dataCadastro;
		this.usuarioCadastro = usuarioCadastro;
		this.dataAlteracao = dataAlteracao;
		this.usuarioAlteracao = usuarioAlteracao;
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

	public StatusFatura getStatusFatura() {
		return statusFatura;
	}

	public void setStatusFatura(StatusFatura statusFatura) {
		this.statusFatura = statusFatura;
	}

	public Set<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(Set<Pedido> pedidos) {
		this.pedidos = pedidos;
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
	
	public Calendar getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
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
			return this.id.equals(((Fatura)o).getId());
		return false;
	}
	
	@Override
	public Fatura clone() {
		Fatura faturaClone = new Fatura(
				this.id,
				this.pessoaPai,
				this.statusFatura,
				this.pedidos,
				this.valorTotal,
				this.dataEmissao,
				this.dataVencimento,
				this.projeto,
				this.notaFiscal,
				this.dataCadastro,
				this.usuarioCadastro,
				this.dataAlteracao,
				this.usuarioAlteracao);
		return faturaClone;
	}
}

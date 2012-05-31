package br.com.tg.entidades;

public class ItemPedido {
	
	private Integer id;
	private Pedido pedidoPai;
	private Plotagem plotagem;
	private String plantaDescricao;
	private float quantidade;
	private float valorUnitario;
	private float valorTotal;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Plotagem getPlotagem() {
		return plotagem;
	}
	
	public void setPlotagem(Plotagem plotagem) {
		this.plotagem = plotagem;
	}
	
	public String getPlantaDescricao() {
		return plantaDescricao;
	}
	
	public void setPlantaDescricao(String plantaDescricao) {
		this.plantaDescricao = plantaDescricao;
	}
	
	public float getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(float quantidade) {
		this.quantidade = quantidade;
	}
	
	public float getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	public float getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public Pedido getPedidoPai() {
		return pedidoPai;
	}

	public void setPedidoPai(Pedido pedidoPai) {
		this.pedidoPai = pedidoPai;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && this.id != null) 
			return this.id.equals(((ItemPedido)o).getId());
		return false;
	}
	
	@Override 
	public String toString() {
		return this.id + " - " + this.plotagem.getDescricao() + " R$ " + this.valorTotal;
	}
	
}

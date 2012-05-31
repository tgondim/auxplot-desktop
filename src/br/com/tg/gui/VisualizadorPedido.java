package br.com.tg.gui;

import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.tg.entidades.Pedido;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.PedidoInexistenteException;
import br.com.tg.gui.util.PedidoDataSource;

public class VisualizadorPedido {
	
	private Pedido pedido;
	
	private JasperReport report;
	
	private PedidoDataSource dsPedidos;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String logoRecicle;
	private String relatorio;
	private String subRelatorio;
	
	@SuppressWarnings("unchecked")
	private HashMap params;
	
	public VisualizadorPedido(Pedido newPedido,
			String newLogo, String newRelatorio, String newSubRelatorio, String newLogoRecicle) 
				throws JRException, PedidoInexistenteException, ErroAcessoRepositorioException {
		
		this.pedido = newPedido;
		this.logo = newLogo;
		this.logoRecicle = newLogoRecicle;
		this.relatorio = newRelatorio;
		this.subRelatorio = newSubRelatorio;
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public PedidoDataSource getDsPedidos()
			throws PedidoInexistenteException, ErroAcessoRepositorioException {
		if (dsPedidos == null) {
			Vector<Pedido> listaPedidos = new Vector<Pedido>();
			listaPedidos.add(pedido);
			dsPedidos = new PedidoDataSource(listaPedidos);
		}
		return dsPedidos;
	}

	public JasperViewer getViewer() throws JRException, PedidoInexistenteException, ErroAcessoRepositorioException {
		if (viewer == null) {
			viewer = new JasperViewer(getImpressao(), false);
			viewer.setVisible(true);
		}
		return viewer;
	}

	public JasperPrint getImpressao() throws JRException, PedidoInexistenteException, ErroAcessoRepositorioException {
		if (impressao == null) {
			impressao = JasperFillManager.fillReport(getReport(), getParams(), getDsPedidos());
		}		
		return impressao;
	}

	@SuppressWarnings("unchecked")
	public HashMap getParams() {
		if (params == null) {
			params = new HashMap();
			params.put("logo", this.logo);
			params.put("logoRecicle", logoRecicle);
			params.put("pathSubRel", subRelatorio);
		}
		return params;
	}
}

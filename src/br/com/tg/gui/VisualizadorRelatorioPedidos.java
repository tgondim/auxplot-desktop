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
import br.com.tg.gui.util.RelatorioPedidosDataSource;

public class VisualizadorRelatorioPedidos {
	
	private JasperReport report;
	
	private RelatorioPedidosDataSource dsPedidos;
	
	private Vector<Pedido> listaPedidos;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String relatorio;
	
	@SuppressWarnings("unchecked")
	private HashMap params;
	
	@SuppressWarnings("unchecked")
	public VisualizadorRelatorioPedidos(String newLogo, String newRelatorio, Vector<Pedido> newListaPedidos, HashMap newParams) 
				throws JRException {
		this.logo = newLogo;
		this.relatorio = newRelatorio;
		this.listaPedidos = newListaPedidos;		
		getParams().put("ordenacao", newParams.get("ordenacao"));
		getParams().put("filtros", newParams.get("filtros"));
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public RelatorioPedidosDataSource getDsPessoas() {
		if (dsPedidos == null) {
			dsPedidos = new RelatorioPedidosDataSource(listaPedidos);
		}
		return dsPedidos;
	}

	public JasperViewer getViewer() throws JRException {
		if (viewer == null) {
			viewer = new JasperViewer(getImpressao(), false);
			viewer.setVisible(true);
		}
		return viewer;
	}

	public JasperPrint getImpressao() throws JRException {
		if (impressao == null) {
			impressao = JasperFillManager.fillReport(getReport(), getParams(), getDsPessoas());
		}		
		return impressao;
	}

	@SuppressWarnings("unchecked")
	public HashMap getParams() {
		if (params == null) {
			float auxTotalValorPedidos = 0;
			for (Pedido p : listaPedidos) {
				auxTotalValorPedidos += p.getValorTotal();
			}
			params = new HashMap();
			params.put("logo", this.logo);
			params.put("totalPedidos", listaPedidos.size());
			params.put("totalValorPedidos", auxTotalValorPedidos);
			params.put("ordenacao", "");
			params.put("filtros", "");
		}
		return params;
	}
}

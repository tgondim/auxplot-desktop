package br.com.tg.gui;

import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.tg.entidades.Fatura;
import br.com.tg.gui.util.RelatorioFaturasDataSource;

public class VisualizadorRelatorioFaturas {
	
	private JasperReport report;
	
	private RelatorioFaturasDataSource dsFaturas;
	
	private Vector<Fatura> listaFaturas;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String relatorio;
	
	@SuppressWarnings("unchecked")
	private HashMap params;
	
	@SuppressWarnings("unchecked")
	public VisualizadorRelatorioFaturas(String newLogo, String newRelatorio, Vector<Fatura> newListaFaturas, HashMap newParams) 
				throws JRException {
		this.logo = newLogo;
		this.relatorio = newRelatorio;
		this.listaFaturas = newListaFaturas;		
		getParams().put("ordenacao", newParams.get("ordenacao"));
		getParams().put("filtros", newParams.get("filtros"));
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public RelatorioFaturasDataSource getDsPessoas() {
		if (dsFaturas == null) {
			dsFaturas = new RelatorioFaturasDataSource(listaFaturas);
		}
		return dsFaturas;
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
			float aux = 0;
			for (Fatura f : listaFaturas) {
				aux += f.getValorTotal();
			}

			params = new HashMap();
			params.put("logo", this.logo);
			params.put("totalFaturas", listaFaturas.size());
			params.put("totalValorFaturas", aux);
			params.put("ordenacao", "");
			params.put("filtros", "");
		}
		return params;
	}
}

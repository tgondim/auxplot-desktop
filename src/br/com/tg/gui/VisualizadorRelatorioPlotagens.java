package br.com.tg.gui;

import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.tg.entidades.Plotagem;
import br.com.tg.gui.util.RelatorioPlotagensDataSource;

public class VisualizadorRelatorioPlotagens {
	
	private JasperReport report;
	
	private RelatorioPlotagensDataSource dsPlotagens;
	
	private Vector<Plotagem> listaPlotagens;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String relatorio;
	
	@SuppressWarnings("unchecked")
	private HashMap params;
	
	@SuppressWarnings("unchecked")
	public VisualizadorRelatorioPlotagens(String newLogo, String newRelatorio, Vector<Plotagem> newListaPlotagens, HashMap newParams) 
				throws JRException {
		
		this.logo = newLogo;
		this.relatorio = newRelatorio;
		this.listaPlotagens = newListaPlotagens;
		getParams().put("ordenacao", newParams.get("ordenacao"));
		getParams().put("filtros", newParams.get("filtros"));
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public RelatorioPlotagensDataSource getDsPlotagens() {
		if (dsPlotagens == null) {
			dsPlotagens = new RelatorioPlotagensDataSource(listaPlotagens);
		}
		return dsPlotagens;
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
			impressao = JasperFillManager.fillReport(getReport(), getParams(), getDsPlotagens());
		}		
		return impressao;
	}

	@SuppressWarnings("unchecked")
	public HashMap getParams() {
		if (params == null) {
			params = new HashMap();
			params.put("logo", this.logo);
			params.put("totalPlotagens", listaPlotagens.size());
		}
		return params;
	}
}

package br.com.tg.gui;

import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.tg.entidades.Pessoa;
import br.com.tg.gui.util.RelatorioPessoasDataSource;

public class VisualizadorRelatorioPessoas {
	
	private JasperReport report;
	
	private RelatorioPessoasDataSource dsPessoas;
	
	private Vector<Pessoa> listaPessoas;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String relatorio;
	
	@SuppressWarnings("unchecked")
	private HashMap params;
	
	@SuppressWarnings("unchecked")
	public VisualizadorRelatorioPessoas(String newLogo, String newRelatorio, Vector<Pessoa> newListaPessoas, HashMap newParams) 
				throws JRException {
		this.logo = newLogo;
		this.relatorio = newRelatorio;
		this.listaPessoas = newListaPessoas;		
		getParams().put("ordenacao", newParams.get("ordenacao"));
		getParams().put("filtros", newParams.get("filtros"));
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public RelatorioPessoasDataSource getDsPessoas() {
		if (dsPessoas == null) {
			dsPessoas = new RelatorioPessoasDataSource(listaPessoas);
		}
		return dsPessoas;
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
			params = new HashMap();
			params.put("logo", this.logo);
			params.put("totalPessoas", listaPessoas.size());
			params.put("ordenacao", "");
			params.put("filtros", "");
		}
		return params;
	}
}

package br.com.tg.gui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.PessoaFisica;
import br.com.tg.entidades.PessoaJuridica;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.FaturaInexistenteException;
import br.com.tg.gui.util.FaturaDataSource;
import br.com.tg.util.Conversor;
import br.com.tg.util.Validador;

public class VisualizadorFatura {
	
	private Fatura fatura;
	
	private JasperReport report;
	
	private FaturaDataSource dsFaturas;
	
	private JasperViewer viewer;
	
	private JasperPrint impressao;
	
	private String logo;
	private String relatorio;
	private String subRelatorio;
	
	@SuppressWarnings("rawtypes")
	private HashMap params;
	
	public VisualizadorFatura(Fatura newFatura,
			String newLogo, String newRelatorio, String newSubRelatorio) 
				throws JRException, FaturaInexistenteException, ErroAcessoRepositorioException {
		
		this.fatura = newFatura;
		this.logo = newLogo;
		this.relatorio = newRelatorio;
		this.subRelatorio = newSubRelatorio;
	}
	
	public JasperReport getReport() throws JRException {
		if (report == null) {
			report = (JasperReport)JRLoader.loadObject(this.relatorio);
		}		
		return report;
	}

	public FaturaDataSource getDsFaturas()
			throws FaturaInexistenteException, ErroAcessoRepositorioException {
		if (dsFaturas == null) {
			Vector<Fatura> listaFaturas = new Vector<Fatura>();
			listaFaturas.add(fatura);
			dsFaturas = new FaturaDataSource(listaFaturas);
		}
		return dsFaturas;
	}

	public JasperViewer getViewer() throws JRException, FaturaInexistenteException, ErroAcessoRepositorioException {
		if (viewer == null) {
			viewer = new JasperViewer(getImpressao(), false);
			viewer.setVisible(true);
		}
		return viewer;
	}

	public JasperPrint getImpressao() throws JRException, FaturaInexistenteException, ErroAcessoRepositorioException {
		if (impressao == null) {
			impressao = JasperFillManager.fillReport(getReport(), getParams(), getDsFaturas());
		}		
		return impressao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap getParams() {
		if (params == null) {
			params = new HashMap();
			params.put("logo", this.logo);
			Calendar auxData = fatura.getDataEmissao();
			auxData.set(Calendar.MONTH, fatura.getDataEmissao().get(Calendar.MONTH)-1);
			params.put("mesFatura", Conversor.dateToMonth(auxData).toUpperCase());
			params.put("idCliente", fatura.getPessoaPai().getNome());
			params.put("endereco", fatura.getPessoaPai().getEndereco().toString());
			params.put("cnpj", (fatura.getPessoaPai() instanceof PessoaJuridica) 
					? Validador.maskCnpj(((PessoaJuridica)fatura.getPessoaPai()).getCnpj()) 
					: Validador.maskCpf(((PessoaFisica)fatura.getPessoaPai()).getCpf()));
			params.put("pathSubRel", subRelatorio);
		}
		return params;
	}
}

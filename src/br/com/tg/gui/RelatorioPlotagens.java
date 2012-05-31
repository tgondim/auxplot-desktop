package br.com.tg.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import net.sf.jasperreports.engine.JRException;
import br.com.tg.entidades.Plotagem;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.util.Fachada;

@SuppressWarnings("serial")
public class RelatorioPlotagens extends JDialog {
	
	private auxPlot telaPrincipal;
	
	private JPanel jpRelatorioPlotagens;
	private JPanel jpFiltros;
	private JPanel jpOrdenacao;
	
	private JLabel jlFiltros; 
	private JLabel jlOrdenacao; 
	private JLabel jlUnidade; 
	private JLabel jlOrdemInversa;
	
	private JTextField jtfUnidade;
	
	private JCheckBox jchOrdemInversa;
	
	private JList jlstOrdenacao;
	
	private DefaultListModel listaOrdenacaoModel;
	
	private JButton jbOk;	
	private JButton jbCancelar;	
	private JButton jbAcima;
	private JButton jbAbaixo;
	
	private String navegarAcimaIcon;
	private String navegarAbaixoIcon;
	private String logo1;
	private String plotagensReport;
	
	public RelatorioPlotagens(JFrame owner) {
		super(owner, "Relatório de Plotagens");
		this.telaPrincipal = (auxPlot)owner;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		inicio();		
	}
	
	private void inicio() {
		File file = new File("auxPlot.properties");
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, 
					"Por favor, feche esta tela e abra novamente.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n", 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, 
					"Por favor, feche esta tela e abra novamente.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n", 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		navegarAcimaIcon = prop.getProperty("navegarAcimaIcon");
		navegarAbaixoIcon = prop.getProperty("navegarAbaixoIcon");
		logo1 = prop.getProperty("logo1");
		plotagensReport = prop.getProperty("plotagensReport");
		
		setResizable(false);
		setSize(new Dimension(600, 260));
		setLocation(new Point((getParent().getLocation().x+(getParent().getSize().width-600)/2), 
				(getParent().getLocation().y+(getParent().getSize().height-260)/2)));
		add(getJpRelatorioPlotagens());
		setVisible(true);
	}

	public JPanel getJpRelatorioPlotagens() {
		if (jpRelatorioPlotagens == null) {
			jpRelatorioPlotagens = new JPanel(null);

			jpRelatorioPlotagens.add(getJlFiltros());
			getJlFiltros().setBounds(15, 10, 120, 20);
			jpRelatorioPlotagens.add(getJpFiltros());
			getJpFiltros().setBounds(10, 30, 280, 150);

			jpRelatorioPlotagens.add(getJlOrdenacao());
			getJlOrdenacao().setBounds(310, 10, 120, 20);
			jpRelatorioPlotagens.add(getJpOrdenacao());
			getJpOrdenacao().setBounds(305, 30, 280, 150);
			
			jpRelatorioPlotagens.add(getJbOk());
			getJbOk().setBounds(198, 190, 90, 30);
			jpRelatorioPlotagens.add(getJbCancelar());
			getJbCancelar().setBounds(305, 190, 90, 30);
		}
		return jpRelatorioPlotagens;
	}
	
	public JPanel getJpFiltros() {
		if (jpFiltros == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpFiltros = new JPanel();
			jpFiltros.setBorder(etched);
			
			SpringLayout filtrosLayout = new SpringLayout();
			jpFiltros.setLayout(filtrosLayout);
			
			jpFiltros.setFocusable(false);
			jpFiltros.add(getJlUnidade());
			jpFiltros.add(getJtfUnidade());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlUnidade(), 20, SpringLayout.WEST, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlUnidade(), 20, SpringLayout.NORTH, jpFiltros);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfUnidade(), 10, SpringLayout.EAST, getJlUnidade());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfUnidade(), 15, SpringLayout.NORTH, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.EAST, jpFiltros, 140, SpringLayout.EAST, getJtfUnidade());
			
		}
		return jpFiltros;
	}

	public JPanel getJpOrdenacao() {
		if (jpOrdenacao == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpOrdenacao = new JPanel();
			jpOrdenacao.setBorder(etched);
			
			SpringLayout ordenacaoLayout = new SpringLayout();
			jpOrdenacao.setLayout(ordenacaoLayout);
			
			jpOrdenacao.setFocusable(false);
			
			jpOrdenacao.add(getJlstOrdenacao());
			jpOrdenacao.add(getJbAcima());
			jpOrdenacao.add(getJbAbaixo());
			jpOrdenacao.add(getJchOrdemInversa());
			jpOrdenacao.add(getJlOrdemInversa());
			
			ordenacaoLayout.putConstraint(SpringLayout.WEST, getJlstOrdenacao(), 20, SpringLayout.WEST, jpOrdenacao);
			ordenacaoLayout.putConstraint(SpringLayout.NORTH, getJlstOrdenacao(), 20, SpringLayout.NORTH, jpOrdenacao);
			ordenacaoLayout.putConstraint(SpringLayout.EAST, getJlstOrdenacao(), -20, SpringLayout.EAST, jpOrdenacao);
			
			ordenacaoLayout.putConstraint(SpringLayout.WEST, getJbAcima(), 0, SpringLayout.WEST, getJlstOrdenacao());
			ordenacaoLayout.putConstraint(SpringLayout.NORTH, getJbAcima(), 5, SpringLayout.SOUTH, getJlstOrdenacao());

			ordenacaoLayout.putConstraint(SpringLayout.WEST, getJbAbaixo(), 5, SpringLayout.EAST, getJbAcima());
			ordenacaoLayout.putConstraint(SpringLayout.NORTH, getJbAbaixo(), 5, SpringLayout.SOUTH, getJlstOrdenacao());

			ordenacaoLayout.putConstraint(SpringLayout.WEST, getJchOrdemInversa(), 20, SpringLayout.EAST, getJbAbaixo());
			ordenacaoLayout.putConstraint(SpringLayout.NORTH, getJchOrdemInversa(), 10, SpringLayout.SOUTH, getJlstOrdenacao());
			
			ordenacaoLayout.putConstraint(SpringLayout.WEST, getJlOrdemInversa(), 3, SpringLayout.EAST, getJchOrdemInversa());
			ordenacaoLayout.putConstraint(SpringLayout.NORTH, getJlOrdemInversa(), 13, SpringLayout.SOUTH, getJlstOrdenacao());
		}
		return jpOrdenacao;
	}
	
	public JLabel getJlFiltros() {
		if (jlFiltros == null) {
			jlFiltros = new JLabel("Filtros", JLabel.LEFT);
			jlFiltros.setFont(new Font("Arial", Font.BOLD, 20));
		}
		return jlFiltros;
	}
	
	public JLabel getJlOrdenacao() {
		if (jlOrdenacao == null) {
			jlOrdenacao = new JLabel("Ordenação", JLabel.LEFT);
			jlOrdenacao.setFont(new Font("Arial", Font.BOLD, 20));
		}
		return jlOrdenacao;
	}
	
	public JLabel getJlUnidade() {
		if (jlUnidade == null) {
			jlUnidade = new JLabel("Unidade", JLabel.LEFT);
		}
		return jlUnidade;
	}
	
	public JLabel getJlOrdemInversa() {
		if (jlOrdemInversa == null) {
			jlOrdemInversa = new JLabel("Ordem Inversa", JLabel.LEFT);
		}
		return jlOrdemInversa;
	}
	
	public JCheckBox getJchOrdemInversa() {
		if (jchOrdemInversa == null) {
			jchOrdemInversa = new JCheckBox();
		}
		return jchOrdemInversa;
	}

	public JTextField getJtfUnidade() {
		if (jtfUnidade == null) {
			jtfUnidade = new JTextField();
			jtfUnidade.setDocument(new FixedLengthDocument(10, false, false));
		}
		return jtfUnidade;
	}
	
	public JList getJlstOrdenacao() {
		if (jlstOrdenacao == null) {
			jlstOrdenacao = new JList(getListaOrdenacaoModel());
			jlstOrdenacao.setBorder(BorderFactory.createEtchedBorder());
			jlstOrdenacao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jlstOrdenacao.setAutoscrolls(true);
		}
		return jlstOrdenacao;
	}
	
	public DefaultListModel getListaOrdenacaoModel() {
		if (listaOrdenacaoModel == null) {
			listaOrdenacaoModel = new DefaultListModel();
			listaOrdenacaoModel.addElement("Id");
			listaOrdenacaoModel.addElement("Descrição");
			listaOrdenacaoModel.addElement("Unidade");
			listaOrdenacaoModel.addElement("Valor");
		}
		return listaOrdenacaoModel;
	}
	
	public JButton getJbAcima() {
		if (jbAcima == null) {
			jbAcima = new JButton();
			try {
				jbAcima.setIcon(ImagemUtil.imagemEscalonada(
						navegarAcimaIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbAcima.setToolTipText("mover para cima");
			jbAcima.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int indexSelecionado = getJlstOrdenacao().getSelectedIndex();
					if ((indexSelecionado != -1) 
							&& (indexSelecionado > 0)) {
						String itemSelecionado = (String)getListaOrdenacaoModel().getElementAt(indexSelecionado);
						getListaOrdenacaoModel().remove(indexSelecionado);
						getListaOrdenacaoModel().add(indexSelecionado-1, itemSelecionado);
						getJlstOrdenacao().setSelectedIndex(indexSelecionado-1);
					}					
				}
			});
		}
		return jbAcima;
	}	
	
	public JButton getJbAbaixo() {
		if (jbAbaixo == null) {
			jbAbaixo = new JButton();
			try {
				jbAbaixo.setIcon(ImagemUtil.imagemEscalonada(
						navegarAbaixoIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbAbaixo.setToolTipText("mover para baixo");
			jbAbaixo.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int indexSelecionado = getJlstOrdenacao().getSelectedIndex();
					if ((indexSelecionado != -1) 
							&& (indexSelecionado < (getListaOrdenacaoModel().getSize()-1))) {
						String itemSelecionado = (String)getListaOrdenacaoModel().getElementAt(indexSelecionado);
						getListaOrdenacaoModel().remove(indexSelecionado);
						getListaOrdenacaoModel().add(indexSelecionado+1, itemSelecionado);
						getJlstOrdenacao().setSelectedIndex(indexSelecionado+1);
					}
				}
			});
		}
		return jbAbaixo;
	}	
	
	public JButton getJbOk() {
		if (jbOk == null) {
			jbOk = new JButton("Ok");
			jbOk.addActionListener(new ActionListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent e) {
					VisualizadorRelatorioPlotagens plotagensReport;
					try {
						List<Plotagem> listaPlotagens = Fachada.obterInstancia().listarPlotagens(
								getRestricoes(), 
								getOrdemListaPlotagens(), 
								isCrescenteListaPlotagens());
						
						HashMap params = new HashMap();
						params.put("ordenacao", getOrdenacao());
						params.put("filtros", getFiltros());
						
						plotagensReport = new VisualizadorRelatorioPlotagens(RelatorioPlotagens.this.logo1, 
								RelatorioPlotagens.this.plotagensReport, new Vector<Plotagem>(listaPlotagens), params);
						plotagensReport.getViewer().setVisible(true);				
					} catch (JRException e1) {
						JOptionPane.showMessageDialog(RelatorioPlotagens.this, 
								"Ocorreu um erro ao gerar o relatório.\n" 
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
								+ e1.getMessage() + "\n" + e1.getStackTrace(), 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(RelatorioPlotagens.this, 
								"Ocorreu um erro inesperado.\n" 
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
								+ e2.getMessage() + "\n" + e2.getStackTrace(), 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);
						e2.printStackTrace();
					}
				}
			});
		}
		return jbOk;
	}
	
	public JButton getJbCancelar() {
		if (jbCancelar == null) {
			jbCancelar = new JButton("Cancelar");
			jbCancelar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					getTelaPrincipal().killRelatoriosPlotagens();
				}
			});
		}
		return jbCancelar;
	}

	private String getFiltros() {
		String s = "";
		if (!getJtfUnidade().getText().isEmpty()) {
			s += "Unidade contendo " + getJtfUnidade().getText();
		}
		if (s.equals("")) {
			s += "Não há.";
		}
		return s;
	}
	
	private String getOrdenacao() {
		Object[] aux = getListaOrdenacaoModel().toArray();
		String s = "";
		for (int i = 0; i < aux.length; i++) {
			if (i == 0) {
				s += aux[i];
			} else {
				s += ", " + aux[i];
			}
		}
		return s;
	}
	
	public Object[] getOrdemListaPlotagens() {
		Object[] aux = getListaOrdenacaoModel().toArray();
		for (int i = 0; i < aux.length; i++) {
			aux[i] = validaOrdem((String)aux[i]);
		}
		return aux; 
	}

	private String validaOrdem(String ordem) {
		if (ordem.equals("Id")) {
			return "id";
		}
		if (ordem.equals("Descrição")) {
			return "descricao";
		}
		if (ordem.equals("Unidade")) {
			return "unidade";
		}
		if (ordem.equals("Valor")) {
			return "valor";
		}
		return "";
	}
	public boolean isCrescenteListaPlotagens() {
		return !getJchOrdemInversa().isSelected();
	}
	
	private ArrayList<Object[]> getRestricoes() {
		ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
		if (!getJtfUnidade().getText().isEmpty()) {
			Object[] restr = { "unidade", "%" + getJtfUnidade().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		return restricoes;
	}
	
	private auxPlot getTelaPrincipal() {
		return telaPrincipal;
	}	
}

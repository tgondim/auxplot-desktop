package br.com.tg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;

import net.sf.jasperreports.engine.JRException;
import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.PessoaJuridica;
import br.com.tg.entidades.StatusFatura;
import br.com.tg.entidades.StatusPessoa;
import br.com.tg.entidades.TipoPessoa;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.util.Conversor;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class RelatorioFaturas extends JDialog {
	
	private auxPlot telaPrincipal;
	
	private JPanel jpRelatorioFaturas;
	private JPanel jpFiltros;
	private JPanel jpOrdenacao;
	
	private JLabel jlFiltros; 
	private JLabel jlOrdenacao; 
	private JLabel jlCliente; 
	private JLabel jlStatusFatura; 
	private JLabel jlEmissaoDe; 
	private JLabel jlEmissaoAte; 
	private JLabel jlOrdemInversa;
	
	private JComboBox jcbCliente;
	
	private JFormattedTextField jtfFiltroDataDe;
	private JFormattedTextField jtfFiltroDataAte;

	private JComboBox jcbStatusFatura;
	
	private JCheckBox jchOrdemInversa;
	
	private JList jlstOrdenacao;
	
	private DefaultListModel listaOrdenacaoModel;
	
	private JButton jbOk;	
	private JButton jbCancelar;	
	private JButton jbAcima;
	private JButton jbAbaixo;
	
	private JRadioButton jrbDataEmissao;
	private JRadioButton jrbDataVencimento;
	
	private ButtonGroup bgFiltroData;
	
	private String navegarAcimaIcon;
	private String navegarAbaixoIcon;
	private String logo1;
	private String faturasReport;
	
	private BarraStatus barraStatus;
	
	public RelatorioFaturas(JFrame owner) {
		super(owner, "Relatório de Faturas");
		this.telaPrincipal = (auxPlot)owner;
		this.barraStatus = getTelaPrincipal().getBarraStatus();
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
		faturasReport = prop.getProperty("faturasReport");
		
		setResizable(false);
		setSize(new Dimension(700, 290));
		setLocation(new Point((getParent().getLocation().x+(getParent().getSize().width-700)/2), 
				(getParent().getLocation().y+(getParent().getSize().height-290)/2)));
		add(getJpRelatorioFaturas());
		setVisible(true);
	}

	public JPanel getJpRelatorioFaturas() {
		if (jpRelatorioFaturas == null) {
			jpRelatorioFaturas = new JPanel(null);

			jpRelatorioFaturas.add(getJlFiltros());
			getJlFiltros().setBounds(15, 10, 120, 20);
			jpRelatorioFaturas.add(getJpFiltros());
			getJpFiltros().setBounds(10, 30, 380, 180);

			jpRelatorioFaturas.add(getJlOrdenacao());
			getJlOrdenacao().setBounds(410, 10, 120, 20);
			jpRelatorioFaturas.add(getJpOrdenacao());
			getJpOrdenacao().setBounds(405, 30, 280, 180);
			
			jpRelatorioFaturas.add(getJbOk());
			getJbOk().setBounds(248, 230, 90, 30);
			jpRelatorioFaturas.add(getJbCancelar());
			getJbCancelar().setBounds(355, 230, 90, 30);
		}
		return jpRelatorioFaturas;
	}
	
	public JPanel getJpFiltros() {
		if (jpFiltros == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpFiltros = new JPanel();
			jpFiltros.setBorder(etched);
			
			SpringLayout filtrosLayout = new SpringLayout();
			jpFiltros.setLayout(filtrosLayout);
			
			jpFiltros.setFocusable(false);
			jpFiltros.add(getJlCliente());
			jpFiltros.add(getJcbCliente());
			jpFiltros.add(getJlStatusFatura());
			jpFiltros.add(getJcbStatusFatura());
			jpFiltros.add(getJrbDataEmissao());
			jpFiltros.add(getJrbDataVencimento());
			getBgFiltroData();
			jpFiltros.add(getJcbStatusFatura());
			jpFiltros.add(getJlEmissaoDe());
			jpFiltros.add(getJtfFiltroDataDe());
			jpFiltros.add(getJlEmissaoAte());
			jpFiltros.add(getJtfFiltroDataAte());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlCliente(), 15, SpringLayout.WEST, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlCliente(), 20, SpringLayout.NORTH, jpFiltros);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJcbCliente(), 10, SpringLayout.EAST, getJlCliente());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJcbCliente(), 15, SpringLayout.NORTH, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.EAST, getJcbCliente(), -15, SpringLayout.EAST, jpFiltros);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJlStatusFatura(), 15, SpringLayout.WEST, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlStatusFatura(), 25, SpringLayout.SOUTH, getJlCliente());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJcbStatusFatura(), 10, SpringLayout.EAST, getJlStatusFatura());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJcbStatusFatura(), 15, SpringLayout.SOUTH, getJcbCliente());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJrbDataEmissao(), 15, SpringLayout.WEST, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJrbDataEmissao(), 25, SpringLayout.SOUTH, getJlStatusFatura());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJrbDataVencimento(), 15, SpringLayout.EAST, getJrbDataEmissao());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJrbDataVencimento(), 25, SpringLayout.SOUTH, getJlStatusFatura());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJlEmissaoDe(), 15, SpringLayout.WEST, jpFiltros);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlEmissaoDe(), 20, SpringLayout.SOUTH, getJrbDataEmissao());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfFiltroDataDe(), 10, SpringLayout.EAST, getJlEmissaoDe());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfFiltroDataDe(), 15, SpringLayout.SOUTH, getJrbDataEmissao());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfFiltroDataDe(), -260, SpringLayout.EAST, jpFiltros);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlEmissaoAte(), 10, SpringLayout.EAST, getJtfFiltroDataDe());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlEmissaoAte(), 20, SpringLayout.SOUTH, getJrbDataVencimento());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfFiltroDataAte(), 10, SpringLayout.EAST, getJlEmissaoAte());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfFiltroDataAte(), 15, SpringLayout.SOUTH, getJrbDataVencimento());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfFiltroDataAte(), -145, SpringLayout.EAST, jpFiltros);
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
	
	public JLabel getJlCliente() {
		if (jlCliente == null) {
			jlCliente = new JLabel("Cliente", JLabel.LEFT);
		}
		return jlCliente;
	}
	
	public JLabel getJlEmissaoDe() {
		if (jlEmissaoDe == null) {
			jlEmissaoDe = new JLabel("De", JLabel.LEFT);
		}
		return jlEmissaoDe;
	}
	
	public JLabel getJlEmissaoAte() {
		if (jlEmissaoAte == null) {
			jlEmissaoAte = new JLabel("Até", JLabel.LEFT);
		}
		return jlEmissaoAte;
	}
	
	public JLabel getJlStatusFatura() {
		if (jlStatusFatura == null) {
			jlStatusFatura = new JLabel("Status", JLabel.LEFT);
		}
		return jlStatusFatura;
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

	public JComboBox getJcbCliente() {
		if (jcbCliente == null) {
			try {
				Vector<Pessoa> listaPessoas = new Vector<Pessoa>();
				PessoaJuridica todasPessoas = new PessoaJuridica();
				todasPessoas.setId(0);
				todasPessoas.setNome("Todos");
				listaPessoas.add(todasPessoas);
				
				ArrayList<Object[]> restr = new ArrayList<Object[]>();
				Object[] restr1 = { "tipoPessoa", new TipoPessoa(1), "eq", "or" };
				Object[] restr2 = { "tipoPessoa", new TipoPessoa(2), "eq", "or" };
				Object[] restr3 = { "statusPessoa", new StatusPessoa(1), "eq" };
				restr.add(restr1);
				restr.add(restr2);
				restr.add(restr3);
				
				listaPessoas.addAll(Fachada.obterInstancia().listarPessoas(restr, new Object[]{"nome"}, true));
				jcbCliente = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaPessoas);
				jcbCliente.setModel(defaultComboBox);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				dispose();
			}
		}
		return jcbCliente;
	}
	
	public JFormattedTextField getJtfFiltroDataDe() {
		if (jtfFiltroDataDe == null) {
			jtfFiltroDataDe = new JFormattedTextField();
			try {
				MaskFormatter maskDataFim = new MaskFormatter("##/##/####");
				jtfFiltroDataDe = new JFormattedTextField(maskDataFim);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de fim");
			}
			jtfFiltroDataDe.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfFiltroDataDe.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfFiltroDataDe.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfFiltroDataDe, Color.red, true);
							jtfFiltroDataDe.grabFocus();
						}
					} else {
						jtfFiltroDataDe.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfFiltroDataDe;
	}

	public JFormattedTextField getJtfFiltroDataAte() {
		if (jtfFiltroDataAte == null) {
			jtfFiltroDataAte = new JFormattedTextField();
			try {
				MaskFormatter maskDataInicio = new MaskFormatter("##/##/####");
				jtfFiltroDataAte = new JFormattedTextField(maskDataInicio);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de início.");
			}
			jtfFiltroDataAte.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfFiltroDataAte.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfFiltroDataAte.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfFiltroDataAte, Color.red, true);
							jtfFiltroDataAte.grabFocus();
						}
					} else {
						jtfFiltroDataAte.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfFiltroDataAte;
	}

	public JComboBox getJcbStatusFatura() {
		if (jcbStatusFatura == null) {
			try {
				Vector<StatusFatura> listaStatusFatura = new Vector<StatusFatura>();
				StatusFatura todosStatusFatura = new StatusFatura();
				todosStatusFatura.setId(0);
				todosStatusFatura.setDescricao("Todos");
				listaStatusFatura.add(todosStatusFatura);
				listaStatusFatura.addAll(Fachada.obterInstancia().listarStatusFaturas());
				jcbStatusFatura = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaStatusFatura);
				jcbStatusFatura.setModel(defaultComboBox);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				dispose();
			}
		}
		return jcbStatusFatura;
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
			listaOrdenacaoModel.addElement("Cliente");
			listaOrdenacaoModel.addElement("Status");
			listaOrdenacaoModel.addElement("Emissão");
			listaOrdenacaoModel.addElement("Vencimento");
			listaOrdenacaoModel.addElement("Total");
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
					VisualizadorRelatorioFaturas pedidosReport;
					try {
						List<Fatura> listaFaturas = Fachada.obterInstancia().listarFaturas(
								getRestricoes(), 
								getOrdemListaPessoas(), 
								isCrescenteListaPessoas());

						HashMap params = new HashMap();
						params.put("ordenacao", getOrdenacao());
						params.put("filtros", getFiltros());
						
						pedidosReport = new VisualizadorRelatorioFaturas(RelatorioFaturas.this.logo1, 
								RelatorioFaturas.this.faturasReport, new Vector<Fatura>(listaFaturas), params);
						pedidosReport.getViewer().setVisible(true);				
					} catch (JRException e1) {
						JOptionPane.showMessageDialog(RelatorioFaturas.this, 
								"Ocorreu um erro ao gerar o relatório.\n" 
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
								+ e1.getMessage() + "\n" + e1.getStackTrace(), 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(RelatorioFaturas.this, 
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
					getTelaPrincipal().killRelatoriosFaturas();
				}
			});
		}
		return jbCancelar;
	}

	private String getFiltros() {
		String s = "";
		boolean barra = false;
		boolean filtroData = true;
		if (getJcbCliente().getSelectedIndex() != 0) {
			s += "Cliente " + getJcbCliente().getSelectedItem() + " ";
			barra = true;
		}
		if (getJcbStatusFatura().getSelectedIndex() != 0) {
			if (barra) {
				s += " / ";
			}
			s+= "Status " + getJcbStatusFatura().getSelectedItem();
			barra = true;
		}
		if (!getJtfFiltroDataDe().getText().equals("  /  /    ")) {
			if (barra) {
				s += " / ";
			}
			if (filtroData) {
				if (getJrbDataEmissao().isSelected()) {
					s += " Emissão ";
				} else if (getJrbDataVencimento().isSelected()) {
					s += " Vencimento ";
				}
				filtroData = false;
			}
			s += "De " + getJtfFiltroDataDe().getText() + " ";
			barra = false;
		}
		if (!getJtfFiltroDataAte().getText().equals("  /  /    ")) {
			if (barra) {
				s += " / ";
			}
			if (filtroData) {
				if (getJrbDataEmissao().isSelected()) {
					s += " Emissão ";
				} else if (getJrbDataVencimento().isSelected()) {
					s += " Vencimento ";
				}
				filtroData = false;
			}
			s += "Até " + getJtfFiltroDataAte().getText();
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

	public Object[] getOrdemListaPessoas() {
		Object[] aux = getListaOrdenacaoModel().toArray();
		for (int i = 0; i < aux.length; i++) {
			aux[i] = validaOrdem((String)aux[i]);
		}
		return aux; 
	}

	public BarraStatus getBarraStatus() {
		return barraStatus;
	}
	
	private String validaOrdem(String ordem) {
		if (ordem.equals("Id")) {
			return "id";
		}
		if (ordem.equals("Cliente")) {
			return "pessoaPai";
		}
		if (ordem.equals("Status")) {
			return "statusFatura";
		}
		if (ordem.equals("Emissão")) {
			return "dataEmissao";
		}
		if (ordem.equals("Vencimento")) {
			return "dataVencimento";
		}
		if (ordem.equals("Total")) {
			return "valorTotal";
		}
		return "";
	}
	public boolean isCrescenteListaPessoas() {
		return !getJchOrdemInversa().isSelected();
	}
	
	private ArrayList<Object[]> getRestricoes() {
		ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
		if (getJcbCliente().getSelectedIndex() != 0) {
			Object[] restr = { "pessoaPai", getJcbCliente().getSelectedItem(), "eq" };
			restricoes.add(restr);
		} 
		if (getJcbStatusFatura().getSelectedIndex() == 1) {
			StatusFatura sp = new StatusFatura();
			sp.setId(1);
			Object[] restr = { "statusFatura", sp, "eq" };
			restricoes.add(restr);
		} else if (getJcbStatusFatura().getSelectedIndex() == 2) {
			StatusFatura sp = new StatusFatura();
			sp.setId(2);
			Object[] restr = { "statusFatura", sp, "eq" };
			restricoes.add(restr);
		} else if (getJcbStatusFatura().getSelectedIndex() == 3) {
			StatusFatura sp = new StatusFatura();
			sp.setId(3);
			Object[] restr = { "statusFatura", sp, "eq" };
			restricoes.add(restr);
		}
		if (getJrbDataEmissao().isSelected() || getJrbDataVencimento().isSelected()) {
			try {
				if (!getJtfFiltroDataDe().getText().equals("  /  /    ") && !getJtfFiltroDataAte().getText().equals("  /  /    ")) {
					Object[] restr = { getJrbDataEmissao().isSelected() ? "dataEmissao" : "dataVencimento", "between", 
							Conversor.stringToCalendar(getJtfFiltroDataDe().getText()),
							Conversor.stringToCalendar(getJtfFiltroDataAte().getText()) };
					restricoes.add(restr);
				} else {
					if (!getJtfFiltroDataDe().getText().equals("  /  /    ")) {
						Object[] restr = { getJrbDataEmissao().isSelected() ? "dataEmissao" : "dataVencimento", Conversor.stringToCalendar(getJtfFiltroDataDe().getText()), "ge" };
						restricoes.add(restr);
					}			
					if (!getJtfFiltroDataAte().getText().equals("  /  /    ")) {
						Object[] restr = { getJrbDataEmissao().isSelected() ? "dataEmissao" : "dataVencimento", Conversor.stringToCalendar(getJtfFiltroDataAte().getText()), "le" };
						restricoes.add(restr);
					}			
				}		
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de emissão");
			}
		}
		return restricoes;
	}
	
	public JRadioButton getJrbDataEmissao() {
		if (jrbDataEmissao == null) {
			jrbDataEmissao = new JRadioButton("Emissão");
			jrbDataEmissao.setMnemonic(KeyEvent.VK_E);
		}
		return jrbDataEmissao;
	}

	public JRadioButton getJrbDataVencimento() {
		if (jrbDataVencimento == null) {
			jrbDataVencimento = new JRadioButton("Vencimento");
			jrbDataVencimento.setMnemonic(KeyEvent.VK_V);
		}
		return jrbDataVencimento;
	}
	
	public ButtonGroup getBgFiltroData() {
		if (bgFiltroData == null) {
			bgFiltroData = new ButtonGroup();
			bgFiltroData.add(getJrbDataEmissao());
			bgFiltroData.add(getJrbDataVencimento());
		}
		return bgFiltroData;
	}
	
	private auxPlot getTelaPrincipal() {
		return telaPrincipal;
	}
}

package br.com.tg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;

import net.sf.jasperreports.engine.JRException;
import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.Pedido;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.PessoaJuridica;
import br.com.tg.entidades.StatusPessoa;
import br.com.tg.entidades.TipoPessoa;
import br.com.tg.exceptions.FaturaInexistenteException;
import br.com.tg.exceptions.PedidoInexistenteException;
import br.com.tg.exceptions.StatusFaturaInexistenteException;
import br.com.tg.exceptions.StatusPedidoInexistenteException;
import br.com.tg.gui.util.BarraCadastro;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.CalendarFormatter;
import br.com.tg.gui.util.FaturarPedidosTableModel;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.gui.util.MultiEditor;
import br.com.tg.gui.util.MultiRenderer;
import br.com.tg.gui.util.Tela;
import br.com.tg.util.Conversor;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class TelaFaturarPedidos extends Tela {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private JTabbedPane jtpPai;
	
	private BarraCadastro barraCadastro;

	private BarraStatus barraStatus;

	private JPanel jpCabecalho;
	
	private JLabel jlNomePessoa;
	private JLabel jlDataInicio;
	private JLabel jlDataFim;
	private JLabel jlDataVencimento;
	private JLabel jlTotalFatura;
	private JLabel jlNotaFiscal;
	private JLabel jlProjeto;
	
	private JButton jbFaturar;
	private JButton jbMarcar;
	private JButton jbDesmarcar;
	
	private JFormattedTextField jtfDataInicio;
	private JFormattedTextField jtfDataFim;
	private JFormattedTextField jtfDataVencimento;
	private JFormattedTextField jtfValorTotal;
	private JFormattedTextField jtfValorTotalTabela;
	private JFormattedTextField jtfDataEmissaoTabela;

	private JTextField jtfNotaFiscal;
	private JTextField jtfProjeto;
	
	private JComboBox jcbPessoa;
	
	public static final String[] COLUNAS_FATURAR_PEDIDOS = { "", "Id", "Nome Projeto", "Solicitante", "Total R$", "Data Emissão" };
	protected JTable tabelaFaturarPedidos;
	protected JScrollPane jspTabelaFaturarPedidos;
	protected FaturarPedidosTableModel faturarPedidosTableModel;
	
	private Object[] ordemTabelaPedidos;
	
	private String marcarIcon;
	private String desmarcarIcon;
	private String logo1;
	private String faturaPedidosReport;
	private String faturaPedidosSubReport;
 
	private Pedido pedidoSelecionado;
	
	private boolean crescenteTabelaPedidos = true;
	private boolean editMode;
	
	public TelaFaturarPedidos() {
		super();
	}

	public TelaFaturarPedidos(String titulo, auxPlot newTelaPrincipal, JTabbedPane newPai, BarraStatus newBarraStatus) {
		super(titulo, newTelaPrincipal);
		this.jtpPai = newPai;
		this.barraStatus = newBarraStatus;
		inicio();
	}

	public void inicio() {

		File file = new File("auxPlot.properties");
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, 
					"O Arquivo auxPlot.properties não foi encontrado.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n" + e.getStackTrace(), 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			getTelaPrincipal().sairSistema(false);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, 
					"Ocorreu um erro inesperado.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n" + e.getStackTrace(), 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			getTelaPrincipal().sairSistema(false);
		}
		
		marcarIcon = prop.getProperty("marcarIcon");
		desmarcarIcon = prop.getProperty("desmarcarIcon");
		logo1 = prop.getProperty("logo1");
		faturaPedidosReport = prop.getProperty("faturaPedidosReport");
		faturaPedidosSubReport = prop.getProperty("faturaPedidosSubReport");
		
		setLayout(null);
		add(getBarraCadastro());
		add(getJpCabecalho());

		add(getJlDataVencimento());
		getJlDataVencimento().setBounds(10, 110, 70, 20);
		add(getJtfDataVencimento());
		getJtfDataVencimento().setBounds(90, 110, 70, 20);
		
		add(getJlNotaFiscal());
		getJlNotaFiscal().setBounds(180, 110, 70, 20);
		add(getJtfNotaFiscal());
		getJtfNotaFiscal().setBounds(260, 110, 110, 20);
		
		add(getJlProjeto());
		getJlProjeto().setBounds(370, 110, 70, 20);
		add(getJtfProjeto());
		
		add(getJbMarcar());
		add(getJbDesmarcar());
		add(getJbFaturar());
		
		
		add(getJspTabelaPedidos());
		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				doResize();
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
		atualizaTabelaPedidos();
		if (getTabelaFaturarPedidos().getRowCount() > 0 ) {
			setPedidoSelecionado(getFaturarPedidosTableModel().getList().get(0));
		}
		setEditMode(false);
		getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
		if (getJtpPai().getSelectedIndex() > 0) {
			getJtpPai().setSelectedIndex(getJtpPai().getSelectedIndex()+1);
		}
	}

	public void atualizaTabelaPedidos() {
		getFaturarPedidosTableModel().clearTable();
		if (getJcbPessoa().getSelectedIndex() >= 0) {
			List<Pedido> listaPedidos;
			try {
				listaPedidos = Fachada.obterInstancia().listarPedidos(getRestricoes(), getOrdemTabelaPedidos(), isCrescenteTabelaPedidos());
				if (listaPedidos != null) {
					for (Pedido pedido : listaPedidos) {
						getFaturarPedidosTableModel().addRow(pedido);
					}
					getTabelaFaturarPedidos().repaint();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}
		}
	}
	
	private ArrayList<Object[]> getRestricoes() {
		ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
		if (getJcbPessoa().getSelectedIndex() != -1) {
			Object[] restr = { "pessoaPai", getJcbPessoa().getSelectedItem(), "eq" };
			restricoes.add(restr);
		}
		try {
			Object[] restrStatus = { "statusPedido", 
					Fachada.obterInstancia().procurarStatusPedido(1), "eq" };
			restricoes.add(restrStatus);
			if (!getJtfDataInicio().getText().equals("  /  /    ") && !getJtfDataFim().getText().equals("  /  /    ")) {
				Object[] restr = { "dataEmissao", "between", 
						Conversor.stringToCalendar(getJtfDataInicio().getText()),
						Conversor.stringToCalendar(getJtfDataFim().getText()) };
				restricoes.add(restr);
			} else {
				if (!getJtfDataInicio().getText().equals("  /  /    ")) {
					Object[] restr = { "dataEmissao", Conversor.stringToCalendar(getJtfDataInicio().getText()), "ge" };
					restricoes.add(restr);
				}			
				if (!getJtfDataFim().getText().equals("  /  /    ")) {
					Object[] restr = { "dataEmissao", Conversor.stringToCalendar(getJtfDataFim().getText()), "le" };
					restricoes.add(restr);
				}			
			}
		} catch (StatusPedidoInexistenteException e1) {
			JOptionPane.showMessageDialog(
					TelaFaturarPedidos.this,
					"Status de pedido inexistente.\n"
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
					" Atenção", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Ocorreu um erro ao executar o parse da data de emissão");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"Ocorreu um erro inesperado.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n" + e.getStackTrace(), 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			getTelaPrincipal().sairSistema(false);
		}
		return restricoes;
	}
	
	private void doResize() {
		getBarraCadastro().setBounds(00, 00, 500, 35);
		getJpCabecalho().setBounds(10, 35, getSize().width - 20, 70);
		getJcbPessoa().setBounds(130, 10, getSize().width - 245, 20);
		getJlTotalFatura().setBounds(getSize().width - 300, 40, 60, 20);
		getJtfValorTotal().setBounds(getSize().width - 212, 40, 98, 20);
		getJtfProjeto().setBounds(450, 110, getSize().width - 460, 20);
		getJspTabelaPedidos().setBounds(00, 180, getSize().width, getSize().height-175);
	}

	public JPanel getJpCabecalho() {
		if (jpCabecalho == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpCabecalho = new JPanel();
			jpCabecalho.setBorder(etched);
			jpCabecalho.setLayout(null);
			jpCabecalho.setFocusable(false);
			
			jpCabecalho.add(getJlNomePessoa());
			getJlNomePessoa().setBounds(55, 10, 60, 20);
			jpCabecalho.add(getJcbPessoa());
			
			jpCabecalho.add(getJlDataInicio());
			getJlDataInicio().setBounds(85, 40, 30, 20);
			jpCabecalho.add(getJtfDataInicio());
			getJtfDataInicio().setBounds(130, 40, 70, 20);
			
			jpCabecalho.add(getJlDataFim());
			getJlDataFim().setBounds(225, 40, 30, 20);
			jpCabecalho.add(getJtfDataFim());
			getJtfDataFim().setBounds(270, 40, 70, 20);
			
			jpCabecalho.add(getJlTotalFatura());
			jpCabecalho.add(getJtfValorTotal());
			
		}
		return jpCabecalho;
	}

	public BarraCadastro getBarraCadastro() {
		if (barraCadastro == null) {
			try {
				barraCadastro = new BarraCadastro(15, 15);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(this, 
						"Por favor, feche esta tela e abra novamente.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e1.getMessage() + "\n", 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, 
						"Por favor, feche esta tela e abra novamente.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e1.getMessage() + "\n", 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			barraCadastro.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String acao = e.getActionCommand();
					if (acao.equals("sair")) {
						getJtpPai().getSelectedComponent().setVisible(false);
						getJtpPai().removeTabAt(getJtpPai().getSelectedIndex());
						getTelaPrincipal().killTelaFaturarPedidos();
					} else if (acao.equals("confirmar")) {
						atualizaTabelaPedidos();
						if (getTabelaFaturarPedidos().getRowCount() > 0 ) {
							setPedidoSelecionado(getFaturarPedidosTableModel().getList().get(0));
						} else {
							setPedidoSelecionado(null);
						}
						if (getJcbPessoa().getSelectedIndex() >= 0) {
							Calendar dataVencimento = Calendar.getInstance();
							if (getJcbPessoa().getSelectedItem() instanceof PessoaJuridica) {
								PessoaJuridica pessoa = (PessoaJuridica)getJcbPessoa().getSelectedItem();
								dataVencimento = pessoa.getProximoVencimento(dataVencimento);
							}
							getJtfDataVencimento().setText(CalendarFormatter.formatDate(dataVencimento));
						}
						setEditMode(false);
						getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
					} else if (acao.equals("limpar")) {
						if (isEditMode()) {
							limpaCampos();
						}
					} else if (acao.equals("buscar")) {
						if (!isEditMode()) {
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.BUSCANDO);
							limpaCampos();
							getFaturarPedidosTableModel().clearTable();
						}
					} else if (acao.equals("cancelar")) {
						if (isEditMode()) {
							setEditMode(false);
							getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							limpaCampos();
						}
					}
				}
			});
		}
		return barraCadastro;
	}
	
	public void limpaCampos() {
		getJcbPessoa().setSelectedIndex(-1);
		getJtfDataInicio().setText("");
		getJtfDataFim().setText("");
		getJtfDataVencimento().setText("");
		getJtfValorTotal().setText("0,00");		
	}
	
	public JFormattedTextField getJtfDataFim() {
		if (jtfDataFim == null) {
			jtfDataFim = new JFormattedTextField();
			jtfDataFim.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataFim = new MaskFormatter("##/##/####");
				jtfDataFim = new JFormattedTextField(maskDataFim);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de fim");
			}
			jtfDataFim.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataFim.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataFim.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataFim, Color.red, true);
							jtfDataFim.grabFocus();
						}
					} else {
						jtfDataFim.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfDataFim;
	}

	public JFormattedTextField getJtfDataInicio() {
		if (jtfDataInicio == null) {
			jtfDataInicio = new JFormattedTextField();
			jtfDataInicio.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataInicio = new MaskFormatter("##/##/####");
				jtfDataInicio = new JFormattedTextField(maskDataInicio);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de início.");
			}
			jtfDataInicio.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataInicio.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataInicio.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataInicio, Color.red, true);
							jtfDataInicio.grabFocus();
						}
					} else {
						jtfDataInicio.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfDataInicio;
	}

	public JFormattedTextField getJtfDataVencimento() {
		if (jtfDataVencimento == null) {
			jtfDataVencimento = new JFormattedTextField();
			jtfDataVencimento.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataVencimento = new MaskFormatter("##/##/####");
				jtfDataVencimento = new JFormattedTextField(maskDataVencimento);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de vencimento.");
			}
			jtfDataVencimento.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataVencimento.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataVencimento.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataVencimento, Color.red, true);
							jtfDataVencimento.grabFocus();
						}
					} else {
						jtfDataVencimento.setValue(null);
					}
				}
				
				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfDataVencimento;
	}
	
	public JFormattedTextField getJtfValorTotal() {
		if (jtfValorTotal == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			jtfValorTotal = new JFormattedTextField(decimalFormat);
			jtfValorTotal.setBorder(BORDAS_TEXT_FIELD);
			jtfValorTotal.setHorizontalAlignment(JFormattedTextField.RIGHT);
			jtfValorTotal.setText("0,00");
			jtfValorTotal.setEditable(false);
		}
		return jtfValorTotal;
	}
	
	public JComboBox getJcbPessoa() {
		if (jcbPessoa == null) {
			try {
				ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
				Object[] restr1 = { "tipoPessoa", new TipoPessoa(1), "eq", "or" };
				Object[] restr2 = { "tipoPessoa", new TipoPessoa(2), "eq", "or" };
				Object[] restr3 = { "statusPessoa", new StatusPessoa(1), "eq" };
				restricoes.add(restr1);
				restricoes.add(restr2);
				restricoes.add(restr3);
				Vector<Pessoa> listaPessoas = new Vector<Pessoa>(Fachada.obterInstancia().listarPessoas(restricoes, new Object[]{"nome"}, true));
				jcbPessoa = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaPessoas);
				jcbPessoa.setModel(defaultComboBox);
				jcbPessoa.setBorder(BORDAS_TEXT_FIELD);
				jcbPessoa.setSelectedIndex(-1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}
		}
		return jcbPessoa;
	}
	
	public JButton getJbFaturar() {
		jbFaturar = new JButton("Faturar Pedidos");
		jbFaturar.setToolTipText("Faturar todos os pedidos marcados");
		jbFaturar.setFocusable(false);
		jbFaturar.setBounds(64, 140, 130, 25);
		jbFaturar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				faturarPedidos();
			}

		});
		return jbFaturar;
	}
	
	private void faturarPedidos() {
		if (Validador.removerVirgula(getJtfValorTotal().getText()) > 0f) {
			Fatura fatura = new Fatura();
			try {
				fatura.setId(Fachada.obterInstancia().getGenerator("GEN_FATURA_ID"));
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e2.getMessage() + "\n" + e2.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}			
			fatura.setPessoaPai((Pessoa)getJcbPessoa().getSelectedItem());
			
			try {
				fatura.setStatusFatura(Fachada.obterInstancia().procurarStatusFatura(1));
			} catch (StatusFaturaInexistenteException e1) {
				JOptionPane.showMessageDialog(
						TelaFaturarPedidos.this,
						"Status de fatura inexistente.\n"
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
						" Atenção", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}
			
			Set<Pedido> listaPedidos = new HashSet<Pedido>();
			for (Pedido p : getFaturarPedidosTableModel().getList()) {
				if (p.isMarcado()) {
					try {
						p.setStatusPedido(Fachada.obterInstancia().procurarStatusPedido(2));
					} catch (StatusPedidoInexistenteException e1) {
						JOptionPane.showMessageDialog(
								TelaFaturarPedidos.this,
								"Status de pedido inexistente.\n"
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
								" Atenção", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this, 
								"Ocorreu um erro inesperado.\n" 
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
								+ e.getMessage() + "\n" + e.getStackTrace(), 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						getTelaPrincipal().sairSistema(false);
					}
					listaPedidos.add(p);
				}
			}
			fatura.setPedidos(listaPedidos);
			
			fatura.setProjeto(getJtfProjeto().getText());
			fatura.setNotaFiscal(getJtfNotaFiscal().getText());
			fatura.setValorTotal(Validador.removerVirgula(getJtfValorTotal().getText()));
			fatura.setDataEmissao(Calendar.getInstance());
			
			if (getJtfDataVencimento().getText().equals("  /  /    ")) {
				fatura.setDataVencimento(Calendar.getInstance());
			} else {
				try {
					fatura.setDataVencimento(Conversor.stringToCalendar(getJtfDataVencimento().getText()));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			
			fatura.setDataCadastro(Calendar.getInstance());
			fatura.setUsuarioCadastro(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
			try {
				Fachada.obterInstancia().cadastrarFatura(fatura);
				for (Pedido p : listaPedidos) {
					Fachada.obterInstancia().atualizarPedido(p);
				}
			} catch (PedidoInexistenteException e1) {
				JOptionPane.showMessageDialog(
						TelaFaturarPedidos.this,
						"Pedido inexistente.\n"
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
						" Atenção", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}
			try {
				VisualizadorFatura faturaReport = new VisualizadorFatura(fatura, this.logo1, this.faturaPedidosReport, this.faturaPedidosSubReport);
				faturaReport.getViewer().setVisible(true);
			} catch (JRException e) {
				JOptionPane.showMessageDialog(TelaFaturarPedidos.this, 
						"Ocorreu um erro ao gerar o relatório.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (FaturaInexistenteException e) {
				JOptionPane.showMessageDialog(
						TelaFaturarPedidos.this,
						"Pedido inexistente.\n"
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
						" Atenção", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(TelaFaturarPedidos.this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e2.getMessage() + "\n" + e2.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			} 
			atualizaTabelaPedidos();
			atualizaValorTotal();
		} else {
			JOptionPane.showMessageDialog(TelaFaturarPedidos.this, 
					"Selecione ao menos 1 (um) pedido para ser faturado.", 
					" Atenção", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public JButton getJbMarcar() {
		try {
			jbMarcar = new JButton(ImagemUtil.imagemEscalonada(marcarIcon, 20, 20));
		} catch (Exception e1) {
			jbMarcar = new JButton("Marcar");
		}
		jbMarcar.setFocusable(false);
		jbMarcar.setToolTipText("Marcar todos os pedidos");
		jbMarcar.setBounds(10, 140, 25, 25);
		jbMarcar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				marcarPedidos();
			}
		});
		return jbMarcar;
	}
	
	public JButton getJbDesmarcar() {
		try {
			jbDesmarcar = new JButton(ImagemUtil.imagemEscalonada(desmarcarIcon, 20, 20));
		} catch (Exception e1) {
			jbDesmarcar = new JButton("Desmarcar");
		}
		jbDesmarcar.setFocusable(false);
		jbDesmarcar.setToolTipText("Desmarcar todos os pedidos");
		jbDesmarcar.setBounds(37, 140, 25, 25);
		jbDesmarcar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				desmarcarPedidos();
			}
		});
		return jbDesmarcar;
	}

	public JFormattedTextField getJtfValorTotalTabela() {
		if (jtfValorTotalTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorTotalTabela = new JFormattedTextField(decimalFormat);
			jtfValorTotalTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValorTotalTabela;
	}

	private void marcarPedidos() {
		for (int i = 0; i < getFaturarPedidosTableModel().getRowCount(); i++) {
			getFaturarPedidosTableModel().setValueAt(true, i, FaturarPedidosTableModel.MARCADO_INDEX);
		}
	}
	
	private void desmarcarPedidos() {
		for (int i = 0; i < getFaturarPedidosTableModel().getRowCount(); i++) {
			getFaturarPedidosTableModel().setValueAt(false, i, FaturarPedidosTableModel.MARCADO_INDEX);
		}
	}
	
	public JLabel getJlNomePessoa() {
		if (jlNomePessoa == null) {
			jlNomePessoa = new JLabel("Cliente", JLabel.RIGHT);
		}
		return jlNomePessoa;
	}

	public JLabel getJlDataInicio() {
		if (jlDataInicio == null) {
			jlDataInicio = new JLabel("De", JLabel.RIGHT);
		}
		return jlDataInicio;
	}

	public JLabel getJlDataFim() {
		if (jlDataFim == null) {
			jlDataFim = new JLabel("Até", JLabel.RIGHT);
		}
		return jlDataFim;
	}
	
	public JLabel getJlTotalFatura() {
		if (jlTotalFatura == null) {
			jlTotalFatura = new JLabel("Total R$", JLabel.RIGHT);
		}
		return jlTotalFatura;
	}
	
	public JLabel getJlDataVencimento() {
		if (jlDataVencimento == null) {
			jlDataVencimento = new JLabel("Vencimento", JLabel.RIGHT);
		}
		return jlDataVencimento;
	}
	
	public JLabel getJlNotaFiscal() {
		if (jlNotaFiscal == null) {
			jlNotaFiscal = new JLabel("Nota Fiscal", JLabel.RIGHT);
		}
		return jlNotaFiscal;
	}
	
	public JLabel getJlProjeto() {
		if (jlProjeto == null) {
			jlProjeto = new JLabel("Projeto", JLabel.RIGHT);
		}
		return jlProjeto;
	}

	public FaturarPedidosTableModel getFaturarPedidosTableModel() {
		if (faturarPedidosTableModel == null) {
			faturarPedidosTableModel = new FaturarPedidosTableModel(COLUNAS_FATURAR_PEDIDOS);
			faturarPedidosTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaFaturarPedidos().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaFaturarPedidos().setRowSelectionInterval(row, row);
						atualizaValorTotal();
					}
				}
			});
		}
		return faturarPedidosTableModel;
	}
	
	public JFormattedTextField getJtfDataEmissaoTabela() {
		if (jtfDataEmissaoTabela == null) {
			jtfDataEmissaoTabela = new JFormattedTextField();
			jtfDataEmissaoTabela.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataEmissao = new MaskFormatter("##/##/####");
				jtfDataEmissaoTabela = new JFormattedTextField(maskDataEmissao);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de emissão");
			}
			jtfDataEmissaoTabela.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataEmissaoTabela.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataEmissaoTabela.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataEmissaoTabela, Color.red, true);
							jtfDataEmissaoTabela.grabFocus();
						}
					} else {
						jtfDataEmissaoTabela.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfDataEmissaoTabela;
	}

	public JTable getTabelaFaturarPedidos() {
		if (tabelaFaturarPedidos == null) {
			tabelaFaturarPedidos = new JTable();
			tabelaFaturarPedidos.setModel(getFaturarPedidosTableModel());
			tabelaFaturarPedidos.setSurrendersFocusOnKeystroke(true);
			tabelaFaturarPedidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaFaturarPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			DefaultTableCellRenderer alinhamentoDireita = new DefaultTableCellRenderer();
			alinhamentoDireita.setHorizontalAlignment(SwingConstants.RIGHT);
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);
			
			TableColumn colunaMarcador = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.MARCADO_INDEX);
			colunaMarcador.setMinWidth(30);
			colunaMarcador.setPreferredWidth(30);
			colunaMarcador.setMaxWidth(30);
			colunaMarcador.setCellRenderer(new MultiRenderer());
			colunaMarcador.setCellEditor(new MultiEditor());

			TableColumn colunaId = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.ID_INDEX);
			colunaId.setMinWidth(50);
			colunaId.setPreferredWidth(50);
			colunaId.setMaxWidth(50);
			colunaId.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaNomeProjeto = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.NOME_PROJETO_INDEX);
			colunaNomeProjeto.setMinWidth(280);
			colunaNomeProjeto.setPreferredWidth(750);
			colunaNomeProjeto.setMaxWidth(750);
			
			TableColumn colunaSolicitante = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.SOLICITANTE_INDEX);
			colunaSolicitante.setMinWidth(250);
			colunaSolicitante.setPreferredWidth(250);
			colunaSolicitante.setMaxWidth(300);
			
			TableColumn colunaValorTotal = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.VALOR_TOTAL_INDEX);
			colunaValorTotal.setCellEditor(new DefaultCellEditor(getJtfValorTotalTabela()));
			colunaValorTotal.setMinWidth(80);
			colunaValorTotal.setPreferredWidth(80);
			colunaValorTotal.setMaxWidth(80);
			colunaValorTotal.setCellRenderer(alinhamentoDireita);
			
			TableColumn colunaDataEmissao = tabelaFaturarPedidos.getColumnModel().getColumn(FaturarPedidosTableModel.DATA_EMISSAO_INDEX);
			colunaDataEmissao.setCellEditor(new DefaultCellEditor(getJtfDataEmissaoTabela()));
			colunaDataEmissao.setMinWidth(80);
			colunaDataEmissao.setPreferredWidth(80);
			colunaDataEmissao.setMaxWidth(80);
			colunaDataEmissao.setCellRenderer(alinhamentoCentro);
			
		}
		return tabelaFaturarPedidos;
	}
	
	public JScrollPane getJspTabelaPedidos() {
		if (jspTabelaFaturarPedidos == null) {
			jspTabelaFaturarPedidos = new JScrollPane(getTabelaFaturarPedidos());
		}
		return jspTabelaFaturarPedidos;
	}
	
	public JTextField getJtfNotaFiscal() {
		if (jtfNotaFiscal == null) {
			jtfNotaFiscal = new JTextField();
			jtfNotaFiscal.setBorder(BORDAS_TEXT_FIELD);
			jtfNotaFiscal.setDocument(new FixedLengthDocument(15, false, true));
		}
		return jtfNotaFiscal;
	}
	
	public JTextField getJtfProjeto() {
		if (jtfProjeto == null) {
			jtfProjeto = new JTextField();
			jtfProjeto.setBorder(BORDAS_TEXT_FIELD);
			jtfProjeto.setDocument(new FixedLengthDocument(100, false, false));
		}
		return jtfProjeto;
	}
	
	private void atualizaValorTotal() {
		float total = 0;
		for (Pedido p : getFaturarPedidosTableModel().getList()) {
			if (p.isMarcado()) {
				total += p.getValorTotal();
			}
		}
		getJtfValorTotal().setText(Validador.inserirVirgula(total));
	}

	public Object[] getOrdemTabelaPedidos() {
		if (ordemTabelaPedidos == null) {
			ordemTabelaPedidos = new Object[]{"id"};
		}
		return ordemTabelaPedidos;
	}

	public void setOrdemTabelaPedidos(Object[] newOrdemTabelaPedidos) {
		this.ordemTabelaPedidos = newOrdemTabelaPedidos;
	}

	public boolean isCrescenteTabelaPedidos() {
		return crescenteTabelaPedidos;
	}

	public void setCrescenteTabelaPedidos(boolean newCrescenteTabelaPedidos) {
		this.crescenteTabelaPedidos = newCrescenteTabelaPedidos;
	}

	public void highlightLastRow(int row, TableModel tableModel, JTable jTable) {
		int lastrow = tableModel.getRowCount();
		if (row == lastrow - 1) {
			jTable.setRowSelectionInterval(lastrow - 1,
					lastrow - 1);
		} else {
			jTable.setRowSelectionInterval(row + 1, row + 1);
		}
		jTable.setColumnSelectionInterval(0, 0);
	}
	
	public JTabbedPane getJtpPai() {
		return this.jtpPai;
	}

	public Pedido getPedidoSelecionado() {
		return pedidoSelecionado;
	}

	public void setPedidoSelecionado(Pedido newPedidoSelecionado) {
		pedidoSelecionado = newPedidoSelecionado;
	}
	
	public boolean isEditMode() {
		return editMode;
	}
	
	public BarraStatus getBarraStatus() {
		return barraStatus;
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			getJcbPessoa().setEnabled(true);
			getJtfDataInicio().setEditable(true);
			getJtfDataFim().setEditable(true);
			getTabelaFaturarPedidos().setEnabled(false);
			getJcbPessoa().grabFocus();
		} else {
			getJcbPessoa().setEnabled(false);
			getJtfDataInicio().setEditable(false);
			getJtfDataFim().setEditable(false);
			getTabelaFaturarPedidos().setEnabled(true);
			this.grabFocus();
		}
		this.editMode = editMode;
	}
	
	
}
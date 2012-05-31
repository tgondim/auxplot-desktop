package br.com.tg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import br.com.tg.entidades.StatusFatura;
import br.com.tg.entidades.StatusPedido;
import br.com.tg.entidades.StatusPessoa;
import br.com.tg.entidades.TipoPessoa;
import br.com.tg.exceptions.FaturaInexistenteException;
import br.com.tg.gui.util.BarraCadastro;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.CalendarFormatter;
import br.com.tg.gui.util.FaturasTableModel;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.gui.util.PedidosFaturaTableModel;
import br.com.tg.gui.util.PedidosTableModel;
import br.com.tg.gui.util.Tela;
import br.com.tg.util.Conversor;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class TelaCadastroFaturas extends Tela {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private JTabbedPane jtpPai;

	private BarraCadastro barraCadastro;
	
	private BarraStatus barraStatus;

	private JPanel jpCabecalho;

	private JLabel jlId;
	private JLabel jlNomePessoa;
	private JLabel jlNomeProjeto;
	private JLabel jlNotaFiscal;
	private JLabel jlDataVencimento;
	private JLabel jlStatusFatura;
	private JLabel jlDataEmissao;
	private JLabel jlValorTotal;
	
	private JTextField jtfId;
	private JTextField jtfNomeProjeto;
	private JTextField jtfNotaFiscal;
	
	private JFormattedTextField jtfDataEmissao;
	private JFormattedTextField jtfDataVencimento;
	private JFormattedTextField jtfValorTotal;
	private JFormattedTextField jtfValorTabela;
	private JFormattedTextField jtfValorUnitarioItemTabela;
	private JFormattedTextField jtfValorTotalPedidoTabela;
	
	private JButton jbImprimir;
	
	private JComboBox jcbPessoa;
	private JComboBox jcbStatusFatura;
	private JComboBox jcbStatusPedidoTabela;

	public static final String[] COLUNAS_FATURAS = { "Id", "Cliente", "Data Emissão", "Data Vencimento", "Status", "Total R$" };
	protected JTable tabelaFaturas;
	protected JScrollPane jspTabelaFaturas;
	protected FaturasTableModel faturaTableModel;

	public static final String[] COLUNAS_PEDIDOS = { "Id", "Data Emissão", "Solicitante", "Descrição Projeto", "Valor R$" };
	protected JTable tabelaPedidosFatura;
	protected JScrollPane jspTabelaPedidosFatura;
	protected PedidosFaturaTableModel pedidosFaturaTableModel;

	private Fatura faturaSelecionada;

	private String logo1;
	private String faturaPedidosReport;
	private String faturaPedidosSubReport;
	private String imprimirIcon;
	private Object[] ordemTabelaFaturas;
	
	private boolean editMode;
	private boolean crescenteTabelaFaturas = true;
	
	public void setCrescenteTabelaFaturas(boolean crescenteTabelaFaturas) {
		this.crescenteTabelaFaturas = crescenteTabelaFaturas;
	}

	public TelaCadastroFaturas() {
		super();
	}

	public TelaCadastroFaturas(String titulo, auxPlot newTelaPrincipal, JTabbedPane newPai, BarraStatus newBarraStatus) {
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
		} catch (FileNotFoundException e) {
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
		logo1 = prop.getProperty("logo1");
		faturaPedidosReport = prop.getProperty("faturaPedidosReport");
		faturaPedidosSubReport = prop.getProperty("faturaPedidosSubReport");
		imprimirIcon = prop.getProperty("imprimirIcon");
		setLayout(null);
		add(getBarraCadastro());
		add(getJbImprimir());
		add(getJpCabecalho());
		add(getJspTabelaPedidosFatura());
		add(getJspTabelaFaturas());
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
		atualizaTabelaFaturas(true);
		if (getTabelaPedidosFatura().getRowCount() > 0 ) {
			setFaturaSelecionada(getFaturasTableModel().getList().get(0));
		}
		atualizaDadosTela();
		setEditMode(false);
		getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
		if (getJtpPai().getSelectedIndex() > 0) {
			getJtpPai().setSelectedIndex(getJtpPai().getSelectedIndex()+1);
		}
	}

	public void atualizaTabelaFaturas(boolean atualizaRestricoes) {
		getFaturasTableModel().clearTable();
		List<Fatura> listaFaturas;
		try {
			listaFaturas = Fachada.obterInstancia().listarFaturas(
					(atualizaRestricoes ? getRestricoes() : new ArrayList<Object[]>()), 
					getOrdemTabelaFaturas(), 
					isCrescenteTabelaFaturas());
			if (listaFaturas != null) {
				for (Fatura fatura : listaFaturas) {
					getFaturasTableModel().addRow(fatura);
				}
				getTabelaFaturas().repaint();
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
	
	private ArrayList<Object[]> getRestricoes() {
		ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
		if (getJcbPessoa().getSelectedIndex() != -1) {
			Object[] restr = { "pessoaPai", getJcbPessoa().getSelectedItem(), "eq" };
			restricoes.add(restr);
		}
		if (!getJtfNomeProjeto().getText().isEmpty()) {
			Object[] restr = { "projeto", "%" + getJtfNomeProjeto().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (!getJtfNotaFiscal().getText().isEmpty()) {
			Object[] restr = { "notaFiscal", "%" + getJtfNotaFiscal().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (getJcbStatusFatura().getSelectedIndex() != -1) {
			Object[] restr = { "statusFatura", getJcbStatusFatura().getSelectedItem(), "eq" };
			restricoes.add(restr);
		}
		return restricoes;
	}
	
	private void doResize() {
		getBarraCadastro().setBounds(00, 00, 500, 35);
		getJbImprimir().setBounds(getSize().width - 60, 05, 50, 25);
		getJpCabecalho().setBounds(10, 35, getSize().width - 20, 105);
		getJcbPessoa().setBounds(240, 10, getSize().width - 275, 20);
		getJtfNomeProjeto().setBounds(105, 40, getSize().width - 365, 20);
		getJlNotaFiscal().setBounds(getSize().width - 235, 40, 80, 20);
		getJtfNotaFiscal().setBounds(getSize().width - 145, 40, 110, 20);
		getJlValorTotal().setBounds(getSize().width - 185, 70, 60, 20);
		getJtfValorTotal().setBounds(getSize().width - 115, 70, 80, 20);
		getJspTabelaPedidosFatura().setBounds(10, 150, getSize().width - 20, 130);
		getJspTabelaFaturas().setBounds(00, 280, getSize().width, getSize().height-310);
	}

	// carrega a tela com os dados do objeto pedidoSelecionado
	public void atualizaDadosTela() {
		if (getFaturaSelecionada() != null) {
			Fatura fatura = getFaturaSelecionada().clone();
			getJtfId().setText(fatura.getId() != null ? fatura.getId().toString() : "");
			getJcbPessoa().setSelectedItem(fatura.getPessoaPai());
			getJtfNomeProjeto().setText(fatura.getProjeto());
			getJtfNotaFiscal().setText(fatura.getNotaFiscal());
			if (fatura.getStatusFatura() != null) {
				getJcbStatusFatura().setSelectedIndex(fatura.getStatusFatura().getId() - 1);
			} else {
				getJcbStatusFatura().setSelectedIndex(-1);
			}
			
			Calendar auxDataEmissao = fatura.getDataEmissao();
			if (auxDataEmissao != null) {
				getJtfDataEmissao().setText(CalendarFormatter.formatDate(auxDataEmissao));
			} else {
				getJtfDataEmissao().setText("");
			}			
			
			getJtfValorTotal().setText(Validador.inserirVirgula(fatura.getValorTotal()));

			Calendar auxDataVencimento = fatura.getDataVencimento();
			if (auxDataVencimento != null) {
				getJtfDataVencimento().setText(CalendarFormatter.formatDate(auxDataVencimento));
			} else {
				getJtfDataVencimento().setText("");
			}			
			
			getJtfValorTotal().setText(Validador.inserirVirgula(fatura.getValorTotal()));
			getPedidosFaturaTableModel().clearTable();
			Set<Pedido> listaPedidos = fatura.getPedidos(); 
			if (listaPedidos != null) {
				for (Pedido p : listaPedidos) {
					getPedidosFaturaTableModel().addRow(p);
				}
				getTabelaPedidosFatura().repaint();
			}			
		}
	}

	// atualiza o objeto pedidoSelecionado com os dados da tela
	public void atualizaDadosObjeto() {
		setFaturaSelecionada(new Fatura());
		getFaturaSelecionada().setId(Integer.parseInt(getJtfId().getText().trim()));
		getFaturaSelecionada().setPessoaPai((Pessoa)getJcbPessoa().getSelectedItem());
		getFaturaSelecionada().setProjeto(getJtfNomeProjeto().getText());
		getFaturaSelecionada().setNotaFiscal(getJtfNotaFiscal().getText());
		getFaturaSelecionada().setStatusFatura((StatusFatura)getJcbStatusFatura().getSelectedItem());
		try {
			getFaturaSelecionada().setDataEmissao(Conversor.stringToCalendar(getJtfDataEmissao().getText()));
		} catch (ParseException e) {
			System.out.println("Ocorreu um erro ao executar o parse da data de emissão.");
		}
		try {
			getFaturaSelecionada().setDataVencimento(Conversor.stringToCalendar(getJtfDataVencimento().getText()));
		} catch (ParseException e) {
			System.out.println("Ocorreu um erro ao executar o parse da data de vencimento.");
		}
		if (!getJtfValorTotal().getText().trim().isEmpty()) {
			getFaturaSelecionada().setValorTotal(Validador.removerVirgula(getJtfValorTotal().getText().trim()));
		} else {
			getFaturaSelecionada().setValorTotal(0);
		}
		getFaturaSelecionada().setPedidos(new HashSet<Pedido>(getPedidosFaturaTableModel().getList()));
	}

	public void limpaCampos() {
		getJtfId().setText("");
		getJcbPessoa().setSelectedIndex(-1);
		getJtfNomeProjeto().setText("");
		getJtfNotaFiscal().setText("");
		getJtfDataEmissao().setText("");
		getJtfDataVencimento().setText("");
		getJtfValorTotal().setText("");
		getPedidosFaturaTableModel().clearTable();
		getTabelaPedidosFatura().repaint();
		getJcbStatusFatura().setSelectedIndex(-1);
	}

	public JPanel getJpCabecalho() {
		if (jpCabecalho == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpCabecalho = new JPanel();
			jpCabecalho.setBorder(etched);
			jpCabecalho.setLayout(null);
			jpCabecalho.setFocusable(false);
			
			jpCabecalho.add(getJlId());
			getJlId().setBounds(55, 10, 30, 20);
			jpCabecalho.add(getJtfId());
			getJtfId().setBounds(105, 10, 50, 20);
			
			jpCabecalho.add(getJlNomePessoa());
			getJlNomePessoa().setBounds(165, 10, 60, 20);			
			jpCabecalho.add(getJcbPessoa());
			
			jpCabecalho.add(getJlNomeProjeto());
			getJlNomeProjeto().setBounds(35, 40, 50, 20);
			jpCabecalho.add(getJtfNomeProjeto());
			
			jpCabecalho.add(getJlNotaFiscal());
			jpCabecalho.add(getJtfNotaFiscal());
			
			jpCabecalho.add(getJlStatusFatura());
			getJlStatusFatura().setBounds(05, 70, 85, 20);
			jpCabecalho.add(getJcbStatusFatura());
			getJcbStatusFatura().setBounds(105, 70, 100, 20);
			
			jpCabecalho.add(getJlDataEmissao());
			getJlDataEmissao().setBounds(220, 70, 90, 20);
			jpCabecalho.add(getJtfDataEmissao());
			getJtfDataEmissao().setBounds(315, 70, 70, 20);

			jpCabecalho.add(getJlDataVencimento());
			getJlDataVencimento().setBounds(415, 70, 100, 20);
			jpCabecalho.add(getJtfDataVencimento());
			getJtfDataVencimento().setBounds(524, 70, 70, 20);

			jpCabecalho.add(getJlValorTotal());
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
						getTelaPrincipal().killTelaFaturas();
					} else if (acao.equals("confirmar")) {
						atualizaTabelaFaturas(true);
						if (getTabelaPedidosFatura().getRowCount() > 0 ) {
							setFaturaSelecionada(getFaturasTableModel().getList().get(0));
						} else {
							setFaturaSelecionada(null);
						}
						atualizaDadosTela();
						setEditMode(false);
						getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
					} else if (acao.equals("limpar")) {
						if (isEditMode()) {
							limpaCampos();
						}
					} else if (acao.equals("salvar")) {
						JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
								"Não é possível salvar faturas.\nPara faturar pedidos acesse Processos -> Faturar Pedidos\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					} else if (acao.equals("buscar")) {
						if (!isEditMode()) {
							setEditMode(true);
							getJcbStatusFatura().setEnabled(true);
							getBarraCadastro().mudarStatus(BarraCadastro.BUSCANDO);
							limpaCampos();
						}
					} else if (acao.equals("cancelar")) {
						if (isEditMode()) {
							int linhaSelecionada = getFaturasTableModel().getRow(getFaturaSelecionada());
							setEditMode(false);
							getJcbStatusFatura().setEnabled(false);
							getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							limpaCampos();
							getTabelaPedidosFatura().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
							atualizaDadosTela();
						}
					} else if (acao.equals("adicionar")) {
						JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
								"Não é possível adicionar faturas.\nPara faturar pedidos acesse Processos -> Faturar Pedidos\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					} else if (acao.equals("editar")) {
						JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
								"Não é possível editar faturas.\nPara cancelá-las acesse Processos -> Cancelar Faturas\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					} else if (acao.equals("remover")) {
						JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
								"Não é possível remover faturas.\nPara cancelá-las acesse Processos -> Cancelar Faturas\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);						
					} else if (acao.equals("navegarAcima")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getFaturaSelecionada() != null) {
								linhaSelecionada = getFaturasTableModel().getRow(getFaturaSelecionada());
							}
							if (linhaSelecionada > 0 ) {
								getTabelaFaturas().getSelectionModel().setSelectionInterval( linhaSelecionada-1, linhaSelecionada-1 );
								setFaturaSelecionada(getFaturasTableModel().getList().get(getTabelaFaturas().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					} else if (acao.equals("navegarAbaixo")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getFaturaSelecionada() != null) {
								linhaSelecionada = getFaturasTableModel().getRow(getFaturaSelecionada());
							}
							if (linhaSelecionada < getFaturasTableModel().getRowCount()-1) {
								getTabelaFaturas().getSelectionModel().setSelectionInterval( linhaSelecionada+1, linhaSelecionada+1 );
								setFaturaSelecionada(getFaturasTableModel().getList().get(getTabelaFaturas().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					}
				}
			});
		}
		return barraCadastro;
	}
	
	public JTextField getJtfId() {
		if (jtfId == null) {
			jtfId = new JTextField();
			jtfId.setBorder(BORDAS_TEXT_FIELD);
			jtfId.setEditable(false);
		}
		return jtfId;
	}

	public JTextField getJtfNomeProjeto() {
		if (jtfNomeProjeto == null) {
			jtfNomeProjeto = new JTextField();
			jtfNomeProjeto.setBorder(BORDAS_TEXT_FIELD);
			jtfNomeProjeto.setDocument(new FixedLengthDocument(120, false, false));
		}
		return jtfNomeProjeto;
	}

	
	public JTextField getJtfNotaFiscal() {
		if (jtfNotaFiscal == null) {
			jtfNotaFiscal = new JTextField();
			jtfNotaFiscal.setBorder(BORDAS_TEXT_FIELD);
		}
		return jtfNotaFiscal;
	}
	
	public JFormattedTextField getJtfValorTotal() {
		if (jtfValorTotal == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			jtfValorTotal = new JFormattedTextField(decimalFormat);
			jtfValorTotal.setBorder(BORDAS_TEXT_FIELD);
			jtfValorTotal.setHorizontalAlignment(JFormattedTextField.RIGHT);
			jtfValorTotal.setEditable(false);
		}
		return jtfValorTotal;
	}

	public JComboBox getJcbStatusFatura() {
		if (jcbStatusFatura == null) {
			try {
				Vector<StatusFatura> listaStatusFaturas = new Vector<StatusFatura>(Fachada.obterInstancia().listarStatusFaturas());
				jcbStatusFatura = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaStatusFaturas);
				jcbStatusFatura.setModel(defaultComboBox);
				jcbStatusFatura.setBorder(BORDAS_TEXT_FIELD);
				jcbStatusFatura.setSelectedIndex(-1);
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
		return jcbStatusFatura;
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

	public JComboBox getJcbStatusPedidoTabela() {
		if (jcbStatusPedidoTabela == null) {
			try {
				Vector<StatusPedido> listaStatusPedidos = new Vector<StatusPedido>(Fachada.obterInstancia().listarStatusPedidos());
				jcbStatusPedidoTabela = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaStatusPedidos);
				jcbStatusPedidoTabela.setModel(defaultComboBox);
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
		return jcbStatusPedidoTabela;
	}

	public JFormattedTextField getJtfValorTabela() {
		if (jtfValorTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorTabela = new JFormattedTextField(decimalFormat);
			jtfValorTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValorTabela;
	}
	
	public JFormattedTextField getJtfValorTotalPedidoTabela() {
		if (jtfValorTotalPedidoTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorTotalPedidoTabela = new JFormattedTextField(decimalFormat);
			jtfValorTotalPedidoTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValorTotalPedidoTabela;
	}
	
	public JFormattedTextField getJtfValorUnitarioItemTabela() {
		if (jtfValorUnitarioItemTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorUnitarioItemTabela = new JFormattedTextField(decimalFormat);
			jtfValorUnitarioItemTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValorUnitarioItemTabela;
	}
	
	public JFormattedTextField getJtfDataEmissao() {
		if (jtfDataEmissao == null) {
			jtfDataEmissao = new JFormattedTextField();
			jtfDataEmissao.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataEmissao = new MaskFormatter("##/##/####");
				jtfDataEmissao = new JFormattedTextField(maskDataEmissao);
			} catch (ParseException e) {
				System.out.println("Ocorreu um erro ao executar o parse da data de emissão.");
			}
			jtfDataEmissao.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataEmissao.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataEmissao.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataEmissao, Color.red, true);
							jtfDataEmissao.grabFocus();
						}
					} else {
						jtfDataEmissao.setValue(null);
					}
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					
				}
			});
		}
		return jtfDataEmissao;
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
	
	public JLabel getJlId() {
		if (jlId == null) {
			jlId = new JLabel("Id", JLabel.RIGHT);
		}
		return jlId;
	}

	public JLabel getJlNomePessoa() {
		if (jlNomePessoa == null) {
			jlNomePessoa = new JLabel("Cliente", JLabel.RIGHT);
		}
		return jlNomePessoa;
	}

	public JLabel getJlNomeProjeto() {
		if (jlNomeProjeto == null) {
			jlNomeProjeto = new JLabel("Projeto", JLabel.RIGHT);
		}
		return jlNomeProjeto;
	}

	public JLabel getJlNotaFiscal() {
		if (jlNotaFiscal == null) {
			jlNotaFiscal = new JLabel("Nota Fiscal", JLabel.RIGHT);
		}
		return jlNotaFiscal;
	}
	
	public JLabel getJlDataVencimento() {
		if (jlDataVencimento == null) {
			jlDataVencimento = new JLabel("Data Vencimento", JLabel.RIGHT);
		}
		return jlDataVencimento;
	}

	public JLabel getJlStatusFatura() {
		if (jlStatusFatura == null) {
			jlStatusFatura = new JLabel("Status", JLabel.RIGHT);
		}
		return jlStatusFatura;
	}
	
	public JLabel getJlDataEmissao() {
		if (jlDataEmissao == null) {
			jlDataEmissao = new JLabel("Data Emissão", JLabel.RIGHT);
		}
		return jlDataEmissao;
	}

	public JLabel getJlValorTotal() {
		if (jlValorTotal == null) {
			jlValorTotal = new JLabel("Total R$", JLabel.RIGHT);
		}
		return jlValorTotal;
	}

	public FaturasTableModel getFaturasTableModel() {
		if (faturaTableModel == null) {
			faturaTableModel = new FaturasTableModel(COLUNAS_FATURAS);
			faturaTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaFaturas().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaFaturas().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return faturaTableModel;
	}

	public PedidosFaturaTableModel getPedidosFaturaTableModel() {
		if (pedidosFaturaTableModel == null) {
			pedidosFaturaTableModel = new PedidosFaturaTableModel(COLUNAS_PEDIDOS);
			pedidosFaturaTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaPedidosFatura().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaPedidosFatura().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return pedidosFaturaTableModel;
	}

	public JTable getTabelaFaturas() {
		if (tabelaFaturas == null) {
			tabelaFaturas = new JTable();
			tabelaFaturas.setModel(getFaturasTableModel());
			tabelaFaturas.setSurrendersFocusOnKeystroke(true);
			tabelaFaturas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaFaturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
			tabelaFaturas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
//					if (e.getValueIsAdjusting()) {
						if (tabelaFaturas.getSelectedRow() != -1 
								&& tabelaFaturas.getRowCount() > 0 
								&& tabelaFaturas.getSelectedRow() < getFaturasTableModel().getList().size()) {
							setFaturaSelecionada(getFaturasTableModel().getList().get(tabelaFaturas.getSelectedRow()));
						}
						atualizaDadosTela();
//					}
				}
			});
			
			tabelaFaturas.getTableHeader().addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					int colunaClicada = ((javax.swing.table.JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
					boolean colunaValida = false;
					switch (colunaClicada) {
					case 0:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"id"});
						break;
					case 1:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"pessoaPai"});
						break;
					case 2:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"dataEmissao"});
						break;
					case 3:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"dataVencimento"});
						break;
					case 4:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"statusFatura"});
						break;
					case 5:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"valorTotal"});
						break;
					}
					if (colunaValida) {
						if (isCrescenteTabelaFaturas()) {
							setCrescenteTabelaFaturas(false);
						} else {
							setCrescenteTabelaFaturas(true);
						}
						atualizaTabelaFaturas(false);
					}
				}
			});			
			
			DefaultTableCellRenderer alinhamentoDireita = new DefaultTableCellRenderer();
			alinhamentoDireita.setHorizontalAlignment(SwingConstants.RIGHT);
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);
			
			TableColumn colunaId = tabelaFaturas.getColumnModel().getColumn(PedidosTableModel.ID_INDEX);
			colunaId.setMinWidth(50);
			colunaId.setPreferredWidth(50);
			colunaId.setMaxWidth(50);
			colunaId.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaNomePessoa = tabelaFaturas.getColumnModel().getColumn(FaturasTableModel.NOME_PESSOA_INDEX);
			colunaNomePessoa.setMinWidth(250);
			colunaNomePessoa.setPreferredWidth(900);
			colunaNomePessoa.setMaxWidth(900);
			
			TableColumn colunaDataEmissao = tabelaFaturas.getColumnModel().getColumn(FaturasTableModel.DATA_EMISSAO_INDEX);
			colunaDataEmissao.setMinWidth(90);
			colunaDataEmissao.setPreferredWidth(90);
			colunaDataEmissao.setMaxWidth(90);
			colunaDataEmissao.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaDataVencimento = tabelaFaturas.getColumnModel().getColumn(FaturasTableModel.DATA_VENCIMENTO_INDEX);
			colunaDataVencimento.setMinWidth(100);
			colunaDataVencimento.setPreferredWidth(100);
			colunaDataVencimento.setMaxWidth(100);
			colunaDataVencimento.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaStatusPedido = tabelaFaturas.getColumnModel().getColumn(FaturasTableModel.STATUS_FATURA_INDEX);
			colunaStatusPedido.setCellEditor(new DefaultCellEditor(getJcbStatusPedidoTabela()));
			colunaStatusPedido.setMinWidth(100);
			colunaStatusPedido.setPreferredWidth(100);
			colunaStatusPedido.setMaxWidth(100);
			colunaStatusPedido.setCellRenderer(alinhamentoCentro);

			TableColumn colunaValorTotal = tabelaFaturas.getColumnModel().getColumn(FaturasTableModel.VALOR_TOTAL_INDEX);
			colunaValorTotal.setCellEditor(new DefaultCellEditor(getJtfValorTabela()));
			colunaValorTotal.setMinWidth(80);
			colunaValorTotal.setPreferredWidth(80);
			colunaValorTotal.setMaxWidth(80);
			colunaValorTotal.setCellRenderer(alinhamentoDireita);
			
		}
		return tabelaFaturas;
	}
	
	public JScrollPane getJspTabelaFaturas() {
		if (jspTabelaFaturas == null) {
			jspTabelaFaturas = new JScrollPane(getTabelaFaturas());
		}
		return jspTabelaFaturas;
	}
	
	public JButton getJbImprimir() {
		if (jbImprimir == null) {
			jbImprimir = new JButton();
			try {
				jbImprimir.setIcon(ImagemUtil.imagemEscalonada(
						imprimirIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbImprimir.setToolTipText("Imprimir fatura selecionada");
			jbImprimir.setFocusable(false);
			jbImprimir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (getFaturaSelecionada() != null) {
						try {
							VisualizadorFatura faturaReport = new VisualizadorFatura(getFaturaSelecionada(), 
									TelaCadastroFaturas.this.logo1, 
									TelaCadastroFaturas.this.faturaPedidosReport, 
									TelaCadastroFaturas.this.faturaPedidosSubReport);
							faturaReport.getViewer().setVisible(true);
						}catch (JRException e) {
							JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
									"Ocorreu um erro ao gerar o relatório.\n" 
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
									+ e.getMessage() + "\n" + e.getStackTrace(), 
									" Atenção", 
									JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (FaturaInexistenteException e) {
							JOptionPane.showMessageDialog(
									TelaCadastroFaturas.this,
									"Fatura inexistente.\n"
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
									" Atenção", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
									"Ocorreu um erro inesperado.\n" 
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
									+ e2.getMessage() + "\n" + e2.getStackTrace(), 
									" Atenção", 
									JOptionPane.ERROR_MESSAGE);
							e2.printStackTrace();
							getTelaPrincipal().sairSistema(false);
						} 
					} else {
						JOptionPane.showMessageDialog(TelaCadastroFaturas.this, 
								"Nenhuma fatura selecinada.\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return jbImprimir;
	}
	
	public JTable getTabelaPedidosFatura() {
		if (tabelaPedidosFatura == null) {
			tabelaPedidosFatura = new JTable();
			tabelaPedidosFatura.setModel(getPedidosFaturaTableModel());
			tabelaPedidosFatura.setSurrendersFocusOnKeystroke(true);
			tabelaPedidosFatura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaPedidosFatura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);
			DefaultTableCellRenderer alinhamentoDireita = new DefaultTableCellRenderer();
			alinhamentoDireita.setHorizontalAlignment(SwingConstants.RIGHT);
			
			TableColumn colunaIdPedido = tabelaPedidosFatura.getColumnModel().getColumn(PedidosFaturaTableModel.ID_INDEX);
			colunaIdPedido.setMinWidth(50);
			colunaIdPedido.setPreferredWidth(50);
			colunaIdPedido.setMaxWidth(50);
			
			TableColumn colunaDataEmissaoPedido = tabelaPedidosFatura.getColumnModel().getColumn(PedidosFaturaTableModel.DATA_EMISSAO_INDEX);
			colunaDataEmissaoPedido.setMinWidth(90);
			colunaDataEmissaoPedido.setPreferredWidth(90);
			colunaDataEmissaoPedido.setMaxWidth(90);
			colunaDataEmissaoPedido.setCellRenderer(alinhamentoCentro);

			TableColumn colunaSolicitantePedido = tabelaPedidosFatura.getColumnModel().getColumn(PedidosFaturaTableModel.SOLICITANTE_INDEX);
			colunaSolicitantePedido.setMinWidth(200);
			colunaSolicitantePedido.setPreferredWidth(500);
			colunaSolicitantePedido.setMaxWidth(500);
			
			TableColumn colunaProjetoPedido = tabelaPedidosFatura.getColumnModel().getColumn(PedidosFaturaTableModel.PROJETO_INDEX);
			colunaProjetoPedido.setMinWidth(300);
			colunaProjetoPedido.setPreferredWidth(700);
			colunaProjetoPedido.setMaxWidth(700);
			
			TableColumn colunaValorTotalPedido = tabelaPedidosFatura.getColumnModel().getColumn(PedidosFaturaTableModel.VALOR_TOTAL_INDEX);
			colunaValorTotalPedido.setCellEditor(new DefaultCellEditor(getJtfValorTotalPedidoTabela()));
			colunaValorTotalPedido.setMinWidth(80);
			colunaValorTotalPedido.setPreferredWidth(80);
			colunaValorTotalPedido.setMaxWidth(80);
			colunaValorTotalPedido.setCellRenderer(alinhamentoDireita);
			
		}
		return tabelaPedidosFatura;
	}
	
	public JScrollPane getJspTabelaPedidosFatura() {
		if (jspTabelaPedidosFatura == null) {
			jspTabelaPedidosFatura = new JScrollPane(getTabelaPedidosFatura());
		}
		return jspTabelaPedidosFatura;
	}

	public Object[] getOrdemTabelaFaturas() {
		if (ordemTabelaFaturas == null) {
			ordemTabelaFaturas = new Object[]{"id"};
		}
		return ordemTabelaFaturas;
	}

	public void setOrdemTabelaPedidos(Object[] newOrdemTabelaPedidos) {
		this.ordemTabelaFaturas = newOrdemTabelaPedidos;
	}

	public boolean isCrescenteTabelaFaturas() {
		return crescenteTabelaFaturas;
	}

	public void setCrescenteTabelaPedidos(boolean newCrescenteTabelaPedidos) {
		this.crescenteTabelaFaturas = newCrescenteTabelaPedidos;
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

	public Fatura getFaturaSelecionada() {
		return faturaSelecionada;
	}

	public void setFaturaSelecionada(Fatura newFaturaSelecionada) {
		faturaSelecionada = newFaturaSelecionada;
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
			getJcbStatusFatura().setEnabled(true);
			getJtfNomeProjeto().setEditable(true);
			getJtfNotaFiscal().setEditable(true);
			getJtfDataEmissao().setEditable(true);
			getJtfDataVencimento().setEditable(true);
			getTabelaPedidosFatura().setEnabled(false);
			getTabelaPedidosFatura().setEnabled(true);
			getJcbPessoa().grabFocus();
		} else {
			getJcbPessoa().setEnabled(false);
			getJcbStatusFatura().setEnabled(false);
			getJtfNomeProjeto().setEditable(false);
			getJtfNotaFiscal().setEditable(false);
			getJtfDataEmissao().setEditable(false);
			getJtfDataVencimento().setEditable(false);
			getTabelaPedidosFatura().setEnabled(true);
			getTabelaPedidosFatura().setEnabled(false);
			this.grabFocus();
		}
		this.editMode = editMode;
	}
	
}
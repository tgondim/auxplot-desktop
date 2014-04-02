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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;

import net.sf.jasperreports.engine.JRException;

import br.com.tg.entidades.ItemPedido;
import br.com.tg.entidades.Pedido;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.Plotagem;
import br.com.tg.entidades.StatusPedido;
import br.com.tg.entidades.StatusPessoa;
import br.com.tg.entidades.TipoPessoa;
import br.com.tg.exceptions.PedidoInexistenteException;
import br.com.tg.gui.util.BarraCadastro;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.CalendarFormatter;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.gui.util.ItensPedidoTableModel;
import br.com.tg.gui.util.PedidosTableModel;
import br.com.tg.gui.util.Tela;
import br.com.tg.util.Conversor;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class TelaCadastroPedidos extends Tela {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private JTabbedPane jtpPai;

	private BarraCadastro barraCadastro;
	
	private BarraStatus barraStatus;

	private JPanel jpCabecalho;

	private JLabel jlId;
	private JLabel jlNomePessoa;
	private JLabel jlNomeProjeto;
	private JLabel jlNomeSolicitante;
	private JLabel jlTaxaEntrega;
	private JLabel jlStatusPedido;
	private JLabel jlDataEmissao;
	private JLabel jlValorTotal;
	
	private JButton jbExcluir;
	private JButton jbAdicionar;
	private JButton jbImprimir;
	
	private JTextField jtfId;
	private JTextField jtfNomeProjeto;
	private JTextField jtfNomeSolicitante;
	private JTextField jtfPlantaDescricaoTabela;
	
	private JFormattedTextField jtfTaxaEntrega;
	private JFormattedTextField jtfDataEmissao;
	private JFormattedTextField jtfValorTotal;
	private JFormattedTextField jtfValorTabela;
	private JTextField jtfQuantidadeItemTabela;
	private JFormattedTextField jtfValorUnitarioItemTabela;
	private JFormattedTextField jtfValorTotalItemTabela;
	
	private JComboBox jcbPessoa;
	private JComboBox jcbStatusPedido;
	private JComboBox jcbStatusPedidoTabela;
	private JComboBox jcbPlotagemItemTabela;

	public static final String[] COLUNAS_PEDIDOS = { "Id", "Cliente", "Nome Projeto", "Status", "Total R$" };
	protected JTable tabelaPedidos;
	protected JScrollPane jspTabelaPedidos;
	protected PedidosTableModel pedidosTableModel;

	public static final String[] COLUNAS_ITENS_PEDIDOS = { "Plotagem", "Descrição", "Qtd", "Unitário R$", "Total R$" };
	protected JTable tabelaItensPedidos;
	protected JScrollPane jspTabelaItensPedidos;
	protected ItensPedidoTableModel itemPedidoTableModel;

	private Pedido pedidoSelecionado;
	private ItemPedido itemPedidoSelecionado;

	private String logo2;
	private String logoRecicle;
	private String pedidoReport;
	private String pedidoSubReport;
	private String tabelaExcluirIcon;
	private String tabelaAdicionarIcon;
	private String imprimirIcon;
	
	private Object[] ordemTabelaPedidos;
	
	private boolean editMode;
	private boolean crescenteTabelaPedidos = true;
	
	public TelaCadastroPedidos() {
		super();
	}

	public TelaCadastroPedidos(String titulo, auxPlot newTelaPrincipal, JTabbedPane newPai, BarraStatus newBarraStatus) {
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
		logo2 = prop.getProperty("logo2");
		logoRecicle = prop.getProperty("logoRecicle");
		pedidoReport = prop.getProperty("pedidoReport");
		pedidoSubReport = prop.getProperty("pedidoSubReport");
		tabelaExcluirIcon = prop.getProperty("tabelaExcluirIcon");
		tabelaAdicionarIcon = prop.getProperty("tabelaAdicionarIcon");
		imprimirIcon = prop.getProperty("imprimirIcon");
		
		setLayout(null);
		add(getBarraCadastro());
		add(getJbImprimir());
		add(getJpCabecalho());
		add(getJspTabelaItensPedidos());
		add(getJbAdicionar());
		add(getJbExcluir());
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
		
		atualizaTabelaPedidos(true);
		if (getTabelaPedidos().getRowCount() > 0 ) {
			setPedidoSelecionado(getPedidosTableModel().getList().get(0));
		}
		atualizaDadosTela();
		setEditMode(false);
		getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
		if (getJtpPai().getSelectedIndex() > 0) {
			getJtpPai().setSelectedIndex(getJtpPai().getSelectedIndex()+1);
		}
	}

	public void atualizaTabelaPedidos(boolean atualizaRestricoes) {
		getPedidosTableModel().clearTable();
		List<Pedido> listaPedidos;
		try {
			listaPedidos = Fachada.obterInstancia().listarPedidos(
					(atualizaRestricoes ? getRestricoes() : new ArrayList<Object[]>()), 
					getOrdemTabelaPedidos(), 
					isCrescenteTabelaPedidos());
			if (listaPedidos != null) {
				for (Pedido pedido : listaPedidos) {
					getPedidosTableModel().addRow(pedido);
				}
				getTabelaPedidos().repaint();
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
			Object[] restr = { "nomeProjeto", "%" + getJtfNomeProjeto().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (!getJtfNomeSolicitante().getText().isEmpty()) {
			Object[] restr = { "nomeSolicitante", "%" + getJtfNomeSolicitante().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (getJcbStatusPedido().getSelectedIndex() != -1) {
			Object[] restr = { "statusPedido", getJcbStatusPedido().getSelectedItem(), "eq" };
			restricoes.add(restr);
		}
		try {
			Object[] restr = { "dataEmissao", "between", 
					Conversor.stringToCalendar("01/01/" + Calendar.getInstance().get(Calendar.YEAR)),
					Conversor.stringToCalendar("31/12/" + Calendar.getInstance().get(Calendar.YEAR)) };
			restricoes.add(restr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return restricoes;
	}
	
	private void doResize() {
		getBarraCadastro().setBounds(00, 00, 500, 35);
		getJbImprimir().setBounds(getSize().width - 60, 05, 50, 25);
		getJpCabecalho().setBounds(10, 35, getSize().width - 20, 105);
		getJcbPessoa().setBounds(240, 10, getSize().width - 275, 20);
		getJtfNomeProjeto().setBounds(105, 40, getSize().width - 505, 20);
		getJlNomeSolicitante().setBounds(getSize().width - 375, 40, 60, 20);
		getJtfNomeSolicitante().setBounds(getSize().width - 305, 40, 270, 20);
		getJlValorTotal().setBounds(getSize().width - 185, 70, 60, 20);
		getJtfValorTotal().setBounds(getSize().width - 115, 70, 80, 20);
		getJspTabelaItensPedidos().setBounds(10, 150, getSize().width - 20, 130);
		getJbAdicionar().setBounds(10, 285, 20, 20);
		getJbExcluir().setBounds(30, 285, 20, 20);
		getJspTabelaPedidos().setBounds(00, 310, getSize().width, getSize().height-310);
	}

	// carrega a tela com os dados do objeto pedidoSelecionado
	public void atualizaDadosTela() {
		if (getPedidoSelecionado() != null) {
			Pedido pedido = getPedidoSelecionado().clone();
			getJtfId().setText(pedido.getId() != null ? pedido.getId().toString() : "");
			getJcbPessoa().setSelectedItem(pedido.getPessoaPai());
			getJtfNomeProjeto().setText(pedido.getNomeProjeto());
			getJtfNomeSolicitante().setText(pedido.getNomeSolicitante());
			if (pedido.getStatusPedido() != null) {
				getJcbStatusPedido().setSelectedIndex(pedido.getStatusPedido().getId() - 1);
			} else {
				getJcbStatusPedido().setSelectedIndex(-1);
			}
			getJtfTaxaEntrega().setText(Validador.inserirVirgula(pedido.getTaxaEntrega()));
			
			Calendar auxDataEmissao = pedido.getDataEmissao();
			if (auxDataEmissao != null) {
				getJtfDataEmissao().setText(CalendarFormatter.formatDate(auxDataEmissao));
			} else {
				getJtfDataEmissao().setText("");
			}			
			
			getJtfValorTotal().setText(Validador.inserirVirgula(pedido.getValorTotal()));
			getItemPedidoTableModel().clearTable();
			Set<ItemPedido> listaItensPedido = pedido.getItensPedido(); 
			if (listaItensPedido != null) {
				for (ItemPedido ip : listaItensPedido) {
					getItemPedidoTableModel().addRow(ip);
				}
				getTabelaItensPedidos().repaint();
			}			
		}
		//atualizaTabelaPedidos();
	}

	// atualiza o objeto pedidoSelecionado com os dados da tela
	public void atualizaDadosObjeto() {
		if (getPedidoSelecionado().isNovo()) {
			setPedidoSelecionado(new Pedido(true));
		} else {
			setPedidoSelecionado(new Pedido());
		}
		if (!getJtfId().getText().trim().isEmpty()) {
			getPedidoSelecionado().setId(Integer.parseInt(getJtfId().getText().trim()));
		} else {
			try {
				//getPedidoSelecionado().setId(Fachada.obterInstancia().getGenerator("GEN_PEDIDO_ID"));
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
		}
		getPedidoSelecionado().setPessoaPai((Pessoa)getJcbPessoa().getSelectedItem());
		getPedidoSelecionado().setNomeProjeto(getJtfNomeProjeto().getText());
		getPedidoSelecionado().setNomeSolicitante(getJtfNomeSolicitante().getText());
		getPedidoSelecionado().setStatusPedido((StatusPedido)getJcbStatusPedido().getSelectedItem());
		try {
			getPedidoSelecionado().setDataEmissao(Conversor.stringToCalendar(getJtfDataEmissao().getText()));
		} catch (ParseException e) {
			System.out.println("Erro ao executar o parse da data de emissão.");
		}
		if (!getJtfTaxaEntrega().getText().trim().isEmpty()) {
			getPedidoSelecionado().setTaxaEntrega(Validador.removerVirgula(getJtfTaxaEntrega().getText().trim()));
		} else {
			getPedidoSelecionado().setTaxaEntrega(0);
		}
		if (!getJtfValorTotal().getText().trim().isEmpty()) {
			getPedidoSelecionado().setValorTotal(Validador.removerVirgula(getJtfValorTotal().getText().trim()));
		} else {
			getPedidoSelecionado().setValorTotal(0);
		}
		Set<ItemPedido> itensPedido = getItemPedidoTableModel().getList();
		for (ItemPedido ip : itensPedido) {
			try {
				//ip.setId(Fachada.obterInstancia().getGenerator("GEN_ITEM_PEDIDO_ID"));
			}  catch (Exception e2) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e2.getMessage() + "\n" + e2.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
				getTelaPrincipal().sairSistema(false);

			} 
			ip.setPedidoPai(getPedidoSelecionado());
		}
		getPedidoSelecionado().setItensPedido(itensPedido);
	}

	public void limpaCampos() {
		getJtfId().setText("");
		getJcbPessoa().setSelectedIndex(-1);
		getJtfNomeProjeto().setText("");
		getJtfNomeSolicitante().setText("");
		getJtfTaxaEntrega().setText("");
		getJtfDataEmissao().setText("");
		getJtfValorTotal().setText("");
		getItemPedidoTableModel().clearTable();
		getTabelaItensPedidos().repaint();
		getJcbStatusPedido().setSelectedIndex(-1);
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
			
			jpCabecalho.add(getJlNomeSolicitante());
			jpCabecalho.add(getJtfNomeSolicitante());
			
			jpCabecalho.add(getJlTaxaEntrega());
			getJlTaxaEntrega().setBounds(05, 70, 85, 20);
			jpCabecalho.add(getJtfTaxaEntrega());
			getJtfTaxaEntrega().setBounds(105, 70, 50, 20);
			
			jpCabecalho.add(getJlStatusPedido());
			getJlStatusPedido().setBounds(160, 70, 60, 20);
			jpCabecalho.add(getJcbStatusPedido());
			getJcbStatusPedido().setBounds(240, 70, 125, 20);
			
			jpCabecalho.add(getJlDataEmissao());
			getJlDataEmissao().setBounds(375, 70, 90, 20);
			jpCabecalho.add(getJtfDataEmissao());
			getJtfDataEmissao().setBounds(474, 70, 70, 20);

			jpCabecalho.add(getJlValorTotal());
			jpCabecalho.add(getJtfValorTotal());
		}
		return jpCabecalho;
	}

	public BarraCadastro getBarraCadastro() {
		if (barraCadastro == null) {
			try {
				barraCadastro = new BarraCadastro(15, 15);
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
			barraCadastro.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String acao = e.getActionCommand();
					if (acao.equals("sair")) {
						getJtpPai().getSelectedComponent().setVisible(false);
						getJtpPai().removeTabAt(getJtpPai().getSelectedIndex());
						getTelaPrincipal().killTelaPedidos();
					} else if (acao.equals("confirmar")) {
						atualizaTabelaPedidos(true);
						if (getTabelaPedidos().getRowCount() > 0 ) {
							setPedidoSelecionado(getPedidosTableModel().getList().get(0));
						} else {
							setPedidoSelecionado(null);
						}
						atualizaDadosTela();
						setEditMode(false);
						getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
					} else if (acao.equals("limpar")) {
						if (isEditMode()) {
							limpaCampos();
						}
					} else if (acao.equals("salvar")) {
						if (isEditMode()) {
							boolean validador = true;
							if (getJcbPessoa().getSelectedIndex() == -1) {
								getBarraStatus().setMensagem("O Cliente deve ser informado.", true);
								Geral.alterarCor(getJcbPessoa(), Color.red, true);
								getJcbPessoa().grabFocus();
								validador = false;
							}
							if (validador) {
								int linhaSelecionada = getPedidosTableModel().getRow(getPedidoSelecionado());
								atualizaValorTotal(false);
								atualizaDadosObjeto();
								try {
									if (getPedidoSelecionado().isNovo()) {
										getPedidoSelecionado().setUsuarioCadastro(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPedidoSelecionado().setDataCadastro(Calendar.getInstance());
										Fachada.obterInstancia().cadastrarPedido(getPedidoSelecionado().clone());
									} else {
										getPedidoSelecionado().setUsuarioAlteracao(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPedidoSelecionado().setDataAlteracao(Calendar.getInstance());
										Fachada.obterInstancia().atualizarPedido(getPedidoSelecionado().clone());
									}
								} catch (PedidoInexistenteException e1) {
									JOptionPane.showMessageDialog(
											TelaCadastroPedidos.this,
											"Pedido inexistente.\n"
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
											" Atenção", JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(TelaCadastroPedidos.this, 
											"Ocorreu um erro inesperado.\n" 
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
											+ e2.getMessage() + "\n" + e2.getStackTrace(), 
											" Atenção", 
											JOptionPane.ERROR_MESSAGE);
									e2.printStackTrace();
									getTelaPrincipal().sairSistema(false);
								} 
								limpaCampos();
								atualizaTabelaPedidos(true);
								getTabelaPedidos().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
								atualizaDadosTela();
								setEditMode(false);
								getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							}
						}
					} else if (acao.equals("buscar")) {
						if (!isEditMode()) {
							setEditMode(true);
							getJcbStatusPedido().setEnabled(true);
							getBarraCadastro().mudarStatus(BarraCadastro.BUSCANDO);
							limpaCampos();
						}
					} else if (acao.equals("cancelar")) {
						if (isEditMode()) {
							int linhaSelecionada = getPedidosTableModel().getRow(getPedidoSelecionado());
							setEditMode(false);
							getJcbStatusPedido().setEnabled(false);
							getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							limpaCampos();
							getTabelaPedidos().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
							atualizaDadosTela();
						}
					} else if (acao.equals("adicionar")) {
						if (!isEditMode()) {
							getTabelaPedidos().getSelectionModel().clearSelection();
							setPedidoSelecionado(new Pedido(true));
							setEditMode(true);
							getJcbStatusPedido().setEnabled(false);
							getBarraCadastro().mudarStatus(BarraCadastro.ADICIONANDO);
							limpaCampos();
							getJtfDataEmissao().setText(CalendarFormatter.formatDate(Calendar.getInstance()));
							getJcbStatusPedido().setSelectedIndex(0);
						}
					} else if (acao.equals("editar")) {
						if (!isEditMode() && (!getPedidoSelecionado().isNovo())) {
							getPedidoSelecionado().setNovo(false);
							setEditMode(true);
							getJcbStatusPedido().setEnabled(false);
							getBarraCadastro().mudarStatus(BarraCadastro.EDITANDO);
						}
					} else if (acao.equals("remover")) {
						JOptionPane.showMessageDialog(TelaCadastroPedidos.this, 
								"Não é possível remover pedidos.\nPara cancelá-los acesse Processos -> Cancelar Pedidos\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);						
					} else if (acao.equals("navegarAcima")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPedidoSelecionado() != null) {
								linhaSelecionada = getPedidosTableModel().getRow(getPedidoSelecionado());
							}
							if (linhaSelecionada > 0 ) {
								getTabelaPedidos().getSelectionModel().setSelectionInterval( linhaSelecionada-1, linhaSelecionada-1 );
								setPedidoSelecionado(getPedidosTableModel().getList().get(getTabelaPedidos().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					} else if (acao.equals("navegarAbaixo")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPedidoSelecionado() != null) {
								linhaSelecionada = getPedidosTableModel().getRow(getPedidoSelecionado());
							}
							if (linhaSelecionada < getPedidosTableModel().getRowCount()-1) {
								getTabelaPedidos().getSelectionModel().setSelectionInterval( linhaSelecionada+1, linhaSelecionada+1 );
								setPedidoSelecionado(getPedidosTableModel().getList().get(getTabelaPedidos().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					}
				}
			});
		}
		return barraCadastro;
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
			jbImprimir.setToolTipText("Imprimir pedido selecionado");
			jbImprimir.setFocusable(false);
			jbImprimir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (getPedidoSelecionado() != null) {
						try {
							VisualizadorPedido pedidoReport = new VisualizadorPedido(getPedidoSelecionado(), 
									TelaCadastroPedidos.this.logo2, 
									TelaCadastroPedidos.this.pedidoReport, 
									TelaCadastroPedidos.this.pedidoSubReport,
									TelaCadastroPedidos.this.logoRecicle);
							pedidoReport.getViewer().setVisible(true);
						} catch (JRException e) {
							JOptionPane.showMessageDialog(TelaCadastroPedidos.this, 
									"Ocorreu um erro ao gerar o relatório.\n" 
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
									+ e.getMessage() + "\n" + e.getStackTrace(), 
									" Atenção", 
									JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (PedidoInexistenteException e) {
							JOptionPane.showMessageDialog(
									TelaCadastroPedidos.this,
									"Pedido inexistente.\n"
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
									" Atenção", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(TelaCadastroPedidos.this, 
									"Ocorreu um erro inesperado.\n" 
									+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
									+ e2.getMessage() + "\n" + e2.getStackTrace(), 
									" Atenção", 
									JOptionPane.ERROR_MESSAGE);
							e2.printStackTrace();
							getTelaPrincipal().sairSistema(false);
						} 
					} else {
						JOptionPane.showMessageDialog(TelaCadastroPedidos.this, 
								"Nenhum pedido selecinado.\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return jbImprimir;
	}
	
	public JButton getJbExcluir() {
		if (jbExcluir == null) {
			jbExcluir = new JButton();
			try {
				jbExcluir.setIcon(ImagemUtil.imagemEscalonada(
						tabelaExcluirIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbExcluir.setToolTipText("Excluir telefone selecionado");
			jbExcluir.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (getItemPedidoTableModel().getRowCount() > 0) {
						jbExcluir.grabFocus();
						int linha;
						if ((linha = getTabelaItensPedidos().getSelectedRow()) > 0) {
							getTabelaItensPedidos().setRowSelectionInterval(linha - 1, linha -1);
						}
						int row = getTabelaItensPedidos().getSelectedRow();
						if (row >= 0) {
							getItemPedidoTableModel().removeRow(row);
							getTabelaItensPedidos().repaint();						
						}
					}
				}
			});
		}
		return jbExcluir;
	}

	public JButton getJbAdicionar() {
		if (jbAdicionar == null) {
			jbAdicionar = new JButton();
			try {
				jbAdicionar.setIcon(ImagemUtil.imagemEscalonada(tabelaAdicionarIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbAdicionar.setToolTipText("Adicionar telefone");
			jbAdicionar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getItemPedidoTableModel().addEmptyRow();
				}
			});
		}
		return jbAdicionar;
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

	public JTextField getJtfPlantaDescricaoTabela() {
		if (jtfPlantaDescricaoTabela == null) {
			jtfPlantaDescricaoTabela = new JTextField();
			jtfPlantaDescricaoTabela.setBorder(BORDAS_TEXT_FIELD);
			jtfPlantaDescricaoTabela.setDocument(new FixedLengthDocument(100, false, false));
		}
		return jtfPlantaDescricaoTabela;
	}
	
	public JTextField getJtfNomeSolicitante() {
		if (jtfNomeSolicitante == null) {
			jtfNomeSolicitante = new JTextField();
			jtfNomeSolicitante.setBorder(BORDAS_TEXT_FIELD);
			jtfNomeSolicitante.setDocument(new FixedLengthDocument(50, false, false));
		}
		return jtfNomeSolicitante;
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

	public JFormattedTextField getJtfTaxaEntrega() {
		if (jtfTaxaEntrega == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			jtfTaxaEntrega = new JFormattedTextField(decimalFormat);
			jtfTaxaEntrega.setBorder(BORDAS_TEXT_FIELD);
			jtfTaxaEntrega.setHorizontalAlignment(JFormattedTextField.RIGHT);
			jtfTaxaEntrega.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					if (e.getLength() == e.getDocument().getLength()) {
						atualizaValorTotal(true);
					}
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
		}
		return jtfTaxaEntrega;
	}
	
	public JComboBox getJcbStatusPedido() {
		if (jcbStatusPedido == null) {
			try {
				Vector<StatusPedido> listaStatusPedidos = new Vector<StatusPedido>(Fachada.obterInstancia().listarStatusPedidos());
				jcbStatusPedido = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaStatusPedidos);
				jcbStatusPedido.setModel(defaultComboBox);
				jcbStatusPedido.setBorder(BORDAS_TEXT_FIELD);
				jcbStatusPedido.setSelectedIndex(-1);
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
		return jcbStatusPedido;
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
			}catch (Exception e) {
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

	public JComboBox getJcbPlotagemItemTabela() {
		if (jcbPlotagemItemTabela == null) {
			try {
				ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
				Vector<Plotagem> listaPlotagens = new Vector<Plotagem>(Fachada.obterInstancia().listarPlotagens(restricoes, new Object[]{"descricao"}, true));
				jcbPlotagemItemTabela = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaPlotagens);
				jcbPlotagemItemTabela.setModel(defaultComboBox);
				jcbPlotagemItemTabela.setEnabled(true);
				jcbPlotagemItemTabela.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						atualizaDadosItemTela();
					}
				});
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
		return jcbPlotagemItemTabela;
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
	
	public JFormattedTextField getJtfValorTotalItemTabela() {
		if (jtfValorTotalItemTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorTotalItemTabela = new JFormattedTextField(decimalFormat);
			jtfValorTotalItemTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValorTotalItemTabela;
	}
	
	public JFormattedTextField getJtfValorUnitarioItemTabela() {
		if (jtfValorUnitarioItemTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setParseIntegerOnly(false);
			jtfValorUnitarioItemTabela = new JFormattedTextField(decimalFormat);
			jtfValorUnitarioItemTabela.setHorizontalAlignment(JFormattedTextField.RIGHT);

			jtfValorUnitarioItemTabela.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
//					if ((e.getDocument().getLength() > 0) && (e.getLength() == e.getDocument().getLength())) {
						atualizaDadosItemTela();
//					}
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
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
				System.out.println("Erro ao executar o parse da data de emissão.");
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
	
	public JTextField getJtfQuantidadeItemTabela() {
		if (jtfQuantidadeItemTabela == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			jtfQuantidadeItemTabela = new JTextField();
			jtfQuantidadeItemTabela.setHorizontalAlignment(JFormattedTextField.CENTER);
			jtfQuantidadeItemTabela.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
//					if ((e.getDocument().getLength() > 0) && (e.getLength() == e.getDocument().getLength())) {						
						atualizaDadosItemTela();
//					}
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
			jtfQuantidadeItemTabela.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					atualizaDadosItemTela();
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					
				}
			});
		}
		return jtfQuantidadeItemTabela;
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

	public JLabel getJlNomeSolicitante() {
		if (jlNomeSolicitante == null) {
			jlNomeSolicitante = new JLabel("Solicitante", JLabel.RIGHT);
		}
		return jlNomeSolicitante;
	}
	
	public JLabel getJlTaxaEntrega() {
		if (jlTaxaEntrega == null) {
			jlTaxaEntrega = new JLabel("Tx Entrega R$", JLabel.RIGHT);
		}
		return jlTaxaEntrega;
	}

	public JLabel getJlStatusPedido() {
		if (jlStatusPedido == null) {
			jlStatusPedido = new JLabel("Status", JLabel.RIGHT);
		}
		return jlStatusPedido;
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

	public PedidosTableModel getPedidosTableModel() {
		if (pedidosTableModel == null) {
			pedidosTableModel = new PedidosTableModel(COLUNAS_PEDIDOS);
			pedidosTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaPedidos().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaPedidos().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return pedidosTableModel;
	}

	public ItensPedidoTableModel getItemPedidoTableModel() {
		if (itemPedidoTableModel == null) {
			itemPedidoTableModel = new ItensPedidoTableModel(COLUNAS_ITENS_PEDIDOS);
//			itemPedidoTableModel.addTableModelListener(new TableModelListener() {
//				
//				@Override
//				public void tableChanged(TableModelEvent evt) {
//					if (evt.getType() == TableModelEvent.UPDATE) {
//						int column = evt.getColumn();
//						int row = evt.getFirstRow();
//						getTabelaItensPedidos().setColumnSelectionInterval(column + 1, column + 1);
//						getTabelaItensPedidos().setRowSelectionInterval(row, row);
//					}
//				}
//			});
		}
		return itemPedidoTableModel;
	}

	public JTable getTabelaPedidos() {
		if (tabelaPedidos == null) {
			tabelaPedidos = new JTable();
			tabelaPedidos.setModel(getPedidosTableModel());
			tabelaPedidos.setSurrendersFocusOnKeystroke(true);
			tabelaPedidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
			tabelaPedidos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
//					if (e.getValueIsAdjusting()) {
						if (tabelaPedidos.getSelectedRow() != -1 
								&& tabelaPedidos.getRowCount() > 0 
								&& tabelaPedidos.getSelectedRow() < getPedidosTableModel().getList().size()) {
							setPedidoSelecionado(getPedidosTableModel().getList().get(tabelaPedidos.getSelectedRow()));
						}
						atualizaDadosTela();
//					}
				}
			});
			
			tabelaPedidos.getTableHeader().addMouseListener(new MouseListener() {
				
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
						setOrdemTabelaPedidos(new Object[]{"nomeProjeto"});
						break;
					case 3:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"statusPedido"});
						break;
					case 4:
						colunaValida = true;
						setOrdemTabelaPedidos(new Object[]{"valorTotal"});
						break;
					}
					if (colunaValida) {
						if (isCrescenteTabelaPedidos()) {
							setCrescenteTabelaPedidos(false);
						} else {
							setCrescenteTabelaPedidos(true);
						}
						atualizaTabelaPedidos(false);
					}
				}
			});
			
			DefaultTableCellRenderer alinhamentoDireita = new DefaultTableCellRenderer();
			alinhamentoDireita.setHorizontalAlignment(SwingConstants.RIGHT);
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);
			
			TableColumn colunaId = tabelaPedidos.getColumnModel().getColumn(PedidosTableModel.ID_INDEX);
			colunaId.setMinWidth(50);
			colunaId.setPreferredWidth(50);
			colunaId.setMaxWidth(50);
			colunaId.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaNomePessoa = tabelaPedidos.getColumnModel().getColumn(PedidosTableModel.NOME_PESSOA_INDEX);
			colunaNomePessoa.setMinWidth(250);
			colunaNomePessoa.setPreferredWidth(700);
			colunaNomePessoa.setMaxWidth(700);
			
			TableColumn colunaNomeProjeto = tabelaPedidos.getColumnModel().getColumn(PedidosTableModel.NOME_PROJETO_INDEX);
			colunaNomeProjeto.setMinWidth(250);
			colunaNomeProjeto.setPreferredWidth(550);
			colunaNomeProjeto.setMaxWidth(550);
			
			TableColumn colunaStatusPedido = tabelaPedidos.getColumnModel().getColumn(PedidosTableModel.STATUS_PEDIDO_INDEX);
			colunaStatusPedido.setCellEditor(new DefaultCellEditor(getJcbStatusPedidoTabela()));
			colunaStatusPedido.setMinWidth(100);
			colunaStatusPedido.setPreferredWidth(100);
			colunaStatusPedido.setMaxWidth(100);
			colunaStatusPedido.setCellRenderer(alinhamentoCentro);

			TableColumn colunaValorTotal = tabelaPedidos.getColumnModel().getColumn(PedidosTableModel.VALOR_TOTAL_INDEX);
			colunaValorTotal.setCellEditor(new DefaultCellEditor(getJtfValorTabela()));
			colunaValorTotal.setMinWidth(80);
			colunaValorTotal.setPreferredWidth(80);
			colunaValorTotal.setMaxWidth(80);
			colunaValorTotal.setCellRenderer(alinhamentoDireita);
			
		}
		return tabelaPedidos;
	}
	
	public JScrollPane getJspTabelaPedidos() {
		if (jspTabelaPedidos == null) {
			jspTabelaPedidos = new JScrollPane(getTabelaPedidos());
		}
		return jspTabelaPedidos;
	}
	
	public JTable getTabelaItensPedidos() {
		if (tabelaItensPedidos == null) {
			tabelaItensPedidos = new JTable();
			tabelaItensPedidos.setModel(getItemPedidoTableModel());
			tabelaItensPedidos.setSurrendersFocusOnKeystroke(true);
			tabelaItensPedidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaItensPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultTableCellRenderer alinhamentoDireita = new DefaultTableCellRenderer();
			alinhamentoDireita.setHorizontalAlignment(SwingConstants.RIGHT);
			
			TableColumn colunaPlotagemItem = tabelaItensPedidos.getColumnModel().getColumn(ItensPedidoTableModel.PLOTAGEM_INDEX);
			colunaPlotagemItem.setCellEditor(new DefaultCellEditor(getJcbPlotagemItemTabela()));
			colunaPlotagemItem.setMinWidth(180);
			colunaPlotagemItem.setPreferredWidth(700);
			colunaPlotagemItem.setMaxWidth(700);
			
			TableColumn colunaPlantaDescricaoItem = tabelaItensPedidos.getColumnModel().getColumn(ItensPedidoTableModel.PLANTA_DESCRICAO_INDEX);
			colunaPlantaDescricaoItem.setCellEditor(new DefaultCellEditor(getJtfPlantaDescricaoTabela()));
			colunaPlantaDescricaoItem.setMinWidth(140);
			colunaPlantaDescricaoItem.setPreferredWidth(500);
			colunaPlantaDescricaoItem.setMaxWidth(500);

			TableColumn colunaQuantidadeItem = tabelaItensPedidos.getColumnModel().getColumn(ItensPedidoTableModel.QUANTIDADE_INDEX);
			colunaQuantidadeItem.setCellEditor(new DefaultCellEditor(getJtfQuantidadeItemTabela()));
			colunaQuantidadeItem.setMinWidth(60);
			colunaQuantidadeItem.setPreferredWidth(60);
			colunaQuantidadeItem.setMaxWidth(60);
			colunaQuantidadeItem.setCellRenderer(alinhamentoDireita);
			
			TableColumn colunaValorUnitarioItem = tabelaItensPedidos.getColumnModel().getColumn(ItensPedidoTableModel.VALOR_UNITARIO_INDEX);
			colunaValorUnitarioItem.setCellEditor(new DefaultCellEditor(getJtfValorUnitarioItemTabela()));
			colunaValorUnitarioItem.setMinWidth(80);
			colunaValorUnitarioItem.setPreferredWidth(80);
			colunaValorUnitarioItem.setMaxWidth(80);
			colunaValorUnitarioItem.setCellRenderer(alinhamentoDireita);
			
			TableColumn colunaValorTotalItem = tabelaItensPedidos.getColumnModel().getColumn(ItensPedidoTableModel.VALOR_TOTAL_INDEX);
			colunaValorTotalItem.setCellEditor(new DefaultCellEditor(getJtfValorTotalItemTabela()));
			colunaValorTotalItem.setMinWidth(80);
			colunaValorTotalItem.setPreferredWidth(80);
			colunaValorTotalItem.setMaxWidth(80);
			colunaValorTotalItem.setCellRenderer(alinhamentoDireita);
			
		}
		return tabelaItensPedidos;
	}

//	public void tableEnterAction(final JTable ao_table) {  
//	  
//	  InputMap im = ao_table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);     
//	            
//	  KeyStroke lo_key_enter = KeyStroke.getKeyStroke("ENTER");     
//	        
//	  im.put(lo_key_enter, im.get(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, 0)));    
//	              
//	  Action enterAction = new AbstractAction() {    
//	  
//	            public void actionPerformed(ActionEvent e)  
//	            {  
//	            	System.out.println("ENTER");
//	            	if (((javax.swing.JTable)e.getSource()).getSelectedColumn() == ItensPedidoTableModel.QUANTIDADE_INDEX) {
//	            		getJtfQuantidadeItemTabela().setSelectionStart(0);
//	            		getJtfQuantidadeItemTabela().setSelectionEnd(getJtfQuantidadeItemTabela().getText().length());
//	            	}
//	            }  
//	    };   
//	    ao_table.getActionMap().put(im.get(lo_key_enter), enterAction);   
//	} 
		
	private void atualizaDadosItemTela() {
		Plotagem p = (Plotagem)getJcbPlotagemItemTabela().getSelectedItem();
		if (p != null) {
			float qtd = Validador.removerVirgula((String)getItemPedidoTableModel().getValueAt(getTabelaItensPedidos().getSelectedRow(), 
					ItensPedidoTableModel.QUANTIDADE_INDEX));
			
			if (((String)getItemPedidoTableModel().getValueAt(getTabelaItensPedidos()
					.getSelectedRow(), ItensPedidoTableModel.VALOR_UNITARIO_INDEX)
					).equals("0,00")) {
				getItemPedidoTableModel().setValueAt(Validador.inserirVirgula(p.getValor()), 
						getTabelaItensPedidos().getSelectedRow(), 
						ItensPedidoTableModel.VALOR_UNITARIO_INDEX);
			}
			getItemPedidoTableModel().setValueAt(Validador.inserirVirgula(
					Validador.removerVirgula((String)getItemPedidoTableModel()
							.getValueAt(getTabelaItensPedidos().getSelectedRow(), 
									ItensPedidoTableModel.VALOR_UNITARIO_INDEX))*qtd), 
					getTabelaItensPedidos().getSelectedRow(), 
					ItensPedidoTableModel.VALOR_TOTAL_INDEX);
			atualizaValorTotal(false);
		} else {
			getJtfValorUnitarioItemTabela().setText("");
			getJtfValorTotalItemTabela().setText("");
		}
		getTabelaItensPedidos().repaint();
	}

	private void atualizaValorTotal(boolean atualizarDoPedidoSelecionado) {
		float total = 0;
		if (atualizarDoPedidoSelecionado) {
			if (getPedidoSelecionado().getItensPedido() != null) {
				for (ItemPedido ip : getPedidoSelecionado().getItensPedido()) {
					total += ip.getValorTotal();
				}
			}
		} else {
			for (ItemPedido ip : getItemPedidoTableModel().getList()) {
				total += ip.getValorTotal();
			}
		}
		total += Validador.removerVirgula(getJtfTaxaEntrega().getText());
		getJtfValorTotal().setText(Validador.inserirVirgula(total));
		getPedidoSelecionado().setValorTotal(total);
	}

	public JScrollPane getJspTabelaItensPedidos() {
		if (jspTabelaItensPedidos == null) {
			jspTabelaItensPedidos = new JScrollPane(getTabelaItensPedidos());
		}
		return jspTabelaItensPedidos;
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
	
	public ItemPedido getItemPedidoSelecionado() {
		return itemPedidoSelecionado;
	}
	
	public void setItemPedidoSelecionado(ItemPedido newItemPedidoSelecionado) {
		itemPedidoSelecionado = newItemPedidoSelecionado;
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
			getJcbStatusPedido().setEnabled(true);
			getJtfNomeProjeto().setEditable(true);
			getJtfNomeSolicitante().setEditable(true);
			getJtfTaxaEntrega().setEditable(true);
			getJtfDataEmissao().setEditable(true);
			getTabelaPedidos().setEnabled(false);
			getTabelaItensPedidos().setEnabled(true);
			getJcbPlotagemItemTabela().setEnabled(true);
			getJbAdicionar().setEnabled(true);
			getJbExcluir().setEnabled(true);
			getJcbPessoa().grabFocus();
		} else {
			getJcbPessoa().setEnabled(false);
			getJcbStatusPedido().setEnabled(false);
			getJtfNomeProjeto().setEditable(false);
			getJtfNomeSolicitante().setEditable(false);
			getJtfTaxaEntrega().setEditable(false);
			getJtfDataEmissao().setEditable(false);
			getTabelaPedidos().setEnabled(true);
			getTabelaItensPedidos().setEnabled(false);
			getJcbPlotagemItemTabela().setEnabled(false);
			getJbAdicionar().setEnabled(false);
			getJbExcluir().setEnabled(false);
			this.grabFocus();
		}
		this.editMode = editMode;
	}
	
	
}
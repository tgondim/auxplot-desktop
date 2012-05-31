package br.com.tg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
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

import br.com.tg.entidades.Plotagem;
import br.com.tg.exceptions.PlotagemInexistenteException;
import br.com.tg.gui.util.BarraCadastro;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.PlotagensTableModel;
import br.com.tg.gui.util.Tela;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class TelaCadastroPlotagens extends Tela {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private JTabbedPane jtpPai;

	private BarraCadastro barraCadastro;
	
	private BarraStatus barraStatus;

	private JPanel jpPlotagem;

	private JLabel jlId;
	private JLabel jlDescricao;
	private JLabel jlUnidade;
	private JLabel jlValor;
	
	private JTextField jtfId;
	private JTextField jtfDescricao;
	private JTextField jtfUnidade;
	
	private JFormattedTextField jtfValor;
	private JFormattedTextField jtfValorTabela;

	public static final String[] COLUNAS_PLOTAGENS = { "Id", "Descrição", "Unidade", "Valor R$" };
	protected JTable tabelaPlotagens;
	protected JScrollPane jspTabelaPlotagens;
	protected PlotagensTableModel plotagensTableModel;

	private Plotagem plotagemSelecionada;

	private Object[] ordemTabelaPlotagens;
	
	private boolean editMode;
	private boolean crescenteTabelaPlotagens = true;
	
	public TelaCadastroPlotagens() {
		super();
	}

	public TelaCadastroPlotagens(String titulo, auxPlot newTelaPrincipal, JTabbedPane newPai, BarraStatus newBarraStatus) {
		super(titulo, newTelaPrincipal);
		this.jtpPai = newPai;
		this.barraStatus = newBarraStatus;
		inicio();
	}

	public void inicio() {

		setLayout(null);
		add(getBarraCadastro());
		add(getJpPlotagem());
		add(getJspTabelaPlotagens());
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
		atualizaTabelaPlotagens(true);
		if (getTabelaPlotagens().getRowCount() > 0 ) {
			setPlotagemSelecionada(getPlotagensTableModel().getList().get(0));
		}
		atualizaDadosTela();
		setEditMode(false);
		getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
		if (getJtpPai().getSelectedIndex() > 0) {
			getJtpPai().setSelectedIndex(getJtpPai().getSelectedIndex()+1);
		}
	}

	public void atualizaTabelaPlotagens(boolean atualizaRestricoes) {
		getPlotagensTableModel().clearTable();
		List<Plotagem> listaPlotagens;
		try {
			listaPlotagens = Fachada.obterInstancia().listarPlotagens(
					(atualizaRestricoes ? getRestricoes() : new ArrayList<Object[]>()), 
					getOrdemTabelaPlotagens(), 
					isCrescenteTabelaPlotagens());
			if (listaPlotagens != null) {
				for (Plotagem plotagem : listaPlotagens) {
					getPlotagensTableModel().addRow(plotagem);
				}
				getTabelaPlotagens().repaint();
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
		if (!getJtfDescricao().getText().isEmpty()) {
			Object[] restr = { "descricao", "%" + getJtfDescricao().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (!getJtfUnidade().getText().isEmpty()) {
			Object[] restr = { "unidade", "%" + getJtfUnidade().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		return restricoes;
	}
	
	private void doResize() {
		getBarraCadastro().setBounds(00, 00, 500, 35);
		getJpPlotagem().setBounds(10, 35, getSize().width - 20, 140);
		getJtfDescricao().setBounds(95, 40, getSize().width - 195, 20);
		getJspTabelaPlotagens().setBounds(00, 190, getSize().width, getSize().height-190);
	}

	// carrega a tela com os dados do objeto plotagemSelecionada
	public void atualizaDadosTela() {
		if (getPlotagemSelecionada() != null) {
			Plotagem plotagem = getPlotagemSelecionada();
			getJtfId().setText(plotagem.getId() != null ? plotagem.getId().toString() : "");
			getJtfDescricao().setText(plotagem.getDescricao());
			getJtfUnidade().setText(plotagem.getUnidade());
			getJtfValor().setText(Validador.inserirVirgula(plotagem.getValor()));
		}
	}

	// atualiza o objeto plotagemSelecionada com os dados da tela
	public void atualizaDadosObjeto() {
		setPlotagemSelecionada(new Plotagem());
		if (!getJtfId().getText().trim().isEmpty()) {
			getPlotagemSelecionada().setId(Integer.parseInt(getJtfId().getText().trim()));
		}
		getPlotagemSelecionada().setDescricao(getJtfDescricao().getText());
		getPlotagemSelecionada().setUnidade(getJtfUnidade().getText());
		if (!getJtfValor().getText().trim().isEmpty()) {
			getPlotagemSelecionada().setValor(Validador.removerVirgula(getJtfValor().getText().trim()));
		} else {
			getPlotagemSelecionada().setValor(0);
		}
	}

	public void limpaCampos() {
		getJtfId().setText("");
		getJtfDescricao().setText("");
		getJtfUnidade().setText("");
		getJtfValor().setText("");
	}

	public JPanel getJpPlotagem() {
		if (jpPlotagem == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpPlotagem = new JPanel();
			jpPlotagem.setBorder(etched);
			jpPlotagem.setLayout(null);
			jpPlotagem.setFocusable(false);
			jpPlotagem.add(getJlId());
			getJlId().setBounds(45, 10, 30, 20);
			jpPlotagem.add(getJtfId());
			getJtfId().setBounds(95, 10, 50, 20);
			jpPlotagem.add(getJlDescricao());
			getJlDescricao().setBounds(15, 40, 60, 20);
			jpPlotagem.add(getJtfDescricao());
			jpPlotagem.add(getJlUnidade());
			getJlUnidade().setBounds(15, 70, 60, 20);
			jpPlotagem.add(getJtfUnidade());
			getJtfUnidade().setBounds(95, 70, 80, 20);
			jpPlotagem.add(getJlValor());
			getJlValor().setBounds(15, 100, 60, 20);
			jpPlotagem.add(getJtfValor());
			getJtfValor().setBounds(95, 100, 80, 20);
		}
		return jpPlotagem;
	}

	public BarraCadastro getBarraCadastro() {
		if (barraCadastro == null) {
			try {
				barraCadastro = new BarraCadastro(15, 15);
			} catch (FileNotFoundException e3) {
				JOptionPane.showMessageDialog(this, 
						"Por favor, feche esta tela e abra novamente.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e3.getMessage() + "\n", 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e3.printStackTrace();
			} catch (IOException e3) {
				JOptionPane.showMessageDialog(this, 
						"Por favor, feche esta tela e abra novamente.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e3.getMessage() + "\n", 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e3.printStackTrace();
			}
			barraCadastro.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String acao = e.getActionCommand();
					if (acao.equals("sair")) {
						getJtpPai().getSelectedComponent().setVisible(false);
						getJtpPai().removeTabAt(getJtpPai().getSelectedIndex());
						getTelaPrincipal().killTelaPlotagens(); 
					} else if (acao.equals("confirmar")) {
						atualizaTabelaPlotagens(true);
						if (getTabelaPlotagens().getRowCount() > 0 ) {
							setPlotagemSelecionada(getPlotagensTableModel().getList().get(0));
						} else {
							setPlotagemSelecionada(null);
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
							if (getJtfDescricao().getText().length() == 0) {
								getBarraStatus().setMensagem("A descrição deve ser informada.", true);
								Geral.alterarCor(getJtfDescricao(), Color.red, true);
								getJtfDescricao().grabFocus();
								validador = false;
							}
							if (validador) {
								int linhaSelecionada = getPlotagensTableModel().getRow(getPlotagemSelecionada());
								atualizaDadosObjeto();
								try {
									if (getPlotagemSelecionada().getId() == null) {
										getPlotagemSelecionada().setUsuarioCadastro(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPlotagemSelecionada().setDataCadastro(Calendar.getInstance());
										Fachada.obterInstancia().cadastrarPlotagem(getPlotagemSelecionada());
									} else {
										getPlotagemSelecionada().setUsuarioAlteracao(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPlotagemSelecionada().setDataAlteracao(Calendar.getInstance());
										Fachada.obterInstancia().atualizarPlotagem(getPlotagemSelecionada());
									}
								} catch (PlotagemInexistenteException e1) {
									JOptionPane.showMessageDialog(
											TelaCadastroPlotagens.this,
											"Plotagem inexistente.\n"
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
											" Atenção", JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(TelaCadastroPlotagens.this, 
											"Ocorreu um erro inesperado.\n" 
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
											+ e1.getMessage() + "\n" + e1.getStackTrace(), 
											" Atenção", 
											JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
									getTelaPrincipal().sairSistema(false);
								} 
								limpaCampos();
								atualizaTabelaPlotagens(true);
								getTabelaPlotagens().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
								atualizaDadosTela();
								setEditMode(false);
								getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							}
						}
					} else if (acao.equals("buscar")) {
						if (!isEditMode()) {
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.BUSCANDO);
							limpaCampos();
						}
					} else if (acao.equals("cancelar")) {
						if (isEditMode()) {
							int linhaSelecionada = getPlotagensTableModel().getRow(getPlotagemSelecionada());
							setEditMode(false);
							getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							limpaCampos();
							getTabelaPlotagens().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
							atualizaDadosTela();
						}
					} else if (acao.equals("adicionar")) {
						if (!isEditMode()) {
							getTabelaPlotagens().getSelectionModel().clearSelection();
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.ADICIONANDO);
							limpaCampos();
						}
					} else if (acao.equals("editar")) {
						if (!isEditMode() && (getPlotagemSelecionada() != null)) {
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.EDITANDO);
						}
					} else if (acao.equals("remover")) {
						if (getPlotagemSelecionada().getId() != null && !isEditMode()) {
							int linhaSelecionada = getPlotagensTableModel().getRow(getPlotagemSelecionada());
							if (linhaSelecionada != -1) {
								Object[] mensagem = { "Confirma exclusão do registro?" };
								int returnCode = JOptionPane.showConfirmDialog(
										getParent(), mensagem, "Atenção!",
										JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								if (returnCode == JOptionPane.OK_OPTION) {
									try {
										Fachada.obterInstancia().descadastrarPlotagem(getPlotagemSelecionada());
									} catch (PlotagemInexistenteException e1) {
										e1.printStackTrace();
									} catch (Exception e1) {
										JOptionPane.showMessageDialog(TelaCadastroPlotagens.this, 
												"Ocorreu um erro inesperado.\n" 
												+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
												+ e1.getMessage() + "\n" + e1.getStackTrace(), 
												" Atenção", 
												JOptionPane.ERROR_MESSAGE);
										e1.printStackTrace();
										getTelaPrincipal().sairSistema(false);
									} 
									limpaCampos();
									atualizaTabelaPlotagens(true);
									getTabelaPlotagens().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
								}
							} 
						}
					} else if (acao.equals("navegarAcima")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPlotagemSelecionada() != null) {
								linhaSelecionada = getPlotagensTableModel().getRow(getPlotagemSelecionada());
							}
							if (linhaSelecionada > 0 ) {
								getTabelaPlotagens().getSelectionModel().setSelectionInterval( linhaSelecionada-1, linhaSelecionada-1 );
								setPlotagemSelecionada(getPlotagensTableModel().getList().get(getTabelaPlotagens().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					} else if (acao.equals("navegarAbaixo")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPlotagemSelecionada() != null) {
								linhaSelecionada = getPlotagensTableModel().getRow(getPlotagemSelecionada());
							}
							if (linhaSelecionada < getPlotagensTableModel().getRowCount()-1) {
								getTabelaPlotagens().getSelectionModel().setSelectionInterval( linhaSelecionada+1, linhaSelecionada+1 );
								setPlotagemSelecionada(getPlotagensTableModel().getList().get(getTabelaPlotagens().getSelectedRow()));
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

	public JTextField getJtfDescricao() {
		if (jtfDescricao == null) {
			jtfDescricao = new JTextField();
			jtfDescricao.setBorder(BORDAS_TEXT_FIELD);
			jtfDescricao.setDocument(new FixedLengthDocument(120, false, false));
		}
		return jtfDescricao;
	}

	public JTextField getJtfUnidade() {
		if (jtfUnidade == null) {
			jtfUnidade = new JTextField();
			jtfUnidade.setBorder(BORDAS_TEXT_FIELD);
			jtfUnidade.setDocument(new FixedLengthDocument(10, false, false));
		}
		return jtfUnidade;
	}

	public JFormattedTextField getJtfValor() {
		if (jtfValor == null) {
			NumberFormat decimalFormat = NumberFormat.getNumberInstance();
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			jtfValor = new JFormattedTextField(decimalFormat);
			jtfValor.setBorder(BORDAS_TEXT_FIELD);
			jtfValor.setHorizontalAlignment(JFormattedTextField.RIGHT);
		}
		return jtfValor;
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

	public JLabel getJlId() {
		if (jlId == null) {
			jlId = new JLabel("Id", JLabel.RIGHT);
		}
		return jlId;
	}

	public JLabel getJlDescricao() {
		if (jlDescricao == null) {
			jlDescricao = new JLabel("Descrição", JLabel.RIGHT);
		}
		return jlDescricao;
	}

	public JLabel getJlUnidade() {
		if (jlUnidade == null) {
			jlUnidade = new JLabel("Unidade", JLabel.RIGHT);
		}
		return jlUnidade;
	}

	public JLabel getJlValor() {
		if (jlValor == null) {
			jlValor = new JLabel("Valor R$", JLabel.RIGHT);
		}
		return jlValor;
	}

	public PlotagensTableModel getPlotagensTableModel() {
		if (plotagensTableModel == null) {
			plotagensTableModel = new PlotagensTableModel(COLUNAS_PLOTAGENS);
			plotagensTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaPlotagens().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaPlotagens().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return plotagensTableModel;
	}

	public JTable getTabelaPlotagens() {
		if (tabelaPlotagens == null) {
			tabelaPlotagens = new JTable();
			tabelaPlotagens.setModel(getPlotagensTableModel());
			tabelaPlotagens.setSurrendersFocusOnKeystroke(true);
			tabelaPlotagens.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaPlotagens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabelaPlotagens.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
//					if (e.getValueIsAdjusting()) {
						if (tabelaPlotagens.getSelectedRow() != -1 
								&& tabelaPlotagens.getRowCount() > 0 
								&& tabelaPlotagens.getSelectedRow() < getPlotagensTableModel().getList().size()) {
							setPlotagemSelecionada(getPlotagensTableModel().getList().get(tabelaPlotagens.getSelectedRow()));
						}
						atualizaDadosTela();
//					}
				}
			});

			tabelaPlotagens.getTableHeader().addMouseListener(new MouseListener() {
				
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
						setOrdemTabelaPlotagens(new Object[]{"id"});
						break;
					case 1:
						colunaValida = true;
						setOrdemTabelaPlotagens(new Object[]{"descricao"});
						break;
					case 2:
						colunaValida = true;
						setOrdemTabelaPlotagens(new Object[]{"unidade"});
						break;
					case 3:
						colunaValida = true;
						setOrdemTabelaPlotagens(new Object[]{"valor"});
						break;
					}
					if (colunaValida) {
						if (isCrescenteTabelaPlotagens()) {
							setCrescenteTabelaPlotagens(false);
						} else {
							setCrescenteTabelaPlotagens(true);
						}
						atualizaTabelaPlotagens(false);
					}
				}
			});
			
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);
			
			TableColumn colunaId = tabelaPlotagens.getColumnModel().getColumn(PlotagensTableModel.ID_INDEX);
			colunaId.setMinWidth(50);
			colunaId.setPreferredWidth(50);
			colunaId.setMaxWidth(50);
			colunaId.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaDescricao = tabelaPlotagens.getColumnModel().getColumn(PlotagensTableModel.DESCRICAO_INDEX);
			colunaDescricao.setMinWidth(400);
			colunaDescricao.setPreferredWidth(2000);
			colunaDescricao.setMaxWidth(2000);
			
			TableColumn colunaUnidade = tabelaPlotagens.getColumnModel().getColumn(PlotagensTableModel.UNIDADE_INDEX);
			colunaUnidade.setMinWidth(60);
			colunaUnidade.setPreferredWidth(60);
			colunaUnidade.setMaxWidth(60);
			
			TableColumn colunaValor = tabelaPlotagens.getColumnModel().getColumn(PlotagensTableModel.VALOR_INDEX);
			colunaValor.setCellEditor(new DefaultCellEditor(getJtfValorTabela()));
			colunaValor.setMinWidth(80);
			colunaValor.setPreferredWidth(80);
			colunaValor.setMaxWidth(80);
			
		}
		return tabelaPlotagens;
	}
	
	public JScrollPane getJspTabelaPlotagens() {
		if (jspTabelaPlotagens == null) {
			jspTabelaPlotagens = new JScrollPane(getTabelaPlotagens());
		}
		return jspTabelaPlotagens;
	}

	public Object[] getOrdemTabelaPlotagens() {
		if (ordemTabelaPlotagens == null) {
			ordemTabelaPlotagens = new Object[]{"id"};
		}
		return ordemTabelaPlotagens;
	}

	public void setOrdemTabelaPlotagens(Object[] newOrdemTabelaPlotagens) {
		this.ordemTabelaPlotagens = newOrdemTabelaPlotagens;
	}

	public boolean isCrescenteTabelaPlotagens() {
		return crescenteTabelaPlotagens;
	}

	public void setCrescenteTabelaPlotagens(boolean newCrescenteTabelaPlotagens) {
		this.crescenteTabelaPlotagens = newCrescenteTabelaPlotagens;
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

	public Plotagem getPlotagemSelecionada() {
		return plotagemSelecionada;
	}

	public void setPlotagemSelecionada(Plotagem newPlotagemSelecionada) {
		plotagemSelecionada = newPlotagemSelecionada;
	}

	public boolean isEditMode() {
		return editMode;
	}
	
	public BarraStatus getBarraStatus() {
		return barraStatus;
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			getJtfDescricao().setEditable(true);
			getJtfUnidade().setEditable(true);
			getJtfValor().setEditable(true);
			getTabelaPlotagens().setEnabled(false);
			getJtfDescricao().grabFocus();
		} else {
			getJtfDescricao().setEditable(false);
			getJtfUnidade().setEditable(false);
			getJtfValor().setEditable(false);
			getTabelaPlotagens().setEnabled(true);
			this.grabFocus();
		}
		this.editMode = editMode;
	}
	
	
}
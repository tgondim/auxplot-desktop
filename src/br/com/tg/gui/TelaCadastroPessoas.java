package br.com.tg.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;

import br.com.tg.entidades.Endereco;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.PessoaFisica;
import br.com.tg.entidades.PessoaJuridica;
import br.com.tg.entidades.StatusPessoa;
import br.com.tg.entidades.Telefone;
import br.com.tg.entidades.TipoTelefone;
import br.com.tg.entidades.TipoUsuario;
import br.com.tg.entidades.Usuario;
import br.com.tg.exceptions.PessoaInexistenteException;
import br.com.tg.exceptions.StatusPessoaInexistenteException;
import br.com.tg.exceptions.TipoPessoaInexistenteException;
import br.com.tg.gui.util.BarraCadastro;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.CalendarFormatter;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.gui.util.PessoasTableModel;
import br.com.tg.gui.util.Tela;
import br.com.tg.gui.util.TelefonesTableModel;
import br.com.tg.util.Conversor;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class TelaCadastroPessoas extends Tela {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);

	private JTabbedPane jtpPai;
	private JTabbedPane jtpEspecifico;

	private BarraCadastro barraCadastro;
	
	private BarraStatus barraStatus;

	private JPanel jpGenerico;
	private JPanel jpPessoa;
	private JPanel jpEndereco;
	private JPanel jpTelefones;
	private JPanel jpPessoaFisica;
	private JPanel jpPessoaJuridica;
	private JPanel jpUsuario;

	private JLabel jlId;
	private JLabel jlNome;
	private JLabel jlAtiva;
	private JLabel jlInativa;
	private JLabel jlSituacao;
	private JLabel jlTipoPessoa;
	private JLabel jlNomeFantasia;
	private JLabel jlCnpj;
	private JLabel jlDiaFatura;
	private JLabel jlDataAbertura;
	private JLabel jlCpf;
	private JLabel jlDataNascimento;
	private JLabel jlLogin;
	private JLabel jlSenha;
	private JLabel jlTipoUsuario;
	private JLabel jlLogradouro;
	private JLabel jlNumero;
	private JLabel jlBairro;
	private JLabel jlComplemento;
	private JLabel jlCidade;
	private JLabel jlUf;
	private JLabel jlCep;
	private JLabel jlEmail;
	
	private JButton jbExcluir;
	private JButton jbAdicionar;

	private JTextField jtfId;
	private JTextField jtfNome;
	private JTextField jtfNomeFantasia;
	private JTextField jtfLogin;
	private JTextField jtfLogradouro;
	private JTextField jtfComplemento;
	private JTextField jtfBairro;
	private JTextField jtfCidade;
	private JTextField jtfUf;
	private JTextField jtfEmail;
	private JTextField jtfNumero;
	
	private JPasswordField jpfSenha;

	private JFormattedTextField jtfCep;
	private JFormattedTextField jtfCnpj;
	private JFormattedTextField jtfDiaFatura;
	private JFormattedTextField jtfDataAbertura;
	private JFormattedTextField jtfCpf;
	private JFormattedTextField jtfDataNascimento;
	private JFormattedTextField jtfCodigoArea;
	private JFormattedTextField jtfNumeroTelefone;

	private ButtonGroup bgSituacao;
	private ButtonGroup bgTipoPessoa;

	private JRadioButton jrbAtiva;
	private JRadioButton jrbInativa;
	private JRadioButton jrbFisica;
	private JRadioButton jrbJuridica;
	private JRadioButton jrbUsuario;

	private JComboBox jcbTipoUsuario;
	private JComboBox jcbTipoTelefone;

	public static final String[] COLUNAS_TELEFONES = { "Tipo", "Área", "Número", "" };
	protected JTable tabelaTelefones;
	protected JScrollPane jspTabelaTelefones;
	protected TelefonesTableModel telefoneTableModel;

	public static final String[] COLUNAS_PESSOAS = { "Id", "Nome", "Tipo", "Logradouro", "Numero", "Bairro", "Cidade", "UF" };
	protected JTable tabelaPessoas;
	protected JScrollPane jspTabelaPessoas;
	protected PessoasTableModel pessoasTableModel;

	private Pessoa pessoaSelecionada;

	private String tabelaExcluirIcon;
	private String tabelaAdicionarIcon;
	
	private Object[] ordemTabelaPessoas;
	
	private boolean editMode;
	private boolean crescenteTabelaPessoas = true;
	
	public TelaCadastroPessoas() {
		super();
	}

	public TelaCadastroPessoas(String titulo, auxPlot newTelaPrincipal, JTabbedPane newPai, BarraStatus newBarraStatus) {
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
		tabelaExcluirIcon = prop.getProperty("tabelaExcluirIcon");
		tabelaAdicionarIcon = prop.getProperty("tabelaAdicionarIcon");
		setLayout(null);
		add(getBarraCadastro());
		add(getJpGenerico());
		add(getJtpEspecifico());
		add(getJspTabelaPessoas());
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
		atualizaTabelaPessoas(true);
		if (getTabelaPessoas().getRowCount() > 0 ) {
			setPessoaSelecionada(getPessoasTableModel().getList().get(0));
		}
		atualizaDadosTela();
		setEditMode(false);
		getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
		if (getJtpPai().getSelectedIndex() > 0) {
			getJtpPai().setSelectedIndex(getJtpPai().getSelectedIndex()+1);
		}
	}

	public void atualizaTabelaPessoas(boolean atualizaRestricoes) {
		getPessoasTableModel().clearTable();
		List<Pessoa> listaPessoas;
		try {
			listaPessoas = Fachada.obterInstancia().listarPessoas(
					(atualizaRestricoes ? getRestricoes() : new ArrayList<Object[]>()), 
					getOrdemTabelaPessoas(), 
					isCrescenteTabelaPessoas());
			if (listaPessoas != null) {
				for (Pessoa pessoa : listaPessoas) {
					getPessoasTableModel().addRow(pessoa);
				}
				getTabelaPessoas().repaint();
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
		if (!getJtfNome().getText().isEmpty()) {
			Object[] restr = { "nome", "%" + getJtfNome().getText() + "%", "ilike" };
			restricoes.add(restr);
		}
		if (getJrbAtiva().isSelected()) {
			StatusPessoa sp = new StatusPessoa();
			sp.setId(1);
			Object[] restr = { "statusPessoa", sp, "eq" };
			restricoes.add(restr);
		} else if (getJrbInativa().isSelected()) {
			StatusPessoa sp = new StatusPessoa();
			sp.setId(2);
			Object[] restr = { "statusPessoa", sp, "eq"};
			restricoes.add(restr);
		}
		if (getJrbFisica().isSelected()) {
			String cpf = Validador.unmask(getJtfCpf().getText()).trim();
			if ( !cpf.isEmpty() ) {
				Object[] restr = { "cpf", "%" + cpf + "%", "ilike" };
				restricoes.add(restr);
			}			
//			if (!Validador.unmask(getJtfDataNascimento().getText()).trim().isEmpty()) {
//				Object[] restr = { "dataNascimento", "%" + getJtfDataNascimento().getText() + "%", "like" };
//				restricoes.add(restr);
//			}
		} else if (getJrbJuridica().isSelected()) {
			if (!getJtfNomeFantasia().getText().isEmpty()) {
				Object[] restr = { "nomeFantasia", "%" + getJtfNomeFantasia().getText() + "%", "ilike" };
				restricoes.add(restr);
			}	
			String cnpj = Validador.unmask(getJtfCnpj().getText()).trim();
			if ( !cnpj.isEmpty() ) {
				Object[] restr = { "cnpj", "%" + cnpj + "%", "ilike" };
				restricoes.add(restr);
			}	
			if (!getJtfDiaFatura().getText().trim().isEmpty()) {
				Object[] restr = { "diaFatura", Short.parseShort(getJtfDiaFatura().getText().trim()), "eq" };
				restricoes.add(restr);
			}	
//			if (!Validador.unmask(getJtfDataAbertura().getText()).trim().isEmpty()) {
//				Object[] restr = { "dataAbertura", getJtfDataAbertura().getText(), "like" };
//				restricoes.add(restr);
//			}
		} else if (getJrbUsuario().isSelected()) {
			if (!getJtfLogin().getText().isEmpty()) {
				Object[] restr = { "login", "%" + getJtfLogin().getText() + "%", "ilike" };
				restricoes.add(restr);
			}	
			if (getJcbTipoUsuario().getSelectedIndex() != -1) {
				TipoUsuario tu = new TipoUsuario();
				tu.setId(getJcbTipoUsuario().getSelectedIndex()+1);
				Object[] restr = { "tipoUsuario", tu, "eq" };
				restricoes.add(restr);
			}	
		}
		return restricoes;
	}
	
	private void doResize() {
		getBarraCadastro().setBounds(00, 00, 500, 35);
		getJpGenerico().setBounds(10, 35, getSize().width - 20, 70);
		getJtpEspecifico().setBounds(00, 110, getSize().width, 170);
		getJspTabelaTelefones().setBounds((getSize().width - 300) / 2, 10, 270, 110);
		getJbAdicionar().setBounds((getSize().width - 300) / 2, 120, 20, 20);
		getJbExcluir().setBounds(((getSize().width - 300) / 2) + 20, 120, 20, 20);
		getJtfNome().setBounds(75, 40, getSize().width - 335, 20);
		getJlSituacao().setBounds(getSize().width - 200, 05, 90, 20);
		getJrbAtiva().setBounds(getSize().width - 200, 35, 80, 20);
		getJrbInativa().setBounds(getSize().width - 115, 35, 80, 20);
		getJpPessoaFisica().setBounds(115, 10, getSize().width - 130, 120);
		getJpPessoaJuridica().setBounds(115, 10, getSize().width - 130, 120);
		getJpUsuario().setBounds(115, 10, getSize().width - 130, 120);
		getJtfNomeFantasia().setBounds(140, 15, getSize().width - 310, 20);
		getJtfLogradouro().setBounds(110, 15, getSize().width - 155, 20);
		getJtfComplemento().setBounds(330, 45, getSize().width - 375, 20);
		getJtfEmail().setBounds(330, 105, getSize().width - 375, 20);
		getJtfCidade().setBounds(470, 75, getSize().width - 516, 20);
		getJspTabelaPessoas().setBounds(00, 280, getSize().width, getSize().height-280);
	}

	// carrega a tela com os dados do objeto pessoaSelecionada
	public void atualizaDadosTela() {
		if (getPessoaSelecionada() != null) {
			Pessoa pessoa;
			if (getPessoaSelecionada().getClass() == PessoaJuridica.class) {
				pessoa = (PessoaJuridica) getPessoaSelecionada();
				getJrbJuridica().setSelected(true);
				getJrbJuridica().setEnabled(true);
				getJrbFisica().setEnabled(false);
				getJrbUsuario().setEnabled(false);
			} else if (getPessoaSelecionada().getClass() == Usuario.class) {
				pessoa = (Usuario) getPessoaSelecionada();
				getJrbUsuario().setSelected(true);
				getJrbUsuario().setEnabled(true);
				getJrbJuridica().setEnabled(false);
				getJrbFisica().setEnabled(false);
			} else {
				pessoa = (PessoaFisica) getPessoaSelecionada();
				if (pessoa.getId() != null) {
					getJrbFisica().setSelected(true);
					getJrbFisica().setEnabled(true);
					getJrbJuridica().setEnabled(false);
					getJrbUsuario().setEnabled(false);
				}
			}
			getJtfId().setText(pessoa.getId() != null ? pessoa.getId().toString() : "");
			getJtfNome().setText(pessoa.getNome());
			int status = pessoa.getStatusPessoa() != null ? pessoa.getStatusPessoa().getId() : 0;
			if (status == 1) {
				getJrbAtiva().setSelected(true);
			} else if (status == 2) {
				getJrbInativa().setSelected(true);
			}
			if (pessoa.getClass() == PessoaFisica.class) {
				getJtfCpf().setText(((PessoaFisica) pessoa).getCpf());
				Calendar auxDataNascimento = ((PessoaFisica) pessoa).getDataNascimento();
				if (auxDataNascimento != null) {
					getJtfDataNascimento().setText(CalendarFormatter.formatDate(auxDataNascimento));
				} else {
					getJtfDataNascimento().setText("");
				}
			} else if (pessoa.getClass() == PessoaJuridica.class) {
				getJtfNomeFantasia().setText(
						((PessoaJuridica) pessoa).getNomeFantasia());
				getJtfCnpj().setText(((PessoaJuridica) pessoa).getCnpj());
				getJtfDiaFatura().setText(
						String.valueOf(((PessoaJuridica) pessoa).getDiaFatura()));
				Calendar auxDataAbertura = ((PessoaJuridica) pessoa)
				.getDataAbertura();
				if (auxDataAbertura != null) {
					getJtfDataAbertura().setText(CalendarFormatter.formatDate(auxDataAbertura));
				} else {
					getJtfDataAbertura().setText("");
				}
			} else if (pessoa.getClass() == Usuario.class) {
				getJtfLogin().setText(((Usuario) pessoa).getLogin());
				getJpfSenha().setText(((Usuario) pessoa).getSenha());
				getJcbTipoUsuario().setSelectedIndex(((Usuario)pessoa).getTipoUsuario().getId() - 1);
			}
			if (pessoa.getEndereco() != null) {
				getJtfLogradouro().setText(pessoa.getEndereco().getLogradouro());
				if (pessoa.getEndereco().getNumero() != null) {
					getJtfNumero().setText(pessoa.getEndereco().getNumero().toString());
				} else {
					getJtfNumero().setText("");	
				}
				getJtfComplemento().setText(pessoa.getEndereco().getComplemento());
				getJtfBairro().setText(pessoa.getEndereco().getBairro());
				getJtfCidade().setText(pessoa.getEndereco().getCidade());
				getJtfUf().setText(pessoa.getEndereco().getUf());
				getJtfCep().setText(pessoa.getEndereco().getCep());
				getJtfEmail().setText(pessoa.getEndereco().getEmail());
			}
			getTelefoneTableModel().clearTable();
			Set<Telefone> listaTelefones = pessoa.getTelefones(); 
			if (listaTelefones != null) {
				for (Telefone tel : listaTelefones) {
					getTelefoneTableModel().addRow(tel);
				}
				getTabelaTelefones().repaint();
			}
		}
	}

	// atualiza o objeto pessoaSelecionada com os dados da tela
	public void atualizaDadosObjeto() {
		//salvo o endereço antes de resetar a pessoaSelecionada para nao perder a referencia do id do endereco
		//dados do jpEndereco
		Endereco endereco = new Endereco();
		if (getBarraCadastro().getStatus() != BarraCadastro.ADICIONANDO) {
			endereco.setId(getPessoaSelecionada().getEndereco().getId());
		}
		endereco.setLogradouro(getJtfLogradouro().getText().trim());
		if (!getJtfNumero().getText().isEmpty()) {
			endereco.setNumero(Integer.parseInt(getJtfNumero().getText().trim()));
		}
		endereco.setComplemento(getJtfComplemento().getText().trim());
		endereco.setBairro(getJtfBairro().getText().trim());
		endereco.setCidade(getJtfCidade().getText().trim());
		endereco.setUf(getJtfUf().getText().trim());
		endereco.setCep(getJtfCep().getText());
		endereco.setEmail(getJtfEmail().getText().trim());
		//dados do jpEspecifico
		//pessoa fisica
		if (getJrbFisica().isSelected()) {
			setPessoaSelecionada(new PessoaFisica());
			((PessoaFisica) getPessoaSelecionada()).setCpf(Validador.unmask(getJtfCpf().getText()));
			try {
				((PessoaFisica) getPessoaSelecionada()).setDataNascimento(Conversor.stringToCalendar(getJtfDataNascimento().getText()));
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse da data de nascimento.");
			}
			try {
				getPessoaSelecionada().setTipoPessoa(
						Fachada.obterInstancia().procurarTipoPessoa(1));
			} catch (TipoPessoaInexistenteException e) {
				JOptionPane.showMessageDialog(
								this,
								"Tipo de pessoa inexistente.\n"
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
								" Atenção", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
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
			//pessoa juridica
		} else if (getJrbJuridica().isSelected()) {
			setPessoaSelecionada(new PessoaJuridica());
			((PessoaJuridica) getPessoaSelecionada()).setNomeFantasia(getJtfNomeFantasia().getText());
			((PessoaJuridica) getPessoaSelecionada()).setCnpj(Validador.unmask(getJtfCnpj().getText()));
			if (!getJtfDiaFatura().getText().trim().isEmpty()) {
				((PessoaJuridica) getPessoaSelecionada()).setDiaFatura(Short.parseShort(getJtfDiaFatura().getText().trim()));
			}
			try {
				((PessoaJuridica) getPessoaSelecionada()).setDataAbertura(Conversor.stringToCalendar(getJtfDataAbertura().getText()));
			} catch (Exception e) {
				//e.printStackTrace();
			}
			try {
				getPessoaSelecionada().setTipoPessoa(Fachada.obterInstancia().procurarTipoPessoa(2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Usuario
		} else if (getJrbUsuario().isSelected()) {
			setPessoaSelecionada(new Usuario());
			((Usuario) getPessoaSelecionada()).setLogin(getJtfLogin().getText());
			((Usuario) getPessoaSelecionada()).setSenha(String.copyValueOf(getJpfSenha().getPassword()));
			TipoUsuario tipoUsuario = (TipoUsuario)getJcbTipoUsuario().getSelectedItem();
			((Usuario) getPessoaSelecionada()).setTipoUsuario(tipoUsuario);
			try {
				getPessoaSelecionada().setTipoPessoa(Fachada.obterInstancia().procurarTipoPessoa(3));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		//dados do jpGenerico
		if (!getJtfId().getText().trim().isEmpty()) {
			getPessoaSelecionada().setId(Integer.parseInt(getJtfId().getText().trim()));
		}
		getPessoaSelecionada().setNome(getJtfNome().getText());
		StatusPessoa status = new StatusPessoa();
		try {
			if (getJrbAtiva().isSelected()) {
				status = Fachada.obterInstancia().procurarStatusPessoa(1);
			} else if (getJrbInativa().isSelected()) {
				status = Fachada.obterInstancia().procurarStatusPessoa(2);
			}
		} catch (StatusPessoaInexistenteException e) {
			JOptionPane.showMessageDialog(
					this,
					"Status de pessoa inexistente.\n"
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
					" Atenção", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
		endereco.setPessoaPai(getPessoaSelecionada());
		getPessoaSelecionada().setStatusPessoa(status);
		getPessoaSelecionada().setEndereco(endereco);
		Set<Telefone> telefones = getTelefoneTableModel().getList();
		for (Telefone tel : telefones) {
			tel.setPessoaPai(getPessoaSelecionada());
		}
		getPessoaSelecionada().setTelefones(telefones);
	}

	public void limpaCampos() {
		getJtfId().setText("");
		getJtfNome().setText("");
		getJtfNomeFantasia().setText("");
		getJtfLogin().setText("");
		getJpfSenha().setText("");
		getJcbTipoUsuario().setSelectedIndex(-1);
		getJtfLogradouro().setText("");
		getJtfComplemento().setText("");
		getJtfBairro().setText("");
		getJtfCidade().setText("");
		getJtfUf().setText("");
		getJtfEmail().setText("");
		getJtfNumero().setText("");
		getJtfCep().setText("");
		getJtfCnpj().setText("");
		getJtfDiaFatura().setText("");
		getJtfDataAbertura().setText("");
		getJtfCpf().setText("");
		getJtfDataNascimento().setText("");
		getTelefoneTableModel().clearTable();
		getTabelaTelefones().repaint();
		getJrbFisica().setEnabled(true);
		getJrbJuridica().setEnabled(true);
		getJrbUsuario().setEnabled(true);
		getJrbFisica().setSelected(true);
		getJrbAtiva().setSelected(true);
		getBgSituacao().clearSelection();
		getBgTipoPessoa().clearSelection();
	}

	public JPanel getJpGenerico() {
		if (jpGenerico == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpGenerico = new JPanel();
			jpGenerico.setBorder(etched);
			jpGenerico.setLayout(null);
			jpGenerico.setFocusable(false);
			jpGenerico.add(getJlId());
			getJlId().setBounds(25, 10, 30, 20);
			jpGenerico.add(getJtfId());
			getJtfId().setBounds(75, 10, 50, 20);
			jpGenerico.add(getJlNome());
			getJlNome().setBounds(25, 40, 40, 20);
			jpGenerico.add(getJtfNome());
			jpGenerico.add(getJlSituacao());
			jpGenerico.add(getJrbAtiva());
			jpGenerico.add(getJrbInativa());
			getBgSituacao();
		}
		return jpGenerico;
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
						getTelaPrincipal().killTelaPessoas();
					} else if (acao.equals("confirmar")) {
						atualizaTabelaPessoas(true);
						if (getTabelaPessoas().getRowCount() > 0 ) {
							setPessoaSelecionada(getPessoasTableModel().getList().get(0));
						} else {
							setPessoaSelecionada(null);
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
							if (getJtfNome().getText().length() == 0) {
								getBarraStatus().setMensagem("O Nome deve ser informado.", true);
								Geral.alterarCor(getJtfNome(), Color.red, true);
								getJtfNome().grabFocus();
								validador = false;
							}
							if (getJrbUsuario().isSelected()) {
								if (validador && getJtfLogin().getText().length() == 0) {
									getBarraStatus().setMensagem("O Login deve ser informado.", true);
									Geral.alterarCor(getJtfLogin(), Color.red, true);
									getJtfLogin().grabFocus();
									validador = false;
								}
								if (validador && getJpfSenha().getPassword().length == 0) {
									getBarraStatus().setMensagem("A senha deve ser informada.", true);
									Geral.alterarCor(getJpfSenha(), Color.red, true);
									getJpfSenha().grabFocus();
									validador = false;
								}
								if (validador && getJcbTipoUsuario().getSelectedIndex() == -1) {
									getBarraStatus().setMensagem("O Tipo de usuário deve ser informado.", true);
									Geral.alterarCor(getJcbTipoUsuario(), Color.red, true);
									getJcbTipoUsuario().grabFocus();
									validador = false;
								}
							}
							if (validador) {
								int linhaSelecionada = getPessoasTableModel().getRow(getPessoaSelecionada());
								atualizaDadosObjeto();
								try {
									if (getPessoaSelecionada().getId() == null) {
										getPessoaSelecionada().setUsuarioCadastro(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPessoaSelecionada().setDataCadastro(Calendar.getInstance());
										Fachada.obterInstancia().cadastrarPessoa(getPessoaSelecionada());
									} else {
										getPessoaSelecionada().setUsuarioAlteracao(getTelaPrincipal().getLogonAtivo().getUsuarioLogado());
										getPessoaSelecionada().setDataAlteracao(Calendar.getInstance());
										Fachada.obterInstancia().atualizarPessoa(getPessoaSelecionada());
									}
								} catch (PessoaInexistenteException e1) {
									JOptionPane.showMessageDialog(
											TelaCadastroPessoas.this,
											"Pessoa inexistente.\n"
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
											" Atenção", JOptionPane.ERROR_MESSAGE);
									e1.printStackTrace();
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(TelaCadastroPessoas.this, 
											"Ocorreu um erro inesperado.\n" 
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
											+ e2.getMessage() + "\n" + e2.getStackTrace(), 
											" Atenção", 
											JOptionPane.ERROR_MESSAGE);
									e2.printStackTrace();
									getTelaPrincipal().sairSistema(false);

								} 
								limpaCampos();
								atualizaTabelaPessoas(true);
								getTabelaPessoas().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
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
							int linhaSelecionada = getPessoasTableModel().getRow(getPessoaSelecionada());
							setEditMode(false);
							getBarraCadastro().mudarStatus(BarraCadastro.NAVEGANDO);
							limpaCampos();
							getTabelaPessoas().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
							atualizaDadosTela();
						}
					} else if (acao.equals("adicionar")) {
						if (!isEditMode()) {
							getTabelaPessoas().getSelectionModel().clearSelection();
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.ADICIONANDO);
							limpaCampos();
							getJrbAtiva().setSelected(true);
							getJrbFisica().setSelected(true);
						}
					} else if (acao.equals("editar")) {
						if (!isEditMode() && (getPessoaSelecionada() != null)) {
							setEditMode(true);
							getBarraCadastro().mudarStatus(BarraCadastro.EDITANDO);
						}
					} else if (acao.equals("remover")) {
						if (getPessoaSelecionada().getId() != null && !isEditMode()) {
							int linhaSelecionada = getPessoasTableModel().getRow(getPessoaSelecionada());
							if (linhaSelecionada != -1) {
								Object[] mensagem = { "Confirma exclusão do registro?" };
								int returnCode = JOptionPane.showConfirmDialog(
										getParent(), mensagem, "Atenção!",
										JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								if (returnCode == JOptionPane.OK_OPTION) {
									try {
										Fachada.obterInstancia().descadastrarPessoa(getPessoaSelecionada());
									} catch (PessoaInexistenteException e1) {
										JOptionPane.showMessageDialog(
												TelaCadastroPessoas.this,
												"Tipo de pessoa inexistente.\n"
												+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
												" Atenção", JOptionPane.ERROR_MESSAGE);
										e1.printStackTrace();
									} catch (Exception e2) {
										JOptionPane.showMessageDialog(TelaCadastroPessoas.this, 
												"Ocorreu um erro inesperado.\n" 
												+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
												+ e2.getMessage() + "\n" + e2.getStackTrace(), 
												" Atenção", 
												JOptionPane.ERROR_MESSAGE);
										e2.printStackTrace();
										getTelaPrincipal().sairSistema(false);
									} 
									limpaCampos();
									atualizaTabelaPessoas(true);
									getTabelaPessoas().getSelectionModel().setSelectionInterval(linhaSelecionada, linhaSelecionada);
								}
							} 
						}
					} else if (acao.equals("navegarAcima")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPessoaSelecionada() != null) {
								linhaSelecionada = getPessoasTableModel().getRow(getPessoaSelecionada());
							}
							if (linhaSelecionada > 0 ) {
								getTabelaPessoas().getSelectionModel().setSelectionInterval( linhaSelecionada-1, linhaSelecionada-1 );
								setPessoaSelecionada(getPessoasTableModel().getList().get(getTabelaPessoas().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					} else if (acao.equals("navegarAbaixo")) {
						if (!isEditMode()) {
							int linhaSelecionada = 0;
							if (getPessoaSelecionada() != null) {
								linhaSelecionada = getPessoasTableModel().getRow(getPessoaSelecionada());
							}
							if (linhaSelecionada < getPessoasTableModel().getRowCount()-1) {
								getTabelaPessoas().getSelectionModel().setSelectionInterval( linhaSelecionada+1, linhaSelecionada+1 );
								setPessoaSelecionada(getPessoasTableModel().getList().get(getTabelaPessoas().getSelectedRow()));
								atualizaDadosTela();
							}
						}
					}
				}
			});
		}
		return barraCadastro;
	}

	public JTabbedPane getJtpEspecifico() {
		if (jtpEspecifico == null) {
			jtpEspecifico = new JTabbedPane(SwingConstants.TOP);
			jtpEspecifico.setVisible(true);
			jtpEspecifico.addTab("Pessoa", getJpPessoa());
			jtpEspecifico.addTab("Endereco", getJpEndereco());
			jtpEspecifico.addTab("Telefones", getJpTelefones());
		}
		return jtpEspecifico;
	}

	public JPanel getJpPessoa() {
		if (jpPessoa == null) {
			jpPessoa = new JPanel();
			jpPessoa.setLayout(null);
			jpPessoa.setFocusable(false);
			jpPessoa.add(getJlTipoPessoa());
			getJlTipoPessoa().setBounds(5, 10, 90, 20);
			jpPessoa.add(getJrbFisica());
			getJrbFisica().setBounds(15, 40, 80, 20);
			jpPessoa.add(getJrbJuridica());
			getJrbJuridica().setBounds(15, 70, 80, 20);
			jpPessoa.add(getJrbUsuario());
			getJrbUsuario().setBounds(15, 100, 80, 20);
			jpPessoa.add(getJpPessoaFisica());
			jpPessoa.add(getJpPessoaJuridica());
			jpPessoa.add(getJpUsuario());
			getJpPessoaFisica().setVisible(true);
			getJpPessoaJuridica().setVisible(false);
			getJpUsuario().setVisible(false);
			getBgTipoPessoa();
			ChangeListener clTipoPessoa = new ChangeListener() {
				public void stateChanged(ChangeEvent changeEvent) {
					AbstractButton aButton = (AbstractButton) changeEvent
							.getSource();
					if (aButton.getText().equals("Física")
							&& aButton.isSelected()) {
						getJpPessoaFisica().setVisible(true);
						getJpPessoaJuridica().setVisible(false);
						getJpUsuario().setVisible(false);
					} else if (aButton.getText().equals("Jurídica")
							&& aButton.isSelected()) {
						getJpPessoaFisica().setVisible(false);
						getJpPessoaJuridica().setVisible(true);
						getJpUsuario().setVisible(false);
					} else if (aButton.getText().equals("Usuário")
							&& aButton.isSelected()) {
						getJpPessoaFisica().setVisible(false);
						getJpPessoaJuridica().setVisible(false);
						getJpUsuario().setVisible(true);
					}
				}
			};
			getJrbFisica().addChangeListener(clTipoPessoa);
			getJrbJuridica().addChangeListener(clTipoPessoa);
			getJrbUsuario().addChangeListener(clTipoPessoa);
		}
		return jpPessoa;
	}

	public JPanel getJpEndereco() {
		if (jpEndereco == null) {
			jpEndereco = new JPanel();
			jpEndereco.setLayout(null);
			jpEndereco.setFocusable(false);
			jpEndereco.add(getJlLogradouro());
			getJlLogradouro().setBounds(00, 15, 100, 20);
			jpEndereco.add(getJtfLogradouro());
			jpEndereco.add(getJlNumero());
			getJlNumero().setBounds(00, 45, 100, 20);
			jpEndereco.add(getJtfNumero());
			getJtfNumero().setBounds(110, 45, 40, 20);
			jpEndereco.add(getJlComplemento());
			getJlComplemento().setBounds(220, 45, 100, 20);
			jpEndereco.add(getJtfComplemento());
			jpEndereco.add(getJlBairro());
			getJlBairro().setBounds(00, 75, 100, 20);
			jpEndereco.add(getJtfBairro());
			getJtfBairro().setBounds(110, 75, 280, 20);
			jpEndereco.add(getJlCidade());
			getJlCidade().setBounds(410, 75, 50, 20);
			jpEndereco.add(getJtfCidade());
			jpEndereco.add(getJlUf());
			getJlUf().setBounds(00, 105, 100, 20);
			jpEndereco.add(getJtfUf());
			getJtfUf().setBounds(110, 105, 25, 20);
			jpEndereco.add(getJlCep());
			getJlCep().setBounds(125, 105, 50, 20);
			jpEndereco.add(getJtfCep());
			getJtfCep().setBounds(185, 105, 65, 20);
			jpEndereco.add(getJlEmail());
			getJlEmail().setBounds(267, 105, 50, 20);
			jpEndereco.add(getJtfEmail());
		}
		return jpEndereco;
	}

	public JPanel getJpTelefones() {
		if (jpTelefones == null) {
			jpTelefones = new JPanel();
			jpTelefones.setLayout(null);
			jpTelefones.setFocusable(false);
			jpTelefones.add(getJspTabelaTelefones());
			jpTelefones.add(getJbAdicionar());
			jpTelefones.add(getJbExcluir());

		}
		return jpTelefones;
	}

	public JPanel getJpPessoaFisica() {
		if (jpPessoaFisica == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpPessoaFisica = new JPanel();
			jpPessoaFisica.setBorder(etched);
			jpPessoaFisica.setLayout(null);
			jpPessoaFisica.setFocusable(false);
			jpPessoaFisica.add(getJlCpf());
			getJlCpf().setBounds(20, 35, 120, 20);
			jpPessoaFisica.add(getJtfCpf());
			getJtfCpf().setBounds(150, 35, 100, 20);
			jpPessoaFisica.add(getJlDataNascimento());
			getJlDataNascimento().setBounds(20, 65, 120, 20);
			jpPessoaFisica.add(getJtfDataNascimento());
			getJtfDataNascimento().setBounds(150, 65, 70, 20);
		}
		return jpPessoaFisica;
	}

	public JPanel getJpPessoaJuridica() {
		if (jpPessoaJuridica == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpPessoaJuridica = new JPanel();
			jpPessoaJuridica.setBorder(etched);
			jpPessoaJuridica.setLayout(null);
			jpPessoaJuridica.setFocusable(false);
			jpPessoaJuridica.add(getJlNomeFantasia());
			getJlNomeFantasia().setBounds(20, 15, 110, 20);
			jpPessoaJuridica.add(getJtfNomeFantasia());
			jpPessoaJuridica.add(getJlCnpj());
			getJlCnpj().setBounds(20, 45, 110, 20);
			jpPessoaJuridica.add(getJtfCnpj());
			getJtfCnpj().setBounds(140, 45, 120, 20);
			jpPessoaJuridica.add(getJlDiaFatura());
			getJlDiaFatura().setBounds(20, 75, 110, 20);
			jpPessoaJuridica.add(getJtfDiaFatura());
			getJtfDiaFatura().setBounds(140, 75, 20, 20);
			jpPessoaJuridica.add(getJlDataAbertura());
			getJlDataAbertura().setBounds(260, 75, 110, 20);
			jpPessoaJuridica.add(getJtfDataAbertura());
			getJtfDataAbertura().setBounds(380, 75, 70, 20);
		}
		return jpPessoaJuridica;
	}

	public JPanel getJpUsuario() {
		if (jpUsuario == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpUsuario = new JPanel();
			jpUsuario.setBorder(etched);
			jpUsuario.setLayout(null);
			jpUsuario.setFocusable(false);
			jpUsuario.add(getJlLogin());
			getJlLogin().setBounds(20, 15, 110, 20);
			jpUsuario.add(getJtfLogin());
			getJtfLogin().setBounds(140, 15, 165, 20);
			jpUsuario.add(getJlSenha());
			getJlSenha().setBounds(20, 45, 110, 20);
			jpUsuario.add(getJpfSenha());
			getJpfSenha().setBounds(140, 45, 120, 20);
			jpUsuario.add(getJlTipoUsuario());
			getJlTipoUsuario().setBounds(20, 75, 110, 20);
			jpUsuario.add(getJcbTipoUsuario());
			getJcbTipoUsuario().setBounds(140, 75, 150, 20);
		}
		return jpUsuario;
	}

	public JTextField getJtfId() {
		if (jtfId == null) {
			jtfId = new JTextField();
			jtfId.setBorder(BORDAS_TEXT_FIELD);
			jtfId.setEditable(false);
		}
		return jtfId;
	}

	public JTextField getJtfNome() {
		if (jtfNome == null) {
			jtfNome = new JTextField();
			jtfNome.setBorder(BORDAS_TEXT_FIELD);
			jtfNome.setDocument(new FixedLengthDocument(120, false, false));
		}
		return jtfNome;
	}

	public JRadioButton getJrbAtiva() {
		if (jrbAtiva == null) {
			jrbAtiva = new JRadioButton("Ativa");
			jrbAtiva.setMnemonic(KeyEvent.VK_A);
		}
		return jrbAtiva;
	}

	public JRadioButton getJrbInativa() {
		if (jrbInativa == null) {
			jrbInativa = new JRadioButton("Inativa");
			jrbInativa.setMnemonic(KeyEvent.VK_I);
		}
		return jrbInativa;
	}

	public ButtonGroup getBgSituacao() {
		if (bgSituacao == null) {
			bgSituacao = new ButtonGroup();
			bgSituacao.add(getJrbAtiva());
			bgSituacao.add(getJrbInativa());
		}
		return bgSituacao;
	}

	public JTextField getJtfNomeFantasia() {
		if (jtfNomeFantasia == null) {
			jtfNomeFantasia = new JTextField();
			jtfNomeFantasia.setBorder(BORDAS_TEXT_FIELD);
			jtfNomeFantasia.setDocument(new FixedLengthDocument(100, false, false));
		}
		return jtfNomeFantasia;
	}

	public JTextField getJtfLogin() {
		if (jtfLogin == null) {
			jtfLogin = new JTextField();
			jtfLogin.setBorder(BORDAS_TEXT_FIELD);
			jtfLogin.setDocument(new FixedLengthDocument(20, true, false));
		}
		return jtfLogin;
	}

	public JPasswordField getJpfSenha() {
		if (jpfSenha == null) {
			jpfSenha = new JPasswordField();
			jpfSenha.setBorder(BORDAS_TEXT_FIELD);
			jpfSenha.setDocument(new FixedLengthDocument(16, false, false));
		}
		return jpfSenha;
	}

	public JFormattedTextField getJtfCnpj() {
		if (jtfCnpj == null) {
			jtfCnpj = new JFormattedTextField();
			jtfCnpj.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskCnpj = new MaskFormatter("##.###.###/####-##");
				jtfCnpj = new JFormattedTextField(maskCnpj);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do cnpj.");
			}
			jtfCnpj.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfCnpj.getText().equals(".   .   /    -")
							&& !jtfCnpj.getText().equals("  .   .   /    -  ")) {
						if (!Validador.validaCNPJ(jtfCnpj.getText())) {
							getBarraStatus().setMensagem("CNPJ inválido.", true);
							Geral.alterarCor(getJtfCnpj(), Color.red, true);
							jtfCnpj.grabFocus();
						}
					} else {
						jtfCnpj.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfCnpj;
	}

	public JFormattedTextField getJtfDiaFatura() {
		if (jtfDiaFatura == null) {
			jtfDiaFatura = new JFormattedTextField();
			jtfDiaFatura.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDiaFatura = new MaskFormatter("##");
				jtfDiaFatura = new JFormattedTextField(maskDiaFatura);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do dia da fatura.");
			}
			jtfDiaFatura.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDiaFatura.getText().trim().equals("")) {
						int dia = Integer.parseInt(jtfDiaFatura.getText().trim());
						if ((dia < 1) || (dia > 31)) {
							getBarraStatus().setMensagem("Dia da fatura inválido.", true);
							Geral.alterarCor(getJtfDiaFatura(), Color.red, true);
							jtfDiaFatura.grabFocus();
						}
						NumberFormat nf = new DecimalFormat("0");
						nf.setMinimumIntegerDigits(2);
						jtfDiaFatura.setText(nf.format(dia));
					} else {
						jtfDiaFatura.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfDiaFatura;
	}

	public JFormattedTextField getJtfDataAbertura() {
		if (jtfDataAbertura == null) {
			jtfDataAbertura = new JFormattedTextField();
			jtfDataAbertura.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataAbertura = new MaskFormatter("##/##/####");
				jtfDataAbertura = new JFormattedTextField(maskDataAbertura);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse da data de abertura.");
			}
			jtfDataAbertura.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataAbertura.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataAbertura.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(jtfDataAbertura, Color.red, true);
							jtfDataAbertura.grabFocus();
						}
					} else {
						jtfDataAbertura.setValue(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfDataAbertura;
	}

	public JFormattedTextField getJtfDataNascimento() {
		if (jtfDataNascimento == null) {
			jtfDataNascimento = new JFormattedTextField();
			jtfDataNascimento.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskDataNascimento = new MaskFormatter("##/##/####");
				jtfDataNascimento = new JFormattedTextField(maskDataNascimento);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse da data de nascimento.");
			}
			jtfDataNascimento.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfDataNascimento.getText().equals("  /  /    ")) {
						if (!Validador.validaData(jtfDataNascimento.getText())) {
							getBarraStatus().setMensagem("Data inválida.", true);
							Geral.alterarCor(getJtfDataNascimento(), Color.red, true);
							jtfDataNascimento.grabFocus();
						}
					} else {
						jtfDataNascimento.setText("");
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfDataNascimento;
	}

	public JFormattedTextField getJtfCpf() {
		if (jtfCpf == null) {
			jtfCpf = new JFormattedTextField();
			jtfCpf.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskCpf = new MaskFormatter("###.###.###-##");
				jtfCpf = new JFormattedTextField(maskCpf);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do cpf.");
			}
			jtfCpf.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (getBarraCadastro().getStatus() != BarraCadastro.BUSCANDO) {
						if (!jtfCpf.getText().equals(".   .   -") 
								&& !jtfCpf.getText().equals("   .   .   -  ")) {
							if (!Validador.validaCPF(jtfCpf.getText())) {
								getBarraStatus().setMensagem("CPF inválido.", true);
								Geral.alterarCor(getJtfCpf(), Color.red, true);
								jtfCpf.grabFocus();
							}
						} else {
							jtfCpf.setText("");
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		}
		return jtfCpf;
	}

	public JComboBox getJcbTipoUsuario() {
		if (jcbTipoUsuario == null) {
			try {
				Vector<TipoUsuario> listaTiposUsuarios = new Vector<TipoUsuario>(Fachada.obterInstancia().listarTiposUsuarios());
				jcbTipoUsuario = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaTiposUsuarios);
				jcbTipoUsuario.setModel(defaultComboBox);
				jcbTipoUsuario.setSelectedIndex(-1);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e1.getMessage() + "\n" + e1.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				getTelaPrincipal().sairSistema(false);
			}
		}
		return jcbTipoUsuario;
	}

	public JComboBox getJcbTipoTelefone() {
		if (jcbTipoTelefone == null) {
			try {
				Vector<TipoTelefone> listaTiposTelefones = new Vector<TipoTelefone>(Fachada.obterInstancia().listarTiposTelefones());
				jcbTipoTelefone = new JComboBox();
				DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(listaTiposTelefones);
				jcbTipoTelefone.setModel(defaultComboBox);
				jcbTipoTelefone.setSelectedIndex(-1);
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
		return jcbTipoTelefone;
	}

	public ButtonGroup getBgTipoPessoa() {
		if (bgTipoPessoa == null) {
			bgTipoPessoa = new ButtonGroup();
			bgTipoPessoa.add(getJrbFisica());
			bgTipoPessoa.add(getJrbJuridica());
			bgTipoPessoa.add(getJrbUsuario());
		}
		return bgTipoPessoa;
	}

	public JRadioButton getJrbFisica() {
		if (jrbFisica == null) {
			jrbFisica = new JRadioButton("Física");
			jrbFisica.setMnemonic(KeyEvent.VK_F);
			jrbFisica.setSelected(true);
			jrbFisica.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getJtfCpf().grabFocus();
				}
			});
		}
		return jrbFisica;
	}

	public JRadioButton getJrbJuridica() {
		if (jrbJuridica == null) {
			jrbJuridica = new JRadioButton("Jurídica");
			jrbJuridica.setMnemonic(KeyEvent.VK_J);
			jrbJuridica.setSelected(true);
			jrbJuridica.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getJtfNomeFantasia().grabFocus();
				}
			});
		}
		return jrbJuridica;
	}

	public JRadioButton getJrbUsuario() {
		if (jrbUsuario == null) {
			jrbUsuario = new JRadioButton("Usuário");
			jrbUsuario.setMnemonic(KeyEvent.VK_U);
			jrbUsuario.setSelected(true);
			jrbUsuario.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getJtfLogin().grabFocus();
				}
			});
		}
		return jrbUsuario;
	}

	public JTextField getJtfLogradouro() {
		if (jtfLogradouro == null) {
			jtfLogradouro = new JTextField();
			jtfLogradouro.setBorder(BORDAS_TEXT_FIELD);
			jtfLogradouro.setDocument(new FixedLengthDocument(100, false, false));
		}
		return jtfLogradouro;
	}

	public JTextField getJtfNumero() {
		if (jtfNumero == null) {
			jtfNumero = new JTextField();
			jtfNumero.setBorder(BORDAS_TEXT_FIELD);
			jtfNumero.setDocument(new FixedLengthDocument(100, false, true));
		}
		return jtfNumero;
	}

	public JTextField getJtfComplemento() {
		if (jtfComplemento == null) {
			jtfComplemento = new JTextField();
			jtfComplemento.setBorder(BORDAS_TEXT_FIELD);
			jtfComplemento.setDocument(new FixedLengthDocument(40, false, false));
		}
		return jtfComplemento;
	}

	public JTextField getJtfBairro() {
		if (jtfBairro == null) {
			jtfBairro = new JTextField();
			jtfBairro.setBorder(BORDAS_TEXT_FIELD);
			jtfBairro.setDocument(new FixedLengthDocument(40, false, false));
		}
		return jtfBairro;
	}

	public JTextField getJtfCidade() {
		if (jtfCidade == null) {
			jtfCidade = new JTextField();
			jtfCidade.setBorder(BORDAS_TEXT_FIELD);
			jtfCidade.setDocument(new FixedLengthDocument(40, false, false));
		}
		return jtfCidade;
	}

	public JTextField getJtfUf() {
		if (jtfUf == null) {
			jtfUf = new JTextField();
			jtfUf.setBorder(BORDAS_TEXT_FIELD);
			jtfUf.setDocument(new FixedLengthDocument(2, true, false));
		}
		return jtfUf;
	}

	public JTextField getJtfEmail() {
		if (jtfEmail == null) {
			jtfEmail = new JTextField();
			jtfEmail.setBorder(BORDAS_TEXT_FIELD);
			jtfEmail.setDocument(new FixedLengthDocument(100, false, false));
			jtfEmail.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (!jtfEmail.getText().trim().equals("")) {
						if (!Validador.validaEmail(jtfEmail.getText())) {
							getBarraStatus().setMensagem("E-mail inválido.", true);
							Geral.alterarCor(getJtfEmail(), Color.red, true);
							jtfEmail.grabFocus();
						}
					} else {
						jtfEmail.setText(null);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jtfEmail;
	}

	public JFormattedTextField getJtfCep() {
		if (jtfCep == null) {
			jtfCep = new JFormattedTextField();
			jtfCep.setBorder(BORDAS_TEXT_FIELD);
			try {
				MaskFormatter maskCep = new MaskFormatter("#####-###");
				jtfCep = new JFormattedTextField(maskCep);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do CEP.");
			}
		}
		return jtfCep;
	}

	public JFormattedTextField getJtfCodigoArea() {
		if (jtfCodigoArea == null) {
			try {
				MaskFormatter maskCodigoArea = new MaskFormatter("##");
				jtfCodigoArea = new JFormattedTextField(maskCodigoArea);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do código de área.");
			}	
		}
		return jtfCodigoArea;
	}

	public JFormattedTextField getJtfNumeroTelefone() {
		if (jtfNumeroTelefone == null) {
			try {
				MaskFormatter maskNumeroTelefone = new MaskFormatter("####-####");
				jtfNumeroTelefone = new JFormattedTextField(maskNumeroTelefone);
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse do telefone.");
			}	
		}
		return jtfNumeroTelefone;
	}

	public JLabel getJlId() {
		if (jlId == null) {
			jlId = new JLabel("Id", JLabel.RIGHT);
		}
		return jlId;
	}

	public JLabel getJlNome() {
		if (jlNome == null) {
			jlNome = new JLabel("Nome", JLabel.RIGHT);
		}
		return jlNome;
	}

	public JLabel getJlSituacao() {
		if (jlSituacao == null) {
			jlSituacao = new JLabel("Situação", JLabel.LEFT);
		}
		return jlSituacao;
	}

	public JLabel getJlAtiva() {
		if (jlAtiva == null) {
			jlAtiva = new JLabel("Ativa", JLabel.RIGHT);
		}
		return jlAtiva;
	}

	public JLabel getJlInativa() {
		if (jlInativa == null) {
			jlInativa = new JLabel("Inativa", JLabel.RIGHT);
		}
		return jlInativa;
	}

	public JLabel getJlTipoPessoa() {
		if (jlTipoPessoa == null) {
			jlTipoPessoa = new JLabel("Tipo de Pessoa", JLabel.RIGHT);
		}
		return jlTipoPessoa;
	}

	public JLabel getJlNomeFantasia() {
		if (jlNomeFantasia == null) {
			jlNomeFantasia = new JLabel("Nome de Fantasia", JLabel.RIGHT);
		}
		return jlNomeFantasia;
	}

	public JLabel getJlCnpj() {
		if (jlCnpj == null) {
			jlCnpj = new JLabel("CNPJ", JLabel.RIGHT);
		}
		return jlCnpj;
	}

	public JLabel getJlDiaFatura() {
		if (jlDiaFatura == null) {
			jlDiaFatura = new JLabel("Dia da Fatura", JLabel.RIGHT);
		}
		return jlDiaFatura;
	}

	public JLabel getJlDataAbertura() {
		if (jlDataAbertura == null) {
			jlDataAbertura = new JLabel("Data de Abertura", JLabel.RIGHT);
		}
		return jlDataAbertura;
	}

	public JLabel getJlCpf() {
		if (jlCpf == null) {
			jlCpf = new JLabel("CPF", JLabel.RIGHT);
		}
		return jlCpf;
	}

	public JLabel getJlLogin() {
		if (jlLogin == null) {
			jlLogin = new JLabel("Login", JLabel.RIGHT);
		}
		return jlLogin;
	}

	public JLabel getJlSenha() {
		if (jlSenha == null) {
			jlSenha = new JLabel("Senha", JLabel.RIGHT);
		}
		return jlSenha;
	}

	public JLabel getJlTipoUsuario() {
		if (jlTipoUsuario == null) {
			jlTipoUsuario = new JLabel("Tipo de Usuario", JLabel.RIGHT);
		}
		return jlTipoUsuario;
	}

	public JLabel getJlDataNascimento() {
		if (jlDataNascimento == null) {
			jlDataNascimento = new JLabel("Data de Nascimento", JLabel.RIGHT);
		}
		return jlDataNascimento;
	}

	public JLabel getJlLogradouro() {
		if (jlLogradouro == null) {
			jlLogradouro = new JLabel("Logradouro", JLabel.RIGHT);
		}
		return jlLogradouro;
	}

	public JLabel getJlNumero() {
		if (jlNumero == null) {
			jlNumero = new JLabel("Numero", JLabel.RIGHT);
		}
		return jlNumero;
	}

	public JLabel getJlComplemento() {
		if (jlComplemento == null) {
			jlComplemento = new JLabel("Complemento", JLabel.RIGHT);
		}
		return jlComplemento;
	}

	public JLabel getJlBairro() {
		if (jlBairro == null) {
			jlBairro = new JLabel("Bairro", JLabel.RIGHT);
		}
		return jlBairro;
	}

	public JLabel getJlCidade() {
		if (jlCidade == null) {
			jlCidade = new JLabel("Cidade", JLabel.RIGHT);
		}
		return jlCidade;
	}

	public JLabel getJlUf() {
		if (jlUf == null) {
			jlUf = new JLabel("Estado", JLabel.RIGHT);
		}
		return jlUf;
	}

	JLabel getJlEmail() {
		if (jlEmail == null) {
			jlEmail = new JLabel("E-mail", JLabel.RIGHT);
		}
		return jlEmail;
	}

	public JLabel getJlCep() {
		if (jlCep == null) {
			jlCep = new JLabel("CEP", JLabel.RIGHT);
		}
		return jlCep;
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
					if (getTelefoneTableModel().getRowCount() > 0) {
						jbExcluir.grabFocus();
						int linha;
						if ((linha = getTabelaTelefones().getSelectedRow()) != 0) {
							getTabelaTelefones().setRowSelectionInterval(linha - 1, linha -1);
						}
						int row = getTabelaTelefones().getSelectedRow();
						if (row >= 0) {
							getTelefoneTableModel().removeRow(row);
							getTabelaTelefones().repaint();						
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
					getTelefoneTableModel().addEmptyRow();
				}
			});
		}
		return jbAdicionar;
	}

	public PessoasTableModel getPessoasTableModel() {
		if (pessoasTableModel == null) {
			pessoasTableModel = new PessoasTableModel(COLUNAS_PESSOAS);
			pessoasTableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaPessoas().setColumnSelectionInterval(column + 1, column + 1);
						getTabelaPessoas().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return pessoasTableModel;
	}

	public JTable getTabelaPessoas() {
		if (tabelaPessoas == null) {
			tabelaPessoas = new JTable();
			tabelaPessoas.setModel(getPessoasTableModel());
			tabelaPessoas.setSurrendersFocusOnKeystroke(true);
			tabelaPessoas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			tabelaPessoas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabelaPessoas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
//					if (e.getValueIsAdjusting()) {
						if (tabelaPessoas.getSelectedRow() != -1 
								&& tabelaPessoas.getRowCount() > 0 
								&& tabelaPessoas.getSelectedRow() < getPessoasTableModel().getList().size()) {
							setPessoaSelecionada(getPessoasTableModel().getList().get(getTabelaPessoas().getSelectedRow()));
						}
						atualizaDadosTela();
//					}
				}
			});
			
			tabelaPessoas.getTableHeader().addMouseListener(new MouseListener() {
				
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
						setOrdemTabelaPessoas(new Object[]{"id"});
						break;
					case 1:
						colunaValida = true;
						setOrdemTabelaPessoas(new Object[]{"nome"});
						break;
					case 2:
						colunaValida = true;
						setOrdemTabelaPessoas(new Object[]{"tipoPessoa"});
						break;
					}
					if (colunaValida) {
						if (isCrescenteTabelaPessoas()) {
							setCrescenteTabelaPessoas(false);
						} else {
							setCrescenteTabelaPessoas(true);
						}
						atualizaTabelaPessoas(false);
					}
				}
			});
			
			DefaultTableCellRenderer alinhamentoCentro = new DefaultTableCellRenderer();
			alinhamentoCentro.setHorizontalAlignment(SwingConstants.CENTER);

			TableColumn colunaId = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.ID_INDEX);
			colunaId.setMinWidth(50);
			colunaId.setPreferredWidth(50);
			colunaId.setMaxWidth(50);
			colunaId.setCellRenderer(alinhamentoCentro);
			
			TableColumn colunaNome = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.NOME_INDEX);
			colunaNome.setMinWidth(200);
			colunaNome.setPreferredWidth(600);
			colunaNome.setMaxWidth(600);
			
			TableColumn colunaTipoPessoa = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.TIPO_PESSOA_INDEX);
			colunaTipoPessoa.setMinWidth(60);
			colunaTipoPessoa.setPreferredWidth(60);
			colunaTipoPessoa.setMaxWidth(60);
			
			TableColumn colunaLogradouro = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.LOGRADOURO_INDEX);
			colunaLogradouro.setMinWidth(200);
			colunaLogradouro.setPreferredWidth(400);
			colunaLogradouro.setMaxWidth(400);
			
			TableColumn colunaNumero = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.NUMERO_INDEX);
			colunaNumero.setMinWidth(45);
			colunaNumero.setPreferredWidth(45);
			colunaNumero.setMaxWidth(45);
			
			TableColumn colunaBairro = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.BAIRRO_INDEX);
			colunaBairro.setMinWidth(95);
			colunaBairro.setPreferredWidth(220);
			colunaBairro.setMaxWidth(220);
			
			TableColumn colunaCidade = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.CIDADE_INDEX);
			colunaCidade.setMinWidth(95);
			colunaCidade.setPreferredWidth(220);
			colunaCidade.setMaxWidth(220);
			
			TableColumn colunaUf = tabelaPessoas.getColumnModel().getColumn(PessoasTableModel.UF_INDEX);
			colunaUf.setMinWidth(30);
			colunaUf.setPreferredWidth(30);
			colunaUf.setMaxWidth(30);
		}
		return tabelaPessoas;
	}
	
	public JScrollPane getJspTabelaPessoas() {
		if (jspTabelaPessoas == null) {
			jspTabelaPessoas = new JScrollPane(getTabelaPessoas());
		}
		return jspTabelaPessoas;
	}

	public Object[] getOrdemTabelaPessoas() {
		if (ordemTabelaPessoas == null) {
			ordemTabelaPessoas = new Object[]{"id"};
		}
		return ordemTabelaPessoas;
	}

	public void setOrdemTabelaPessoas(Object[] ordemTabelaPessoas) {
		this.ordemTabelaPessoas = ordemTabelaPessoas;
	}

	public boolean isCrescenteTabelaPessoas() {
		return crescenteTabelaPessoas;
	}

	public void setCrescenteTabelaPessoas(boolean crescenteTabelaPessoas) {
		this.crescenteTabelaPessoas = crescenteTabelaPessoas;
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
	
	public TelefonesTableModel getTelefoneTableModel() {
		if (telefoneTableModel == null) {
			telefoneTableModel = new TelefonesTableModel(COLUNAS_TELEFONES);
			telefoneTableModel.addTableModelListener(new TableModelListener() {

				@Override
				public void tableChanged(TableModelEvent evt) {
					if (evt.getType() == TableModelEvent.UPDATE) {
						int column = evt.getColumn();
						int row = evt.getFirstRow();
						getTabelaTelefones().setColumnSelectionInterval(
								column + 1, column + 1);
						getTabelaTelefones().setRowSelectionInterval(row, row);
					}
				}
			});
		}
		return telefoneTableModel;
	}

	public JTable getTabelaTelefones() {
		if (tabelaTelefones == null) {
			tabelaTelefones = new JTable();
			tabelaTelefones.setModel(getTelefoneTableModel());
			tabelaTelefones.setSurrendersFocusOnKeystroke(true);
			tabelaTelefones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
			
			//{ "Tipo", "Área", "Número", "" }
			TableColumn colunaTipo = getTabelaTelefones().getColumnModel().getColumn(TelefonesTableModel.TIPO_TELEFONE_INDEX);
			colunaTipo.setCellEditor(new DefaultCellEditor(getJcbTipoTelefone()));
			colunaTipo.setMinWidth(160);
			colunaTipo.setPreferredWidth(160);
			colunaTipo.setMaxWidth(160);
			
			TableColumn colunaCodigoArea = getTabelaTelefones().getColumnModel().getColumn(TelefonesTableModel.CODIGO_AREA_INDEX);			
			colunaCodigoArea.setCellEditor(new DefaultCellEditor(getJtfCodigoArea()));
			colunaCodigoArea.setMinWidth(40);
			colunaCodigoArea.setPreferredWidth(40);
			colunaCodigoArea.setMaxWidth(40);
		
			TableColumn colunaNumero = getTabelaTelefones().getColumnModel().getColumn(TelefonesTableModel.NUMERO_INDEX);
			colunaNumero.setCellEditor(new DefaultCellEditor(getJtfNumeroTelefone()));
			colunaNumero.setMinWidth(75);
			colunaNumero.setPreferredWidth(75);
			colunaNumero.setMaxWidth(75);
		}
		return tabelaTelefones;
	}

	public JScrollPane getJspTabelaTelefones() {
		if (jspTabelaTelefones == null) {
			jspTabelaTelefones = new JScrollPane(getTabelaTelefones());
		}
		return jspTabelaTelefones;
	}

	public JTabbedPane getJtpPai() {
		return this.jtpPai;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa newPessoaSelecionada) {
		pessoaSelecionada = newPessoaSelecionada;
	}

	public boolean isEditMode() {
		return editMode;
	}
	
	public BarraStatus getBarraStatus() {
		return barraStatus;
	}

	public void setEditMode(boolean editMode) {
		if (editMode) {
			getJtfNome().setEditable(true);
			getJrbAtiva().setEnabled(true);
			getJrbInativa().setEnabled(true);
			getJrbFisica().setEnabled(true);
			getJrbJuridica().setEnabled(true);
			getJrbUsuario().setEnabled(true);
			getJtfCpf().setEditable(true);
			getJtfDataNascimento().setEditable(true);
			getJtfNomeFantasia().setEditable(true);
			getJtfCnpj().setEditable(true);
			getJtfDiaFatura().setEditable(true);
			getJtfDataAbertura().setEditable(true);
			getJtfLogin().setEditable(true);
			getJpfSenha().setEditable(true);
			getJcbTipoUsuario().setEnabled(true);
			getJtfLogradouro().setEditable(true);
			getJtfNumero().setEditable(true);
			getJtfComplemento().setEditable(true);
			getJtfBairro().setEditable(true);
			getJtfCidade().setEditable(true);
			getJtfUf().setEditable(true);
			getJtfCep().setEditable(true);
			getJtfEmail().setEditable(true);
			getJbAdicionar().setEnabled(true);
			getJbExcluir().setEnabled(true);
			getTabelaTelefones().setEnabled(true);
			getTabelaPessoas().setEnabled(false);
			getJtfNome().grabFocus();
		} else {
			getJtfNome().setEditable(false);
			getJrbAtiva().setEnabled(false);
			getJrbInativa().setEnabled(false);
			getJrbFisica().setEnabled(false);
			getJrbJuridica().setEnabled(false);
			getJrbUsuario().setEnabled(false);
			getJtfCpf().setEditable(false);
			getJtfDataNascimento().setEditable(false);
			getJtfNomeFantasia().setEditable(false);
			getJtfCnpj().setEditable(false);
			getJtfDiaFatura().setEditable(false);
			getJtfDataAbertura().setEditable(false);
			getJtfLogin().setEditable(false);
			getJpfSenha().setEditable(false);
			getJcbTipoUsuario().setEnabled(false);
			getJtfLogradouro().setEditable(false);
			getJtfNumero().setEditable(false);
			getJtfComplemento().setEditable(false);
			getJtfBairro().setEditable(false);
			getJtfCidade().setEditable(false);
			getJtfUf().setEditable(false);
			getJtfCep().setEditable(false);
			getJtfEmail().setEditable(false);
			getJbAdicionar().setEnabled(false);
			getJbExcluir().setEnabled(false);
			getTabelaTelefones().setEnabled(false);
			getTabelaPessoas().setEnabled(true);
			this.grabFocus();
		}
		this.editMode = editMode;
	}
	
	
}
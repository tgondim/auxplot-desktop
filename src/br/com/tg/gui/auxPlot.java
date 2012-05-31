package br.com.tg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.com.tg.exceptions.PessoaInexistenteException;
import br.com.tg.gui.util.BackgroundedTabbedPane;
import br.com.tg.gui.util.BarraStatus;

public class auxPlot extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private BackgroundedTabbedPane jtPane;
	
	private TelaCadastroPessoas telaPessoas;
	private TelaCadastroPlotagens telaPlotagens;
	private TelaCadastroPedidos telaPedidos;
	private TelaCadastroFaturas telaFaturas;
	private TelaFaturarPedidos telaFaturarPedidos;
	
	private Sobre sobreAuxPlot;
	
	private CancelarPedidos cancelarPedidos;
	
	private CancelarFaturas cancelarFaturas;
	
	private RelatorioPessoas relatorioPessoas;
	private RelatorioPlotagens relatorioPlotagens;
	private RelatorioPedidos relatorioPedidos;
	private RelatorioFaturas relatorioFaturas;
	
	private static Logon logonAtivo;
	
	private BarraStatus barraStatus;
	
	private String pessoaIcon;
	private String plotagemIcon;
	private String pedidoIcon;
	private String faturarPedidosIcon;
	private String lookAndFeel;
	private String auxPlotIcon;
	
	private JMenuBar jmbPrincipal;
	
	private JMenu jmGeral;	
	private JMenu jmTabelas;	
	private JMenu jmProcessos;	
	private JMenu jmRelatorios;	
	private JMenu jmAjuda;
	
	private JMenuItem jmiPessoas;
	private JMenuItem jmiPlotagens;
	private JMenuItem jmiPedidos;
	private JMenuItem jmiFaturas;
	private JMenuItem jmiRelatorioPessoas;
	private JMenuItem jmiRelatorioPlotagens;
	private JMenuItem jmiRelatorioPedidos;
	private JMenuItem jmiRelatorioFaturas;
	private JMenuItem jmiSobre;
	private JMenuItem jmiLogon;
	private JMenuItem jmiTrocarSenha; 
	private JMenuItem jmiFaturarPedidos; 
	private JMenuItem jmiCancelarPedidos; 
	private JMenuItem jmiCancelarFaturas; 
	private JMenuItem jmiSair;
	
	private JSeparator jsManutencao;
	
	public auxPlot() {	
		inicio();
	}
	
	private void inicio() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("auxPlot - Controle de Plotagens");
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(Color.BLACK);
		setLayout(new BorderLayout());
		File file = new File("auxPlot.properties");
		if (!file.exists()) {
			JOptionPane.showMessageDialog(getParent(),
					"O arquivo de configuração AuxPlot.properties" +
					"\nnão foi encontrado. O sistema será encerrado." +
					"\n\nEntre em contato com o administrador.", 
					" Atenção",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);			
		} else { 
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(file));
				pessoaIcon = prop.getProperty("pessoaIcon");
				plotagemIcon = prop.getProperty("plotagemIcon");
				pedidoIcon = prop.getProperty("pedidoIcon");
				faturarPedidosIcon = prop.getProperty("faturarPedidosIcon");
				lookAndFeel = prop.getProperty("lookAndFeel");
				auxPlotIcon = prop.getProperty("iconImg");
				
				if (lookAndFeel != null) {
					UIManager.setLookAndFeel(lookAndFeel);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getParent(),
						"O arquivo de configuração AuxPlot.properties" +
						"\ncontém erros. O sistema será encerrado." +
						"\n\nEntre em contato com o administrador.", 
						" Atenção",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);	
			}
		}
		File iconFile = new File(auxPlotIcon);
		if (iconFile.exists()) {
			try {
				this.setIconImage(ImageIO.read(iconFile));
			} catch (IOException e) {
				System.out.println("Ocorreu um erro ao carregar a imagem do ícone.");
				e.printStackTrace();
			}
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sairSistema(true);
			}
		});		
		setJMenuBar(getJmbPrincipal());
		add(getJtPane(), BorderLayout.CENTER);
		add(getBarraStatus(), BorderLayout.SOUTH);
		
		//Solicita credenciais do usuario
		if (getNovoLogon().getUsuarioLogado() == null) {
			sairSistema(false);
		}
		getBarraStatus().setUsuarioLogado(getLogonAtivo().getUsuarioLogado().getLogin());

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJmiPessoas()) {
			if (pessoaIcon == null) {
				getJtPane().addTab("Cadastro de Pessoas", getTelaPessoas());
			} else {
				getJtPane().addTab("Cadastro de Pessoas", new ImageIcon(pessoaIcon), getTelaPessoas());
			}
		} else if (e.getSource() == getJmiPlotagens()) {
			if (plotagemIcon == null) {
				getJtPane().addTab("Cadastro de Plotagens", getTelaPlotagens());
			} else {
				getJtPane().addTab("Cadastro de Plotagens", new ImageIcon(plotagemIcon), getTelaPlotagens());
			}
		} else if (e.getSource() == getJmiPedidos()) {
			if (pedidoIcon == null) {
				getJtPane().addTab("Cadastro de Pedidos", getTelaPedidos());
			} else {
				getJtPane().addTab("Cadastro de Pedidos", new ImageIcon(pedidoIcon), getTelaPedidos());
			}
		} else if (e.getSource() == getJmiFaturas()) {
			if (faturarPedidosIcon == null) {
				getJtPane().addTab("Cadastro de Faturas", getTelaFaturas());
			} else {
				getJtPane().addTab("Cadastro de Faturas", new ImageIcon(faturarPedidosIcon), getTelaFaturas());
			}
		} else if (e.getSource() == getJmiFaturarPedidos()) {
			if (faturarPedidosIcon == null) {
				getJtPane().addTab("Faturar Pedidos", getTelaFaturarPedidos());
			} else {
				getJtPane().addTab("Faturar Pedidos", new ImageIcon(faturarPedidosIcon), getTelaFaturarPedidos());
			}
		} else if (e.getSource() == getJmiLogon()) {
			//Solicita credenciais do usuario
			if (getNovoLogon().getUsuarioLogado() == null) {
				sairSistema(false);
			}
			getBarraStatus().setUsuarioLogado(getLogonAtivo().getUsuarioLogado().getLogin());
		} else if (e.getSource() == getJmiTrocarSenha()) {
			//Efetua a troca de senha
			try {
				@SuppressWarnings("unused")
				TrocaSenha trocaSenha = new TrocaSenha(this);
			} catch (PessoaInexistenteException e2) {
				JOptionPane.showMessageDialog(
						this,
						"Pessoa inexistente.\n"
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n",
						" Atenção", JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
				sairSistema(false);
			}  catch (Exception e1) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e1.getMessage() + "\n" + e1.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				sairSistema(false);
			}
		} else if (e.getSource() == getJmiCancelarPedidos()) {
			getCancelarPedidos().setVisible(true);
		} else if (e.getSource() == getJmiCancelarFaturas()) {
			getCancelarFaturas().setVisible(true);
		} else if (e.getSource() == getJmiRelatorioPessoas()) {
			getRelatoriosPessoas().setVisible(true);
		} else if (e.getSource() == getJmiRelatorioPlotagens()) {
			getRelatoriosPlotagens().setVisible(true);
		} else if (e.getSource() == getJmiRelatorioPedidos()) {
			getRelatoriosPedidos().setVisible(true);
		} else if (e.getSource() == getJmiRelatorioFaturas()) {
			getRelatoriosFaturas().setVisible(true);
		} else if (e.getSource() == getJmiSobre()) {
			getSobreAuxPlot().setVisible(true);
		} else if (e.getSource() == getJmiSair()) {
			sairSistema(true);
		}
	}

	public JTabbedPane getJtPane() {
		if (jtPane == null) {
			try {
				jtPane = new BackgroundedTabbedPane();
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
			jtPane.setVisible(true);	
		}
		return jtPane;
	}
	
	public JMenu getJmAjuda() {
		if (jmAjuda == null) {
			jmAjuda = new JMenu("Ajuda");
			jmAjuda.setMnemonic(KeyEvent.VK_A);
			jmAjuda.add(getJmiSobre());
		}
		return jmAjuda;
	}
	
	public JMenuItem getJmiSobre() {
		if (jmiSobre == null) {
			jmiSobre = new JMenuItem("Sobre");
			jmiSobre.setMnemonic(KeyEvent.VK_S);
			jmiSobre.addActionListener(this);
		}
		return jmiSobre;
	}

	public JMenuItem getJmiSair() {
		if (jmiSair == null) {
			jmiSair = new JMenuItem("Sair");
			jmiSair.setMnemonic(KeyEvent.VK_S);
			jmiSair.addActionListener(this);
		}
		return jmiSair;
	}
	
	public JMenuItem getJmiLogon() {
		if (jmiLogon == null) {
			jmiLogon = new JMenuItem("Logon");
			jmiLogon.setMnemonic(KeyEvent.VK_L);
			jmiLogon.addActionListener(this);
		}
		return jmiLogon;
	}
	
	public JMenuItem getJmiTrocarSenha() {
		if (jmiTrocarSenha == null) {
			jmiTrocarSenha = new JMenuItem("Trocar Senha");
			jmiTrocarSenha.setMnemonic(KeyEvent.VK_T);
			jmiTrocarSenha.addActionListener(this);
		}
		return jmiTrocarSenha;
	}
	
	public JSeparator getJsManutencao() {
		if (jsManutencao == null) {
			jsManutencao = new JSeparator();
		}
		return jsManutencao;
	}
	
	public JMenuItem getJmiPessoas() {
		if (jmiPessoas == null) {
			jmiPessoas = new JMenuItem("Pessoas");
			jmiPessoas.setMnemonic(KeyEvent.VK_P);
			jmiPessoas.addActionListener(this);
		}
		return jmiPessoas;
	}

	public JMenuItem getJmiPlotagens() {
		if (jmiPlotagens == null) {
			jmiPlotagens = new JMenuItem("Plotagens");
			jmiPlotagens.setMnemonic(KeyEvent.VK_L);
			jmiPlotagens.addActionListener(this);
		}
		return jmiPlotagens;
	}

	public JMenuItem getJmiPedidos() {
		if (jmiPedidos == null) {
			jmiPedidos = new JMenuItem("Pedidos");
			jmiPedidos.setMnemonic(KeyEvent.VK_E);
			jmiPedidos.addActionListener(this);
		}
		return jmiPedidos;
	}
	
	public JMenuItem getJmiFaturas() {
		if (jmiFaturas == null) {
			jmiFaturas = new JMenuItem("Faturas");
			jmiFaturas.setMnemonic(KeyEvent.VK_F);
			jmiFaturas.addActionListener(this);
		}
		return jmiFaturas;
	}
	
	public JMenuItem getJmiRelatorioPessoas() {
		if (jmiRelatorioPessoas == null) {
			jmiRelatorioPessoas = new JMenuItem("Pessoas");
			jmiRelatorioPessoas.setMnemonic(KeyEvent.VK_P);
			jmiRelatorioPessoas.addActionListener(this);
		}
		return jmiRelatorioPessoas;
	}
	
	public JMenuItem getJmiRelatorioPlotagens() {
		if (jmiRelatorioPlotagens == null) {
			jmiRelatorioPlotagens = new JMenuItem("Plotagens");
			jmiRelatorioPlotagens.setMnemonic(KeyEvent.VK_L);
			jmiRelatorioPlotagens.addActionListener(this);
		}
		return jmiRelatorioPlotagens;
	}
	
	public JMenuItem getJmiRelatorioPedidos() {
		if (jmiRelatorioPedidos == null) {
			jmiRelatorioPedidos = new JMenuItem("Pedidos");
			jmiRelatorioPedidos.setMnemonic(KeyEvent.VK_E);
			jmiRelatorioPedidos.addActionListener(this);
		}
		return jmiRelatorioPedidos;
	}
	
	public JMenuItem getJmiRelatorioFaturas() {
		if (jmiRelatorioFaturas == null) {
			jmiRelatorioFaturas = new JMenuItem("Faturas");
			jmiRelatorioFaturas.setMnemonic(KeyEvent.VK_F);
			jmiRelatorioFaturas.addActionListener(this);
		}
		return jmiRelatorioFaturas;
	}
	
	public JMenuItem getJmiFaturarPedidos() {
		if (jmiFaturarPedidos == null) {
			jmiFaturarPedidos = new JMenuItem("Faturar Pedidos");
			jmiFaturarPedidos.setMnemonic(KeyEvent.VK_A);
			jmiFaturarPedidos.addActionListener(this);
		}
		return jmiFaturarPedidos;
	}
	
	public JMenuItem getJmiCancelarPedidos() {
		if (jmiCancelarPedidos == null) {
			jmiCancelarPedidos = new JMenuItem("Cancelar Pedidos");
			jmiCancelarPedidos.setMnemonic(KeyEvent.VK_P);
			jmiCancelarPedidos.addActionListener(this);
		}
		return jmiCancelarPedidos;
	}
	
	public JMenuItem getJmiCancelarFaturas() {
		if (jmiCancelarFaturas == null) {
			jmiCancelarFaturas = new JMenuItem("Cancelar Faturas");
			jmiCancelarFaturas.setMnemonic(KeyEvent.VK_F);
			jmiCancelarFaturas.addActionListener(this);
		}
		return jmiCancelarFaturas;
	}
	
	public JMenu getJmTabelas() {
		if (jmTabelas == null) {
			jmTabelas = new JMenu("Tabelas");
			jmTabelas.setMnemonic(KeyEvent.VK_T);
			jmTabelas.add(getJmiPessoas());
			jmTabelas.add(getJmiPlotagens());
			jmTabelas.add(getJmiPedidos());
			jmTabelas.add(getJmiFaturas());
		}
		return jmTabelas;
	}

	public JMenu getJmProcessos() {
		if (jmProcessos == null) {
			jmProcessos = new JMenu("Processos");
			jmProcessos.setMnemonic(KeyEvent.VK_P);
			jmProcessos.add(getJmiFaturarPedidos());
			jmProcessos.add(getJmiCancelarPedidos());
			jmProcessos.add(getJmiCancelarFaturas());
		}
		return jmProcessos;
	}
	
	public JMenu getJmRelatorios() {
		if (jmRelatorios == null) {
			jmRelatorios = new JMenu("Relatórios");
			jmRelatorios.setMnemonic(KeyEvent.VK_R);
			jmRelatorios.add(getJmiRelatorioPessoas());
			jmRelatorios.add(getJmiRelatorioPlotagens());
			jmRelatorios.add(getJmiRelatorioPedidos());
			jmRelatorios.add(getJmiRelatorioFaturas());
		}
		return jmRelatorios;
	}
	
	public JMenu getJmGeral() {
		if (jmGeral == null) {
			jmGeral = new JMenu("Geral");
			jmGeral.setMnemonic(KeyEvent.VK_G);
			jmGeral.add(getJmiLogon());
			jmGeral.add(getJmiTrocarSenha());
			jmGeral.add(getJsManutencao());
			jmGeral.add(getJmiSair());
		}
		return jmGeral;
	}

	public JMenuBar getJmbPrincipal() {
		if (jmbPrincipal == null) {
			jmbPrincipal = new JMenuBar();
			jmbPrincipal.add(getJmGeral());
			jmbPrincipal.add(getJmTabelas());
			jmbPrincipal.add(getJmProcessos());
			jmbPrincipal.add(getJmRelatorios());
			jmbPrincipal.add(getJmAjuda());
		}
		return jmbPrincipal;
	}
	
	public TelaCadastroPessoas getTelaPessoas() {
		if (telaPessoas == null) {
			telaPessoas = new TelaCadastroPessoas("Cadastro de Pessoas", this, getJtPane(), getBarraStatus());
			telaPessoas.setVisible(true);
		}
		return telaPessoas;
	}

	public void killTelaPessoas() {
		this.telaPessoas = null;
	}
	
	public TelaCadastroPlotagens getTelaPlotagens() {
		if (telaPlotagens == null) {
			telaPlotagens = new TelaCadastroPlotagens("Cadastro de Plotagens", this, getJtPane(), getBarraStatus());
			telaPlotagens.setVisible(true);
		}
		return telaPlotagens;
	}
	
	public void killTelaPlotagens() {
		this.telaPlotagens = null;
	}

	public TelaCadastroPedidos getTelaPedidos() {
		if (telaPedidos == null) {
			telaPedidos = new TelaCadastroPedidos("Cadastro de Pedidos", this, getJtPane(), getBarraStatus());
			telaPedidos.setVisible(true);
		}
		return telaPedidos;
	}
	
	public void killTelaPedidos() {
		this.telaPedidos = null;
	}
	
	public TelaCadastroFaturas getTelaFaturas() {
		if (telaFaturas == null) {
			telaFaturas = new TelaCadastroFaturas("Cadastro de Faturas", this, getJtPane(), getBarraStatus());
			telaFaturas.setVisible(true);
		}
		return telaFaturas;
	}
	
	public void killTelaFaturas() {
		this.telaFaturas = null;
	}
	
	public TelaFaturarPedidos getTelaFaturarPedidos() {
		if (telaFaturarPedidos == null) {
			try {
				telaFaturarPedidos = new TelaFaturarPedidos("Faturar Pedidos", this, getJtPane(), getBarraStatus());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Ocorreu um erro inesperado.\n" 
						+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
						+ e.getMessage() + "\n" + e.getStackTrace(), 
						" Atenção", 
						JOptionPane.ERROR_MESSAGE);
				getJtPane().removeTabAt(getJtPane().getSelectedIndex());
			}
			telaFaturarPedidos.setVisible(true);
		}
		return telaFaturarPedidos;
	}

	public void killTelaFaturarPedidos() {
		this.telaFaturarPedidos = null;
	}

	public Logon getNovoLogon() {
		try {
			logonAtivo = new Logon(this);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, 
					"Não há usuários cadastrados no sistema.\n" 
					+ "Entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n" + e.getStackTrace(), 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			sairSistema(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"Ocorreu um erro inesperado.\n" 
					+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
					+ e.getMessage() + "\n" + e.getStackTrace(), 
					" Atenção", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			sairSistema(false);
		}
		return logonAtivo;
	}
	
	public Logon getLogonAtivo() {
		return logonAtivo;
	}
	
	public Sobre getSobreAuxPlot() {
		if (sobreAuxPlot == null) {
			sobreAuxPlot = new Sobre(this);
		}
		return sobreAuxPlot;
	}	
	
	public void killSobreAuxPlot() {
		this.sobreAuxPlot = null;
	}
	
	public CancelarPedidos getCancelarPedidos() {
		if (cancelarPedidos == null) {
			cancelarPedidos = new CancelarPedidos(this);
		}
		return cancelarPedidos;
	}	
	
	public void killCancelarPedidos() {
		this.cancelarPedidos = null;
	}

	public CancelarFaturas getCancelarFaturas() {
		if (cancelarFaturas == null) {
			cancelarFaturas = new CancelarFaturas(this);
		}
		return cancelarFaturas;
	}	
	
	public void killCancelarFaturas() {
		this.cancelarPedidos = null;
	}
	
	public RelatorioPessoas getRelatoriosPessoas() {
		if (relatorioPessoas == null) {
			relatorioPessoas = new RelatorioPessoas(this);
		}
		return relatorioPessoas;
	}	
	
	public void killRelatoriosPessoas() {
		this.relatorioPessoas = null;
	}
	
	public RelatorioPlotagens getRelatoriosPlotagens() {
		if (relatorioPlotagens == null) {
			relatorioPlotagens = new RelatorioPlotagens(this);
		}
		return relatorioPlotagens;
	}	

	public void killRelatoriosPlotagens() {
		this.relatorioPlotagens = null;
	}
	
	public RelatorioPedidos getRelatoriosPedidos() {
		if (relatorioPedidos == null) {
			relatorioPedidos = new RelatorioPedidos(this);
		}
		return relatorioPedidos;
	}	
	
	public void killRelatoriosPedidos() {
		this.relatorioPedidos = null;
	}
	
	public RelatorioFaturas getRelatoriosFaturas() {
		if (relatorioFaturas == null) {
			relatorioFaturas = new RelatorioFaturas(this);
		}
		return relatorioFaturas;
	}	
	
	public void killRelatoriosFaturas() {
		this.relatorioFaturas = null;
	}
	
	public BarraStatus getBarraStatus() {
		if (barraStatus == null) {
			barraStatus = new BarraStatus();
		}
		return barraStatus;
	}
	
	/*
	 * Realiza os procedimentos de saída do sistema.
	 */
	public void sairSistema(boolean confirmar) {
		if (!confirmar || JOptionPane
				.showConfirmDialog(
						this,
						"Tem certeza que deseja sair do sistema?",
						" Confirmar",
						JOptionPane.OK_CANCEL_OPTION) == 0) {
			System.exit(0);		
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				auxPlot telaPrincipal = new auxPlot();
				telaPrincipal.pack();
				telaPrincipal.setLocationRelativeTo(null);
				Dimension d = new Dimension(800, 600);				
				telaPrincipal.setMinimumSize(d);
				telaPrincipal.setSize(800, 600);
				telaPrincipal.setLocationRelativeTo(null);
				telaPrincipal.setVisible(true);
			}
		});
	}
}

package br.com.tg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import org.hibernate.ObjectNotFoundException;

import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.Pedido;
import br.com.tg.entidades.StatusFatura;
import br.com.tg.entidades.StatusPedido;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.FaturaInexistenteException;
import br.com.tg.exceptions.PedidoInexistenteException;
import br.com.tg.exceptions.StatusFaturaInexistenteException;
import br.com.tg.exceptions.StatusPedidoInexistenteException;
import br.com.tg.gui.util.BarraStatus;
import br.com.tg.gui.util.CalendarFormatter;
import br.com.tg.gui.util.ImagemUtil;
import br.com.tg.util.Fachada;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class CancelarFaturas extends JDialog {
	
	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private auxPlot telaPrincipal; 
	
	private JPanel jpCancelarFaturas;
	private JPanel jpGeral;
	
	private Fatura faturaSelecionada;
	
	private JLabel jlId; 
	private JLabel jlCliente; 
	private JLabel jlStatusPedido; 
	private JLabel jlEmissao; 
	private JLabel jlVencimento; 
	private JLabel jlValorTotal; 
	private JLabel jlAviso; 
	
	private JTextField jtfCliente;
	private JTextField jtfId;
	private JTextField jtfEmissao;
	private JTextField jtfVencimento;
	private JTextField jtfValorTotal;
	private JTextField jtfStatusPedido;
	
	private JButton jbConfirmar;	
	private JButton jbCancelar;	
	private JButton jbBuscar;
	
	private String buscarIcon;
	
	private BarraStatus barraStatus;
	
	public CancelarFaturas(JFrame owner) {
		super(owner, "Cancelar Faturas");
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
		buscarIcon = prop.getProperty("buscarIcon");
		
		setResizable(false);
		setSize(new Dimension(700, 260));
		setLocation(new Point((getParent().getLocation().x+(getParent().getSize().width-700)/2), 
				(getParent().getLocation().y+(getParent().getSize().height-260)/2)));
		add(getJpCancelarPedidos());
		setVisible(true);
	}

	public JPanel getJpCancelarPedidos() {
		if (jpCancelarFaturas == null) {
			jpCancelarFaturas = new JPanel(null);

			jpCancelarFaturas.add(getJpFiltros());
			getJpFiltros().setBounds(10, 10, 675, 170);

			jpCancelarFaturas.add(getJbConfirmar());
			getJbConfirmar().setBounds(238, 190, 100, 30);
			jpCancelarFaturas.add(getJbCancelar());
			getJbCancelar().setBounds(355, 190, 100, 30);
		}
		return jpCancelarFaturas;
	}
	
	public JPanel getJpFiltros() {
		if (jpGeral == null) {
			Border etched = BorderFactory.createEtchedBorder();
			jpGeral = new JPanel();
			jpGeral.setBorder(etched);
			
			SpringLayout filtrosLayout = new SpringLayout();
			jpGeral.setLayout(filtrosLayout);
			
			jpGeral.setFocusable(false);
			jpGeral.add(getJlId());
			jpGeral.add(getJtfId());
			jpGeral.add(getJbBuscar());
			jpGeral.add(getJlCliente());
			jpGeral.add(getJtfCliente());
			jpGeral.add(getJlStatusPedido());
			jpGeral.add(getJtfStatusFatura());
			jpGeral.add(getJlEmissao());
			jpGeral.add(getJtfEmissao());
			jpGeral.add(getJlVencimento());
			jpGeral.add(getJtfVencimento());
			jpGeral.add(getJlValorTotal());
			jpGeral.add(getJtfValorTotal());
			jpGeral.add(getJlAviso());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlId(), 55, SpringLayout.WEST, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlId(), 20, SpringLayout.NORTH, jpGeral);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfId(), 10, SpringLayout.EAST, getJlId());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfId(), 15, SpringLayout.NORTH, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfId(), -540, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJbBuscar(), 5, SpringLayout.EAST, getJtfId());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJbBuscar(), 12, SpringLayout.NORTH, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.EAST, getJbBuscar(), -510, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlStatusPedido(), 360, SpringLayout.EAST, getJtfId());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlStatusPedido(), 20, SpringLayout.NORTH, jpGeral);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfStatusFatura(), 10, SpringLayout.EAST, getJlStatusPedido());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfStatusFatura(), 17, SpringLayout.NORTH, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfStatusFatura(), -15, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlCliente(), 25, SpringLayout.WEST, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlCliente(), 20, SpringLayout.SOUTH, getJlId());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfCliente(), 10, SpringLayout.EAST, getJlCliente());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfCliente(), 17, SpringLayout.SOUTH, getJtfId());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfCliente(), -15, SpringLayout.EAST, jpGeral);
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJlEmissao(), 15, SpringLayout.WEST, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlEmissao(), 20, SpringLayout.SOUTH, getJlCliente());

			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfEmissao(), 10, SpringLayout.EAST, getJlEmissao());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfEmissao(), 20, SpringLayout.SOUTH, getJtfCliente());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfEmissao(), -525, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlVencimento(), 30, SpringLayout.EAST, getJtfEmissao());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlVencimento(), 20, SpringLayout.SOUTH, getJlCliente());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfVencimento(), 10, SpringLayout.EAST, getJlVencimento());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfVencimento(), 20, SpringLayout.SOUTH, getJtfCliente());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfVencimento(), -345, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlValorTotal(), 100, SpringLayout.EAST, getJtfVencimento());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlValorTotal(), 20, SpringLayout.SOUTH, getJlCliente());
			
			filtrosLayout.putConstraint(SpringLayout.WEST, getJtfValorTotal(), 10, SpringLayout.EAST, getJlValorTotal());
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJtfValorTotal(), 20, SpringLayout.SOUTH, getJtfCliente());
			filtrosLayout.putConstraint(SpringLayout.EAST, getJtfValorTotal(), -15, SpringLayout.EAST, jpGeral);

			filtrosLayout.putConstraint(SpringLayout.WEST, getJlAviso(), 140, SpringLayout.WEST, jpGeral);
			filtrosLayout.putConstraint(SpringLayout.NORTH, getJlAviso(), 20, SpringLayout.SOUTH, getJlValorTotal());
		}
		return jpGeral;
	}
	
	public JLabel getJlCliente() {
		if (jlCliente == null) {
			jlCliente = new JLabel("Cliente", JLabel.LEFT);
		}
		return jlCliente;
	}
	
	public JLabel getJlId() {
		if (jlId == null) {
			jlId = new JLabel("Id", JLabel.LEFT);
		}
		return jlId;
	}
	
	public JLabel getJlValorTotal() {
		if (jlValorTotal == null) {
			jlValorTotal = new JLabel("Valor Total R$", JLabel.LEFT);
		}
		return jlValorTotal;
	}
	
	public JLabel getJlAviso() {
		if (jlAviso == null) {
			jlAviso = new JLabel("Atenção! Faturas canceladas não poderão ser quitadas novamente.", JLabel.LEFT);
			jlAviso.setForeground(Color.RED);
		}
		return jlAviso;
	}
	
	public JLabel getJlEmissao() {
		if (jlEmissao == null) {
			jlEmissao = new JLabel("Emissão", JLabel.LEFT);
		}
		return jlEmissao;
	}
	
	public JLabel getJlVencimento() {
		if (jlVencimento == null) {
			jlVencimento = new JLabel("Vencimento", JLabel.LEFT);
		}
		return jlVencimento;
	}
	
	public JLabel getJlStatusPedido() {
		if (jlStatusPedido == null) {
			jlStatusPedido = new JLabel("Status", JLabel.LEFT);
		}
		return jlStatusPedido;
	}
	
	public JTextField getJtfId() {
		if (jtfId == null) {
			jtfId = new JTextField();
		}
		return jtfId;
	}
	
	public JTextField getJtfCliente() {
		if (jtfCliente == null) {
			jtfCliente = new JTextField();
			jtfCliente.setEditable(false);
			jtfCliente.setBorder(BORDAS_TEXT_FIELD);
		}
		return jtfCliente;
	}
	
	public JTextField getJtfEmissao() {
		if (jtfEmissao == null) {
			jtfEmissao = new JTextField();
			jtfEmissao.setEditable(false);
			jtfEmissao.setBorder(BORDAS_TEXT_FIELD);
		}
		return jtfEmissao;
	}
	
	public JTextField getJtfVencimento() {
		if (jtfVencimento == null) {
			jtfVencimento = new JTextField();
			jtfVencimento.setEditable(false);
			jtfVencimento.setBorder(BORDAS_TEXT_FIELD);
		}
		return jtfVencimento;
	}
	
	public JTextField getJtfValorTotal() {
		if (jtfValorTotal == null) {
			jtfValorTotal = new JTextField();
			jtfValorTotal.setBorder(BORDAS_TEXT_FIELD);
			jtfValorTotal.setHorizontalAlignment(JFormattedTextField.RIGHT);
			jtfValorTotal.setEditable(false);
		}
		return jtfValorTotal;
	}
	
	public JTextField getJtfStatusFatura() {
		if (jtfStatusPedido == null) {
			jtfStatusPedido = new JTextField();
			jtfStatusPedido.setEditable(false);
			jtfStatusPedido.setBorder(BORDAS_TEXT_FIELD);			
		}
		return jtfStatusPedido;
	}
	
	public JButton getJbBuscar() {
		if (jbBuscar == null) {
			jbBuscar = new JButton();
			try {
				jbBuscar.setIcon(ImagemUtil.imagemEscalonada(
						buscarIcon, 15, 15));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbBuscar.setToolTipText("buscar fatura");
			jbBuscar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						setFaturaSelecionada(Fachada.obterInstancia().procurarFatura(Integer.valueOf(getJtfId().getText().trim())));
						getJtfStatusFatura().setText(getFaturaSelecionada().getStatusFatura().getDescricao());
						getJtfCliente().setText(getFaturaSelecionada().getPessoaPai().getNome());
						getJtfEmissao().setText(CalendarFormatter.formatDate(getFaturaSelecionada().getDataEmissao()));
						getJtfVencimento().setText(CalendarFormatter.formatDate(getFaturaSelecionada().getDataVencimento()));
						getJtfValorTotal().setText(Validador.inserirVirgula(getFaturaSelecionada().getValorTotal()));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(CancelarFaturas.this, 
								"Id inválido.\n\n", 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);
					} catch (FaturaInexistenteException e) {
						JOptionPane.showMessageDialog(CancelarFaturas.this, 
								"Pedido inexistente.\n\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					} catch (ObjectNotFoundException e) {
						JOptionPane.showMessageDialog(CancelarFaturas.this, 
								"Pedido inexistente.\n\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);
					} catch (ErroAcessoRepositorioException e) {
						JOptionPane.showMessageDialog(CancelarFaturas.this, 
								"Ocorreu um erro inesperado.\n" 
								+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
								+ e.getMessage() + "\n" + e.getStackTrace(), 
								" Atenção", 
								JOptionPane.ERROR_MESSAGE);						
						e.printStackTrace();
					}
				}
			});
		}
		return jbBuscar;
	}	
	
	public JButton getJbConfirmar() {
		if (jbConfirmar == null) {
			jbConfirmar = new JButton("Confirmar");
			jbConfirmar.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					StatusFatura statusFaturaAux = new StatusFatura();
					statusFaturaAux.setId(1);
					if (getFaturaSelecionada().getStatusFatura().equals(statusFaturaAux)) {
						if (JOptionPane.showConfirmDialog(
										CancelarFaturas.this,
										"Após o cancelamento esta fatura não poderá\n" 
										+ "ser alterada ou quitada.\n"
										+ "Tem certeza que deseja cancelar a fatura?\n\n",
										" Confirmar",
										JOptionPane.OK_CANCEL_OPTION) == 0) {
							try {
								StatusPedido statusPedidoAux = Fachada.obterInstancia().procurarStatusPedido(1);
								for (Pedido p : getFaturaSelecionada().getPedidos()) {
									p.setStatusPedido(statusPedidoAux);
									Fachada.obterInstancia().atualizarPedido(p);
								}
								statusFaturaAux = Fachada.obterInstancia().procurarStatusFatura(3);
								getFaturaSelecionada().setStatusFatura(statusFaturaAux);
								try {
									Fachada.obterInstancia().atualizarFatura(getFaturaSelecionada());
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(CancelarFaturas.this, 
											"Ocorreu um erro inesperado.\n" 
											+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
											+ e1.getMessage() + "\n" + e1.getStackTrace(), 
											" Atenção", 
											JOptionPane.ERROR_MESSAGE);						
									e1.printStackTrace();								}
								getJtfStatusFatura().setText("Cancelada");
							} catch (StatusPedidoInexistenteException e1) {
								JOptionPane.showMessageDialog(CancelarFaturas.this, 
										"StatusFatura inexistente.\n\n", 
										" Atenção", 
										JOptionPane.INFORMATION_MESSAGE);
								e1.printStackTrace();
							} catch (StatusFaturaInexistenteException e1) {
								JOptionPane.showMessageDialog(CancelarFaturas.this, 
										"StatusFatura inexistente.\n\n", 
										" Atenção", 
										JOptionPane.INFORMATION_MESSAGE);
								e1.printStackTrace();
							} catch (PedidoInexistenteException e1) {
								JOptionPane.showMessageDialog(CancelarFaturas.this, 
										"StatusPedido inexistente.\n\n", 
										" Atenção", 
										JOptionPane.INFORMATION_MESSAGE);
								e1.printStackTrace();
							} catch (ErroAcessoRepositorioException e1) {
								JOptionPane.showMessageDialog(CancelarFaturas.this, 
										"Ocorreu um erro inesperado.\n" 
										+ "Caso o erro persista, entre em contato com o Administrador. \n\n"
										+ e1.getMessage() + "\n" + e1.getStackTrace(), 
										" Atenção", 
										JOptionPane.ERROR_MESSAGE);						
								e1.printStackTrace();
							}
						}
					} else {
						JOptionPane.showMessageDialog(CancelarFaturas.this, 
								"Só é possível cancelar faturas a pagar.\n\n", 
								" Atenção", 
								JOptionPane.INFORMATION_MESSAGE);	
					}
				}
			});
		}
		return jbConfirmar;
	}
	
	public JButton getJbCancelar() {
		if (jbCancelar == null) {
			jbCancelar = new JButton("Cancelar");
			jbCancelar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					getTelaPrincipal().killCancelarFaturas();
				}
			});
		}
		return jbCancelar;
	}

	public BarraStatus getBarraStatus() {
		return barraStatus;
	}

	private auxPlot getTelaPrincipal() {
		return telaPrincipal;
	}
	
	public Fatura getFaturaSelecionada() {
		return faturaSelecionada;
	}

	public void setFaturaSelecionada(Fatura pedido) {
		this.faturaSelecionada = pedido;
	}
}
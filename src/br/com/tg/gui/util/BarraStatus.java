package br.com.tg.gui.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class BarraStatus extends JPanel {

	private final static Border BORDER = BorderFactory.createLoweredBevelBorder();
	
	private JPanel jpUsuario;
	
	private JLabel jlUsuario;
	private JLabel jlMensagem;
	 
	public BarraStatus() {
		super();
		setLayout(new GridLayout());
		setVisible(true);
		setFocusable(false);
		add(getJpUsuario());
		getJpUsuario().setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
		getJpUsuario().add(getJlMensagem());
		getJpUsuario().add(getJlUsuario());
	}
	
	public JPanel getJpUsuario() {
		if (jpUsuario == null) {
			jpUsuario = new JPanel();
		}
		return jpUsuario;
	}

	public JLabel getJlUsuario() {
		if (jlUsuario == null) {
			jlUsuario = new JLabel("  Usuário: ", JLabel.LEFT);
			jlUsuario.setPreferredSize(new Dimension(220, 20));
			jlUsuario.setBorder(BORDER);
		}
		return jlUsuario;
	}
	
	public void setUsuarioLogado(String user) {
		getJlUsuario().setText("  Usuário: " + user);
	}

	public JLabel getJlMensagem() {
		if (jlMensagem == null) {
			jlMensagem = new JLabel("", JLabel.LEFT);
			jlMensagem.setPreferredSize(new Dimension(562, 20));
			jlMensagem.setBorder(BORDER);
		}
		return jlMensagem;
	}
	
	public void setMensagem(String mensagem, boolean temporaria) {
		getJlMensagem().setText("  " + mensagem);
		if (temporaria) {
			Thread msgTmp = new Thread() {
				public void run() {
					try {
						Thread.sleep(4000);
						getJlMensagem().setText("");
					} catch (InterruptedException e) {
						System.out.println("A thread de Mensagem foi interrompida.");
						e.printStackTrace();
					}
				}
			};
			msgTmp.start();
		}
	}

}

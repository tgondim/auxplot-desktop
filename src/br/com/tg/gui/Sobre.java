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
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.tg.gui.util.ImagemUtil;

@SuppressWarnings("serial")
public class Sobre extends JDialog {
	
	private auxPlot telaPrincipal;
	
	private JPanel jpSobre;
	
	private JLabel jlAuxPlot; 
	private JLabel jlSistema; 
	private JLabel jlVersao; 
	private JLabel jlAuxPlotIcon;
	
	private String auxPlotIcon;
	
	private JButton jbOk;	

	public Sobre(JFrame owner) {
		super(owner, "Sobre AuxPlot");
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
		auxPlotIcon = prop.getProperty("iconImg");
		setAlwaysOnTop(true);
		setResizable(false);
		setSize(new Dimension(400, 300));
		setLocation(new Point((getParent().getLocation().x+(getParent().getSize().width-400)/2), 
				(getParent().getLocation().y+(getParent().getSize().height-300)/2)));
		add(getJpSobre());
		setVisible(true);
	}

	public JPanel getJpSobre() {
		if (jpSobre == null) {
			jpSobre = new JPanel(null);
			jpSobre.add(getJlAuxPlotIcon());
			getJlAuxPlotIcon().setBounds(120, 45, 53, 56);
			jpSobre.add(getJlAuxPlot());
			getJlAuxPlot().setBounds(170, 80, 120, 20);
			jpSobre.add(getJlSistema());
			getJlSistema().setBounds(90, 120, 200, 20);
			jpSobre.add(getJlVersao());
			getJlVersao().setBounds(135, 150, 120, 20);
			jpSobre.add(getJbOk());
			getJbOk().setBounds(170, 190, 50, 30);
		}
		return jpSobre;
	}

	public JLabel getJlAuxPlot() {
		if (jlAuxPlot == null) {
			jlAuxPlot = new JLabel("auxPlot™", JLabel.CENTER);
			jlAuxPlot.setFont(new Font("Arial", Font.BOLD, 20));
		}
		return jlAuxPlot;
	}

	public JLabel getJlSistema() {
		if (jlSistema == null) {
			jlSistema = new JLabel("Sistema de Controle de Plotagens", JLabel.CENTER);
		}
		return jlSistema;
	}

	public JLabel getJlVersao() {
		if (jlVersao == null) {
			jlVersao = new JLabel("Versão 1.0 beta", JLabel.CENTER);
		}
		return jlVersao;
	}

	public JLabel getJlAuxPlotIcon() {
		if (jlAuxPlotIcon == null) {
			ImageIcon icon;
			try {
				icon = ImagemUtil.imagemEscalonada(auxPlotIcon, 53, 56);
				jlAuxPlotIcon = new JLabel(icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jlAuxPlotIcon;
	}

	public JButton getJbOk() {
		if (jbOk == null) {
			jbOk = new JButton("Ok");
			jbOk.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					getTelaPrincipal().killSobreAuxPlot();
				}
			});
		}
		return jbOk;
	}

	private auxPlot getTelaPrincipal() {
		return telaPrincipal;
	}
}

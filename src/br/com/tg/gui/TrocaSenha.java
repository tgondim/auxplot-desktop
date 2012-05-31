package br.com.tg.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.exceptions.PessoaInexistenteException;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.util.Fachada;

@SuppressWarnings("serial")
public class TrocaSenha extends JOptionPane {

	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);
	
	private JFrame owner;
	
	private JPasswordField jpfSenha1;
	private JPasswordField jpfSenha2;
	
	private boolean senhasIguais;
	
	private JLabel jlMsgSenha;
	
	
	public TrocaSenha(JFrame newOwner) throws PessoaInexistenteException, ErroAcessoRepositorioException {
		super();
		this.owner = newOwner;
		this.senhasIguais = false;
		setSize(new Dimension(400, 350));
		setVisible(true);
		Object[] mensagem = {"Usuario: " + ((auxPlot)owner).getLogonAtivo().getUsuarioLogado().getLogin(), "Nova senha", getJpfSenha1(), "Repita a senha", getJpfSenha2(), getJlMsgSenha() };
		
		while (!isSenhasIguais()) {
			int returnCode = JOptionPane.showConfirmDialog(owner, mensagem, "Troca de senha", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (returnCode == JOptionPane.OK_OPTION) {
				boolean validador = true;
				if (String.copyValueOf(getJpfSenha1().getPassword()).length() == 0) {
					Geral.alterarCor(getJpfSenha1(), Color.red, true);
					getJpfSenha1().grabFocus();
					getJlMsgSenha().setText("A senha deve ser preenchida! Tente novamente.");
					validador = false;
				}
				if (validador) {
					if (isPasswordCorrect(getJpfSenha1().getPassword(), getJpfSenha2().getPassword())) {
						((auxPlot)owner).getLogonAtivo().getUsuarioLogado().setSenha(String.valueOf(getJpfSenha2().getPassword()));
						setSenhasIguais(true);
						Fachada.obterInstancia().atualizarPessoa(((auxPlot)owner).getLogonAtivo().getUsuarioLogado());
					} else {
						getJlMsgSenha().setText("Senha não confere! Tente novamente.");
						br.com.tg.gui.util.Geral.alterarCor(getJpfSenha2(), Color.red, true);
					}
				}
			} else {
				break;
			}
		}
	}
	
    private static boolean isPasswordCorrect(char[] input, char[] test) {
        if (input.length != test.length)
            return false;
        for (int i = 0;  i < input.length; i ++)
            if (input[i] != test[i])
                return false;
        return true;
    }
	
	public JPasswordField getJpfSenha1() {
		if (jpfSenha1 == null) {
			jpfSenha1 = new JPasswordField();
			jpfSenha1.setBorder(BORDAS_TEXT_FIELD);
			jpfSenha1.setDocument(new FixedLengthDocument(16, false, false));
			jpfSenha1.setFocusable(true);
		}
		return jpfSenha1;
	}

	public JPasswordField getJpfSenha2() {
		if (jpfSenha2 == null) {
			jpfSenha2 = new JPasswordField();
			jpfSenha2.setBorder(BORDAS_TEXT_FIELD);
			jpfSenha2.setDocument(new FixedLengthDocument(16, false, false));
			jpfSenha2.setFocusable(true);
		}
		return jpfSenha2;
	}
	
	public JLabel getJlMsgSenha() {
		if (jlMsgSenha == null) {
			jlMsgSenha = new JLabel();
			jlMsgSenha.setFocusable(false);
		}
		return jlMsgSenha;
	}

	public boolean isSenhasIguais() {
		return senhasIguais;
	}

	public void setSenhasIguais(boolean senhasIguais) {
		this.senhasIguais = senhasIguais;
	}
	
}

package br.com.tg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.TipoPessoa;
import br.com.tg.entidades.Usuario;
import br.com.tg.exceptions.ErroAcessoRepositorioException;
import br.com.tg.gui.util.FixedLengthDocument;
import br.com.tg.gui.util.Geral;
import br.com.tg.util.Fachada;

@SuppressWarnings("serial")
public class Logon extends JOptionPane {
	
	private static final Border BORDAS_TEXT_FIELD = BorderFactory.createLineBorder(Color.lightGray);

	private final static int TENTATIVAS_LOGIN = 3;

	private JFrame owner;
	
	private JComboBox jcbUsuario;
	
	private JPasswordField jpfSenha;
	
	private Usuario usuarioLogado;
	
	private JLabel jlMsgSenha;
	
	
	public Logon(JFrame newOwner) throws IllegalArgumentException, ErroAcessoRepositorioException {
		super();
		this.owner = newOwner;
		setSize(new Dimension(400, 350));
		setVisible(true);
		String senhaDigitada;
		Boolean sair = false;
		Object[] mensagem = {"Usuário", getJcbUsuario(), "Senha", getJpfSenha(), getJlMsgSenha() };
		int tentativas = 0;
		
		while ((tentativas < TENTATIVAS_LOGIN) && !sair) {
			int returnCode = JOptionPane.showConfirmDialog(owner, mensagem, "Digite a senha", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (returnCode == JOptionPane.OK_OPTION) {
				senhaDigitada = String.valueOf(getJpfSenha().getPassword());
				Usuario usuario = (Usuario)getJcbUsuario().getSelectedItem();
				if (isPasswordCorrect(usuario.getSenha().toCharArray(), senhaDigitada.toCharArray())) {
					usuarioLogado = usuario;
					break;
				} else {
					getJlMsgSenha().setText("Senha incorreta! Tente novamente.");
					Geral.alterarCor(getJpfSenha(), Color.red, true);
				}
			} else {
				sair = true;
			}		
			tentativas++;
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
	
	public JComboBox getJcbUsuario() throws ErroAcessoRepositorioException, IllegalArgumentException {
		if (jcbUsuario == null) {
			ArrayList<Object[]> restricoes = new ArrayList<Object[]>();
			// Apenas pessoas do tipo usuario
			Object[] restr = { "tipoPessoa", new TipoPessoa(3), "eq" };
			restricoes.add(restr);
			Vector<Pessoa> listaUsuarios = new Vector<Pessoa>(Fachada
					.obterInstancia().listarPessoas(restricoes, new Object[]{"nome"}, true));
			jcbUsuario = new JComboBox();
			jcbUsuario.setBorder(BORDAS_TEXT_FIELD);
			DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(
					listaUsuarios);
			jcbUsuario.setModel(defaultComboBox);
				jcbUsuario.setSelectedIndex(0);
			jcbUsuario.setFocusable(true);
		}
		return jcbUsuario;
	}
	
	public JPasswordField getJpfSenha() {
		if (jpfSenha == null) {
			jpfSenha = new JPasswordField();
			jpfSenha.setBorder(BORDAS_TEXT_FIELD);
			jpfSenha.setDocument(new FixedLengthDocument(16, false, false));
			jpfSenha.setFocusable(true);
		}
		return jpfSenha;
	}
	
	public JLabel getJlMsgSenha() {
		if (jlMsgSenha == null) {
			jlMsgSenha = new JLabel();
			jlMsgSenha.setFocusable(false);
		}
		return jlMsgSenha;
	}
	
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

}

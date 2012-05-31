package br.com.tg.entidades;

public class Usuario extends Pessoa {
 
	private String login;
	private String senha;
	private TipoUsuario tipoUsuario;
	 
	public String getLogin() {
		return login;
	}
	 
	public void setLogin(String newLogin) {
		this.login = newLogin;
	 
	}
	 
	public String getSenha() {
		return senha;
	}
	 
	public void setSenha(String newSenha) {
		this.senha = newSenha;
	 
	}
	 
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}
	 
	public void setTipoUsuario(TipoUsuario newTipoUsuario) {
		this.tipoUsuario = newTipoUsuario;
	 
	}
	
	@Override
	public String toString() {
		return this.login;
	}
	 
}
 

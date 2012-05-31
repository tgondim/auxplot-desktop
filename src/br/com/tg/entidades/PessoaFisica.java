package br.com.tg.entidades;

import java.util.Calendar;

public class PessoaFisica extends Pessoa {
 
	private String cpf;
	private Calendar dataNascimento;
	
	public String getCpf() {
		return cpf;
	}
	 
	public void setCpf(String newCpf) {
		this.cpf = newCpf;
	 
	}
	 
	public Calendar getDataNascimento() {
		return dataNascimento;
	}
	 
	public void setDataNascimento(Calendar newDataNascimento) {
		this.dataNascimento = newDataNascimento;
	 
	}
	
}
 

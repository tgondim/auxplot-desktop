package br.com.tg.entidades;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PessoaJuridica extends Pessoa {
 
	private String nomeFantasia;
	private String cnpj;
	private Calendar dataAbertura;
	private short diaFatura;
	 
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	 
	public void setNomeFantasia(String newNomeFantasia) {
		this.nomeFantasia = newNomeFantasia;
	 
	}
	 
	public String getCnpj() {
		return cnpj;
	}
	 
	public void setCnpj(String newCnpj) {
		this.cnpj = newCnpj;
	 
	}
	 
	public Calendar getDataAbertura() {
		return dataAbertura;
	}
	 
	public void setDataAbertura(Calendar newDataAbertura) {
		this.dataAbertura = newDataAbertura;
	 
	}
	 
	public short getDiaFatura() {
		return diaFatura;
	}
	 
	public void setDiaFatura(short newDiaFatura) {
		this.diaFatura = newDiaFatura;
	 
	}
	
	public Calendar getProximoVencimento(Calendar data) {
		Calendar dataRetorno = data;
		if (this.diaFatura != 0) {
			switch (data.get(Calendar.MONTH)) {
			case Calendar.JANUARY:
				dataRetorno.set(Calendar.MONTH, Calendar.FEBRUARY);
				if (this.diaFatura > 28) {					
					if (new GregorianCalendar().isLeapYear(data.get(Calendar.YEAR))) {
						dataRetorno.set(Calendar.DATE, 29);
					} else {
						dataRetorno.set(Calendar.DATE, 28);
					}
				} else {
					dataRetorno.set(Calendar.DATE, this.diaFatura);
				}
				break;
			case Calendar.FEBRUARY:
				dataRetorno.set(Calendar.MONTH, Calendar.MARCH);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.MARCH:
				dataRetorno.set(Calendar.MONTH, Calendar.APRIL);
				if (this.diaFatura > 30) {
					dataRetorno.set(Calendar.DATE, 30);
				} else {
					dataRetorno.set(Calendar.DATE, this.diaFatura);
				}
				break;
			case Calendar.APRIL:
				dataRetorno.set(Calendar.MONTH, Calendar.MAY);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.MAY:
				dataRetorno.set(Calendar.MONTH, Calendar.JUNE);
				if (this.diaFatura > 30) {
					dataRetorno.set(Calendar.DATE, 30);
				} else {
					dataRetorno.set(Calendar.DATE, this.diaFatura);
				}
				break;
			case Calendar.JUNE:
				dataRetorno.set(Calendar.MONTH, Calendar.JULY);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.JULY:
				dataRetorno.set(Calendar.MONTH, Calendar.AUGUST);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.AUGUST:
				dataRetorno.set(Calendar.MONTH, Calendar.SEPTEMBER);
				if (this.diaFatura > 30) {
					dataRetorno.set(Calendar.DATE, 30);
				} else {
					dataRetorno.set(Calendar.DATE, this.diaFatura);
				}
				break;
			case Calendar.SEPTEMBER:
				dataRetorno.set(Calendar.MONTH, Calendar.OCTOBER);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.OCTOBER:
				dataRetorno.set(Calendar.MONTH, Calendar.NOVEMBER);
				if (this.diaFatura > 30) {
					dataRetorno.set(Calendar.DATE, 30);
				} else {
					dataRetorno.set(Calendar.DATE, this.diaFatura);
				}
				break;
			case Calendar.NOVEMBER:
				dataRetorno.set(Calendar.MONTH, Calendar.DECEMBER);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			case Calendar.DECEMBER:
				dataRetorno.set(Calendar.YEAR, data.get(Calendar.YEAR) + 1);
				dataRetorno.set(Calendar.MONTH, Calendar.JANUARY);
				dataRetorno.set(Calendar.DATE, this.diaFatura);
				break;
			}
		}
		return dataRetorno;
	}
	
}
 

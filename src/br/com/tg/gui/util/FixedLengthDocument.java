package br.com.tg.gui.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FixedLengthDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;

	private int iMaxLength;
	private boolean upperCase;
	private boolean onlyDigits;

	public FixedLengthDocument(int maxlen, boolean newUpper, boolean newOnlyDigits) {
		super();
		iMaxLength = maxlen;
		upperCase = newUpper;
		onlyDigits = newOnlyDigits;
	}

	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null)
			return;

		if (onlyDigits) {
			for( int i = 0; i < str.length(); i++ ) {  
				if( !Character.isDigit( str.charAt( i ) )) { 
					return;  
				}
			}  
		}
			
		if (iMaxLength <= 0) // aceitara qualquer no. de caracteres
		{
			super.insertString(offset, upperCase ? str.toUpperCase() : str, attr);
			return;
		}

		int ilen = (getLength() + str.length());		
		if (ilen <= iMaxLength) // se o comprimento final for menor...
			super.insertString(offset, upperCase ? str.toUpperCase() : str, attr); // ...aceita str
	}
}

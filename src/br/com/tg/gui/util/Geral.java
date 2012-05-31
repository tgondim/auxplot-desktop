package br.com.tg.gui.util;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class Geral {
	
	//realça a cor da borda de um componente
	public static void alterarCor(final JComponent tf, Color cor, boolean temporaria) {
		Border colorida = BorderFactory.createLineBorder(cor);
		final Border vazia = BorderFactory.createLineBorder(Color.lightGray);
		tf.setBorder(colorida);
		if (temporaria) {
			Thread colorTmp = new Thread() {
				public void run() {
					try {
						Thread.sleep(4000);
						tf.setBorder(vazia);
					} catch (InterruptedException e) {
						System.out.println("A thread de alterar cor foi interrompida.");
						e.printStackTrace();
					}
				}
			};
			colorTmp.start();
		}
	}

}

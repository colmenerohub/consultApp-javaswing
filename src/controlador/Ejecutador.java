package controlador;

import vista.VntPrincipal;

/**
 * Clase que contiene el main, crea la ventana
 * @author Mario Sanchez Colmenero
 */
public class Ejecutador {
	
	/**
	 * Metodo que ejecuta la aplicacion
	 * @param args
	 */
	public static void main(String[] args) {
		VntPrincipal frame = new VntPrincipal();
		frame.cargaVentana(frame);
	}
}
package modelo;

import javax.swing.Icon;

/**
 * Clase que define objetos para rellenar combobox con imágenes
 * @author Mario Sanchez Colmenero
 */
public class IconItem {
	private String texto;
	private Icon icono;
	
	/**
	 * Metodo constructor de IconItems 
	 * @param texto
	 * @param icono
	 */
	public IconItem(String texto, Icon icono) {
		super();
		this.texto = texto;
		this.icono = icono;
	}
	
	/**
	 * @return texto
	 */
	@Override
	public String toString() {
		return texto;
	}
	
	/**
	 * @param texto
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	/**
	 * @return icono
	 */
	public Icon getIcono() {
		return icono;
	}
	
	/**
	 * @param icono
	 */
	public void setIcono(Icon icono) {
		this.icono = icono;
	}
}
package vista;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import modelo.IconItem;

/**
 * Clase que permite insertar imagenes en combobox
 * @author Mario Sanchez Colmenero
 */
public class ImgComboRenderer extends JLabel implements ListCellRenderer<IconItem>{
	/**
	 * Metodo constructor
	 */
	public ImgComboRenderer() {
		setOpaque(true);
		
	}
	
	/**
	 * Metodo que inserta imagenes en combobox
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends IconItem> list, IconItem value, int index, 
			boolean isSelected, boolean cellHasFocus) {
		setText(value.toString());
		setIcon(value.getIcono());
		if(isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}
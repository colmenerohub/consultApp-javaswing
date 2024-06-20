package vista;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Clase que permite insertar imagenes en JTable
 * @author Mario Sanchez Colmenero
 */
public class ImgTablaRenderer extends DefaultTableCellRenderer {
	
	/**
	 * Metodo que inserta imagenes en JTable
	 */
	@Override
	public Component getTableCellRendererComponent(
			JTable table, 
			Object o,
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int column) {
		
		if (o instanceof JLabel) {
			JLabel lbl = (JLabel)o;
			return lbl;
		}
		
		return super.getTableCellRendererComponent(table, o, isSelected, hasFocus, row, column);
	}
}
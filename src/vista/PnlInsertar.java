package vista;

import javax.swing.JPanel;
import javax.swing.JTextField;

import controlador.BaseDeDatos;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ItemEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 * Clase que contiene el JPanel para insertar ciudades
 * @author Mario Sanchez Colmenero
 */
public class PnlInsertar extends JPanel {
	private JTextField txtCiudadesI;
	private JButton btnTick;
	private JButton btnCross;
	private JSpinner spnPoblacionI;
	private JComboBox cmbDistritosI;
	private JComboBox cmbPaisesI;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;

	/**
	 * Metodo constructor
	 */
	public PnlInsertar() {
		setLayout(null);
		BaseDeDatos bd = new BaseDeDatos();

		//Recoger ciudad
		txtCiudadesI = new JTextField();
		txtCiudadesI.setBounds(360, 153, 316, 41);
		add(txtCiudadesI);
		txtCiudadesI.setColumns(10);
		
		
		//Recoger pais
		cmbPaisesI = new JComboBox();
		cmbPaisesI.setBounds(360, 222, 316, 41);
		cargaComboPais(cmbPaisesI);
		add(cmbPaisesI);
		
		cmbPaisesI.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String selected = (String) cmbPaisesI.getSelectedItem();
				BaseDeDatos bd = new BaseDeDatos();
				ResultSet rs;
				rs = bd.consultarDistritosPorPais(selected);
				cmbDistritosI.removeAllItems();
				cmbDistritosI.addItem("Distritos");
				try {
					while(rs.next()) {
						cmbDistritosI.addItem(rs.getString("district"));
					}
					bd.desconectar();
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			}
		});
		

		//Recoger distrito
		cmbDistritosI = new JComboBox();
		cmbDistritosI.addItem("Distritos");
		cmbDistritosI.setBounds(360, 297, 316, 41);
		add(cmbDistritosI);

		
		//Recoger población
		spnPoblacionI = new JSpinner();
		spnPoblacionI.setBounds(360, 375, 316, 41);
		add(spnPoblacionI);
		

		//Botón que confirma la inserción
		btnTick = new JButton("✔");
		btnTick.setBackground(new Color(0, 255, 0));
		btnTick.setBounds(360, 443, 53, 31);
		add(btnTick);
		
		btnTick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!txtCiudadesI.getText().equals("") && cmbPaisesI.getSelectedItem().toString() != "Países" && cmbDistritosI.getSelectedItem().toString() != "Distritos" && (int)spnPoblacionI.getValue() != 0) {
					bd.insertar(
							txtCiudadesI.getText(), 
							cmbPaisesI.getSelectedItem().toString(),
							cmbDistritosI.getSelectedItem().toString(),
							(int)spnPoblacionI.getValue()
					);
					JOptionPane.showMessageDialog(null, "Ciudad insertada");
				} else {
					JOptionPane.showMessageDialog(null, "Datos incompletos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		//Botón que limpia los datos escritos
		btnCross = new JButton("X");
		btnCross.setBackground(new Color(255, 0, 0));
		btnCross.setBounds(623, 443, 53, 31);
		add(btnCross);
		
		btnCross.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCiudadesI.setText("");
				cmbPaisesI.setSelectedItem("Países");
				cmbDistritosI.setSelectedItem("Distritos");
				spnPoblacionI.setValue(0);
			}
		});
		
		
		//Elementos del JPanel
		lblNewLabel_1 = new JLabel("NUEVA CIUDAD");
		lblNewLabel_1.setForeground(new Color(139, 69, 19));
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Tempus Sans ITC", Font.BOLD | Font.ITALIC, 42));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(481, 513, 353, 73);
		add(lblNewLabel_1);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("src/multimedia/mapa.jpg"));
		lblNewLabel.setBounds(0, 0, 1050, 633);
		add(lblNewLabel);
	}
	
	/**
	 *  Metodo que vuelca en un combobox los paises de base de datos
	 * @param cmbActual
	 */
	public void cargaComboPais(JComboBox cmbActual){
		BaseDeDatos bd = new BaseDeDatos();
		ResultSet rs = bd.consultarPaisesResultSet();
		cmbActual.addItem("Países");
		try {
			while(rs.next()) {
				cmbActual.addItem(rs.getString("name"));
			}
			bd.desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
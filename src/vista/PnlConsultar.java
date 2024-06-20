package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controlador.BaseDeDatos;
import controlador.Ficheros;
import modelo.City;
import modelo.IconItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Clase que contiene el JPanel para consultar, modificar y borrar ciudades
 * @author Mario Sanchez Colmenero
 */
public class PnlConsultar extends JPanel {
	private JTextField txtCiudades;
	private JButton btnXML;
	private JButton btnSQL;
	private JButton btnXSL;
	private JScrollPane scrList;
	private JToolBar toolBar;
	private JSlider sldPoblacion;
	private JList lstDistritos;
	DefaultListModel modeloList = new DefaultListModel();
	private JComboBox<IconItem> cmbPaises;
	private JScrollPane scrTable;
	private JTable tblCiudades;
	DefaultTableModel modeloTabla = new DefaultTableModel();
	private JButton btnModificar;
	private JButton btnBorrar;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JButton btnReset;

	/**
	 * Metodo constructor
	 */
	public PnlConsultar() {
		setLayout(null);
		BaseDeDatos bd = new BaseDeDatos();
		Ficheros f = new Ficheros();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setVisible(false);
				btnBorrar.setVisible(false);
			}
		});
		
		//Boton reset para filtros
		btnReset = new JButton("🔄");
		btnReset.setBounds(147, 97, 49, 23);
		add(btnReset);
		
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCiudades.setText("");
				cmbPaises.setSelectedIndex(0);
				lstDistritos.setSelectedIndex(0);
				sldPoblacion.setValue(0);
				modeloTabla.setRowCount(0);
			}
		});
		
		//Elementos JTable
		scrTable = new JScrollPane(); 
		scrTable.setBounds(253, 131, 771, 391);
		scrTable.setViewportView(tblCiudades);
		add(scrTable);
		
		tblCiudades = new JTable();
		scrTable.setViewportView(tblCiudades);
		
		modeloTabla.setColumnIdentifiers(new Object[]{"Ciudad", "Distrito", "País", "Continente", "Idioma Oficial", "Población", "Bandera"});
		tblCiudades.setDefaultRenderer(Object.class, new ImgTablaRenderer());
		tblCiudades.setModel(modeloTabla);
		
		//Datos centrados
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tblCiudades.getColumnModel().getColumn(0).setCellRenderer(tcr);
		tblCiudades.getColumnModel().getColumn(1).setCellRenderer(tcr);
		tblCiudades.getColumnModel().getColumn(2).setCellRenderer(tcr);
		tblCiudades.getColumnModel().getColumn(3).setCellRenderer(tcr);
		tblCiudades.getColumnModel().getColumn(4).setCellRenderer(tcr);
		tblCiudades.getColumnModel().getColumn(5).setCellRenderer(tcr);
		
		tblCiudades.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setVisible(true);
				btnBorrar.setVisible(true);
			}
		});
		
		
		//Filtro por poblacion
		sldPoblacion = new JSlider(SwingConstants.HORIZONTAL, 0, 2000000, 50000);
		sldPoblacion.setOpaque(false);
		sldPoblacion.setFont(new Font("Tahoma", Font.BOLD, 7));
		sldPoblacion.setBounds(29, 537, 995, 67);
		sldPoblacion.setPaintTicks(true);
		sldPoblacion.setPaintLabels(true);
		sldPoblacion.setMajorTickSpacing(100000);
		sldPoblacion.setValue(0);
		add(sldPoblacion);
		
		//Sustituye las cifras por K
		DecimalFormat format = new DecimalFormat("0K");
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for(int i=sldPoblacion.getMinimum(); i<=sldPoblacion.getMaximum(); i+=sldPoblacion.getMajorTickSpacing()) {
			double scaledValue = i / 1000.0;
			String label = format.format(scaledValue);
			labelTable.put(i, new JLabel(label));
		}
		sldPoblacion.setLabelTable(labelTable);
		
		sldPoblacion.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				btnModificar.setVisible(false);
				btnBorrar.setVisible(false);
				
				int selected = sldPoblacion.getValue();
				String pais = null;
				String distrito = null;
				String ciudad = null;
				
				if (!txtCiudades.getText().equals("Ciudad...")) {
					ciudad = txtCiudades.getText();
				}
				
				if (cmbPaises.getSelectedItem() != null && cmbPaises.getSelectedItem().toString() != "Paises") {
					pais = cmbPaises.getSelectedItem().toString();
				}
				
				if (lstDistritos != null && (String) lstDistritos.getSelectedValue() != "Distritos") {
					distrito = (String) lstDistritos.getSelectedValue();
				}
				
				modeloTabla.setRowCount(0);
				for(City p : bd.filtroGlobal(ciudad, pais, distrito, selected)) {
					ImageIcon img;
					try {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/" + p.getBandera()));
					} catch (NullPointerException n) {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/NOO.png"));
					}
					modeloTabla.addRow(new Object[] {p.getCiudad(), p.getDistrito(), p.getPais(), p.getContinente(), p.getIdioma(), p.getPoblacion(), 
							new JLabel(img)});
				}
			}
		});
		
		
		//Filtro por nombre de ciudad
		txtCiudades = new JTextField();
		txtCiudades.setForeground(new Color(128, 128, 128));
		txtCiudades.setBounds(29, 131, 191, 32);
		txtCiudades.setColumns(10);
		txtCiudades.setText("Ciudad...");
		add(txtCiudades);
		
		txtCiudades.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtCiudades.setText("");
				txtCiudades.setForeground(new Color(0, 0, 0));
			}
		});
		
		txtCiudades.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnModificar.setVisible(false);
				btnBorrar.setVisible(false);
				
				String selected = txtCiudades.getText();
				String pais = null;
				String distrito = null;
				
				if (cmbPaises.getSelectedItem() != null && cmbPaises.getSelectedItem().toString() != "Paises") {
					pais = cmbPaises.getSelectedItem().toString();
				}
				
				if (lstDistritos != null && (String) lstDistritos.getSelectedValue() != "Distritos") {
					distrito = (String) lstDistritos.getSelectedValue();
				}

				modeloTabla.setRowCount(0);
				for(City p : bd.filtroGlobal(selected, pais, distrito, sldPoblacion.getValue())) {
					ImageIcon img;
					try {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/" + p.getBandera()));
					} catch (NullPointerException n) {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/NOO.png"));
					}
					modeloTabla.addRow(new Object[] {p.getCiudad(), p.getDistrito(), p.getPais(), p.getContinente(), p.getIdioma(), p.getPoblacion(), new JLabel(img)});
				}
			}
		});
		

		//Filtro por pais
		cmbPaises = new JComboBox<>();
		cmbPaises.setRenderer(new ImgComboRenderer());
		cmbPaises.setBounds(29, 187, 191, 32);
		add(cmbPaises);
		cargaComboPais();
		
		cmbPaises.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				btnModificar.setVisible(false);
				btnBorrar.setVisible(false);
				
				String selected = cmbPaises.getSelectedItem().toString();
				String ciudad = null;
				String distrito = null;
				
				if (!txtCiudades.getText().equals("Ciudad...")) {
					ciudad = txtCiudades.getText();
				}
				
				if (lstDistritos != null && (String) lstDistritos.getSelectedValue() != "Distritos") {
					distrito = (String) lstDistritos.getSelectedValue();
				}
				
				if (selected.equals("Paises")) {
					selected = null;
				}
				
				modeloTabla.setRowCount(0);
				for(City p : bd.filtroGlobal(ciudad, selected, distrito, sldPoblacion.getValue())) {
					ImageIcon img;
					try {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/" + p.getBandera()));
					} catch (NullPointerException n) {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/NOO.png"));
					}
					modeloTabla.addRow(new Object[] {p.getCiudad(), p.getDistrito(), p.getPais(), p.getContinente(), p.getIdioma(), p.getPoblacion(), new JLabel(img)});
				}
			}
		});
		

		//Filtro por distrito
		scrList = new JScrollPane();
		scrList.setBounds(29, 250, 191, 272);
		add(scrList);
		
		lstDistritos = cargaListDistritos();
		scrList.setViewportView(lstDistritos);

		lstDistritos.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				btnModificar.setVisible(false);
				btnBorrar.setVisible(false);
				
				String selected = (String) lstDistritos.getSelectedValue();
				String pais = null;
				String ciudad = null;
				
				if (!txtCiudades.getText().equals("Ciudad...")) {
					ciudad = txtCiudades.getText();
				}
				
				if (cmbPaises.getSelectedItem() != null && cmbPaises.getSelectedItem().toString() != "Paises") {
					pais = cmbPaises.getSelectedItem().toString();
				}
				
				if (selected.equals("Distritos")) {
					selected = null;
				}
				
				modeloTabla.setRowCount(0);
				for(City p : bd.filtroGlobal(ciudad, pais, selected, sldPoblacion.getValue())) {
					ImageIcon img;
					try {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/" + p.getBandera()));
					} catch (NullPointerException n) {
						img = new ImageIcon(getClass().getResource("/multimedia/banderas/NOO.png"));
					}
					modeloTabla.addRow(new Object[] {p.getCiudad(), p.getDistrito(), p.getPais(), p.getContinente(), p.getIdioma(), p.getPoblacion(), new JLabel(img)});
				}
			}
		});
		
		
		//Botones para exportar JTable
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(253, 89, 128, 45);
		add(toolBar);
		
		
		//Exportar a XML
		btnXML = new JButton("");
		btnXML.setIcon(new ImageIcon("src/multimedia/xml.png"));
		toolBar.add(btnXML);
		
		btnXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tblCiudades.getRowCount() != 0) {
					ArrayList<City> arrlCities = new ArrayList<>();
					for (int i = 0; i < tblCiudades.getRowCount(); i++) {
						City filaActual = new City(
			            		(String) modeloTabla.getValueAt(i, 0), 
			            		(String) modeloTabla.getValueAt(i, 1), 
			            		(String) modeloTabla.getValueAt(i, 2), 
			            		(String) modeloTabla.getValueAt(i, 3), 
			            		(String) modeloTabla.getValueAt(i, 4), 
			            		(int) modeloTabla.getValueAt(i, 5),
			            		modeloTabla.getValueAt(i, 6).toString()
			            );
						arrlCities.add(filaActual);
					}
					try {
						f.escribirXML(arrlCities);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Tabla vacia", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		});
		
		
		//Exportar a SQL
		btnSQL = new JButton("");
		btnSQL.setIcon(new ImageIcon("src/multimedia/sql.png"));
		toolBar.add(btnSQL);
		
		btnSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tblCiudades.getRowCount() != 0) {
					ArrayList<City> arrlCities = new ArrayList<>();
					for (int i = 0; i < tblCiudades.getRowCount(); i++) {
						City filaActual = new City(
			            		(String) modeloTabla.getValueAt(i, 0), 
			            		(String) modeloTabla.getValueAt(i, 1), 
			            		(String) modeloTabla.getValueAt(i, 2), 
			            		(String) modeloTabla.getValueAt(i, 3), 
			            		(String) modeloTabla.getValueAt(i, 4), 
			            		(int) modeloTabla.getValueAt(i, 5),
			            		modeloTabla.getValueAt(i, 6).toString()
			            );
						arrlCities.add(filaActual);
					}
					try {
						f.escribirSQL(arrlCities);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Tabla vacia", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		
		//Exportar a XSL
		btnXSL = new JButton("");
		btnXSL.setIcon(new ImageIcon("src/multimedia/excell.png"));
		toolBar.add(btnXSL);
		
		btnXSL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tblCiudades.getRowCount() != 0) {
			        try {
			        	f.escribirXSL(tblCiudades);
			        } catch (IOException ex) {
			            System.out.println("Error: " + ex);
			        }
				} else {
					JOptionPane.showMessageDialog(null, "Tabla vacia", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		
		//Boton para modificar ciudades
		btnModificar = new JButton("");
		btnModificar.setToolTipText("Modificar");
		btnModificar.setIcon(new ImageIcon("src/multimedia/llave.png"));
		btnModificar.setBounds(916, 82, 49, 38);
		add(btnModificar);
		
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tblCiudades.getSelectedRow();
				if (selectedRow != -1) {
					City cityActual = new City(
		            		(String)modeloTabla.getValueAt(selectedRow, 0), 
		            		(String) modeloTabla.getValueAt(selectedRow, 1), 
		            		(String) modeloTabla.getValueAt(selectedRow, 2), 
		            		(String) modeloTabla.getValueAt(selectedRow, 3), 
		            		(String) modeloTabla.getValueAt(selectedRow, 4), 
		            		(int) modeloTabla.getValueAt(selectedRow, 5),
		            		modeloTabla.getValueAt(selectedRow, 6).toString()
		            );

					JTextField txtCiudades = new JTextField();
					txtCiudades.setText(cityActual.getCiudad());
					// Crear los JComboBox
					String[] opciones1 = (String[]) bd.consultarDistritos().toArray(new String[0]);
					JComboBox<String> comboBox1 = new JComboBox<>(opciones1);
					comboBox1.setSelectedItem(cityActual.getDistrito());

					String[] opciones2 = (String[]) bd.consultarPaisesList().toArray(new String[0]);
					JComboBox<String> comboBox2 = new JComboBox<>(opciones2);
					comboBox2.setSelectedItem(cityActual.getPais());
					comboBox2.setEnabled(false);
					
					JSpinner spnPoblacion = new JSpinner();
					spnPoblacion.setValue(cityActual.getPoblacion());
					
					JLabel lblCiudad = new JLabel("Ciudad");
					lblCiudad.setAlignmentX(SwingConstants.LEADING);
					JLabel lblDistrito = new JLabel("Distrito");
					lblDistrito.setAlignmentX(SwingConstants.LEADING);
					JLabel lblPais= new JLabel("Pais");
					lblPais.setAlignmentX(SwingConstants.LEADING);
					JLabel lblPoblacion = new JLabel("Población");
					lblPoblacion.setAlignmentX(SwingConstants.LEADING);

					// Crear el JPanel y agregar los JComboBox
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.add(lblCiudad);
					panel.add(txtCiudades);
					panel.add(lblDistrito);
					panel.add(comboBox1);
					panel.add(lblPais);
					panel.add(comboBox2);
					panel.add(lblPoblacion);
					panel.add(spnPoblacion);

					// Mostrar el cuadro de diálogo con el JPanel
					int result = JOptionPane.showOptionDialog(null, panel, "Modificar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

					// Obtener las opciones seleccionadas
					if (result == JOptionPane.OK_OPTION) {
						City cityNueva = new City(
								txtCiudades.getText(),
								comboBox1.getSelectedItem().toString(),
								comboBox2.getSelectedItem().toString(),
								null,
								null,
								(int) spnPoblacion.getValue(),
								null
					    );
						
					    bd.modificar(cityActual, cityNueva);
	    	        	modeloTabla.setValueAt(cityNueva.getCiudad(), selectedRow, 0);
	    	        	modeloTabla.setValueAt(cityNueva.getDistrito(), selectedRow, 1);
	    	        	modeloTabla.setValueAt(cityNueva.getPoblacion(), selectedRow, 5);
	    				JOptionPane.showMessageDialog(null, "Ciudad modificada");
					}
				}
			}
		});
		
		//Boton para borrar ciudades
		btnBorrar = new JButton("");
		btnBorrar.setToolTipText("Borrar");
		btnBorrar.setIcon(new ImageIcon("src/multimedia/papelera.png"));
		btnBorrar.setBounds(975, 82, 49, 38);
		add(btnBorrar);
		
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tblCiudades.getSelectedRow();
				City cityActual = new City(
	            		(String)modeloTabla.getValueAt(selectedRow, 0), 
	            		(String) modeloTabla.getValueAt(selectedRow, 1), 
	            		(String) modeloTabla.getValueAt(selectedRow, 2), 
	            		(String) modeloTabla.getValueAt(selectedRow, 3), 
	            		(String) modeloTabla.getValueAt(selectedRow, 4), 
	            		(int) modeloTabla.getValueAt(selectedRow, 5),
	            		modeloTabla.getValueAt(selectedRow, 6).toString()
	            );

				int result = JOptionPane.showOptionDialog(null, "¿Realmente desea eliminar la fila?", "Borrar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
				if (result == JOptionPane.OK_OPTION) {
					bd.borrar(cityActual);
					modeloTabla.removeRow(selectedRow);
    				JOptionPane.showMessageDialog(null, "Ciudad borrada");
				}
			}
		});
		
		//Escondemos botones al inicio de la app
		btnModificar.setVisible(false);
		btnBorrar.setVisible(false);
		
		//Elementos del JPanel
		lblNewLabel_1 = new JLabel("CONSULTAR CIUDADES");
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Eras Medium ITC", Font.BOLD, 42));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(351, 31, 544, 53);
		add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Filtros\r\n");
		lblNewLabel_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(46, 88, 118, 32);
		add(lblNewLabel_2);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("src/multimedia/consultar.jpg"));
		lblNewLabel.setBounds(0, 0, 1106, 658);
		add(lblNewLabel);
	}
	
	/**
	 * Metodo que vuelca en un combobox los paises de base de datos
	 */
	public void cargaComboPais(){
		BaseDeDatos bd = new BaseDeDatos();
		ResultSet rs = bd.consultarPaisesResultSet();
		IconItem uno = new IconItem("Paises", null);
		IconItem segundo; 
		cmbPaises.addItem(uno);
		ImageIcon img;
		try {
			while(rs.next()) {
				try {
					img = new ImageIcon(getClass().getResource("/multimedia/banderas/" + rs.getString("code").concat(".png")));
				} catch (NullPointerException n) {
					img = new ImageIcon(getClass().getResource("/multimedia/banderas/NOO.png"));
				}
				segundo = new IconItem(rs.getString("name"), img);
				cmbPaises.addItem(segundo);
			}
			bd.desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que vuelca en una lista los distritos de base de datos
	 * @return list
	 */
	public JList cargaListDistritos(){
		BaseDeDatos bd = new BaseDeDatos();
		List rs = bd.consultarDistritos();
		modeloList.addElement("Distritos");
		for(int i = 0; i < rs.size(); i++) {
			modeloList.addElement(rs.get(i));
		}
		JList list = new JList(modeloList);
		return list;
	}
}
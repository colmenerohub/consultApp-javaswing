package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Toolkit;

/**
 * Clase que contiene el JFrame
 * @author Mario Sanchez Colmenero
 */
public class VntPrincipal extends JFrame {

	private JPanel contentPane;
	private JPanel pnlIndex;

	/**
	 * Metodo que carga el JFrame
	 */
	public static void cargaVentana(VntPrincipal frame) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Metodo constructor del JFrame
	 */
	public VntPrincipal() {
		setTitle("CONSULTAPP");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/multimedia/logo.png"));
		setBackground(new Color(0, 0, 0));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1076, 698);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Operaciones");
		mnNewMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Consultar\r\n");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PnlConsultar c = new PnlConsultar();
				nuevoPanel(c);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Insertar\r\n");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PnlInsertar i = new PnlInsertar();
				nuevoPanel(i);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("Help");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Acerca de");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

		        String tanque = ("\n                        ( '_')\n"
		        				+" ---------    /************\\============> \n"
		        				+"--------   /*********************\\\n"
		        				+"-------   /******************MSC**\\\n"
		        				+" -------  \\@_@_@_@_@_@_@/");
		        
				JOptionPane.showMessageDialog(null, tanque, "JUAN XXIII", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/multimedia/logo.png")));
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		pnlIndex = new JPanel();
		contentPane.add(pnlIndex, "name_23527433181500");
		pnlIndex.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("src/multimedia/fondo.png"));
		lblNewLabel.setBounds(0, 23, 1063, 633);
		pnlIndex.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("BIENVENIDO A LA CONSULTAPP");
		lblNewLabel_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 32));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(44, 42, 505, 82);
		pnlIndex.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Gestionamos tus ciudades\r\n");
		lblNewLabel_2.setFont(new Font("Yu Gothic", Font.PLAIN, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(126, 104, 313, 23);
		pnlIndex.add(lblNewLabel_2);
	}
	
	/**
	 * Metodo que cambia paneles con CardLayout
	 * @param panelActual 
	 */
	public void nuevoPanel(JPanel panelActual) {
		contentPane.removeAll();
		contentPane.add(panelActual);
		contentPane.repaint();
		contentPane.revalidate();
	}
}
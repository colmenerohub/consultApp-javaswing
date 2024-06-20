package controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import modelo.City;

/**
 * Clase que exporta a distintos archivos
 * @author Mario Sanchez Colmenero
 */
public class Ficheros {
	
	/**
	 * Metodo para exportar ArrayList a xml
	 * @param arrayList
	 * @throws IOException
	 */
	public void escribirXML(ArrayList<City> arrayList) throws IOException {
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de xml", "xml");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xml");
            
	        File archivo = new File(ruta);
	        PrintWriter pw = null;
	        
	        try {
	            pw = new PrintWriter(new FileWriter (archivo, false));
	        	pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        	pw.println("<world>");
	            for (int i = 0; i < arrayList.size(); i++) {
	            	pw.println("\t<ciudad>");
	            	pw.println("\t\t<nombre>" + arrayList.get(i).getCiudad() + "</nombre>");
	            	pw.println("\t\t<distrito>" + arrayList.get(i).getDistrito() + "</distrito>");
	            	pw.println("\t\t<pais>" + arrayList.get(i).getPais() + "</pais>");
	            	pw.println("\t\t<continente>" + arrayList.get(i).getContinente() + "</continente>");
	            	pw.println("\t\t<idioma oficial>" + arrayList.get(i).getIdioma() + "</idioma oficial>");
	            	pw.println("\t\t<poblacion>" + arrayList.get(i).getPoblacion() + "</poblacion>");
	            	pw.println("\t</ciudad>");
	            }
	        	pw.println("</world>");
				JOptionPane.showMessageDialog(null, "XML exportado");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            if (pw != null) {
	                pw.close();
	            }
	        }
        }
    }
	
	/**
	 * Metodo para exportar ArrayList a sql
	 * @param arrayList
	 * @throws IOException
	 */
	public void escribirSQL(ArrayList<City> arrayList) throws IOException {
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de sql", "sql");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".sql");
	        File archivo = new File(ruta);
	        PrintWriter pw = null;
	        
	        try {
	        	if(!archivo.exists()) {
	                pw = new PrintWriter(new FileWriter (archivo, false));
	            	pw.println("use world");
	            	pw.println("create table contenidoJTable ("
	            			+ "\n\tciudad VARCHAR(50), "
	            			+ "\n\tdistrito VARCHAR(50), "
	            			+ "\n\tpais VARCHAR(50), "
	            			+ "\n\tcontinente VARCHAR(50), "
	            			+ "\n\tidiomaOficial VARCHAR(50), "
	            			+ "\n\tpoblacion INT"
	            			+ "\n);\n");
	        	} else {
	        		pw = new PrintWriter(new FileWriter (archivo, true));
	        	}
	        	
	            for (int i = 0; i < arrayList.size(); i++) {
	            	pw.println("insert into contenidoJTable values ('"
	            	+ arrayList.get(i).getCiudad() + "','"
	                + arrayList.get(i).getDistrito() + "','"
	                + arrayList.get(i).getPais() + "','"
	                + arrayList.get(i).getContinente() + "','"
	                + arrayList.get(i).getIdioma() + "',"
	                + arrayList.get(i).getPoblacion() + ");");
	            }
				JOptionPane.showMessageDialog(null, "SQL exportado");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            if (pw != null) {
	                pw.close();
	            }
	        }
        }
    }
	
	/**
	 * Metodo para exportar ArrayList a xls
	 * @param t
	 * @throws IOException
	 */
	public void escribirXSL(JTable t) throws IOException{
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xls");
            try {
                File archivoXLS = new File(ruta);
                if (archivoXLS.exists()) {
                    archivoXLS.delete();
                }
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("Mi hoja de trabajo 1");
                hoja.setDisplayGridlines(false);
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.createRow(f);
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.createCell(c);
                        if (f == 0) {
                            celda.setCellValue(t.getColumnName(c));
                        }
                    }
                }
                int filaInicio = 1;
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.createRow(filaInicio);
                    filaInicio++;
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.createCell(c);
                        if (t.getValueAt(f, c) instanceof Double) {
                            celda.setCellValue(Double.parseDouble(t.getValueAt(f, c).toString()));
                        } else if (t.getValueAt(f, c) instanceof Float) {
                            celda.setCellValue(Float.parseFloat((String) t.getValueAt(f, c)));
                        } else {
                            celda.setCellValue(String.valueOf(t.getValueAt(f, c)));
                        }
                    }
                }
                libro.write(archivo);
                archivo.close();
				JOptionPane.showMessageDialog(null, "XLS exportado");
            } catch (IOException | NumberFormatException e) {
                throw e;
            }
        }
	}
}
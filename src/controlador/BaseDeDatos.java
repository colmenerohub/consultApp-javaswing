package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import modelo.City;

/**
 * Clase que interactua con base de datos
 * @author Mario Sanchez Colmenero
 */
public class BaseDeDatos {
	
	Connection conexion;
	Statement consulta = null;
	Map<String, String> idiomas;
	
	/**
	 * Metodo constructor
	 */
	public BaseDeDatos() {
		setIdiomasOficiales();
	}

	/**
	 * Metodo que realiza la conexion a base de datos
	 * @return consulta
	 */
	public Statement conectar() {
		try {
			//conexion = DriverManager.getConnection("jdbc:mysql://localhost/world", "root", "");
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/world", "paises2023", "paises2023");
			consulta = conexion.createStatement();
		} catch (CommunicationsException err) {
			System.err.println("\n\t(╬ ಠ益ಠ)\n\n"
							+ " BASE DE DATOS NO INICIADA");
			JOptionPane.showMessageDialog(null, "Base de datos no iniciada", "Error", JOptionPane.ERROR_MESSAGE);
			System. exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return consulta;
	}
	
	/**
	 * Metodo que realiza la desconexion a base de datos
	 */
	public void desconectar() {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que consulta los paises de base de datos y devuelve ResultSet
	 * @return rs
	 */
	public ResultSet consultarPaisesResultSet() {
		Statement consultaActual = conectar();
		ResultSet rs = null;
		
		try {
			rs = consultaActual.executeQuery("select DISTINCT name, code from country order by name");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 *  Metodo que consulta los paises de base de datos y devuelve List
	 * @return paises
	 */
	public List consultarPaisesList() {
		Statement consultaActual = conectar();
		ResultSet rs = null;
		
		List arrayPaises = new ArrayList<>();
		
		try {
			rs = consultaActual.executeQuery("select DISTINCT name, code from country order by name");
			while(rs.next()) {
				arrayPaises.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrayPaises;
	}
	
	/**
	 * Metodo que consulta los distritos de base de datos y devuelve List
	 * @return arrayDistritos
	 */
	public List consultarDistritos() {
		Statement consultaActual = conectar();
		ResultSet rs = null;
		
		List arrayDistritos = new ArrayList<>();
		
		try {
			rs = consultaActual.executeQuery("select DISTINCT district from city WHERE district != '' order by district");
			
			while(rs.next()) {
				arrayDistritos.add(rs.getString("district"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrayDistritos;
	}
	
	/**
	 * Metodo que consulta los distritos de un pais de base de datos
	 * @param selected
	 * @return rs
	 */
	public ResultSet consultarDistritosPorPais(String selected) {
		Statement consultaActual = conectar();
		ResultSet rs = null;
		
		try {
			rs = consultaActual.executeQuery("select DISTINCT district from city ci, country co "
											+ "WHERE district != '' "
											+ "and countrycode = code AND code = (SELECT code from country where name = '" + selected + "') order by district");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * Metodo que consulta los idiomas oficiales de base de datos al iniciar la aplicacion
	 */
	public void setIdiomasOficiales() {
		Statement consultaActual = conectar();
		
		//Mapa que recoge el nombre de pais y sus respectivos idiomas oficiales
		idiomas = new HashMap();
		
		try {
			ResultSet rs = consultaActual.executeQuery("select DISTINCT name, group_concat(DISTINCT language) "
														+ "from countrylanguage, country "
														+ "WHERE isOfficial = 'T' "
														+ "AND code = countrycode GROUP BY 1");
			while(rs.next()) {
				idiomas.put(rs.getString("name"), rs.getString("group_concat(DISTINCT language)"));
			}
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que consulta de base de datos siguiendo o no filtros
	 * @param ciudad
	 * @param pais
	 * @param distrito
	 * @param poblacion
	 * @return arrlCities
	 */
	public ArrayList<City> filtroGlobal(String ciudad, String pais, String distrito, int poblacion) {
		Statement consultaActual = conectar();

		ArrayList<City> arrlCities = new ArrayList<>();
		String query = "";
		
		//Generamos la query según los filtros utilizados
		if(ciudad != null && !ciudad.equals("")) {
			query += " AND ci.name like '%" + ciudad + "%'";
		} 
		
		if(pais != null) {
			query += " AND co.name = '" + pais + "'";
		} 
		
		if(distrito != null) {
			query += " AND district = '" + distrito + "'";
		} 
		
		if(poblacion != -1) {
			query += " AND ci.population >= " + poblacion;
		} 
		
		try {
			ResultSet rs = consultaActual.executeQuery("select ci.name, code, district, co.name, continent, ci.population "
														+ "from country co, city ci, countrylanguage cl "
														+ "WHERE ci.countrycode = code AND code = cl.countrycode"
														+ query
														+ " GROUP BY 1 order by ci.name");
			while(rs.next()) {
				String idiomasCheckeados = "";
				if (idiomas.get(rs.getString("co.name")) != null) {
					idiomasCheckeados += idiomas.get(rs.getString("co.name"));
				} else {
					idiomasCheckeados = ":(";
				}
				
				City cityActual = new City(
						rs.getString("ci.name"),
						rs.getString("district"), 
						rs.getString("co.name"),
						rs.getString("continent"),
						idiomasCheckeados, 
						rs.getInt("population"), 
						rs.getString("code").concat(".png")
				);
				arrlCities.add(cityActual);
			}
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrlCities;
	}
	
	/**
	 * Metodo que modifica ciudades
	 * @param ciudadActual
	 * @param ciudadNueva
	 */
	public void modificar(City ciudadActual, City ciudadNueva) {
		Statement consultaActual = conectar();
		try {
			int valor = consultaActual.executeUpdate("update city "
					+ "set name ='" + ciudadNueva.getCiudad() + "', "
					+ "district ='" + ciudadNueva.getDistrito() + "', "
					+ "population = " + ciudadNueva.getPoblacion()
					+ " where name = '" + ciudadActual.getCiudad() + "'"
					+ " AND district = '" + ciudadActual.getDistrito() + "'"
					+ " AND population = " + ciudadActual.getPoblacion());
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que borra ciudades 
	 * @param ciudadActual
	 */
	public void borrar(City ciudadActual) {
		Statement consultaActual = conectar();
		try {
			consultaActual.executeUpdate("delete from city"
										+ " where name ='" + ciudadActual.getCiudad() + "'"
										+ " AND district = '" + ciudadActual.getDistrito() + "'"
										+ " AND population = " + ciudadActual.getPoblacion()
			);
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que inserta nuevas ciudades
	 * @param ciudad
	 * @param pais
	 * @param distrito
	 * @param poblacion
	 */
	public void insertar(String ciudad, String pais, String distrito, int poblacion) {
		Statement consultaActual = conectar();
		ResultSet rs;
		try {
			rs = consultaActual.executeQuery("select code from country where name = '" + pais + "'");
			if(rs.next()){
				String countrycode = rs.getString("code");
				consultaActual.executeUpdate("insert into city (name, countrycode, district, population) values ('" 
						+ ciudad + "', '"
						+ countrycode + "', '"
						+ distrito + "', " 
						+ poblacion + ")");
			}
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
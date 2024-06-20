package modelo;

/**
 * Clase que define objetos para ciudades
 * @author Mario Sanchez Colmenero
 */
public class City {
	private String ciudad;
	private String distrito;
	private String pais;
	private String continente;
	private String idioma;
	private int poblacion;
	private String bandera;
	
	/**
	 * Metodo constructor de ciudades
	 */
	public City() {
		ciudad = "";
		distrito = "";
		pais = "";
		continente = "";
		idioma = "";
		poblacion = 0;
		bandera = "";
	}
	
	/**
	 * Metodo constructor por parametros de ciudades
	 * @param ciudad
	 * @param distrito
	 * @param pais
	 * @param continente
	 * @param idioma
	 * @param poblacion
	 * @param bandera
	 */
	public City(String ciudad, String distrito, String pais, String continente, String idioma, int poblacion, String bandera) {
		this.ciudad = ciudad;
		this.distrito = distrito;
		this.pais = pais;
		this.continente = continente;
		this.idioma = idioma;
		this.poblacion = poblacion;
		this.bandera = bandera;
	}
	
	/**
	 * @return ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return distrito
	 */
	public String getDistrito() {
		return distrito;
	}

	/**
	 * @param distrito
	 */
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	
	/**
	 * @return pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return continente
	 */
	public String getContinente() {
		return continente;
	}

	/**
	 * @param continente
	 */
	public void setContinente(String continente) {
		this.continente = continente;
	}

	/**
	 * @return idioma
	 */
	public String getIdioma() {
		return idioma;
	}

	/**
	 * @param idioma
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	/**
	 * @return poblacion
	 */
	public int getPoblacion() {
		return poblacion;
	}

	/**
	 * @param poblacion
	 */
	public void setPoblacion(int poblacion) {
		this.poblacion = poblacion;
	}

	/**
	 * @return bandera
	 */
	public String getBandera() {
		return bandera;
	}

	/**
	 * @param bandera
	 */
	public void setBandera(String bandera) {
		this.bandera = bandera;
	}
}
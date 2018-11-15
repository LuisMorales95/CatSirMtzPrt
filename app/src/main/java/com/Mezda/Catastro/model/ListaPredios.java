package com.Mezda.Catastro.model;

public class ListaPredios {

	private String IdPredio;
	private String Letra;
	private String Area;
	private String Antiguedad;
	private String ValorC;
	private String ClaveTipo;
	private String IdTipo;
	private String ClaveEstado;
	private String IdEstado;
	private String ClaveTermino;
	private String IdTermino;
    private String thumbnailUrl;
	private boolean isDown;

	public ListaPredios() {}

	public ListaPredios(String letra, String area, String antiguedad, String valorC, String claveTipo, String claveEstado, String claveTermino, String thumbnailUrl) {
		Letra = letra;
		Area = area;
		Antiguedad = antiguedad;
		ValorC = valorC;
		ClaveTipo = claveTipo;
		ClaveEstado = claveEstado;
		ClaveTermino = claveTermino;
		this.thumbnailUrl = thumbnailUrl;
	}

	public ListaPredios(String letra, String area, String antiguedad, String valorC, String claveTipo, String claveEstado, String claveTermino) {
		Letra = letra;
		Area = area;
		Antiguedad = antiguedad;
		ValorC = valorC;
		ClaveTipo = claveTipo;
		ClaveEstado = claveEstado;
		ClaveTermino = claveTermino;
	}

	public String getIdPredio() {
		return IdPredio;
	}

	public void setIdPredio(String idPredio) {
		IdPredio = idPredio;
	}

	public String getIdTipo() {
		return IdTipo;
	}

	public void setIdTipo(String idTipo) {
		IdTipo = idTipo;
	}

	public String getIdEstado() {
		return IdEstado;
	}

	public void setIdEstado(String idEstado) {
		IdEstado = idEstado;
	}

	public String getIdTermino() {
		return IdTermino;
	}

	public void setIdTermino(String idTermino) {
		IdTermino = idTermino;
	}

	public String getLetra() {
		return Letra;
	}

	public void setLetra(String letra) {
		Letra = letra;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getAntiguedad() {
		return Antiguedad;
	}

	public void setAntiguedad(String antiguedad) {
		Antiguedad = antiguedad;
	}

	public String getValorC() {
		return ValorC;
	}

	public void setValorC(String valorC) {
		ValorC = valorC;
	}

	public String getClaveTipo() {
		return ClaveTipo;
	}

	public void setClaveTipo(String claveTipo) {
		ClaveTipo = claveTipo;
	}

	public String getClaveEstado() {
		return ClaveEstado;
	}

	public void setClaveEstado(String claveEstado) {
		ClaveEstado = claveEstado;
	}

	public String getClaveTermino() {
		return ClaveTermino;
	}

	public void setClaveTermino(String claveTermino) {
		ClaveTermino = claveTermino;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean down) {
		isDown = down;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}

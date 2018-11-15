package com.Mezda.Catastro.model;

public class RezagoQR {

	private String Id;
	private String ClaveCatastral;
	private String TipoPredio;
	private String IdBrigadista;
	private String Lat;
	private String Lng;
	private String Fecha;
	private String Hora;
	private String Foto;
	private String Observacion;
	private String IdNot1;
	private String IdNot2;


	public RezagoQR() {}


	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTipoPredio() {
		return TipoPredio;
	}

	public void setTipoPredio(String tipoPredio) {
		TipoPredio = tipoPredio;
	}

	public String getClaveCatastral() {
		return ClaveCatastral;
	}

	public void setClaveCatastral(String claveCatastral) {
		ClaveCatastral = claveCatastral;
	}

	public String getIdBrigadista() {
		return IdBrigadista;
	}

	public void setIdBrigadista(String idBrigadista) {
		IdBrigadista = idBrigadista;
	}

	public String getLat() {
		return Lat;
	}

	public void setLat(String lat) {
		Lat = lat;
	}

	public String getLng() {
		return Lng;
	}

	public void setLng(String lng) {
		Lng = lng;
	}

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public String getHora() {
		return Hora;
	}

	public void setHora(String hora) {
		Hora = hora;
	}

	public String getFoto() {
		return Foto;
	}

	public void setFoto(String foto) {
		Foto = foto;
	}

	public String getObservacion() {
		return Observacion;
	}

	public void setObservacion(String observacion) {
		Observacion = observacion;
	}

	public String getIdNot1() {
		return IdNot1;
	}

	public void setIdNot1(String idNot1) {
		IdNot1 = idNot1;
	}

	public String getIdNot2() {
		return IdNot2;
	}

	public void setIdNot2(String idNot2) {
		IdNot2 = idNot2;
	}
}

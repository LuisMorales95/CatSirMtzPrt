package com.Mezda.Catastro.model;

public class Tareas {

	private String IdTareaP;
	private String IdPredio;
	private String ClaveCatast;
	private String IdUsoPred;
	private String UsoPredio;
	private String Verif;
	private String Propietario;
	private String DirPro;
	private String thumbnailUrl;

	public Tareas() {}


	public Tareas(String idTareaP, String idPredio, String claveCatast, String idUsoPred, String usoPredio, String verif, String propietario, String dirPro, String thumbnailUrl) {
		IdTareaP = idTareaP;
		IdPredio = idPredio;
		ClaveCatast = claveCatast;
		IdUsoPred = idUsoPred;
		UsoPredio = usoPredio;
		Verif = verif;
		Propietario = propietario;
		DirPro = dirPro;
		this.thumbnailUrl = thumbnailUrl;
	}

	public Tareas(String idTareaP, String idPredio, String claveCatast, String idUsoPred, String usoPredio, String verif) {
		IdTareaP = idTareaP;
		IdPredio = idPredio;
		ClaveCatast = claveCatast;
		IdUsoPred = idUsoPred;
		UsoPredio = usoPredio;
		Verif = verif;
	}

	public Tareas(String idTareaP, String idPredio, String claveCatast, String idUsoPred, String usoPredio, String verif, String thumbnailUrl) {
		IdTareaP = idTareaP;
		IdPredio = idPredio;
		ClaveCatast = claveCatast;
		IdUsoPred = idUsoPred;
		UsoPredio = usoPredio;
		Verif = verif;
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getIdTareaP() {
		return IdTareaP;
	}

	public void setIdTareaP(String idTareaP) {
		IdTareaP = idTareaP;
	}

	public String getIdPredio() {
		return IdPredio;
	}

	public void setIdPredio(String idPredio) {
		IdPredio = idPredio;
	}

	public String getClaveCatast() {
		return ClaveCatast;
	}

	public void setClaveCatast(String claveCatast) {
		ClaveCatast = claveCatast;
	}

	public String getIdUsoPred() {
		return IdUsoPred;
	}

	public void setIdUsoPred(String idUsoPred) {
		IdUsoPred = idUsoPred;
	}

	public String getUsoPredio() {
		return UsoPredio;
	}

	public void setUsoPredio(String usoPredio) {
		UsoPredio = usoPredio;
	}

	public String getVerif() {
		return Verif;
	}

	public void setVerif(String verif) {
		Verif = verif;
	}

	public String getPropietario() {
		return Propietario;
	}

	public void setPropietario(String propietario) {
		Propietario = propietario;
	}

	public String getDirPro() {
		return DirPro;
	}

	public void setDirPro(String dirPro) {
		DirPro = dirPro;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}

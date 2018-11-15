package com.Mezda.Catastro;

import java.util.ArrayList;
import java.util.List;

public class Const {
	public static final String URL_JSON_OBJECT = "http://api.androidhive.info/volley/person_object.json";
	public static final String URL_JSON_ARRAY = "http://api.androidhive.info/volley/person_array.json";
	public static final String URL_STRING_REQ = "http://api.androidhive.info/volley/string_response.html";
	public static final String URL_IMAGE = "http://i.imgur.com/ETFSMG7.png";
	public static  String IdPerson = "";
	public static  String User = "";
	public static  String Token = "";
	public static  String IdBrig = "";
	public static  String Munish = "martinez103/";
	public static  String Perote = "perote131/";

	
	
	public static final String SP_idpersona = "idpersona";
	public static final String SP_user = "user";
	public static final String SP_token = "token";
	public static final String SP_idbrigada = "idbrigada";
	public static final String SP_password = "password";
	public static final String SP_Links = "links";
	
	public static String bitacora="";
	
	
	
	
	public static List<lugares> Ciudades(){
		List<lugares> lugaresList = new ArrayList<>();
		lugaresList.add(new lugares("0","Martínez de la Torre"));
		lugaresList.add(new lugares("1","Perote"));
		return lugaresList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static class lugares{
		String id,name;
		
		public lugares(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name ;
		}
	}

}

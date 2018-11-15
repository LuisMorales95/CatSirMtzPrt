package com.Mezda.Catastro.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.VolleySingleton;
import com.Mezda.Catastro.model.DataSpinn;
import com.Mezda.Catastro.model.EstadoCons;
import com.Mezda.Catastro.model.ListaPredios;
import com.Mezda.Catastro.model.TerminoCons;
import com.Mezda.Catastro.model.TipoCons;

import java.util.List;


public class CustomListAdapterDetails extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ListaPredios> movieItems;
	ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();

	public CustomListAdapterDetails(Activity activity, List<ListaPredios> movieItems) {
		this.activity = activity;
		this.movieItems = movieItems;
	}

	@Override
	public int getCount() {
		return movieItems.size();
	}

	@Override
	public Object getItem(int location) {
		return movieItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.layout_result_details, null);

		//if (imageLoader == null)
			//imageLoader = VolleySingleton.getInstance().getImageLoader();
		//NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		ImageView verifi = (ImageView) convertView.findViewById(R.id.verificationImgView);

		//String imageUrl =  NO_IMAGE_FOUND;
		//Picasso.with(activity).load(imageUrl).placeholder(R.drawable.ic_placeholder).tag(imageUrl)
		//		.into(thumbNail);
		//thumbNail.setImageResource(R.drawable.ic_placeholder);
		// getting movie data for the row
		ListaPredios m = movieItems.get(position);

		// thumbnail image
		//thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// title
		title.setText("Letra: "+ m.getLetra() +" - Area: "+m.getArea());

		// rating
		rating.setText("Antiguedad: "+m.getAntiguedad()+"     Valor: "+m.getValorC());

		// genre
		if (m.isDown()){
			genre.setText("C.Tipo: "+m.getClaveTipo()+"    C.Estado: "+m.getClaveEstado()+"    C.Termino: "+m.getClaveTermino());
		}else{
			String TIPO="", ESTADO="", TERMINO="";
			
			List<TipoCons> tipoConsList = DataSpinn.TipoCons();
			for (int i=0; i<tipoConsList.size(); i++){
				if (tipoConsList.get(i).getId().equals(m.getClaveTipo())){
					TIPO=tipoConsList.get(i).getClave();
				}
			}
			List<EstadoCons> estadoConsList = DataSpinn.EstadoCons();
			for (int i=0; i<estadoConsList.size(); i++){
				if (estadoConsList.get(i).getId().equals(m.getClaveEstado())){
					ESTADO=estadoConsList.get(i).getClave();
				}
			}
			List<TerminoCons> terminoConsList = DataSpinn.TerminoCons();
			for (int i=0; i<terminoConsList.size(); i++){
				if (terminoConsList.get(i).getId().equals(m.getClaveTermino())){
					TERMINO=terminoConsList.get(i).getClave();
				}
			}
			genre.setText("C.Tipo: "+TIPO+"    C.Estado: "+ESTADO+"    C.Termino: "+TERMINO);
			
		}

		Log.e("Lista::::::::","C.Tipo: "+m.getClaveTipo()+"    C.Estado: "+m.getClaveEstado()+"    C.Termino: "+m.getClaveTermino());


		if(!m.isDown()) {
			verifi.setVisibility(View.VISIBLE);
		}else{
			verifi.setVisibility(View.INVISIBLE);
		}

		
		// release year
//		year.setText("");

		return convertView;
	}

}
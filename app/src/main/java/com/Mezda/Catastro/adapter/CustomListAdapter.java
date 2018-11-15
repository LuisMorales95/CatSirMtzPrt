package com.Mezda.Catastro.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.VolleySingleton;
import com.Mezda.Catastro.model.Tareas;
import com.bumptech.glide.Glide;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Tareas> movieItems;
	ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Tareas> movieItems) {
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
			convertView = inflater.inflate(R.layout.layout_result_list, null);
		
		//if (imageLoader == null)
			//imageLoader = VolleySingleton.getInstance().getImageLoader();
		//NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
		ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
		TextView CLANumReg = (TextView) convertView.findViewById(R.id.CLANumReg);
		TextView Clave = (TextView) convertView.findViewById(R.id.title);
		TextView Propietario = (TextView) convertView.findViewById(R.id.rating);
		TextView UseoPredio = (TextView) convertView.findViewById(R.id.genre);
		TextView Direccion = (TextView) convertView.findViewById(R.id.releaseYear);
		ImageView verifi = (ImageView) convertView.findViewById(R.id.verificationImgView);

		//String imageUrl =  NO_IMAGE_FOUND;
		//Picasso.with(activity).load(imageUrl).placeholder(R.drawable.ic_placeholder).tag(imageUrl)
		//		.into(thumbNail);
		//thumbNail.setImageResource(R.drawable.ic_placeholder);
		// getting movie data for the row
		Tareas m = movieItems.get(position);
		CLANumReg.setText(String.valueOf(position+1));
		
		//thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		Glide.with(convertView).load(R.drawable.ic_launcher).into(thumbnail);
//		thumbnail.setImageResource(R.drawable.ic_launcher);
		
		Clave.setText("Clave: "+m.getClaveCatast());
		
		Propietario.setText("Prop: "+m.getPropietario());

		if(m.getVerif().equals("1")) {
			verifi.setVisibility(View.VISIBLE);
		}else {
			verifi.setVisibility(View.INVISIBLE);
		}
		
		UseoPredio.setText("U.P.: "+m.getUsoPredio());
		Direccion.setText(m.getDirPro());

		return convertView;
	}

}
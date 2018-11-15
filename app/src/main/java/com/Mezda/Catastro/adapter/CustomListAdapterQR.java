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
import com.Mezda.Catastro.model.RezagoQR;

import java.util.List;


public class CustomListAdapterQR extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<RezagoQR> movieItems;
	ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();

	public CustomListAdapterQR(Activity activity, List<RezagoQR> movieItems) {
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

		ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
		TextView CLANumReg = (TextView) convertView.findViewById(R.id.CLANumReg);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		ImageView verifi = (ImageView) convertView.findViewById(R.id.verificationImgView);
		
		RezagoQR m = movieItems.get(position);
		thumbnail.setImageResource(R.drawable.ic_launcher);
		CLANumReg.setText(String.valueOf(position));
		title.setText("Clave: "+m.getClaveCatastral());
		rating.setText("");
		genre.setText("Tipo Predio: "+m.getTipoPredio());
		year.setText("");

		return convertView;
	}

}
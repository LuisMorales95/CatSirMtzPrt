package com.Mezda.Catastro.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Mezda.Catastro.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Audacity IT Solutions Ltd.
 * @class ImagePagerAdapter
 * @brief Adapter class for showing images like gallery. Used in DetailViewFragment class
 */

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> imageUrls;
    private LayoutInflater inflater;

    public ImagePagerAdapter(Context context, ArrayList<String> imageUrls) {
        this.mContext = context;
        if (imageUrls != null && imageUrls.size() == 0) {
            ArrayList<String> d = new ArrayList<>();
            d.add(getDefaultImage(mContext));
            this.imageUrls = d;
        }
        else {
            this.imageUrls = imageUrls;
        }
        inflater = LayoutInflater.from(context);
    }

    private String getDefaultImage(Context context) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/drawable/pager_place_holder").toString();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.layout_detail_image, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        // load real image using the url coming from server
//        Picasso.with(mContext).load(imageUrls[position]).placeholder(R.drawable.ic_placeholder).into(imageView);
        Uri uri = Uri.fromFile(new File(imageUrls.get(position)));
        Picasso.with(mContext).load(uri).resize(0,2000).
                placeholder(R.drawable.ic_placeholder2).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}

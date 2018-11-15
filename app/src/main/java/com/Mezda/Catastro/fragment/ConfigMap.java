package com.Mezda.Catastro.fragment;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Black Swan on 24/02/2017.
 */

public class ConfigMap {

    public static double Lat = 20.07082;
    public static double Lng = -97.06078;
    public static String StyleMap = "mapbox://styles/mapbox/streets-v9";
    // streets, light, dark

    public static String getStyleMap() {
        return StyleMap;
    }

    public static String getStyleMap(String NameStyle) {
        return "mapbox://styles/mapbox/"+NameStyle+"-v9";
    }


    public static void setLat(double lat) {
        Lat = lat;
    }

    public static void setLng(double lng) {
        Lng = lng;
    }

    public static CameraPosition getActualCity(){
        LatLng cor = new LatLng();
        cor.setLatitude(Lat);
        cor.setLongitude(Lng);

        return  new CameraPosition.Builder()
                .target(cor)
                .zoom(14)
                .build();
    }
}

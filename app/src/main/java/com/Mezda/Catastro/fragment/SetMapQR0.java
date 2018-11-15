package com.Mezda.Catastro.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.activity.GPSService;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetMapQR0 extends AppCompatActivity implements PermissionsListener {

  private MapView mapView;
  private MapboxMap map;
  boolean ban = true;
  private TextView tbbuscar1, tbbuscar2;
  private EditText etbuscar;
  private LocationEngine locationServices;
  private LocationEngineListener locationListener;
  private PermissionsManager permissionsManager;
  private ImageButton bgp;
  private ImageView vgp;
  public static String Lat = "", Lng = "";
  String calle;
  Address bestMatch = null;
  private static final int PERMISSIONS_LOCATION = 0;
  Icon icon;

  private ImageView cross;
  private boolean isUserCanceled = false;
  private Marker droppedMarker;
  private ImageView hoveringMarker;
  private Button selectLocationButton, bt;
  private static final String TAG = "LocationPickerActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));


    // This contains the MapView in XML and needs to be called after the account manager
    setContentView(R.layout.activity_set_maptoqrzero);

    locationServices = LocationSource.getLocationEngine(this);
    //locationServices.activate();

  /*  bt = (Button) findViewById(R.id.btn_set_dir);

    bt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(SetMapQR.this, GeneratorActivity.class));
        //finish();
      }
    });*/

    //etbuscar = (EditText)findViewById(R.id.et_search_pickup_address_number_editable);
    //tbbuscar1 = (TextView)findViewById(R.id.tv_search_pickup_address_line_1);
    //tbbuscar2 = (TextView)findViewById(R.id.tv_search_pickup_address_line_2);


    final Activity activity = this;


    // Create the marker icon the dropped marker will be using.
    IconFactory iconFactory = IconFactory.getInstance(SetMapQR0.this);
    //Drawable iconDrawable = ContextCompat.getDrawable(GoDestDriver.this, R.drawable.pin_customer);
    icon = iconFactory.fromResource(R.drawable.pin_customer);


    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.setCameraPosition(ConfigMap.getActualCity());
        map.setStyleUrl(ConfigMap.getStyleMap());

        // Once map is ready, we want to position the camera above the user location. We
        // first check that the user has granted the location permission, then we call
        // setInitialCamera.
        permissionsManager = new PermissionsManager(SetMapQR0.this);
        if (!PermissionsManager.areLocationPermissionsGranted(SetMapQR0.this)) {
          permissionsManager.requestLocationPermissions(SetMapQR0.this);
        } else {
          // setInitialCamera();
        }



        cross = (ImageView) findViewById(R.id.crossImgView);
        cross.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            isUserCanceled = true;
            onPause();
          }
        });


        map.setOnScrollListener(new MapboxMap.OnScrollListener(){

          @Override
          public void onScroll() {
            if(ban){
              ban = false;
              new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  getDir();
                  ban = true;
                }
              }, 1500);
            }

          }
        });



      }
    });

   // vgp = (ImageView)findViewById(R.id.img_origen);
    bgp = (ImageButton) findViewById(R.id.gps_button);
    bgp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (map != null) {
          //toggleGps(!map.isMyLocationEnabled());
          setUB();
        }
      }
    });
    bgp.setImageResource(R.drawable.ic_my_location_24dp);



    //-------------------------------------------------------------------------------------
    // When user is still picking a location, we hover a marker above the map in the center.
    // This is done by using an image view with the default marker found in the SDK. You can
    // swap out for your own marker image, just make sure it matches up with the dropped marker.
    hoveringMarker = new ImageView(this);
    hoveringMarker.setImageResource(R.drawable.pin_customer);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    hoveringMarker.setLayoutParams(params);
    mapView.addView(hoveringMarker);

    // Button for user to drop marker or to pick marker back up.
    selectLocationButton = (Button) findViewById(R.id.select_location_buttonf);
    selectLocationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        getDir();
        //onBackPressed();
        //finish();
        Toast.makeText(getApplication(), "Latitud: " + Lat+ "Longitud: "+Lng, Toast.LENGTH_SHORT).show();
        Log.e("fgd","Latitud: " + Lat+ "Longitud: "+Lng);
       // ZeroQR.UpdateGPS(Lat,Lng);

      //  ImageView imageView = (ImageView) findViewById(R.id.mapImage);
     /*   MapboxStaticImage staticImage;
        try {
          staticImage = new MapboxStaticImage.Builder()
                  .setAccessToken(Mapbox.getAccessToken())
                  .setUsername(Constants.MAPBOX_USER)
                  .setStyleId("light-v9")
                  .setLon(Double.parseDouble(Lng)) // Image center longitude
                  .setLat(Double.parseDouble(Lat)) // Image center Latitude
                  .setZoom(14)
                  .setWidth(640) // Image width
                  .setHeight(360) // Image height
                  .setRetina(true) // Retina 2x image will be returned
                  .build();

          System.out.println(staticImage.getUrl().toString());
          new DownloadImageTask().execute(staticImage.getUrl().toString());

        } catch (ServicesException servicesException) {
          Log.e(TAG, "MapboxStaticImage error: " + servicesException.getMessage());
          servicesException.printStackTrace();
        }*/
      }
    });
  }

  private void setUB(){

    String address = "";
    GPSService mGPSService = new GPSService(this);
    mGPSService.getLocation();

    if (mGPSService.isLocationAvailable == false) {

      // Here you can ask the user to try again, using return; for that
      Toast.makeText(this, "Tu localizacion no esta disponible, por favor intenta de nuevo.", Toast.LENGTH_SHORT).show();

      bgp.setImageResource(R.drawable.ic_my_location_24dp);
      return;

      // Or you can continue without getting the location, remove the return; above and uncomment the line given below
      // address = "Location not available";

    } else {

      // Getting location co-ordinates
      double latitude = mGPSService.getLatitude();
      double longitude = mGPSService.getLongitude();

      ConfigMap.setLat(latitude);
      ConfigMap.setLng(longitude);

      Toast.makeText(this, "Latitud:" + latitude + " | Longitud: " + longitude, Toast.LENGTH_LONG).show();

      map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mGPSService.getLocation()), 16));

      address = mGPSService.getLocationAddress();
      bgp.setImageResource(R.drawable.ic_location_disabled_24dp);
    }




    Toast.makeText(this, "Your address is: " + address, Toast.LENGTH_SHORT).show();

    // make sure you close the gps after using it. Save user's battery power
    mGPSService.closeGPS();
  }

  @Override
  public void onBackPressed(){}

  private void getDir(){
    if (droppedMarker == null) {
      // We first find where the hovering marker position is relative to the map.
      // Then we set the visibility to gone.
      float coordinateX = hoveringMarker.getLeft() + (hoveringMarker.getWidth() / 2);
      float coordinateY = hoveringMarker.getBottom();
      float[] coords = new float[] {coordinateX, coordinateY};
      final LatLng latLng = map.getProjection().fromScreenLocation(new PointF(coords[0], coords[1]));
      hoveringMarker.setVisibility(View.GONE);


      // Placing the marker on the map as soon as possible causes the illusion
      // that the hovering marker and dropped marker are the same.
      droppedMarker = map.addMarker(new MarkerViewOptions().position(latLng).icon(icon));

      // Finally we getActualCity the geocoding information
      reverseGeocode(latLng);

     // Log.e("LT","L:"+ latLng.getLatitude());
      map.removeMarker(droppedMarker);


      // Lastly, set the hovering marker back to visible.
      hoveringMarker.setVisibility(View.VISIBLE);
      droppedMarker = null;

    }
  }
  //INICIAL GPS
  private void setInitialCamera() {
    // Method is used to set the initial map camera position. Should only be called once when
    // the map is ready. We first try using the users last location so we can quickly set the
    // camera as fast as possible.
    if (locationServices.getLastLocation() != null) {
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationServices.getLastLocation()), 16));

    }

    // This location listener is used in a very specific use case. If the users last location is
    // unknown we wait till the GPS locates them and position the camera above.

    locationListener = new LocationEngineListener() {
      @Override
      public void onConnected() {
        if (ActivityCompat.checkSelfPermission(SetMapQR0.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SetMapQR0.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          // TODO: Consider calling
          //    ActivityCompat#requestPermissions
          // here to request the missing permissions, and then overriding
          //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
          //                                          int[] grantResults)
          // to handle the case where the user grants the permission. See the documentation
          // for ActivityCompat#requestPermissions for more details.
          return;
        }
        locationServices.requestLocationUpdates();
      }

      @Override
      public void onLocationChanged(Location location) {
        if (location != null) {
          // Move the map camera to where the user location is
          map.setCameraPosition(new CameraPosition.Builder()
                  .target(new LatLng(location))
                  .zoom(16)
                  .build());

          String lat = ""+ location.getLatitude();
          Log.e("zzz",lat);
          Log.e("sss",lat.substring(5,7));

          locationServices.removeLocationEngineListener(this);
        }
      }
    };
    locationServices.addLocationEngineListener(locationListener);
    // Enable the location layer on the map and track the user location until they perform a
    // map gesture.
    map.setMyLocationEnabled(true);
    map.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
  } // End setInitialCamera



  private void reverseGeocode(final LatLng point) {
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

    try {
      List<Address> addresses = geocoder.getFromLocation(point.getLatitude(),point.getLongitude(), 1);

      if (addresses.size() > 0) {
        bestMatch = addresses.get(0);
        // If the geocoder returns a result, we take the first in the list and update
        // the dropped marker snippet with the information. Lastly we open the info
        // window.
        if (droppedMarker != null) {
          //LatLng lt = new LatLng();
          //lt.setLatitude(bestMatch.getLatitude());
          //lt.setLongitude(bestMatch.getLongitude());
          //droppedMarker.setPosition(lt);

          Lat = ""+bestMatch.getLatitude();
          Lng = ""+ bestMatch.getLongitude();
          //DetailViewFragment.Lat = Lat;
          //DetailViewFragment.Lng = Lng;

          Log.e("LT","L:"+ Lat);
          //GeneratorActivity.Num = ""+ bestMatch.getFeatureName();
          //GeneratorActivity.Dir1 = ""+ bestMatch.getThoroughfare();
          //GeneratorActivity.Dir2 = bestMatch.getSubLocality()+", "+bestMatch.getLocality();

         // Brain.GenFirst = false;

          //tbbuscar2.setVisibility(View.VISIBLE);
         // calle = bestMatch.getThoroughfare();
         // tbbuscar1.setText(calle + " "+bestMatch.getFeatureName());
         // tbbuscar2.setText(bestMatch.getSubLocality()+", "+bestMatch.getLocality());

        }

      } else {
        if (droppedMarker != null) {
         // tbbuscar2.setVisibility(View.INVISIBLE);
         // tbbuscar1.setText("Sin resultado..");

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


  } // reverseGeocode

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();


  }



  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
    if (isUserCanceled) {
      overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
      finish();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    //mapView.onDestroy();
    // Ensure no memory leak occurs if we register the location listener but the call hasn't
    // been made yet.
    if (locationListener != null) {
      locationServices.removeLocationEngineListener(locationListener);
    }
  }


  private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
  //  ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
     // this.imageView = imageView;
    }

    public DownloadImageTask() {

    }

    protected Bitmap doInBackground(String... urls) {

      // Create OkHttp object
      final OkHttpClient client = new OkHttpClient();

      // Build request
      Request request = new Request.Builder()
              .url(urls[0])
              .build();

      Response response = null;
      Bitmap bitmap = null;
      try {
        // Make request
        response = client.newCall(request).execute();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }

      // If the response is successful, create the static map image
      if (response != null) {
        if (response.isSuccessful()) {
          try {
            bitmap = BitmapFactory.decodeStream(response.body().byteStream());
          } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
            exception.printStackTrace();
          }
        }
      }
      return bitmap;
    }

    protected void onPostExecute(Bitmap data) {
      // Add static map image to imageView
      //imageView.setImageBitmap(result);

      String path = "/Catastro";
      FileOutputStream outStream = null;

      try {
        File f = new File(Environment.getExternalStorageDirectory() + path);
        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        if(!f.isDirectory()) {
          String newFolder = path;
          File myNewFolder = new File(extStorageDirectory + newFolder);
          myNewFolder.mkdir();
          Log.d("MENSAJE",path+" ha sido creado!");
        }else{
          Log.d("MENSAJE",path+" estaba creado");
        }

        Calendar c = Calendar.getInstance();
        String date = Integer.toString(c.get(Calendar.MONTH))  + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.YEAR)) + Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + Integer.toString(c.get(Calendar.MINUTE)) + Integer.toString(c.get(Calendar.SECOND));
        outStream = new FileOutputStream(String.format(extStorageDirectory+path+"/ctr_"+ date +".jpg", System.currentTimeMillis()));
        data.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
        outStream.close();
        System.out.println(extStorageDirectory+path+"/ctr_"+ date +".jpg");
        //DetailViewFragment.rr.add(extStorageDirectory+path+"/ctr_"+ date +".jpg");

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      }
    }
  }


  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  private void toggleGps(boolean enableGps) {
    if(enableGps) {
      // Check if user has granted location permission
      permissionsManager = new PermissionsManager(this);
      if (!PermissionsManager.areLocationPermissionsGranted(this)) {
        permissionsManager.requestLocationPermissions(this);
        //enableLocation(true);
      } else {
        enableLocation(true);
      }
    } else {
      enableLocation(false);
    }
  }

  private void enableLocation(boolean enabled) {
    if (enabled) {
      // If we have the last location of the user, we can move the camera to that position.
      Location lastLocation = locationServices.getLastLocation();


      if (lastLocation != null) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

      }

      locationListener = new LocationEngineListener() {
        @Override
        public void onConnected() {
          if (ActivityCompat.checkSelfPermission(SetMapQR0.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SetMapQR0.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
          }
          locationServices.requestLocationUpdates();
        }

        @Override
        public void onLocationChanged(Location location) {
          if (location != null) {
            // Move the map camera to where the user location is
            LatLng lt = new LatLng();
            String lat = ""+ location.getLatitude();
            //Log.e("zzz",lat);
            int var = 4; //variante
            String latcom = lat.substring(0,3) + (Integer.parseInt(lat.substring(3,7))+var)+ lat.substring(7,lat.length());
            //Log.e("xxx",latcom);

            lt.setLatitude(Double.parseDouble(latcom));
            lt.setLongitude(location.getLongitude());

            map.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(lt))
                    .zoom(16)
                    .build());

            new android.os.Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                getDir();
              }
            }, 500);


            locationServices.removeLocationEngineListener(this);
          }
        }
      };
      locationServices.addLocationEngineListener(locationListener);

      bgp.setImageResource(R.drawable.ic_location_disabled_24dp);
    } else {
      bgp.setImageResource(R.drawable.ic_my_location_24dp);
    }
    // Enable or disable the location layer on the map
    map.setMyLocationEnabled(enabled);
  }

  @Override
  public void onRequestPermissionsResult(
          int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSIONS_LOCATION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        enableLocation(true);
      }
    }
  }


  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
            Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      //setInitialCamera();
    } else {
      Toast.makeText(this, "You didn't grant location permissions.",
              Toast.LENGTH_LONG).show();
      finish();
    }
  }
}


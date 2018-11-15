package com.Mezda.Catastro.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.activity.HomeActivity;
import com.Mezda.Catastro.adapter.CategoryAdapter;
import com.Mezda.Catastro.model.Category;
import com.Mezda.Catastro.util.ApiHandler;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.Mezda.Catastro.util.Constants.JF_BACKGROUND_IMAGE;
import static com.Mezda.Catastro.util.Constants.JF_ICON;
import static com.Mezda.Catastro.util.Constants.JF_TITLE;
import static com.Mezda.Catastro.util.Constants.SP_ID;
import static com.Mezda.Catastro.util.UtilMethods.loadJSONFromAsset;
import static com.Mezda.Catastro.util.UtilMethods.showNoInternetDialog;

/**
 * @author Audacity IT Solutions Ltd.
 * @class HomeFragment
 * @brief Fragment for showing the category list
 */

public class HomeFragment extends Fragment implements InternetConnectionListener, ApiHandler.ApiHandlerListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int CATEGORY_ACTION = 1;
    private CategorySelectionCallbacks mCallbacks;
    private ArrayList<Category> categoryList;
    private ListView categoryListView;
    // GPSTracker class

    private InternetConnectionListener internetConnectionListener;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        try {
            mCallbacks = (CategorySelectionCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CategorySelectionCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView HomeBtnLevantamientoCampo = (TextView) rootView.findViewById(R.id.HomeBtnLevantamientoCampo);
//        final TextView HomeBtnBitacora = (TextView) rootView.findViewById(R.id.HomeBtnBitacora);
        HomeBtnLevantamientoCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Fragment nuevoFragmento = new ResultListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        /*HomeBtnBitacora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeBtnBitacora.setText(Const.bitacora);
            }
        });
        HomeBtnBitacora.setText(Const.bitacora);*/
        rootView.findViewById(R.id.HomeBtnListaTemporal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeFragment.super.getActivity(), ResultListFragmentqr.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UtilMethods.isConnectedToInternet(getActivity())) {
           // initCategoryList();
        } else {
            internetConnectionListener = (InternetConnectionListener) HomeFragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), CATEGORY_ACTION);
        }

    }



    //! function for populate category list
    private void initCategoryList() {
        
        String jsonString = loadJSONFromAsset(getActivity(), "get_category_id_list");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            categoryList = new ArrayList<Category>();

            for (int i = 0; i < jsonArray.length(); i++) {
                Category category = new Category();
                category.setId(jsonArray.getJSONObject(i).getString(SP_ID));
                category.setTitle(jsonArray.getJSONObject(i).getString(JF_TITLE));
                category.setIconUrl(jsonArray.getJSONObject(i).getString(JF_ICON));

                if (!TextUtils.isEmpty(jsonArray.getJSONObject(i).getString(JF_BACKGROUND_IMAGE))) {
                    category.setImageUrl(jsonArray.getJSONObject(i).getString(JF_BACKGROUND_IMAGE));
                }
                categoryList.add(category);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    categoryListView.setAdapter(new CategoryAdapter(getActivity(), mCallbacks, categoryList));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CATEGORY_ACTION) {
            initCategoryList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CATEGORY_ACTION) {
            getActivity().finish();
        }
    }

    //! catch json response from here
    @Override
    public void onSuccessResponse(String tag, String jsonString) {
        //! do same parsing as done in initCategoryList()
    }

    //! detect response error here
    @Override
    public void onFailureResponse(String tag) {

    }

    //! callback interface listen by HomeActivity to detect user click on category
    public static interface CategorySelectionCallbacks {
        void onCategorySelected(String catID, String title);
    }

}

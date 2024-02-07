package com.example.healthcareapp.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.DisplayMetrics;

import com.example.healthcareapp.Adapter.RegisterAdapter;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Grafics.BarGraphic;
import com.example.healthcareapp.Grafics.StatisticalGraphic;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Repository.RegisterRepository;
import com.example.healthcareapp.Room.Datasource;
//import com.example.healthcareapp.StadisticView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class GraphicFragment extends Fragment {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    // Mas componentes
    public LinearLayout linearLayout,linearLayout2;
    // Mas componentes
    private RegisterAdapter adapter;
    private Datasource datasource;
    private RegisterRepository registerRepository;
    public GraphicFragment(){}
    /*
    public static GraphicFragment newInstance(String param1, String param2){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphic, container, false);
        linearLayout = view.findViewById(R.id.grafico);
        linearLayout2 = view.findViewById(R.id.grafico2);
        // Obtén los datos de la base de datos
        datasource = Datasource.newInstance(getActivity().getApplicationContext());
        //llenar grafico prueba
        //DisplayMetrics metrics = new DisplayMetrics();

        new Thread(() -> {
            List<Double> glusemia = datasource.registerDAO().readGlusemiaAllData();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            getActivity().runOnUiThread(() -> {
                // Inicializa stadisticView con los datos y agrégalo a linearLayout
                StatisticalGraphic statisticalGraphic = new StatisticalGraphic(getActivity(), metrics, (ArrayList<Double>) glusemia);
                BarGraphic barGraphic = new BarGraphic(getActivity(), metrics, (ArrayList<Double>) glusemia);
                linearLayout.addView(statisticalGraphic);
                linearLayout2.addView(barGraphic);
            });
        }).start();
        Button btn = view.findViewById(R.id.regresar);
        btn.setOnClickListener(view1 -> {
            replaceFragments(new HomeFragment());
        });

        return view;
    }
    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}

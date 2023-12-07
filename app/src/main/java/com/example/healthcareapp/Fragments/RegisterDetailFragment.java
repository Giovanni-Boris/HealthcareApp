package com.example.healthcareapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.AdapterListDetail;
import com.example.healthcareapp.Adapter.RegisterAdapter;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Room.Datasource;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> data;
    private TextView date ;
    private ListView listview;
    private Datasource datasource;


    public RegisterDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterDetailFragment newInstance(String param1, String param2) {
        RegisterDetailFragment fragment = new RegisterDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register_detail, container, false);
        listview = view.findViewById(R.id.list_view);
        date = view.findViewById(R.id.date);
        datasource = Datasource.newInstance(getActivity().getApplicationContext());
        data = new ArrayList<>();
        storeDataInArrays(getArguments().getInt("id"));
        return view;
    }
    private void storeDataInArrays(int id ){
        new Thread(() -> {
            Register register = datasource.registerDAO().getItemById(id);
            date.setText(register.getHora());
            data.add(register.getGlucemia()+"   Unidades \nmg/dL");
            data.add(register.getInsulina()+"   Unidades \nInsulina(corrida)");
            data.add(register.getCarbohidrato()+" g");
            data.add(register.getMedicamento()+" Unidades");
            data.add(register.getActividad()+" h  \nActividad");
            data.add(register.getTension()+" mmHg \nTension arterial");
            data.add(register.getPeso()+" kg \nPeso Corporal");
            AdapterListDetail myAdapter = new AdapterListDetail(getActivity(), R.layout.list_item, data);
            listview.setAdapter(myAdapter);
        }).start();

    }

}
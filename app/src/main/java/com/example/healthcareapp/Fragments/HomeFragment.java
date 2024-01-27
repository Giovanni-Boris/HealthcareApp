package com.example.healthcareapp.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.RegisterAdapter;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Room.Datasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    List<Register> registers;
    private RegisterAdapter adapter;
    private Datasource datasource;
    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(view1 -> {
            replaceFragments(new AddRegisterFragment());
        });
        datasource = Datasource.newInstance(getActivity().getApplicationContext());
        storeDataInArrays();

        return view;
    }
    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    private void storeDataInArrays(){
        new Thread(() -> {
            registers = datasource.registerDAO().readAllData();
            adapter = new RegisterAdapter(registers , new RegisterAdapter.OnItemClickListener() {
                @Override public void onItemClick(Register item) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Registro "+item.getId(), Toast.LENGTH_SHORT).show();
                    // Create a bundle with the data you want to pass to the detail fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", item.getId());
                    RegisterDetailFragment detailFragment = new RegisterDetailFragment();
                    detailFragment.setArguments(bundle);
                    replaceFragments(detailFragment);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }).start();

    }

}
package com.example.healthcareapp.Fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.RegisterAdapter;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Repository.RegisterRepository;
import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Services.FirebaseFetchService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class HomeFragment extends Fragment {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Register> dataList = (ArrayList<Register>) intent.getSerializableExtra("dataList");
            Log.d("HomeFragment","registros");
            chargeData(dataList);
        }
    };
    private static final int JOB_ID = 1;
    private JobScheduler jobScheduler;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    List<Register> registers;
    private RegisterAdapter adapter;
    private Datasource datasource;
    private RegisterRepository registerRepository;
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
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                dataReceiver,
                new IntentFilter("DATA_FETCHED_ACTION")
        );
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
        registerRepository = new RegisterRepository(datasource.registerDAO());
        registers = new ArrayList<>();
        iniciarJobService();

        return view;
    }
    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    private void storeDataInArrays(){
        Disposable disposable = registerRepository.getAllRegisters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registersChange -> {
                    int originalSize = registers.size();
                    registers.addAll(registersChange.subList(originalSize, registersChange.size()));

                    if (adapter != null) {
                        adapter.notifyItemRangeInserted(originalSize, registersChange.size() - originalSize);
                        return;
                    }
                    adapter = new RegisterAdapter(registers , item -> {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Registro "+item.getId(), Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", item.getId());
                        RegisterDetailFragment detailFragment = new RegisterDetailFragment();
                        detailFragment.setArguments(bundle);
                        replaceFragments(detailFragment);
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }, throwable -> {
                    Log.d("HomeFragment",throwable.getMessage());
                });
        compositeDisposable.add(disposable);


    }

    private void chargeData(ArrayList<Register> dataList ){
        Disposable disposable = registerRepository.syncWithFirebase(dataList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("HomeFragment","Guardo con exito");
                    storeDataInArrays();
                }, throwable -> {
                    Log.d("HomeFragment","No se pudo cargar");
                });
        compositeDisposable.add(disposable);
    }
    private void iniciarJobService() {
        jobScheduler = (JobScheduler) requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(requireContext(), FirebaseFetchService.class);

        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(false)
                .build();

        int result = jobScheduler.schedule(jobInfo);

        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("HomeFragment","Job creado");
        } else {
            Log.d("HomeFragment","Job no creado");
        }
    }

    private void detenerJobService() {
        if (jobScheduler != null) {
            jobScheduler.cancel(JOB_ID);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        detenerJobService();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(dataReceiver);
    }

}
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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.graphics.Path;

>>>>>>> 4a3383f36bf3a658424907c77146cba74ba6bfc8

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

<<<<<<< HEAD
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
=======
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


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

        // Crear gráfico estadístico
        LinearLayout graficoLayout = view.findViewById(R.id.grafico);
        graficoLayout.addView(createStatisticChart());

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

    private View createStatisticChart() {
        View chartView = new View(requireContext()) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                // Dibuja el gráfico estadístico basado en los datos de registers
                drawStatisticChart(canvas);
            }
        };

        // Configurar el tamaño del gráfico (ajusta según sea necesario)
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return chartView;
    }

    private void drawStatisticChart(Canvas canvas) {
        if (registers != null && registers.size() > 0) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(40);

            int numDataPoints = registers.size();
            float chartWidth = canvas.getWidth() - 40; // Ajusta según sea necesario
            float barWidth = chartWidth / numDataPoints;

            float minValue = 50f;  // Establece el valor mínimo deseado
            float maxValue = 190f; // Establece el valor máximo deseado
            float range = maxValue - minValue;

            // Dibuja la línea y coloca etiquetas en los puntos
            for (int i = 0; i < numDataPoints; i++) {
                float startX = i * barWidth + 20;
                float endX = startX + barWidth;
                float glucemia = (float) registers.get(i).getGlucemia();

                // Escala el valor de glucemia al rango deseado
                float scaledGlucemia = (float) (minValue + (glucemia - getMinGlucemia()) / (getMaxGlucemia() - getMinGlucemia()) * range);

                // Dibuja la línea vertical en el punto
                if (i < numDataPoints - 1) {
                    float nextX = (i + 1) * barWidth + 20;
                    float nextY = (float) registers.get(i + 1).getGlucemia();
                    float scaledNextY = (float) (minValue + (nextY - getMinGlucemia()) / (getMaxGlucemia() - getMinGlucemia()) * range);

                    canvas.drawLine(startX, canvas.getHeight() - scaledGlucemia, nextX, canvas.getHeight() - scaledNextY, paint);

                    // Muestra el valor de glucemia encima de la línea
                    canvas.drawText(String.format("%.2f", glucemia), startX + barWidth / 2, canvas.getHeight() - scaledGlucemia - 10, textPaint);
                }
            }
            /*
            // Etiquetas en el eje izquierdo
            for (int i = 0; i <= 5; i++) {
                float y = i * (canvas.getHeight() / 5);
                float scaledLabel = maxValue - i * (range / 5); // Reversa la escala para que comience desde el valor máximo
                canvas.drawText(String.format("%.2f", scaledLabel), 0, canvas.getHeight() - y, textPaint);
            }
            */
        }
    }

    private double getMaxGlucemia() {
        double max = registers.get(0).getGlucemia();
        for (Register register : registers) {
            max = Math.max(max, register.getGlucemia());
        }
        return max;
    }

    private double getMinGlucemia() {
        double min = registers.get(0).getGlucemia();
        for (Register register : registers) {
            min = Math.min(min, register.getGlucemia());
        }
        return min;
    }

    private void replaceFragments(Fragment fragment) {
>>>>>>> 4a3383f36bf3a658424907c77146cba74ba6bfc8
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

<<<<<<< HEAD
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

=======
    private void storeDataInArrays() {
        new Thread(() -> {
            registers = datasource.registerDAO().readAllData();
            adapter = new RegisterAdapter(registers, new RegisterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Register item) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Registro " + item.getId(), Toast.LENGTH_SHORT).show();
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

>>>>>>> 4a3383f36bf3a658424907c77146cba74ba6bfc8
}
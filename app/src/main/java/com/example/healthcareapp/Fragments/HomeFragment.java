package com.example.healthcareapp.Fragments;

import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.RegisterAdapter;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.R;
import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Grafics.StadisticView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    LinearLayout graficoLayout;
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

        // Crear gráfico estadístico
        graficoLayout = view.findViewById(R.id.grafico);
        graficoLayout.addView(createStatisticChart());

        add_button.setOnClickListener(view1 -> {
            replaceFragments(new AddRegisterFragment());
        });
        datasource = Datasource.newInstance(getActivity().getApplicationContext());
        storeDataInArrays();

        return view;
    }

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

            float startX = 0;
            float startY = (float) registers.get(0).getGlucemia();

            // Dibuja la línea y coloca etiquetas en los puntos
            for (int i = 1; i < registers.size(); i++) {
                float endX = i * 150; // Ajusta según sea necesario
                float endY = (float) registers.get(i).getGlucemia();

                canvas.drawLine(startX, startY, endX, endY, paint);

                // Muestra el valor de glucemia en cada punto
                canvas.drawText(String.format("%.2f", registers.get(i).getGlucemia()), endX - 20, endY, textPaint);

                startX = endX;
                startY = endY;
            }

            // Etiquetas en el eje izquierdo
            float maxValue = (float) registers.get(0).getGlucemia();
            float minValue = (float) registers.get(0).getGlucemia();
            float range = maxValue - minValue;
            float increment = range / 5; // Número de etiquetas en el eje izquierdo

            for (int i = 0; i <= 5; i++) {
                float y = canvas.getHeight() - i * (canvas.getHeight() / 5);
                // Ajusta la posición x de las etiquetas
                canvas.drawText(String.format("%.2f", minValue + i * increment), 10, y, textPaint);
            }
        }
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void storeDataInArrays() {
        new Thread(() -> {
            registers = datasource.registerDAO().readAllData();
            adapter = new RegisterAdapter(registers, item -> {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Registro " + item.getId(), Toast.LENGTH_SHORT).show();
                // Create a bundle with the data you want to pass to the detail fragment
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                RegisterDetailFragment detailFragment = new RegisterDetailFragment();
                detailFragment.setArguments(bundle);
                replaceFragments(detailFragment);
            });

            getActivity().runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            });

            getActivity().runOnUiThread(() -> createAndShowStatisticChart());
        }).start();
    }

    private void createAndShowStatisticChart() {
        StadisticView chartView = new StadisticView(requireContext(), registers);
        graficoLayout.addView(chartView);
    }
}

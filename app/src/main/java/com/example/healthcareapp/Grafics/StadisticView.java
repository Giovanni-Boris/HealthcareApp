package com.example.healthcareapp.Grafics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.healthcareapp.Entity.Register;

import java.util.List;

public class StadisticView extends View {

    private DisplayMetrics metrics;
    private Paint pencil;
    private Paint pencilText;
    private Paint penceilLineinGraph;
    private List<Register> data;

    private int margen_x = 120;
    private int espacio_central_texto_dias = 25;
    private int distancia_dias;
    private int dias = 7;
    private float prev_x;
    private float prev_y;
    private int init_point;
    private double variable;

    public StadisticView(Context context, DisplayMetrics metrics, List<Register> registers) {
        super(context);
        this.metrics = metrics;
    }

    private void init() {
        initialStyleFigure();
        initialStyleText();
        initialStyleFigureLines();
    }

    private void initialStyleFigure() {
        float[] intervals = new float[] { 0.0f, 0.0f };
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        pencil = new Paint();
        pencil.setAntiAlias(true);
        // Utiliza tus propios valores o métodos para obtener colores
        pencil.setARGB(250, 255, 0, 0);
        pencil.setStrokeWidth(4);
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setPathEffect(dashPathEffect);
    }

    private void initialStyleFigureLines() {
        float[] intervals = new float[] { 0.0f, 0.0f };
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        penceilLineinGraph = new Paint();
        penceilLineinGraph.setAntiAlias(true);
        // Utiliza tus propios valores o métodos para obtener colores
        penceilLineinGraph.setARGB(250, 0, 0, 255);
        penceilLineinGraph.setStrokeWidth(6);
        penceilLineinGraph.setStyle(Paint.Style.STROKE);
        penceilLineinGraph.setPathEffect(dashPathEffect);
    }

    private void initialStyleText() {
        float[] intervals = new float[] { 0.0f, 0.0f };
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        pencilText = new Paint();
        pencilText.setAntiAlias(true);
        // Utiliza tus propios valores o métodos para obtener colores
        pencilText.setARGB(250, 0, 128, 0);
        pencilText.setStrokeWidth(1);
        pencilText.setTextSize(20);
        pencilText.setStyle(Paint.Style.FILL);
        pencilText.setPathEffect(dashPathEffect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int margen_inferior = 400;
        int altoMax = metrics.heightPixels;
        int anchoMax = metrics.widthPixels;
        int margen_y = 60;
        int alto_disponible = altoMax - margen_y * 2;
        int ancho_disponible = anchoMax - margen_x * 2;
        int cantidad_lineas = 10;
        distancia_dias = ancho_disponible / dias + 11;
        init_point = (cantidad_lineas - 1) * distancia_dias + margen_y;
        variable = (init_point - margen_y) / getMaxValue(data);
    }

    public void actualizarData(Register newRegister) {
        // Asegurarse de que data no exceda el límite de días
        if (data.size() == 7) {
            this.data.remove(0);
        }
        this.data.add(newRegister);
        invalidate();
    }

    private double getMaxValue(List<Register> data) {
        double max = data.get(0).getGlucemia();
        for (Register register : data) {
            double glucemia = register.getGlucemia();
            if (glucemia > max) {
                max = glucemia;
            }
        }
        return max;
    }

    public void setData(List<Register> data) {
        this.data = data.subList(data.size() - 7, data.size());
        invalidate();
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }
    /*
    private void drawLeftAxisLabels(Canvas canvas) {
        // Etiquetas en el eje izquierdo
        float maxValue = (float) data.get(0).getGlucemia();
        float minValue = (float) data.get(0).getGlucemia();
        float range = maxValue - minValue;
        float increment = range / 5; // Número de etiquetas en el eje izquierdo

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);

        for (int i = 0; i <= 5; i++) {
            float y = getHeight() - i * (getHeight() / 5);
            canvas.drawText(String.format("%.2f", minValue + i * increment), 20, y, textPaint);
        }
    }

    private void drawMainGraph(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);

        prev_x = 0;
        prev_y = (float) data.get(0).getGlucemia();

        // Dibuja la línea y coloca etiquetas en los puntos
        for (int i = 1; i < data.size(); i++) {
            float endX = i * distancia_dias;
            float endY = (float) data.get(i).getGlucemia();

            canvas.drawLine(prev_x, prev_y, endX, endY, paint);

            // Muestra el valor de glucemia en cada punto
            canvas.drawText(String.format("%.2f", data.get(i).getGlucemia()), endX, endY, textPaint);

            prev_x = endX;
            prev_y = endY;
        }
    }
    */
}

package com.example.healthcareapp.Grafics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.healthcareapp.Entity.Register;

import java.util.List;

public class StadisticViewN1 extends View {

    private List<Register> registers;

    public StadisticViewN1(Context context, List<Register> registers) {
        super(context);
        this.registers = registers;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
                canvas.drawText(String.format("%.2f", registers.get(i).getGlucemia()), endX, endY, textPaint);

                startX = endX;
                startY = endY;
            }

            // Etiquetas en el eje izquierdo
            float maxValue = (float) registers.get(0).getGlucemia();
            float minValue = (float) registers.get(0).getGlucemia();

            for (Register register : registers) {
                float glucemiaValue = (float) register.getGlucemia();
                maxValue = Math.max(maxValue, glucemiaValue);
                minValue = Math.min(minValue, glucemiaValue);
            }

            float range = maxValue - minValue;
            float increment = range / 5; // Número de etiquetas en el eje izquierdo

            for (int i = 0; i <= 5; i++) {
                float y = canvas.getHeight() - i * (canvas.getHeight() / 5);
                canvas.drawText(String.format("%.2f", minValue + i * increment), 20, y, textPaint);
            }
        }
    }
}

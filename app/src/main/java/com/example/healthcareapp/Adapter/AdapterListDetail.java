package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthcareapp.R;

import java.util.ArrayList;

public class AdapterListDetail extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> names;
    private ArrayList<Integer> imagenes = new ArrayList<>();
    public AdapterListDetail(Context context, int layout, ArrayList<String> names) {
        this.context = context;
        this.layout = layout;
        this.names = names;
        imagenes.add(android.R.drawable.ic_menu_gallery);
        imagenes.add(android.R.drawable.ic_menu_camera);
        imagenes.add(android.R.drawable.ic_menu_slideshow);
        imagenes.add(android.R.drawable.ic_menu_send);
        imagenes.add(android.R.drawable.ic_menu_share);
        imagenes.add(android.R.drawable.ic_menu_compass);
        imagenes.add(android.R.drawable.ic_menu_mapmode);
        imagenes.add(android.R.drawable.ic_menu_rotate);
    }


    @Override
    public int getCount() {
        return this.names.size();
    }

    @Override
    public Object getItem(int position) {
        return this.names.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Copiamos la vista
        View v = convertView;

        //Inflamos la vista con nuestro propio layout
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        v = layoutInflater.inflate(R.layout.list_item, null);
        // Valor actual según la posición

        String currentName = names.get(position);

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView textView = (TextView) v.findViewById(R.id.textView);
        ImageView imagen = v.findViewById(R.id.imageView);
        imagen.setImageResource(imagenes.get(position));
        textView.setText(currentName);
        //Devolvemos la vista inflada
        return v;
    }
}
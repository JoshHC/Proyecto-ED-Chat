package com.example.josue.proyecto_ed_chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsuarioAdapter extends BaseAdapter
{
    protected Activity activity;
    protected ArrayList<Usuario> items;

    public UsuarioAdapter(Activity activity, ArrayList<Usuario> items) {
        this.activity = activity;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.contact_format, null);
        }

        Usuario dir = items.get(position);

        TextView Nombre = (TextView) v.findViewById(R.id.Name);
        Nombre.setText(dir.getName());
        char [] Auxiliar =  dir.getName().toCharArray();
        TextView Temporal = (TextView) v.findViewById(R.id.Temporal);
        Temporal.setText(String.valueOf(Auxiliar[0]));
        return v;
    }
}

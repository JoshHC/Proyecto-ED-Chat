package com.example.josue.proyecto_ed_chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConversacionAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    List<Mensaje> messages = new ArrayList<Mensaje>();
    Activity activity;

    public ConversacionAdapter(Activity activity, ArrayList<Mensaje> items) {
        this.activity = activity;
        this.messages = items;

        //Inicialización del inflater
        inflater =(LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Mensaje message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View Emisor = inflater.inflate(R.layout.my_message,null);
        final View Receptor = inflater.inflate(R.layout.their_message,null);
        TextView mensajeEmisor = (TextView) Emisor.findViewById(R.id.message_body);
        TextView mensajeReceptor = (TextView) Receptor.findViewById(R.id.message_body);
        TextView nombreReceptor = (TextView) Receptor.findViewById(R.id.name);
        Mensaje message = messages.get(position);

        if(message.isPertenecealusuariologeado() == true){
            mensajeEmisor.setText(message.getMensaje());
            return Emisor;
        }else{
            mensajeReceptor.setText(message.getMensaje());
            nombreReceptor.setText(message.getReceptor());
            return Receptor;
        }
    }

    }

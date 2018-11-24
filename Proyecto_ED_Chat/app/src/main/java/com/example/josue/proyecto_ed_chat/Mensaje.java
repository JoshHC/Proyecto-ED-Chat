package com.example.josue.proyecto_ed_chat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Mensaje
{
    private String mensaje;
    private String Emisor;
    private boolean pertenecealusuariologeado;
    private String Receptor;
    private String Clave;

    public String getReceptor() {
        return Receptor;
    }

    public void setReceptor(String receptor) {

        this.Receptor = receptor;
    }

    public Mensaje(String mensaje, String emisor, boolean pertenecealusuariologeado) {
        this.mensaje = mensaje;
        //Clave Nueva = new Clave();
        Emisor = emisor;
        this.pertenecealusuariologeado = pertenecealusuariologeado;
    }

    public Mensaje(String mensaje, String emisor,String receptor, boolean pertenecealusuariologeado) {
        this.mensaje = mensaje;
        Emisor = emisor;
        Receptor = receptor;
        this.pertenecealusuariologeado = pertenecealusuariologeado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmisor() {
        return Emisor;
    }

    public void setEmisor(String emisor) {
        Emisor = emisor;
    }

    public boolean isPertenecealusuariologeado() {
        return pertenecealusuariologeado;
    }

    public void setPertenecealusuariologeado(boolean pertenecealusuariologeado) {
        this.pertenecealusuariologeado = pertenecealusuariologeado;
    }
}

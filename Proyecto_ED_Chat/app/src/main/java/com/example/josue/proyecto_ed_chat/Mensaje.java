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

    public Mensaje(String mensaje, String emisor,String receptor, boolean pertenecealusuariologeado) {
        SDES Cifrado = new SDES();

        Emisor = emisor;
        Receptor = receptor;

        //String ClaveCifrado = GenerarClaveCifrado(Emisor, Receptor);
        String ClaveCifrado = "1001000000";
        this.mensaje = Cifrado.Cifrar(mensaje, ClaveCifrado);

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

    // Genera la llave para el Cifrado de Punto a Punto
    private String GenerarClaveCifrado(String Emisor, String Receptor)
    {
        Emisor = Emisor.toUpperCase();
        Receptor = Receptor.toUpperCase();

        String[] Temp = new String[10];
        char[] emisor = Emisor.toCharArray();
        char[] receptor = Receptor.toCharArray();

        // El menor Nombre siempre va de primero
        if(Emisor.compareTo(Receptor) < 0)
        {
            for(int i = 0; i < 5; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(emisor[i]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

            for(int i = 5; i < 10; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(receptor[i-5]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

        }
        else
        {
            for(int i = 0; i < 5; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(receptor[i]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

            for(int i = 5; i < 10; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(emisor[i-5]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }
        }

        String Clave = "";

        // Si es consonante es 1 y si es vocal 0
        for(int i = 0; i < 10; i++)
        {
            if (Temp[i].equals("A") || Temp[i].equals("E") || Temp[i].equals("I") || Temp[i].equals("O") || Temp[i].equals("U"))
                Clave += "0";
            else
                Clave += "1";
        }

        return Clave;
    }
}

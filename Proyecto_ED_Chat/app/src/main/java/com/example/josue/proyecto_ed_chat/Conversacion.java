package com.example.josue.proyecto_ed_chat;

import java.util.List;

public class Conversacion
{
    private String Emisor;
    private String Receptor;
    private List<Mensaje> Mensajes;

    public Conversacion(String emisor, String receptor, List<Mensaje> mensajes) {
        Emisor = emisor;
        Receptor = receptor;
        Mensajes = mensajes;
    }

    public String getEmisor() {
        return Emisor;
    }

    public void setEmisor(String emisor) {
        Emisor = emisor;
    }

    public String getReceptor() {
        return Receptor;
    }

    public void setReceptor(String receptor) {
        Receptor = receptor;
    }

    public List<Mensaje> getMensajes() {
        return Mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        Mensajes = mensajes;
    }
}



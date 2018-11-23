package com.example.josue.proyecto_ed_chat;

public class Mensaje
{
    private String mensaje;
    private String Emisor;
    private boolean pertenecealusuariologeado;

    public Mensaje(String mensaje, String emisor, boolean pertenecealusuariologeado) {
        this.mensaje = mensaje;
        Emisor = emisor;
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

package com.lgastelu.petapp.models;

public class Usuario {

    private Long id;
    private String usuarioNombre;
    private String usuarioCorreo;
    private String usuarioContraseña;

    public Usuario(String usuarioNombre, String usuarioCorreo, String usuarioContraseña) {
        this.usuarioNombre = usuarioNombre;
        this.usuarioCorreo = usuarioCorreo;
        this.usuarioContraseña = usuarioContraseña;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public String getUsuarioContraseña() {
        return usuarioContraseña;
    }

    public void setUsuarioContraseña(String usuarioContraseña) {
        this.usuarioContraseña = usuarioContraseña;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", usuarioCorreo='" + usuarioCorreo + '\'' +
                ", usuarioContraseña='" + usuarioContraseña + '\'' +
                '}';
    }
}

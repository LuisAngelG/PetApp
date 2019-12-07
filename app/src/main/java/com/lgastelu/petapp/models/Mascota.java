package com.lgastelu.petapp.models;

public class Mascota {

    private Long id;
    private String nombres;
    private String raza;
    private String edad;
    private String foto;
    private String usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", raza='" + raza + '\'' +
                ", edad='" + edad + '\'' +
                ", foto='" + foto + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}


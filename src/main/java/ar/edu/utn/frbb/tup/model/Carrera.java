package ar.edu.utn.frbb.tup.model;

import java.util.List;

public class Carrera {
    private final String nombre;
    private int carreraId;
    private int departamento;
    private int cantidadCuatrimestres;
    private List<Materia> materiasList;

    public Carrera(String nombre, int carreraId, int departamento, int cantidadCuatrimestres) {
        this.nombre = nombre;
        this.carreraId = carreraId;
        this.departamento = departamento;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }

    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }

    public void agregarMateria(Materia materia) {
        materiasList.add(materia);
    }
}

package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.dto.MateriaDto;

import java.util.ArrayList;
import java.util.List;

public class Carrera {
    private String nombre;
    private int carreraId;
    private int departamento;
    private int cantidadCuatrimestres;
    private List<MateriaDto> materiasList = new ArrayList<>();

    public Carrera(){}
    public Carrera(String nombre, int carreraId, int departamento, int cantidadCuatrimestres) {
        this.nombre = nombre;
        this.carreraId = carreraId;
        this.departamento = departamento;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<MateriaDto> getMateriasList() {
        return materiasList;
    }

    public void setMateriasList(List<MateriaDto> materiasList) {
        this.materiasList = materiasList;
    }

    public void agregarMateria(MateriaDto materia) {
        materiasList.add(materia);
    }
}

package ar.edu.utn.frbb.tup.model.dto;

import java.util.ArrayList;
import java.util.List;

public class MateriaDto {
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private int profesorId;
    private List<Integer> carreraIds = new ArrayList<>();
    private List<Integer> correlativasIds = new ArrayList<>();

    public int getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(int profesorId) {
        this.profesorId = profesorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public List<Integer> getCarreraIds() {
        return carreraIds;
    }
    public void setCarreraIds(List<Integer> carreraIds) {
        this.carreraIds = carreraIds;
    }

    public List<Integer> getCorrelativasIds() {
        return correlativasIds;
    }

    public void setCorrelativasIds(List<Integer> correlativasIds) {
        this.correlativasIds = correlativasIds;
    }

    public void agregarCorrelativaId(int id){
        this.correlativasIds.add(id);
    }
}

package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;

import java.util.ArrayList;
import java.util.List;

public class MateriaDto {
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private long profesorId;
    private long carreraId;
    private List<Integer> correlativasIds = new ArrayList<>();

    public long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(long profesorId) {
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

    public long getCarreraId() {
        return carreraId;
    }
    public void setCarreraId(long carreraId) {
        this.carreraId = carreraId;
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

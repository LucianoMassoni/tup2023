package ar.edu.utn.frbb.tup.model.dto;

import java.util.Objects;

public class MateriaInfoDto {
    private int id;
    private String nombre;
    private int anio;
    private int cuatrimestre;

    public MateriaInfoDto(){}
    public MateriaInfoDto(int id, String nombre, int anio, int cuatrimestre){
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MateriaInfoDto that = (MateriaInfoDto) o;
        return id == that.id &&
                Objects.equals(nombre, that.nombre) &&
                anio == that.anio &&
                cuatrimestre == that.cuatrimestre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, anio, cuatrimestre);
    }
}

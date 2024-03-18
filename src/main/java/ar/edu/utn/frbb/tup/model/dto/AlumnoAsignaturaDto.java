package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Asignatura;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlumnoAsignaturaDto {
    int id;
    String nombre;
    String apellido;
    int dni;
    List<Asignatura> asignaturas = new ArrayList<>();

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public void addAsignatura(Asignatura asignatura){
        this.asignaturas.add(asignatura);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlumnoAsignaturaDto that = (AlumnoAsignaturaDto) o;
        return id == that.id &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(apellido, that.apellido) &&
                dni == that.dni &&
                Objects.equals(asignaturas, that.asignaturas);
    }
}

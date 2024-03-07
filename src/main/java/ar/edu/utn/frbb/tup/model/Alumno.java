package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private int id;
    private String nombre;
    private String apellido;
    private int dni;
    private List<Integer> asignaturasIds = new ArrayList<>();

    public Alumno() {
    }
    public Alumno(String nombre, String apellido, int dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;

        asignaturasIds = new ArrayList<>();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getDni() {
        return dni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addAsignaturaId(int id){
        this.asignaturasIds.add(id);
    }

    public void setAsignaturasIds(List<Integer> asignaturasIds){
        this.asignaturasIds = asignaturasIds;
    }

    public List<Integer> getAsignaturasIds(){
        return this.asignaturasIds;
    }

}

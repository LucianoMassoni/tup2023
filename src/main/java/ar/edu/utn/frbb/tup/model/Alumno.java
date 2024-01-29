package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private int id;
    private String nombre;
    private String apellido;
    private int dni;
    private List<Asignatura> asignaturas = new ArrayList<>();

    public Alumno() {
    }
    public Alumno(String nombre, String apellido, int dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;

        asignaturas = new ArrayList<>();
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

    public void addAsignatura(Asignatura a){
        this.asignaturas.add(a);
    }

    public void deleteAsignatura(Asignatura a){
        this.asignaturas.remove(a);
    }

    public void setAsignaturas(List<Asignatura> asignaturas){
        this.asignaturas = asignaturas;
    }

    public List<Asignatura> getAsignaturas(){
        return this.asignaturas;
    }

    /*
    * Aca terminan los getters y setters y arrancan las funciones que creo que deberian estar en la capa de
    * servicio y no en esta.
    * */

    private void chequearCorrelatividad(Materia correlativa) throws CorrelatividadException {
        for (Asignatura a: asignaturas) {
            if (correlativa.getNombre().equals(a.getNombreAsignatura())) {
                if (!EstadoAsignatura.APROBADA.equals(a.getEstado())) {
                    throw new CorrelatividadException("La asignatura " + a.getNombreAsignatura() + " no está aprobada");
                }
            }
        }
    }

    private Asignatura getAsignaturaAAprobar(Materia materia) throws AsignaturaInexistenteException {

        for (Asignatura a: asignaturas) {
            if (materia.getNombre().equals(a.getNombreAsignatura())) {
                return a;
            }
        }
        throw new AsignaturaInexistenteException("No se encontró la materia");
    }

    public boolean puedeAprobar(Asignatura asignatura) {
        return true;
    }

    public void actualizarAsignatura(Asignatura asignatura) {
        for (Asignatura a:
             asignaturas) {
            if (a.getNombreAsignatura().equals(asignatura.getNombreAsignatura())) {
                a.setEstado(asignatura.getEstado());
                a.setNota(asignatura.getNota().get());
            }
        }

    }
}

package ar.edu.utn.frbb.tup.model.dto;

import java.util.ArrayList;
import java.util.List;

public class AlumnoDto {
    String nombre;
    String apellido;
    long dni;

    //ToDo esta lista puede ser de AsignaturaDto asi cuando creo las materias despues puedo buscarlas por id dentro del dto.
    // o puede ser una lista de ids de las materias a las que pertenecen las asignaturas y las asignaturas se crean a partir de eso./ idMateriaDeAsignatura
    List<Long> idMateriasDeAsignatura = new ArrayList<>();

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

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public List<Long> getIdMateriasDeAsignatura() {
        return idMateriasDeAsignatura;
    }

    public void setIdMateriaDeAsignatura(List<Long> idMateriasDeAsignatura) {
        this.idMateriasDeAsignatura = idMateriasDeAsignatura;
    }

    public void addIdMateriaDeAsignatura(Long idMateriaDeAsinatura){
        this.idMateriasDeAsignatura.add(idMateriaDeAsinatura);
    }
}

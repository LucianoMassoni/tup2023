package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface AsignaturaService {

    Asignatura crearAsignatura(long materiaId) throws MateriaNotFoundException;
    Asignatura getAsignatura(int materiaId, long dni);
    List<Asignatura> getAllAsignaturas();
    void eliminarAsignatura(long asignaturaId) throws AsignaturaNotFoundException;
    void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException;


}

package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface AsignaturaService {

    Asignatura crearAsignatura(int materiaId) throws MateriaNotFoundException;
    Asignatura getAsignatura(int idAsignatura) throws AsignaturaNotFoundException;
    List<Asignatura> getAllAsignaturas();
    void eliminarAsignatura(int asignaturaId) throws AsignaturaNotFoundException;
    void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException;

     List<Materia> getListaMateriasCorrelativasByIdAsignaturaYIdMateria(int idAsignatura, int idMateria) throws MateriaNotFoundException, AsignaturaNotFoundException;
     List<Asignatura> getListaAsignaturasDeMateriasCorrelativas(int idAsignatura, int idMateria) throws MateriaNotFoundException, AsignaturaNotFoundException;

}

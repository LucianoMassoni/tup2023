package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface AsignaturaService {

    Asignatura crearAsignatura(int materiaId) throws MateriaNotFoundException;
    Asignatura getAsignatura(int idAsignatura) throws AsignaturaNotFoundException;
    List<Asignatura> getAllAsignaturas();
    void eliminarAsignatura(int asignaturaId) throws AsignaturaNotFoundException;
    void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException;

    void verificarAsignaturaEstaAprobada(int asignaturaId) throws EstadoIncorrectoException, AsignaturaNotFoundException;

    void verificarAsignaturaEstaCursada(int asignaturaId) throws EstadoIncorrectoException, AsignaturaNotFoundException;

    void verificarAsignaturaEstaNoCursada(int asignaturaId) throws EstadoIncorrectoException, AsignaturaNotFoundException;

    void verificarCorrelativasEstenAprobadas(int asignaturaId, List<Integer> asignaturasIds) throws AsignaturaNotFoundException, EstadoIncorrectoException;

    void verificarCorrelativasEstenCursadas(int asignaturaId, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException, EstadoIncorrectoException;

    void verificarAsignaturasParaPerderCursada(int asignaturaId, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException, EstadoIncorrectoException;

    void verificarNotaCorrecta(int nota);
}

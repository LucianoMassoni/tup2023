package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

import java.util.List;

public interface AsignaturaDao {

    Asignatura save(Asignatura a);

    Asignatura actualizar(Asignatura a) throws AsignaturaNotFoundException;

    void delete(long id) throws AsignaturaNotFoundException;

    List<Asignatura> getAllAsignaturas();
}

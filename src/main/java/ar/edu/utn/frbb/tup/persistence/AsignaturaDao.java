package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

import java.util.List;
import java.util.Map;

public interface AsignaturaDao {
    Asignatura save(Asignatura a);

    void actualizar(Asignatura a) throws AsignaturaNotFoundException;

    void delete(int id) throws AsignaturaNotFoundException;

    Asignatura findById(int id) throws AsignaturaNotFoundException;

    Map<Integer, Asignatura> getAllAsignaturas();
}

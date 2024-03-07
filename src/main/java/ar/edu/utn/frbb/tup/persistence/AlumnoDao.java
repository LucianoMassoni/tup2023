package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

import java.util.Map;

public interface AlumnoDao {

    Alumno save(Alumno a);

    Alumno findById(Integer id) throws AlumnoNotFoundException;

    void actualizar(Alumno a) throws AlumnoNotFoundException;

    void delete(Integer id) throws AlumnoNotFoundException;

    Map<Integer, Alumno> getAllAlunno();
}

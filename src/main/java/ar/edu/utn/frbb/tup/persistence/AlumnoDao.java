package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

public interface AlumnoDao {

    Alumno saveAlumno(Alumno a);

    Alumno findAlumno(Integer id) throws AlumnoNotFoundException;

    Alumno loadAlumno(Integer id);

    Alumno actualizarAlumno(Alumno a) throws AlumnoNotFoundException;

    void deleteAlumno(Integer id);
}

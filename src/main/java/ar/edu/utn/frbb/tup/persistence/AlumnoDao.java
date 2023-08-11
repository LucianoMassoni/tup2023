package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

public interface AlumnoDao {

    Alumno saveAlumno(Alumno a);

    Alumno findAlumno(Long id);

    Alumno loadAlumno(Long id);

    Alumno actualizarAlumno(Alumno a);

    void deleteAlumno(Long id);
}

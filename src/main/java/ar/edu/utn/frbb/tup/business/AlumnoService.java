package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface AlumnoService {
    Alumno crearAlumno(AlumnoDto alumno) throws MateriaNotFoundException;

    Alumno buscarAlumno(int alumnoId) throws AlumnoNotFoundException;

    void eliminarAlumno(int alumnoId) throws AsignaturaNotFoundException, AlumnoNotFoundException;

    void actualizarAlumno(int alumnoId, AlumnoDto alumnoDto) throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException;

}

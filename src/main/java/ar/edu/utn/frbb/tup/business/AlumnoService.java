package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface AlumnoService {
    void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AsignaturaNotFoundException;

    Alumno crearAlumno(AlumnoDto alumno) throws MateriaNotFoundException;

    Alumno buscarAlumno(Long alumnoId);

    void eliminarAlumno(Long alumnoId) throws AsignaturaNotFoundException;

    Alumno actualizarAlumno(AlumnoDto alumno) throws MateriaNotFoundException, AsignaturaNotFoundException;

    Alumno cambiarEstadoAsignatura(long idAlumno, long idAsignatura, AsignaturaDto asignaturaDto);
}

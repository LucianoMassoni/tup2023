package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface AlumnoService {
    void aprobarAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AsignaturaNotFoundException;

    Alumno crearAlumno(AlumnoDto alumno) throws MateriaNotFoundException;

    Alumno buscarAlumno(int alumnoId) throws AlumnoNotFoundException;

    void eliminarAlumno(int alumnoId) throws AsignaturaNotFoundException;

    Alumno actualizarAlumno(AlumnoDto alumno) throws MateriaNotFoundException, AsignaturaNotFoundException, AlumnoNotFoundException;

    Alumno cambiarEstadoAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, MateriaNotFoundException, EstadoIncorrectoException;

    Alumno aprobar(Alumno alumno, Asignatura asignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException;
}

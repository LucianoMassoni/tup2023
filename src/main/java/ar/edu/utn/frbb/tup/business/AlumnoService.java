package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

public interface AlumnoService {
    void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException;

    Alumno crearAlumno(AlumnoDto alumno);

    Alumno buscarAlumno(String apellidoAlumno);

    void eliminarAlumno(long alumnoId);

    //Funcion de prueba
    Alumno cargarAlumno(long dni);

    //Alumno actualizarAlumno(AlumnoDto alumno);
}

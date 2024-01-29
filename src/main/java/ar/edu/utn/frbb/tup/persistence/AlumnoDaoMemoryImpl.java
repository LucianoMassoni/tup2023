package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Integer, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public Alumno saveAlumno(Alumno alumno) {
        Random random = new Random();
        alumno.setId(random.nextInt());
        return repositorioAlumnos.put(alumno.getId(), alumno);
    }

    @Override
    public Alumno findAlumno(Integer id) throws AlumnoNotFoundException {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == id){
                return a;
            }
        }
        throw new AlumnoNotFoundException("No se encontró un alumno con el id: " + id);
    }

    @Override
    public void deleteAlumno(Integer idAlumno){
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == idAlumno){
                repositorioAlumnos.remove(idAlumno);
            }
        }
    }

    @Override
    public Alumno loadAlumno(Integer id) {
        return repositorioAlumnos.getOrDefault(id, null);
    }

    @Override
    public Alumno actualizarAlumno(Alumno a) throws AlumnoNotFoundException {
        if (findAlumno(a.getId()) == null)
            return null;
        else {
            return repositorioAlumnos.put(a.getId(),a);
        }
    }
}

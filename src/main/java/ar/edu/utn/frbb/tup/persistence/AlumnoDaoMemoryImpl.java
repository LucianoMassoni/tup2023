package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public Alumno saveAlumno(Alumno alumno) {
        Random random = new Random();
        alumno.setId(random.nextLong());
        return repositorioAlumnos.put(alumno.getDni(), alumno);
    }

    @Override
    public Alumno findAlumno(Long id) {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == id){
                return a;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existen alumnos con esos datos."
        );
    }

    @Override
    public void deleteAlumno(Long idAlumno){
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == idAlumno){
                repositorioAlumnos.remove(idAlumno);
            }
        }
    }

    @Override
    public Alumno loadAlumno(Long dni) {
        return repositorioAlumnos.getOrDefault(dni, null);
    }

    @Override
    public Alumno actualizarAlumno(Alumno a){
        if (findAlumno(a.getId()) == null)
            return null;
        else {
            return repositorioAlumnos.put(a.getId(),a);
        }
    }
}

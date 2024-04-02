package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static final Map<Integer, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public Alumno save(Alumno alumno) {
        Random r = new Random();
        int random;

        do {
            random = r.nextInt();
            if (random < 0) random *= -1;
        }while (repositorioAlumnos.containsKey(random));

        alumno.setId(random);
        repositorioAlumnos.put(alumno.getId(), alumno);

        return alumno;
    }

    @Override
    public Alumno findById(Integer id) throws AlumnoNotFoundException {
        if (!repositorioAlumnos.containsKey(id))
            throw new AlumnoNotFoundException("No se encontró un alumno con el id: " + id);
        return repositorioAlumnos.get(id);
    }

    @Override
    public void delete(Integer idAlumno) throws AlumnoNotFoundException {
        if (!repositorioAlumnos.containsKey(idAlumno)){
            throw new AlumnoNotFoundException("No se encontró un alumno con el id: " + idAlumno);
        }
        repositorioAlumnos.remove(idAlumno);
    }

    @Override
    public void update(Alumno alumno) throws AlumnoNotFoundException {
        if (!repositorioAlumnos.containsValue(alumno)){
            throw new AlumnoNotFoundException("No existe el alumno");
        }
        repositorioAlumnos.replace(alumno.getId(), alumno);
    }

    public Map<Integer, Alumno> getAllAlunno(){
        return repositorioAlumnos;
    }
}

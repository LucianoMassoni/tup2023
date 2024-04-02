package ar.edu.utn.frbb.tup.persistance;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.impl.AlumnoDaoMemoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class AlumnoDaoTest {
    @InjectMocks
    private AlumnoDaoMemoryImpl dao;

    @Test
    public void SaveTest(){
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        Alumno alumnoGuardado = dao.save(alumno);

        //asserts
        assertNotNull(alumnoGuardado);
        assertEquals(alumno, alumnoGuardado);
        assertTrue(dao.getAllAlunno().containsValue(alumno));

        // comprobar si se guardo el alumno
        Map<Integer, Alumno> alumnos = dao.getAllAlunno();
        assertTrue(alumnos.containsValue(alumno));
        assertTrue(alumnos.containsKey(alumno.getId()));
        assertEquals(alumno, alumnos.get(alumno.getId()));
    }

    @Test
    public void findByIdTest() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        dao.save(alumno);

        Alumno alumnoBusqueda = dao.findById(alumno.getId());

        assertNotNull(alumnoBusqueda);
        assertEquals(alumnoBusqueda, alumno);
    }

    @Test
    public void findByIdAlumnoInexistenteTest() throws AlumnoNotFoundException {
        int id = 1;

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> dao.findById(id));

        assertEquals("No se encontró un alumno con el id: " + id, exception.getMessage());
    }

    @Test
    public void deleteTest() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        dao.save(alumno);

        Map<Integer, Alumno> repo = dao.getAllAlunno();
        assertTrue(repo.containsValue(alumno));

        dao.delete(alumno.getId());

        assertFalse(repo.containsValue(alumno));
    }

    @Test
    public void deleteAlumnoInexistenteTest() throws AlumnoNotFoundException {
        int id = 1;

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> dao.delete(id));

        assertEquals("No se encontró un alumno con el id: " + id, exception.getMessage());
    }

    //La función del business controla la longitud del dni y la existencia de las asignaturas además de que no se puede
    // cambiar el ID.
    @Test
    public void actualizarTest() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        dao.save(alumno);

        alumno.setNombre("Otro nombre");

        dao.update(alumno);

        assertEquals(alumno.getNombre(), "Otro nombre");
        assertEquals(alumno.getApellido(), "Apellido");
        assertEquals(alumno.getDni(), 12345678);
        assertEquals(alumno.getAsignaturasIds(), List.of(1,2));
    }

    @Test
    public void actualizarAlumnoInexistenteTest() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> dao.update(alumno));

        assertEquals("No existe el alumno", exception.getMessage());
    }

    @Test
    public void getAllAlumnosTest(){
        Alumno alumno = new Alumno();
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        dao.save(alumno);

        Map<Integer, Alumno> repo = dao.getAllAlunno();

        assertTrue(repo.containsValue(alumno));
    }
}

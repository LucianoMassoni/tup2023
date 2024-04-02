package ar.edu.utn.frbb.tup.persistance;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.impl.AsignaturaDaoMemoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AsignaturaDaoTest {
    @InjectMocks
    private AsignaturaDaoMemoryImpl dao;

    @Test
    public void saveAsignaturaTest(){
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        Asignatura asignatura = new Asignatura(materia);

        Asignatura asignaturaGuardada = dao.save(asignatura);

        //asserts
        assertNotNull(asignaturaGuardada);
        assertEquals(asignatura, asignaturaGuardada);
        assertTrue(dao.getAllAsignaturas().containsValue(asignatura));

        // comprobar si se guardo el alumno
        Map<Integer, Asignatura> asignaturas = dao.getAllAsignaturas();
        assertTrue(asignaturas.containsValue(asignatura));
        assertTrue(asignaturas.containsKey(asignatura.getId()));
        assertEquals(asignatura, asignaturas.get(asignatura.getId()));
    }

    @Test
    public void deleteAsignaturaTest() throws AsignaturaNotFoundException {
        Asignatura asignatura = new Asignatura();

        dao.save(asignatura);

        Map<Integer, Asignatura> repo = dao.getAllAsignaturas();
        assertTrue(repo.containsValue(asignatura));

        dao.delete(asignatura.getId());

        assertFalse(repo.containsValue(asignatura));

    }

    @Test
    public void deleteAsignaturaInexistenteTest(){
        int id = 1;

        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> dao.delete(id));

        assertEquals("No se encontró una asignatura con id: " + id, exception.getMessage());
    }

    @Test
    public void actualizarAsignaturaTest() throws AsignaturaNotFoundException {
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        Asignatura asignatura = new Asignatura(materia);

        dao.save(asignatura);

        assertEquals(asignatura.getEstado(), EstadoAsignatura.NO_CURSADA);

        asignatura.setEstado(EstadoAsignatura.CURSADA);

        dao.update(asignatura);

        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
    }

    @Test
    public void actualizarAsignaturaInexistenteTest(){
        Asignatura asignatura = new Asignatura();

        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> dao.update(asignatura));

        assertEquals("No se encontró una asignatura.", exception.getMessage());
    }

    @Test
    public void findByIdAsignaturaTest() throws AsignaturaNotFoundException {
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        Asignatura asignatura = new Asignatura(materia);

        dao.save(asignatura);

        Asignatura asignaturaBusqueda = dao.findById(asignatura.getId());

        assertEquals(asignatura, asignaturaBusqueda);
    }

    @Test
    public void findByIdAsgnaturaInexistenteTest(){
        int id = 1;

        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> dao.findById(id));

        assertEquals("No se encontró una asignatura con id: " + id, exception.getMessage());
    }
}

package ar.edu.utn.frbb.tup.persistance;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.impl.MateriaDaoMemoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class MateriaDaoTest {
    @InjectMocks
    private MateriaDaoMemoryImpl dao;

    @Test
    public void saveMateriaTest(){
        Map<Integer, Materia> repo;

        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        repo = dao.getAllMaterias();
        assertFalse(repo.containsValue(materia));

        dao.save(materia);

        assertTrue(repo.containsValue(materia));
    }

    @Test
    public void deleteMateriaTest() throws MateriaNotFoundException {
        Map<Integer, Materia> repo;

        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        repo = dao.getAllMaterias();
        assertFalse(repo.containsValue(materia));

        dao.save(materia);
        assertTrue(repo.containsValue(materia));

        dao.delete(materia.getMateriaId());
        assertFalse(repo.containsValue(materia));
    }

    @Test
    public void deleteMateriaInexistenteTest(){
        int id = 1;

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> dao.delete(id));

        assertEquals("No se encontró la materia con id: " + id, exception.getMessage());
    }

    @Test
    public void findByIdMateriaTest() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        dao.save(materia);

        Materia materiaBusqueda = dao.findById(materia.getMateriaId());

        assertEquals(materia, materiaBusqueda);
    }

    @Test
    public void findByIdMateriaInexistenteTest(){
        int id = 1;

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> dao.findById(id));

        assertEquals("No se encontró la materia con id: " + id, exception.getMessage());
    }

    @Test
    public void updateMateriaTest() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        dao.save(materia);

        materia.setNombre("materia2");
        materia.setAnio(2);
        materia.setCuatrimestre(2);

        dao.update(materia);
        Materia m = dao.findById(materia.getMateriaId());

        assertEquals(m.getNombre(), "materia2");
        assertEquals(m.getAnio(), 2);
        assertEquals(m.getCuatrimestre(), 2);
    }

    @Test
    public void updateMateriaInexistente(){
        Materia materia = new Materia();
        materia.setNombre("materia");
        materia.setAnio(1);
        materia.setCuatrimestre(1);

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> dao.update(materia));

        assertEquals("No se encontro la materia", exception.getMessage());
    }
}

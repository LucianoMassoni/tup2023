package ar.edu.utn.frbb.tup.persistance;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.impl.CarreraDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class CarreraDaoTest {
    @InjectMocks
    private CarreraDaoImpl dao;

    @Test
    public void saveCarreraTest(){
        Map<Integer, Carrera> repo;

        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera");
        carrera.setDepartamento(1);
        carrera.setCantidadCuatrimestres(3);
        carrera.setMateriasIds(List.of(1,2));

        repo = dao.getAll();

        assertFalse(repo.containsValue(carrera));

        dao.save(carrera);

        repo = dao.getAll();
        assertTrue(repo.containsValue(carrera));
    }

    @Test
    public void findByIdCarreraTest() throws CarreraNotFoundException {
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera");
        carrera.setDepartamento(1);
        carrera.setCantidadCuatrimestres(3);
        carrera.setMateriasIds(List.of(1,2));
        dao.save(carrera);

        Carrera carreraBusqueda = dao.findById(carrera.getCarreraId());

        assertEquals(carrera, carreraBusqueda);
    }

    @Test
    public void findByIdCarreraInexistenteTest(){
        int id = 1;

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> dao.findById(id));

        assertEquals("No se encontró una carrera con el id: " + id, exception.getMessage());
    }

    @Test
    public void deleteCarreraTest() throws CarreraNotFoundException {
        Map<Integer, Carrera> repo;

        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera");
        carrera.setDepartamento(1);
        carrera.setCantidadCuatrimestres(3);
        carrera.setMateriasIds(List.of(1,2));

        dao.save(carrera);
        repo = dao.getAll();
        assertTrue(repo.containsValue(carrera));

        dao.delete(carrera.getCarreraId());

        repo = dao.getAll();
        assertFalse(repo.containsValue(carrera));
    }

    @Test
    public void deleteCarreraInexistenteTest(){
        int id = 1;

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> dao.delete(id));

        assertEquals("No se encontró una carrera con el id: " + id, exception.getMessage());
    }

    @Test
    public void updateCarreraTest(){
        Carrera carrera = new Carrera();
        carrera.setNombre("Carrera");
        carrera.setDepartamento(1);
        carrera.setCantidadCuatrimestres(3);
        carrera.setMateriasIds(List.of(1,2));

        dao.save(carrera);

        carrera.setNombre("Carrsera");
        carrera.setDepartamento(2);
        carrera.setCantidadCuatrimestres(3);
        carrera.setMateriasIds(List.of(2));

        dao.update(carrera);

        assertEquals(carrera.getNombre(), "Carrsera");
        assertEquals(carrera.getDepartamento(), 2);
        assertEquals(carrera.getCantidadCuatrimestres(), 3);
        assertEquals(carrera.getMateriasIds(), List.of(2));
    }
}

package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.CarreraServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CarreraServiceTest {

    @InjectMocks
    CarreraServiceImpl carreraService;
    @Mock
    CarreraDao carreraDao;

    @Test
    public void testCrearCarrera(){
        //config del mock
        when(carreraDao.save(any(Carrera.class))).thenAnswer(invocation -> invocation.<Carrera>getArgument(0));

        //datos
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("TUP");
        carreraDto.setDepartamento(1);
        carreraDto.setCantidadCuatrimestres(4);

        //metodo a probar
        Carrera resultado = carreraService.crearCarrera(carreraDto);

        //asserts
        assertNotNull(resultado);
        assertEquals(resultado.getNombre(), carreraDto.getNombre());
        assertEquals(resultado.getDepartamento(), carreraDto.getDepartamento());
        assertEquals(resultado.getCantidadCuatrimestres(), carreraDto.getCantidadCuatrimestres());
        assertTrue(resultado.getMateriasIds().isEmpty());
    }

    @Test
    public void testCrearMateriaConMismoNombre(){
        Carrera carreraGuardada = new Carrera();
        carreraGuardada.setCarreraId(1);
        carreraGuardada.setNombre("TUP");

        Map<Integer, Carrera> carreraMap = new HashMap<>();
        carreraMap.put(1, carreraGuardada);

        //Config mock
        when(carreraDao.getAll()).thenReturn(carreraMap);

        //datos
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("TUP");

        //metodo a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carreraService.crearCarrera(carreraDto));

        //asserts
        assertEquals("Ya existe una carrera con el nombre " + carreraDto.getNombre(), exception.getMessage());
    }

    @Test
    public void testCrearMateriaDepartamentoInvalido(){
        //config del mock
        when(carreraDao.save(any(Carrera.class))).thenAnswer(invocation -> invocation.<Carrera>getArgument(0));

        //datos
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("TUP");
        carreraDto.setDepartamento(-1);
        carreraDto.setCantidadCuatrimestres(4);

        //metodo a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carreraService.crearCarrera(carreraDto));

        //asserts
        assertEquals("El departanto no puede tener un id negativo", exception.getMessage());
    }

    @Test
    public void testCrearMateriacuatrimestreInvalido(){
        //config del mock
        when(carreraDao.save(any(Carrera.class))).thenAnswer(invocation -> invocation.<Carrera>getArgument(0));

        //datos
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("TUP");
        carreraDto.setDepartamento(1);
        carreraDto.setCantidadCuatrimestres(0);

        //metodo a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carreraService.crearCarrera(carreraDto));

        //asserts
        assertEquals("El cuatrimestre no puede ser un numero negativo o 0", exception.getMessage());
    }

    @Test
    public void testBuscarCarrera() throws CarreraNotFoundException {
        //datos
        Carrera carrera = new Carrera();
        carrera.setCarreraId(1);
        carrera.setNombre("TUP");
        carrera.setDepartamento(1);
        carrera.setCantidadCuatrimestres(0);

        //config del mock
        when(carreraDao.load(1)).thenReturn(carrera);

        //metodo a probar
        Carrera resultado = carreraService.buscarCarrera(1);

        //asserts
        assertNotNull(resultado);
        assertEquals(resultado, carrera);
    }

    @Test
    public void testBuscarCarreraInexistente() throws CarreraNotFoundException {
        int carreraId = 1;
        //config del mock
        when(carreraDao.load(carreraId)).thenThrow(new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId));

        //metodo a probar
        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> carreraService.buscarCarrera(carreraId));

        //asserts
        assertEquals("No se encontró una carrera con el id: " + carreraId, exception.getMessage());
    }

    @Test
    public void testActualizarCarrera() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP",carreraId,2,4);
        when(carreraDao.load(carreraId)).thenReturn(carrera);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Matematica");
        carreraDto.setCantidadCuatrimestres(6);

        carreraService.actualizarCarrera(carreraId, carreraDto);

        assertEquals(carrera.getNombre(), carreraDto.getNombre());
        assertEquals(carrera.getCantidadCuatrimestres(), carreraDto.getCantidadCuatrimestres());
    }

    @Test
    public void testActualizarCarreraANombreExistente() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP",carreraId,2,4);
        Carrera carreraExistente = new Carrera("Matematica",4,2,4);
        Map<Integer, Carrera> carreraMap = new HashMap<>();
        carreraMap.put(carreraExistente.getCarreraId(), carreraExistente);

        when(carreraDao.load(carreraId)).thenReturn(carrera);
        when(carreraDao.getAll()).thenReturn(carreraMap);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Matematica");
        carreraDto.setCantidadCuatrimestres(6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> carreraService.actualizarCarrera(carreraId, carreraDto));

        assertEquals("Ya existe una carrera con el nombre " + carreraExistente.getNombre(), exception.getMessage());
    }

    @Test
    public void testActualizarCarreraCuatrimestreInvalido() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP",carreraId,2,4);
        when(carreraDao.load(carreraId)).thenReturn(carrera);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Matematica");
        carreraDto.setCantidadCuatrimestres(-6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> carreraService.actualizarCarrera(carreraId, carreraDto));

        assertEquals("El cuatrimestre no puede ser un numero negativo o 0", exception.getMessage());
    }

    @Test
    public void testActualizarCarreraDepartamentoInvalido() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP",carreraId,2,4);
        when(carreraDao.load(carreraId)).thenReturn(carrera);

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Matematica");
        carreraDto.setCantidadCuatrimestres(6);
        carreraDto.setDepartamento(-4);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> carreraService.actualizarCarrera(carreraId, carreraDto));

        assertEquals("El departanto no puede tener un id negativo", exception.getMessage());
    }

    @Test
    public void testEliminarCarrera() throws CarreraNotFoundException {
        // Configurar el mock para que no lance excepción al llamar a delete
        doNothing().when(carreraDao).delete(anyInt());

        // Ejecutar el método a probar
        carreraService.eliminarCarrera(1);

        // Verificar que se llamó al método delete del DAO con el id correcto
        verify(carreraDao).delete(1);
    }

    @Test
    public void testEliminarCarreraInexistente() throws CarreraNotFoundException {
        int carreraId = 1;
        // Config del mock
        doThrow(new CarreraNotFoundException("No se encontró una carrera con el id: "+ carreraId)).when(carreraDao).delete(anyInt());

        // metodo a probar
        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> carreraService.eliminarCarrera(carreraId));

        // asserts
        assertNotNull(exception);
        assertEquals("No se encontró una carrera con el id: "+ carreraId, exception.getMessage());
    }

    @Test
    public void testAgregarMateriaEnCarrera() throws CarreraNotFoundException{
        int carreraId = 1;
        int materiaId = 1;
        Carrera carrera = new Carrera("TUP",carreraId, 2, 4);

        when(carreraDao.load(carreraId)).thenReturn(carrera);

        assertTrue(carrera.getMateriasIds().isEmpty());

        carreraService.agregarMateriaEnCarrera(carreraId, materiaId);

        assertTrue(carrera.getMateriasIds().contains(materiaId));
    }

    @Test
    public void testAgregarMateriaEnCarreraInexistente() throws CarreraNotFoundException {
        int carreraId = 1;
        int materiaId = 1;

        when(carreraDao.load(carreraId)).thenThrow(new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId));

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> carreraService.agregarMateriaEnCarrera(carreraId, materiaId));

        assertEquals("No se encontró una carrera con el id: " + carreraId, exception.getMessage());
    }

    @Test
    public void testEliminarMateriaEnCarrera() throws CarreraNotFoundException {
        int carreraId = 1;
        int materiaId = 5;
        Carrera carrera = new Carrera("TUP",carreraId, 2, 4);
        carrera.agregarMateria(materiaId);

        when(carreraDao.load(carreraId)).thenReturn(carrera);

        carreraService.eliminarMateriaEnCarrera(carreraId, materiaId);

        assertFalse(carrera.getMateriasIds().contains(materiaId));
        assertTrue(carrera.getMateriasIds().isEmpty());
    }

    @Test
    public void testEliminarMateriaEnCarreraInexistente() throws CarreraNotFoundException {
        int carreraId = 1;
        int materiaId = 1;

        when(carreraDao.load(carreraId)).thenThrow(new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId));

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> carreraService.eliminarMateriaEnCarrera(carreraId, materiaId));

        assertEquals("No se encontró una carrera con el id: " + carreraId, exception.getMessage());
    }

    @Test
    public void testEliminarMateriaIxistenteEnCarrera() throws CarreraNotFoundException {
        int carreraId = 1;
        int materiaId = 1;
        Carrera carrera = new Carrera("TUP",carreraId, 2, 4);

        when(carreraDao.load(carreraId)).thenReturn(carrera);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carreraService.eliminarMateriaEnCarrera(carreraId, materiaId));

        assertEquals("La carrera " + carrera.getNombre() + " no tiene una materia con id: " + materiaId, exception.getMessage());
    }

    @Test
    public void testActualizarMateriaEnCarrera(){
        /*
          Esta funcion se accede desde materiaService donde ya se testea que exista la carrera ademas de otras
          verificaciones por lo que no lo testeo aca.
          **/
        int materiaId = 1;
        List<Integer> carrerasIds = new ArrayList<>(Arrays.asList(2, 3));
        Map<Integer, Carrera> carreraMap = new HashMap<>();

        Carrera carrera1 = new Carrera("carrera 1",1, 2, 4);
        Carrera carrera2 = new Carrera("carrera 2",2, 2, 4);
        Carrera carrera3 = new Carrera("carrera 3",3, 2, 4);

        carrera1.getMateriasIds().add(materiaId);
        carrera2.getMateriasIds().add(materiaId);

        carreraMap.put(carrera1.getCarreraId(),carrera1);
        carreraMap.put(carrera2.getCarreraId(),carrera2);
        carreraMap.put(carrera3.getCarreraId(),carrera3);

        when(carreraDao.getAll()).thenReturn(carreraMap);

        carreraService.actualizarMateriaEnCarrera(carrerasIds, materiaId);

        assertFalse(carrera1.getMateriasIds().contains(materiaId));
        assertTrue(carrera2.getMateriasIds().contains(materiaId));
        assertTrue(carrera3.getMateriasIds().contains(materiaId));
    }
}

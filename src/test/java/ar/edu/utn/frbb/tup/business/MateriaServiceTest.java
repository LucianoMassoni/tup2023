package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.MateriaServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MateriaServiceTest {

    @InjectMocks
    private MateriaServiceImpl materiaService;
    @Mock
    private CarreraService carreraService;
    @Mock
    private ProfesorService profesorService;
    @Mock
    private MateriaDao dao;

    @Test
    public void testCrearMateriasinCorrelativasNiCarrera() throws CarreraNotFoundException, MateriaNotFoundException {
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        // Configurar mocks
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);


        // Ejecutar el método a probar
        Materia resultado = materiaService.crearMateria(materiaDto);

        // Aserts
        assertNotNull(resultado);
        assertEquals("Programacion 1", resultado.getNombre());
        assertEquals(1, resultado.getAnio());
        assertEquals(2, resultado.getCuatrimestre());
        assertEquals(profesor, resultado.getProfesor());
        assertEquals(0, resultado.getCorrelatividades().size());
    }

    @Test
    public void testCrearMateriasinCorrelativas() throws CarreraNotFoundException, MateriaNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));


        // Ejecutar el método a probar
        Materia resultado = materiaService.crearMateria(materiaDto);

        // Aserts
        assertNotNull(resultado);
        assertEquals("Programacion 1", resultado.getNombre());
        assertTrue(resultado.getCarreraIds().contains(carrera.getCarreraId()));
        assertEquals(1, resultado.getAnio());
        assertEquals(2, resultado.getCuatrimestre());
        assertEquals(profesor, resultado.getProfesor());
        assertEquals(0, resultado.getCorrelatividades().size());
    }

    @Test
    public void testCrearMateriaConCorrelativas() throws CarreraNotFoundException, MateriaNotFoundException {
        int carreraId = 1;
        Materia mCorrelativa1 = new Materia();
        Materia mCorrelativa2 = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan Pedro", "Fasola", "seee");

        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(1);
        mCorrelativa1.setCuatrimestre(1);
        mCorrelativa1.setProfesor(profesor);
        mCorrelativa1.setCarreraIds(List.of(carreraId));

        mCorrelativa2.setMateriaId(2);
        mCorrelativa2.setNombre("lab 1");
        mCorrelativa2.setAnio(1);
        mCorrelativa2.setCuatrimestre(1);
        mCorrelativa2.setProfesor(profesor);
        mCorrelativa2.setCarreraIds(List.of(carreraId));

        MateriaInfoDto mCorrelativaInfo1 = new MateriaInfoDto(mCorrelativa1.getMateriaId(), mCorrelativa1.getNombre(), mCorrelativa1.getAnio(), mCorrelativa1.getCuatrimestre());
        MateriaInfoDto mCorrelativaInfo2 = new MateriaInfoDto(mCorrelativa2.getMateriaId(), mCorrelativa2.getNombre(), mCorrelativa2.getAnio(), mCorrelativa2.getCuatrimestre());

        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(1)).thenReturn(mCorrelativa1);
        when(dao.findById(2)).thenReturn(mCorrelativa2);
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 2");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));
        materiaDto.setCorrelativasIds(Arrays.asList(1,2));

        // Ejecutar el método a probar
        Materia resultado = materiaService.crearMateria(materiaDto);

        // Aserts
        assertNotNull(resultado);
        assertEquals("Programacion 2", resultado.getNombre());
        assertTrue(resultado.getCarreraIds().contains(carreraId));
        assertEquals(1, resultado.getAnio());
        assertEquals(2, resultado.getCuatrimestre());
        assertEquals(profesor, resultado.getProfesor());
        assertEquals(2, resultado.getCorrelatividades().size());
        assertTrue(resultado.getCorrelatividades().contains(mCorrelativaInfo1));
        assertTrue(resultado.getCorrelatividades().contains(mCorrelativaInfo2));

    }

    @Test
    public void testCrearMateriaNombreRepetido() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.getAllMaterias()).thenReturn(createMateriasMap());
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));


        // Ejecutar el método a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.crearMateria(materiaDto));

        // Aserts
        assertEquals("Ya existe una materia con el nombre Programacion 1", exception.getMessage());

    }

    // Método de utilidad para crear un mapa de materias ficticias
    private Map<Integer, Materia> createMateriasMap() {
        Map<Integer, Materia> materias = new HashMap<>();
        Materia materiaExistente = new Materia();
        materiaExistente.setMateriaId(1);
        materiaExistente.setNombre("Programacion 1");
        materias.put(1, materiaExistente);
        return materias;
    }

    @Test
    public void testCrearMateriaCarreraInexistente() throws CarreraNotFoundException {
        int carreraId = 1;
        Profesor profesor = new Profesor("Juan Pedro", "Fasola", "seee");
        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenThrow(new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId));
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));


        // Ejecutar el método a probar
        CarreraNotFoundException exception = assertThrows( CarreraNotFoundException.class, () -> materiaService.crearMateria(materiaDto));

        // Aserts
        assertEquals("No se encontró una carrera con el id: " + carreraId, exception.getMessage());

    }

    @Test
    public void testCrearMateriaCoincidenciaCuatrimestresAnosDeCarrera() throws CarreraNotFoundException {
        int carreraId = 1;
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(4);
        materiaDto.setCuatrimestre(2);
        materiaDto.setCarreraIds(List.of(carreraId));


        // Ejecutar el método a probar
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class, () -> materiaService.crearMateria(materiaDto));

        // Aserts
        assertEquals("La carrera TUP tiene 2 años.", exception.getMessage());

    }

    @Test
    public void testCrearMateriaExistenciaCorrelativas() throws CarreraNotFoundException, MateriaNotFoundException {
        int carreraId = 1;
        int correlativaId = 2;
        Materia mCorrelativa1 = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan Pedro", "Fasola", "seee");

        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(1);
        mCorrelativa1.setCuatrimestre(1);
        mCorrelativa1.setProfesor(profesor);
        mCorrelativa1.setCarreraIds(List.of(carreraId));

        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(1)).thenReturn(mCorrelativa1);
        when(dao.findById(eq(correlativaId))).thenThrow(new MateriaNotFoundException("No se encontró la materia con id: "+correlativaId));
        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 2");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));
        materiaDto.setCorrelativasIds(Arrays.asList(1,2));


        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(1);
        mCorrelativa1.setCuatrimestre(1);


        // Ejecutar el método a probar
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> materiaService.crearMateria(materiaDto));

        // Aserts
        assertEquals("No se encontró la materia con id: " + correlativaId, exception.getMessage());
    }

    @Test
    public void testCrearMateriaCorrelativasConAnioMayorALaMateriaActual() throws CarreraNotFoundException, MateriaNotFoundException {
        int carreraId = 1;
        Materia mCorrelativa1 = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(2);
        mCorrelativa1.setCuatrimestre(1);
        mCorrelativa1.setProfesor(profesor);
        mCorrelativa1.setCarreraIds(List.of(carreraId));
        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(1)).thenReturn(mCorrelativa1);

        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 2");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));
        materiaDto.setCorrelativasIds(List.of(1));


        // Ejecutar el método a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.crearMateria(materiaDto));

        // Aserts
        assertEquals("La materia actual tiene un año menor al de las correlativas" , exception.getMessage());
    }

    @Test
    public void testCrearMateriaCorrelativasConCuatrimestreMayorALaMateriaActual() throws CarreraNotFoundException, MateriaNotFoundException {
        int carreraId = 1;
        Materia mCorrelativa1 = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(2);
        mCorrelativa1.setCuatrimestre(1);
        mCorrelativa1.setProfesor(profesor);
        mCorrelativa1.setCarreraIds(List.of(carreraId));
        // Configurar mocks
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(1)).thenReturn(mCorrelativa1);

        when(dao.save(any(Materia.class))).thenAnswer(invocation -> invocation.<Materia>getArgument(0));

        // Datos de prueba para la MateriaDto
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 2");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));
        materiaDto.setCorrelativasIds(List.of(1));


        // metodo a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.crearMateria(materiaDto));

        // Asserts
        assertEquals("La materia actual tiene un año menor al de las correlativas" , exception.getMessage());
    }

    @Test
    public void testEliminarMateria() throws MateriaNotFoundException, CarreraNotFoundException {
        Materia materia = new Materia();
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        materia.setMateriaId(1);
        materia.setNombre("programacion 1");
        materia.setAnio(2);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);

        //mock
        when(dao.findById(1)).thenReturn(materia);

        //Metodo a testear
        materiaService.eliminarMateria(materia.getMateriaId());

        //asserts
        assertEquals(0, dao.getAllMaterias().size());
    }

    @Test
    public void testEliminarMateriaInexistente() throws MateriaNotFoundException {
        int materiaId = 1;
        //mock
        when(dao.findById(materiaId)).thenThrow(new MateriaNotFoundException("No se encontró la materia con id: " + materiaId));

        //metodo a probar
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> materiaService.eliminarMateria(materiaId));

        //asserts
        assertEquals("No se encontró la materia con id: " + materiaId , exception.getMessage());
    }

    @Test
    public void testModificarMateria() throws MateriaNotFoundException, CarreraNotFoundException {
        int materiaId = 1;
        int carreraId = 1;
        Materia materia = new Materia();

        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 1");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));

        //mock
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);

        //datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 4");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(carreraId));


        //metodo a probar
        materiaService.modificarMateria(materiaId, materiaDto);

        //asserts
        assertEquals("Programacion 4", materia.getNombre());
        assertEquals(2, materia.getAnio());
        assertEquals(2, materia.getCuatrimestre());
        assertEquals(profesor, materia.getProfesor());
        assertTrue(materia.getCarreraIds().contains(carreraId));
    }

    @Test
    public void testModificarMateriaConCarreraASinCarrera() throws MateriaNotFoundException, CarreraNotFoundException {
        int materiaId = 1;
        int carreraId = 1;
        Materia materia = new Materia();

        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 1");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));

        //mock
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);

        //datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 4");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);


        //metodo a probar
        materiaService.modificarMateria(materiaId, materiaDto);

        //asserts
        assertEquals("Programacion 4", materia.getNombre());
        assertEquals(2, materia.getAnio());
        assertEquals(2, materia.getCuatrimestre());
        assertEquals(profesor, materia.getProfesor());
        assertTrue(materia.getCarreraIds().isEmpty());
    }

    @Test
    public void testModificarMateriaACarreraInexistente() throws MateriaNotFoundException, CarreraNotFoundException {
        int materiaId = 1;
        int carreraId = 1;
        Materia materia = new Materia();

        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 1");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));

        //mock
        when(carreraService.buscarCarrera(eq(carreraId))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(carreraService.buscarCarrera(eq(5))).thenThrow(new CarreraNotFoundException("No se encontró una carrera con el id: 5"));

        //datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 4");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCarreraIds(List.of(5));

        // Método a probar
        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> materiaService.modificarMateria(materiaId, materiaDto));

        // Asserts
        assertEquals("No se encontró una carrera con el id: 5" , exception.getMessage());

    }

    @Test
    public void testModificarMateriaConCorrelativas() throws CarreraNotFoundException, MateriaNotFoundException {
        int materiaId = 4;
        int carreraId = 1;
        Materia materia = new Materia();
        Materia mCorrelativa1 = new Materia();
        Materia mCorrelativa2 = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        mCorrelativa1.setMateriaId(1);
        mCorrelativa1.setNombre("programacion 1");
        mCorrelativa1.setAnio(1);
        mCorrelativa1.setCuatrimestre(1);
        mCorrelativa1.setProfesor(profesor);
        mCorrelativa1.setCarreraIds(List.of(carreraId));

        mCorrelativa2.setMateriaId(2);
        mCorrelativa2.setNombre("lab 1");
        mCorrelativa2.setAnio(1);
        mCorrelativa2.setCuatrimestre(1);
        mCorrelativa2.setProfesor(profesor);
        mCorrelativa2.setCarreraIds(List.of(carreraId));

        MateriaInfoDto mCorrelativaInfo1 = new MateriaInfoDto(mCorrelativa1.getMateriaId(), mCorrelativa1.getNombre(), mCorrelativa1.getAnio(), mCorrelativa1.getCuatrimestre());
        MateriaInfoDto mCorrelativaInfo2 = new MateriaInfoDto(mCorrelativa2.getMateriaId(), mCorrelativa2.getNombre(), mCorrelativa2.getAnio(), mCorrelativa2.getCuatrimestre());

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 1");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));
        materia.setCorrelatividades(new ArrayList<>(List.of(mCorrelativaInfo1, mCorrelativaInfo2)));


        //mock
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(dao.findById(1)).thenReturn(mCorrelativa1);
        when(dao.findById(2)).thenReturn(mCorrelativa2);

        //datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 4");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);
        materiaDto.setCorrelativasIds(List.of(1,2));

        //metodo a probar
        materiaService.modificarMateria(materiaId, materiaDto);

        //asserts
        assertEquals("Programacion 4", materia.getNombre());
        assertEquals(2, materia.getAnio());
        assertEquals(2, materia.getCuatrimestre());
        assertEquals(profesor, materia.getProfesor());
        assertEquals(2, materia.getCorrelatividades().size());
        assertTrue(materia.getCorrelatividades().contains(mCorrelativaInfo1));
        assertTrue(materia.getCorrelatividades().contains(mCorrelativaInfo2));
    }

    @Test
    public void testModificarMateriaCambioDeNombre() throws CarreraNotFoundException, MateriaNotFoundException {
        int materiaId = 4;
        int carreraId = 1;
        Materia materia = new Materia();
        Materia materiaExistente = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        materiaExistente.setMateriaId(1);
        materiaExistente.setNombre("Programacion 1");
        materiaExistente.setAnio(1);
        materiaExistente.setCuatrimestre(1);
        materiaExistente.setProfesor(profesor);
        materiaExistente.setCarreraIds(List.of(carreraId));

        Map<Integer, Materia> materiasMap = new HashMap<>();
        materiasMap.put(materiaExistente.getMateriaId(), materiaExistente);

        materia.setMateriaId(materiaId);
        materia.setNombre("Laboratorio 1");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));

        //mock
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(dao.getAllMaterias()).thenReturn(materiasMap);

        //datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);

        //metodo a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.modificarMateria(materiaId, materiaDto));

        //asserts
        assertEquals("Ya existe una materia con el nombre Programacion 1", exception.getMessage());
    }

    @Test
    public void testModificarMateriaCambioAnioEnCorrelativa() throws CarreraNotFoundException, MateriaNotFoundException {
        int materiaId = 4;
        int correlativaId = 1;
        int carreraId = 1;

        Materia materia = new Materia();
        Materia correlativa = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        correlativa.setMateriaId(correlativaId);
        correlativa.setNombre("programacion 1");
        correlativa.setAnio(1);
        correlativa.setCuatrimestre(1);
        correlativa.setProfesor(profesor);
        correlativa.setCarreraIds(List.of(carreraId));

        MateriaInfoDto correlativaInfoDto = new MateriaInfoDto(correlativa.getMateriaId(), correlativa.getNombre(), correlativa.getAnio(), correlativa.getCuatrimestre());

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 2");
        materia.setAnio(1);
        materia.setCuatrimestre(2);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));
        materia.setCorrelatividades(new ArrayList<>(List.of(correlativaInfoDto)));

        Map<Integer, Materia> listaDeMaterias = new HashMap<>();
        listaDeMaterias.put(materiaId, materia);

        // Mocks
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(dao.findById(correlativaId)).thenReturn(correlativa);
        when(dao.getAllMaterias()).thenReturn(listaDeMaterias);

        // Datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(2);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(12);

        // Método a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.modificarMateria(correlativaId, materiaDto));

        // Asserts
        assertEquals("Esta materia es correlativa de otra la cual se cursa en un anio anterior", exception.getMessage());
    }

    @Test
    public void testModificarMateriaCambioCuatrimestreIgualEnCorrelativa() throws CarreraNotFoundException, MateriaNotFoundException {
        int materiaId = 4;
        int correlativaId = 1;
        int carreraId = 1;

        Materia materia = new Materia();
        Materia correlativa = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        correlativa.setMateriaId(correlativaId);
        correlativa.setNombre("programacion 1");
        correlativa.setAnio(1);
        correlativa.setCuatrimestre(1);
        correlativa.setProfesor(profesor);
        correlativa.setCarreraIds(List.of(carreraId));

        MateriaInfoDto correlativaInfoDto = new MateriaInfoDto(correlativa.getMateriaId(), correlativa.getNombre(), correlativa.getAnio(), correlativa.getCuatrimestre());

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 2");
        materia.setAnio(1);
        materia.setCuatrimestre(2);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));
        materia.setCorrelatividades(new ArrayList<>(List.of(correlativaInfoDto)));

        Map<Integer, Materia> listaDeMaterias = new HashMap<>();
        listaDeMaterias.put(materiaId, materia);

        // Mocks
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(dao.findById(correlativaId)).thenReturn(correlativa);
        when(dao.getAllMaterias()).thenReturn(listaDeMaterias);

        // Datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setProfesorId(12);

        // Método a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.modificarMateria(correlativaId, materiaDto));

        // Asserts
        assertEquals("Esta materia es correlativa de otra la cual se cursa en un cuatrimestre igual o anterior", exception.getMessage());
    }

    @Test
    public void testModificarMateriaCambioCuatrimestreMayorEnCorrelativa() throws CarreraNotFoundException, MateriaNotFoundException {
        int materiaId = 4;
        int correlativaId = 1;
        int carreraId = 1;

        Materia materia = new Materia();
        Materia correlativa = new Materia();
        Carrera carrera = new Carrera("TUP", carreraId, 2, 4);
        Profesor profesor = new Profesor("Juan", "Perez", "ing. Informatica");

        correlativa.setMateriaId(correlativaId);
        correlativa.setNombre("programacion 1");
        correlativa.setAnio(1);
        correlativa.setCuatrimestre(1);
        correlativa.setProfesor(profesor);
        correlativa.setCarreraIds(List.of(carreraId));

        MateriaInfoDto correlativaInfoDto = new MateriaInfoDto(correlativa.getMateriaId(), correlativa.getNombre(), correlativa.getAnio(), correlativa.getCuatrimestre());

        materia.setMateriaId(materiaId);
        materia.setNombre("programacion 2");
        materia.setAnio(1);
        materia.setCuatrimestre(2);
        materia.setProfesor(profesor);
        materia.setCarreraIds(List.of(carreraId));
        materia.setCorrelatividades(new ArrayList<>(List.of(correlativaInfoDto)));

        Map<Integer, Materia> listaDeMaterias = new HashMap<>();
        listaDeMaterias.put(materiaId, materia);

        // Mocks
        when(carreraService.buscarCarrera(eq(1))).thenReturn(carrera);
        when(profesorService.buscarProfesor(anyInt())).thenReturn(profesor);
        when(dao.findById(materiaId)).thenReturn(materia);
        when(dao.findById(correlativaId)).thenReturn(correlativa);
        when(dao.getAllMaterias()).thenReturn(listaDeMaterias);

        // Datos de prueba
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Programacion 1");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(4);
        materiaDto.setProfesorId(12);

        // Método a probar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> materiaService.modificarMateria(correlativaId, materiaDto));

        // Asserts
        assertEquals("Esta materia es correlativa de otra la cual se cursa en un cuatrimestre igual o anterior", exception.getMessage());
    }

    @Test
    public void testGetMateriaByName() throws MateriaNotFoundException {
        int materiaId = 1;
        String nombre = "Programacion 1";
        Materia materia = new Materia();

        materia.setMateriaId(materiaId);
        materia.setNombre(nombre);

        Map<Integer, Materia> materiaMap = new HashMap<>();
        materiaMap.put(materiaId, materia);
        //Mock
        when(dao.getAllMaterias()).thenReturn(materiaMap);

        //Metodo a probar
        List<Materia> lista = new ArrayList<>(materiaService.getMateriaByName(nombre));


        //Asserts
        assertTrue(lista.contains(materia));
    }

    @Test
    public void testGetMateriaByNameNombreParcial() throws MateriaNotFoundException {
        Materia materia1 = new Materia();
        Materia materia2 = new Materia();
        Materia materia3 = new Materia();

        materia1.setMateriaId(1);
        materia1.setNombre("Programacion 1");

        materia2.setMateriaId(2);
        materia2.setNombre("programacion 2");

        materia3.setMateriaId(3);
        materia3.setNombre("matematica");

        Map<Integer, Materia> materiaMap = new HashMap<>();
        materiaMap.put(materia1.getMateriaId(), materia1);
        materiaMap.put(materia2.getMateriaId(), materia2);
        materiaMap.put(materia3.getMateriaId(), materia3);
        //Mock
        when(dao.getAllMaterias()).thenReturn(materiaMap);

        //Metodo a probar
        List<Materia> lista = new ArrayList<>(materiaService.getMateriaByName("progr"));


        //Asserts
        assertTrue(lista.contains(materia1));
        assertTrue(lista.contains(materia2));
        assertFalse(lista.contains(materia3));
    }

    @Test
    public void testGetMateriaByNameError() {
        int materiaId = 1;
        String nombre = "Programacion 1";
        Materia materia = new Materia();

        materia.setMateriaId(materiaId);
        materia.setNombre(nombre);

        Map<Integer, Materia> materiaMap = new HashMap<>();
        materiaMap.put(materiaId, materia);
        //Mock
        when(dao.getAllMaterias()).thenReturn(materiaMap);

        //Metodo a probar
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> materiaService.getMateriaByName("programacion1"));

        //Asserts
        assertEquals("La materia programacion1 no existe", exception.getMessage());
    }

    @Test
    public void testGetMateriaById() throws MateriaNotFoundException {
        int materiaId = 1;
        Materia materia = new Materia();

        materia.setMateriaId(materiaId);
        materia.setAnio(2);
        materia.setNombre("Laboratorio 2");

        //Mock
        when(dao.findById(materiaId)).thenReturn(materia);

        //Metodo a probar
        Materia resultado = materiaService.getMateriaById(materiaId);

        //Asserts
        assertEquals(materia, resultado);
    }

    @Test
    public void testGetMateriaByIdError() throws MateriaNotFoundException {
        int materiaId = 1;
        int materiaIdError = 3;
        Materia materia = new Materia();

        materia.setMateriaId(materiaId);
        materia.setAnio(2);
        materia.setNombre("Laboratorio 2");

        //Mock
        when(dao.findById(materiaIdError)).thenThrow(new MateriaNotFoundException("No se encontró la materia con id: " + materiaIdError));

        //Metodo a probar
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> materiaService.getMateriaById(materiaIdError));
        //Asserts
        assertEquals("No se encontró la materia con id: " + materiaIdError, exception.getMessage());
    }
}

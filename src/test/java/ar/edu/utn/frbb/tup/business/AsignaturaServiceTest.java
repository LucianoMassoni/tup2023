package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.AsignaturaServiceImpl;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
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
public class AsignaturaServiceTest {
    @InjectMocks
    AsignaturaServiceImpl asignaturaService;
    @Mock
    AsignaturaDao asignaturaDao;
    @Mock
    MateriaService materiaService;

    @Test
    public void crearAsignaturaTest() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setMateriaId(1);

        when(materiaService.getMateriaById(1)).thenReturn(materia);

        Asignatura asignatura = asignaturaService.crearAsignatura(materia.getMateriaId());

        assertEquals(asignatura.getEstado(), EstadoAsignatura.NO_CURSADA);
        assertEquals(asignatura.getMateria(), materia);
        assertEquals(asignatura.getNota(), Optional.empty());
    }

    @Test
    public void crearAsignaturaMateriaErroneaTest() throws MateriaNotFoundException {

        when(materiaService.getMateriaById(anyInt())).thenThrow(new MateriaNotFoundException("No se encontro la materia"));

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () ->  asignaturaService.crearAsignatura(1));

        assertEquals("No se encontro la materia", exception.getMessage());
    }

    @Test
    public void eliminarAsignaturaTest() throws AsignaturaNotFoundException {
        int asignaturaId = 1;

        asignaturaService.eliminarAsignatura(1);

        verify(asignaturaDao).delete(asignaturaId);
    }

    @Test
    public void eliminarAsignaturaInexistenteTest() throws AsignaturaNotFoundException {
        int asignaturaId = 1;

        doThrow(AsignaturaNotFoundException.class).when(asignaturaDao).delete(asignaturaId);

        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.eliminarAsignatura(asignaturaId));
    }

    @Test
    public void getAsignaturaTest() throws AsignaturaNotFoundException {
        int id = 1;
        Asignatura asignatura = new Asignatura();
        asignatura.setId(id);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.getAsignatura(id);

        assertEquals(resultado, asignatura);
    }

    @Test
    public void getAsignaturaInexistenteTest() throws AsignaturaNotFoundException {
        int id  = 1;

        when(asignaturaDao.findById(id)).thenThrow(AsignaturaNotFoundException.class);

        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.getAsignatura(id));
    }

    @Test
    public void verificarCorrelativasEstenAprobadasTest() throws AsignaturaNotFoundException, EstadoIncorrectoException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.APROBADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.APROBADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);


        asignaturaService.verificarCorrelativasEstenAprobadas(1, listaIdsAsignaturasDelAlumno);

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
    }

    @Test
    public void verificarCorrelativasEstenAprobadasCorrelativaCursadaTest() throws AsignaturaNotFoundException, EstadoIncorrectoException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.APROBADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);


        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarCorrelativasEstenAprobadas(1, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
        assertEquals("La asignatura " + asignatura3.getNombreAsignatura() +
                " con id: " + asignatura3.getId() + " No está aprobada", exception.getMessage());
    }

    @Test
    public void verificarCorrelativasEstenAprobadasCorrelativaNoCursadaTest() throws AsignaturaNotFoundException, EstadoIncorrectoException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.APROBADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);


        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarCorrelativasEstenAprobadas(1, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
        assertEquals("La asignatura " + asignatura3.getNombreAsignatura() +
                " con id: " + asignatura3.getId() + " No está aprobada", exception.getMessage());
    }

    @Test
    public void verificarCorrelativasEstenCursadasTest() throws AsignaturaNotFoundException, EstadoIncorrectoException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.APROBADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);


        asignaturaService.verificarCorrelativasEstenCursadas(1, listaIdsAsignaturasDelAlumno);

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
    }

    @Test
    public void verificarCorrelativasEstenCursadasCorrelativaNoCursadaTest() throws AsignaturaNotFoundException, EstadoIncorrectoException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2);
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3);

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);


        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarCorrelativasEstenCursadas(1, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
        assertEquals("La asignatura " + asignatura3.getNombreAsignatura() +
                " con id: " + asignatura3.getId() + " No está cursada", exception.getMessage());
    }

    @Test
    public void verificarAsignaturasParaPerderCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);

        asignaturaService.verificarAsignaturasParaPerderCursada(2, listaIdsAsignaturasDelAlumno);

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());
    }

    @Test
    public void verificarAsignaturasParaPerderCursadaAsignaturaAprobadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarAsignaturasParaPerderCursada(1, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());

        assertEquals("No se puede perder la cursada si la asignatura está aprobada", exception.getMessage());
    }

    @Test
    public void verificarAsignaturasParaPerderCursadaAsignaturaCorrelativaCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarAsignaturasParaPerderCursada(2, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());

        assertEquals("No se puede perder la cursada si la que tiene como correlativa a esta está cursada o aprobada", exception.getMessage());
    }

    @Test
    public void verificarAsignaturasParaPerderCursadaAsignaturaCorrelativaAprobadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        // Configuración de las correlativas de la asignatura
        MateriaInfoDto materiaCorrelativa1 = new MateriaInfoDto();
        materiaCorrelativa1.setId(2); // Asignatura correlativa con ID 2
        MateriaInfoDto materiaCorrelativa2 = new MateriaInfoDto();
        materiaCorrelativa2.setId(3); // Asignatura correlativa con ID 3

        // Configuración de asignaturas
        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setCorrelatividades(List.of(materiaCorrelativa1,materiaCorrelativa2));
        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(1);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        asignatura2.setEstado(EstadoAsignatura.CURSADA);

        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        asignatura3.setEstado(EstadoAsignatura.NO_CURSADA);

        List<Integer> listaIdsAsignaturasDelAlumno = Arrays.asList(1, 2, 3);

        // Configuración del mock para las dos llamadas a findById
        when(asignaturaDao.findById(1)).thenReturn(asignatura);
        when(asignaturaDao.findById(2)).thenReturn(asignatura2);
        when(asignaturaDao.findById(3)).thenReturn(asignatura3);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.verificarAsignaturasParaPerderCursada(2, listaIdsAsignaturasDelAlumno));

        // Verificación de llamadas al DAO
        verify(asignaturaDao, times(3)).findById(anyInt());

        assertEquals("No se puede perder la cursada si la que tiene como correlativa a esta está cursada o aprobada", exception.getMessage());
    }

    @Test
    public void verificarNotaCorrectaTest(){
        int nota = 8;

        asignaturaService.verificarNotaCorrecta(nota);
    }

    @Test
    public void verificarNotaCorrectaNumeroNegativoTest(){
        int nota = -8;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> asignaturaService.verificarNotaCorrecta(nota));

        assertEquals("La nota no puede ser un numero negativo", exception.getMessage());
    }

    @Test
    public void verificarNotaCorrectaNotaInferiorASeisTest(){
        int nota = 5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> asignaturaService.verificarNotaCorrecta(nota));

        assertEquals("La nota debe ser mayor o igual a 6 para aprobar", exception.getMessage());
    }

    @Test
    public void verificarNotaCorrectaNumeroMayorADiezTest(){
        int nota = 11;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> asignaturaService.verificarNotaCorrecta(nota));

        assertEquals("La nota debe ser menor o igual a 10 para aprobar", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaAprobadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Asignatura asignatura = new Asignatura();
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        asignaturaService.verificarAsignaturaEstaAprobada(id);
    }

    @Test
    public void verificarAsignaturaEstaAprobadaAsignaturaNoCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaAprobada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está APROBADA", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaAprobadaAsignaturaCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaAprobada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está APROBADA", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Asignatura asignatura = new Asignatura();
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        asignaturaService.verificarAsignaturaEstaCursada(id);
    }

    @Test
    public void verificarAsignaturaEstaCursadaAsignaturaNoCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaCursada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está CURSADA", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaCursadaAsignaturaAprobadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaCursada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está CURSADA", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaNoCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Asignatura asignatura = new Asignatura();
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        asignaturaService.verificarAsignaturaEstaNoCursada(id);
    }

    @Test
    public void verificarAsignaturaEstaNoCursadaAsignaturaCursadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaNoCursada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está NO_CURSADA", exception.getMessage());
    }

    @Test
    public void verificarAsignaturaEstaNoCursadaAsignaturaAprobadaTest() throws EstadoIncorrectoException, AsignaturaNotFoundException {
        int id = 1;

        Materia materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("materia");

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(id);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        when(asignaturaDao.findById(id)).thenReturn(asignatura);

        EstadoIncorrectoException exception = assertThrows(EstadoIncorrectoException.class, () -> asignaturaService.verificarAsignaturaEstaNoCursada(id));

        assertEquals("La asignatura " + asignatura.getNombreAsignatura() + " No está NO_CURSADA", exception.getMessage());
    }
}

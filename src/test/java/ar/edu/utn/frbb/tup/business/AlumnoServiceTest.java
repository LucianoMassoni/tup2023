package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoAsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
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
public class AlumnoServiceTest {
    @InjectMocks
    private AlumnoServiceImpl alumnoService;
    @Mock
    private AsignaturaService asignaturaService;
    @Mock
    private MateriaService materiaService;
    @Mock
    private AlumnoDao alumnoDao;

    @Test
    public void crearAlumnoTest() throws MateriaNotFoundException {

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Nombre");
        alumnoDto.setApellido("Apellido");
        alumnoDto.setDni(12345678);
        alumnoDto.setIdMateriaDeAsignatura(Arrays.asList(1, 2, 3)); // Ids de asignaturas

        when(asignaturaService.crearAsignatura(anyInt())).thenReturn(new Asignatura());
        when(alumnoDao.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de alumno

        // Ejecutar la función a testear
        Alumno alumno = alumnoService.crearAlumno(alumnoDto);

        // Verificar que se llamaron a los métodos necesarios
        verify(asignaturaService, times(3)).crearAsignatura(anyInt()); // Se llama a crearAsignatura 3 veces con los IDs de materias proporcionados
        verify(alumnoDao, times(1)).save(any(Alumno.class)); // Se llama a save una vez para guardar el alumno

        // Verificar que el objeto Alumno devuelto contiene los datos esperados
        assertEquals("Nombre", alumno.getNombre());
        assertEquals("Apellido", alumno.getApellido());
        assertEquals(12345678, alumno.getDni());
        assertEquals(3, alumno.getAsignaturasIds().size()); // Se esperan 3 asignaturas
    }

    @Test
    public void crearAlumnoDniRepetidoTest() throws MateriaNotFoundException {
        int dni = 12345678;
        Alumno alumnoCreado = new Alumno();
        alumnoCreado.setDni(dni);
        Map<Integer, Alumno> alumnoMap = new HashMap<>();
        alumnoMap.put(1, alumnoCreado);

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Nombre");
        alumnoDto.setApellido("Apellido");
        alumnoDto.setDni(dni);
        alumnoDto.setIdMateriaDeAsignatura(Arrays.asList(1, 2, 3)); // Ids de asignaturas

        when(asignaturaService.crearAsignatura(anyInt())).thenReturn(new Asignatura());
        when(alumnoDao.getAllAlunno()).thenReturn(alumnoMap);
        when(alumnoDao.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de alumno

        // Ejecutar la función a testear
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alumnoService.crearAlumno(alumnoDto));

        assertEquals("Ya existe un alumno con DNI: " + dni, exception.getMessage()); // Se esperan 3 asignaturas
    }

    @Test
    public void crearAlumnoDniMasDe8DigitosTest() throws MateriaNotFoundException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Nombre");
        alumnoDto.setApellido("Apellido");
        alumnoDto.setDni(123454678);
        alumnoDto.setIdMateriaDeAsignatura(Arrays.asList(1, 2, 3)); // Ids de asignaturas

        when(asignaturaService.crearAsignatura(anyInt())).thenReturn(new Asignatura());
        when(alumnoDao.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de alumno

        // Ejecutar la función a testear
        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () -> alumnoService.crearAlumno(alumnoDto));

        assertEquals("El dni debe tener 8 digitos", exception.getMessage()); // Se esperan 3 asignaturas
    }

    @Test
    public void crearAlumnoDniNegativoTest() throws MateriaNotFoundException {

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Nombre");
        alumnoDto.setApellido("Apellido");
        alumnoDto.setDni(-12345678);
        alumnoDto.setIdMateriaDeAsignatura(Arrays.asList(1, 2, 3)); // Ids de asignaturas

        when(asignaturaService.crearAsignatura(anyInt())).thenReturn(new Asignatura());
        when(alumnoDao.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de alumno

        // Ejecutar la función a testear
        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, () -> alumnoService.crearAlumno(alumnoDto));

        assertEquals("El dni no puede ser negativo", exception.getMessage()); // Se esperan 3 asignaturas
    }

    @Test
    public void crearAlumnoDniMateriasIncorrectasTest() throws MateriaNotFoundException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Nombre");
        alumnoDto.setApellido("Apellido");
        alumnoDto.setDni(12345678);
        alumnoDto.setIdMateriaDeAsignatura(Arrays.asList(1, 2, 3)); // Ids de asignaturas

        when(asignaturaService.crearAsignatura(anyInt())).thenReturn(new Asignatura());
        when(materiaService.getMateriaById(anyInt())).thenThrow(new MateriaNotFoundException("La materia no existe"));
        when(alumnoDao.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simular guardado de alumno

        // Ejecutar la función a testear
        MateriaNotFoundException exception =  assertThrows(MateriaNotFoundException.class, () -> alumnoService.crearAlumno(alumnoDto));

        assertEquals("La materia no existe", exception.getMessage()); // Se esperan 3 asignaturas
    }

    @Test
    public void buscarAlumnoTest() throws AlumnoNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");

        when(alumnoDao.findById(id)).thenReturn(alumno);

        Alumno resultado = alumnoService.buscarAlumno(id);

        assertEquals(alumno, resultado);
    }

    @Test
    public void buscarAlumnoInexistenteTest() throws AlumnoNotFoundException {
        int id = 1;

        when(alumnoDao.findById(id)).thenThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id));

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> alumnoService.buscarAlumno(id));

        assertEquals("No se encontró un alumno con el id: " + id, exception.getMessage());

    }

    @Test
    public void eliminarAlumnoTest() throws AlumnoNotFoundException {
        // Configurar el mock para que no lance excepción al llamar a delete
        doNothing().when(alumnoDao).delete(anyInt());

        // Ejecutar el método a probar
        alumnoService.eliminarAlumno(1);

        // Verificar que se llamó al método delete del DAO con el id correcto
        verify(alumnoDao).delete(1);
    }

    @Test
    public void eliminarAlumnoInexistenteTest() throws AlumnoNotFoundException {
        int id = 1;

        doThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id)).when(alumnoDao).delete(anyInt());

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> alumnoService.eliminarAlumno(id));

        assertNotNull(exception);
        assertEquals("No se encontró un alumno con el id: " + id, exception.getMessage());
    }

    @Test
    public void actualizarAlumnoTest() throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(87654321);
        alumnoDto.setNombre("nombreDto");
        alumnoDto.setApellido("ApellidoDto");

        when(alumnoDao.findById(id)).thenReturn(alumno);
        doNothing().when(alumnoDao).actualizar(alumno);

        alumnoService.actualizarAlumno(id, alumnoDto);

        verify(alumnoDao).actualizar(alumno);
    }

    @Test
    public void actualizarAlumnoConAsignaturasTest() throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        // Configurar instancias de Materia
        Materia materia1 = new Materia();
        materia1.setMateriaId(1);
        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        Materia materia3 = new Materia();
        materia3.setMateriaId(3);
        Materia materia4 = new Materia();
        materia4.setMateriaId(4);

        // Configurar instancias de Asignatura
        Asignatura asignatura1 = new Asignatura(materia1);
        asignatura1.setId(1);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);
        Asignatura asignatura3 = new Asignatura(materia3);
        asignatura3.setId(3);
        Asignatura asignatura4 = new Asignatura(materia4);
        asignatura4.setId(4);

        // instancias de Alumno
        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");
        alumno.setAsignaturasIds(List.of(1,2,3));

        // Datos del Dto
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(87654321);
        alumnoDto.setNombre("nombreDto");
        alumnoDto.setApellido("ApellidoDto");
        alumnoDto.setIdMateriaDeAsignatura(List.of(1,3,4));

        when(asignaturaService.getAsignatura(1)).thenReturn(asignatura1);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);
        when(asignaturaService.getAsignatura(3)).thenReturn(asignatura3);
        when(asignaturaService.crearAsignatura(4)).thenReturn(asignatura4);
        when(materiaService.getMateriaById(1)).thenReturn(materia1);
        when(materiaService.getMateriaById(2)).thenReturn(materia2);
        when(materiaService.getMateriaById(3)).thenReturn(materia3);
        when(materiaService.getMateriaById(4)).thenReturn(materia4);
        when(alumnoDao.findById(id)).thenReturn(alumno);
        doNothing().when(alumnoDao).actualizar(alumno);

        alumnoService.actualizarAlumno(id, alumnoDto);

        verify(alumnoDao).actualizar(alumno);
        verify(asignaturaService, times(3)).getAsignatura(anyInt());
        verify(asignaturaService, times(1)).crearAsignatura(anyInt());
    }

    @Test
    public void actualizarAlumnoDniIncorrectoTest() throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(876542321);
        alumnoDto.setNombre("nombreDto");
        alumnoDto.setApellido("ApellidoDto");

        when(alumnoDao.findById(id)).thenReturn(alumno);
        doNothing().when(alumnoDao).actualizar(alumno);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alumnoService.actualizarAlumno(id, alumnoDto));

        assertEquals("El dni debe tener 8 digitos", exception.getMessage());
    }

    @Test
    public void actualizarAlumnoDniDuplicadoTest() throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        int id = 1;
        int dni = 87654321;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");

        Alumno alumno2 = new Alumno();
        alumno.setId(2);
        alumno.setDni(dni);

        Map<Integer, Alumno> alumnoMap = new HashMap<>();
        alumnoMap.put(id, alumno);
        alumnoMap.put(alumno2.getId(), alumno2);

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(dni);
        alumnoDto.setNombre("nombreDto");
        alumnoDto.setApellido("ApellidoDto");

        when(alumnoDao.getAllAlunno()).thenReturn(alumnoMap);
        when(alumnoDao.findById(id)).thenReturn(alumno);
        doNothing().when(alumnoDao).actualizar(alumno);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alumnoService.actualizarAlumno(id, alumnoDto));

        assertEquals("Ya existe un alumno con DNI: " + dni, exception.getMessage());
    }

    @Test
    public void actualizarAlumnoMateriaInexistenteTest() throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setDni(12345678);
        alumno.setNombre("nombre");
        alumno.setApellido("apellido");

        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(87654321);
        alumnoDto.setNombre("nombreDto");
        alumnoDto.setApellido("ApellidoDto");
        alumnoDto.setIdMateriaDeAsignatura(List.of(1));

        when(materiaService.getMateriaById(anyInt())).thenThrow(new MateriaNotFoundException("no se encontro materia con id: 1"));
        when(alumnoDao.findById(id)).thenReturn(alumno);
        doNothing().when(alumnoDao).actualizar(alumno);

        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> alumnoService.actualizarAlumno(id, alumnoDto));

        assertEquals("no se encontro materia con id: 1", exception.getMessage());
    }

    @Test
    public void getAllAsignaturasDeAlumnoTest() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        Asignatura asignatura1 = new Asignatura();
        asignatura1.setId(1);
        Asignatura asignatura2 = new Asignatura();
        asignatura1.setId(2);

        when(alumnoDao.findById(id)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenReturn(asignatura1);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);


        AlumnoAsignaturaDto resultado = alumnoService.getAllAsignaturasDeAlumno(id);

        assertEquals(resultado.getId(), alumno.getId());
        assertEquals(resultado.getNombre(), alumno.getNombre());
        assertEquals(resultado.getApellido(), alumno.getApellido());
        assertEquals(resultado.getDni(), alumno.getDni());
        assertTrue(resultado.getAsignaturas().contains(asignatura1));
        assertTrue(resultado.getAsignaturas().contains(asignatura2));
    }

    @Test
    public void getAllAsignaturasDeAlumnoInexistenteTest() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        when(alumnoDao.findById(id)).thenThrow(AlumnoNotFoundException.class);

        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.getAllAsignaturasDeAlumno(id));
    }

    @Test
    public void getAllAsignaturasDeAlumnoAsignaturaInexistenteTest() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        int id = 1;

        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        when(alumnoDao.findById(id)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenThrow(AsignaturaNotFoundException.class);

        assertThrows(AsignaturaNotFoundException.class, () -> alumnoService.getAllAsignaturasDeAlumno(id));

        verify(alumnoDao, times(1)).findById(id);
    }

    @Test
    public void cambiarEstadoAsignaturaAAprobadaTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;
        int idAsignatura = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        Materia materia1 = new Materia();
        materia1.setMateriaId(1);
        Materia materia2 = new Materia();
        materia2.setMateriaId(2);

        Asignatura asignatura1 = new Asignatura(materia1);
        asignatura1.setId(1);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(8);
        asignaturaDto.setMateriaId(materia1.getMateriaId());

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenReturn(asignatura1);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);

        alumnoService.cambiarEstadoAsignatura(idAlumno, idAsignatura, asignaturaDto);

        assertEquals(asignatura1.getEstado(), EstadoAsignatura.APROBADA);
        assertEquals(asignatura2.getEstado(), EstadoAsignatura.NO_CURSADA);
        assertEquals(asignatura1.getNota(), Optional.of(8));
        verify(asignaturaService, times(1)).actualizarAsignatura(asignatura1);
    }

    @Test
    public void cambiarEstadoAsignaturaAAprobadaAlumnoInexistenteTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;

        when(alumnoDao.findById(idAlumno)).thenThrow(AlumnoNotFoundException.class);

        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.cambiarEstadoAsignatura(idAlumno, 4, new AsignaturaDto()));
    }

    @Test
    public void cambiarEstadoAsignaturaAAprobadaAsignaturaInexistenteTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setAsignaturasIds(List.of(1));

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenThrow(AsignaturaNotFoundException.class);

        assertThrows(AsignaturaNotFoundException.class, () -> alumnoService.cambiarEstadoAsignatura(idAlumno, 4, new AsignaturaDto()));

        verify(alumnoDao, times(1)).findById(idAlumno);
    }

    @Test
    public void cambiarEstadoAsignaturaAAprobadaAsignaturaNoPerteneceAlAlumnoTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setAsignaturasIds(List.of(1));

        Asignatura asignatura = new Asignatura();
        asignatura.setId(4);

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(4)).thenReturn(asignatura);

        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () ->
                alumnoService.cambiarEstadoAsignatura(idAlumno, asignatura.getId(), new AsignaturaDto()));

        assertEquals("El alumno no tiene una asignatura con id: " + asignatura.getId(), exception.getMessage());

        verify(alumnoDao, times(1)).findById(idAlumno);
        verify(asignaturaService, times(0)).actualizarAsignatura(any(Asignatura.class));
    }

    @Test
    public void cambiarEstadoAsignaturaAprobadaCorrelativaDesaprobadaTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        Materia materia1 = new Materia();
        materia1.setMateriaId(1);

        MateriaInfoDto materiaInfoDto = new MateriaInfoDto();
        materiaInfoDto.setId(1);

        Materia materia2 = new Materia();
        materia2.setMateriaId(2);
        materia2.setCorrelatividades(List.of(materiaInfoDto));

        Asignatura asignatura1 = new Asignatura(materia1);
        asignatura1.setId(1);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(8);
        asignaturaDto.setMateriaId(materia2.getMateriaId());

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenReturn(asignatura1);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);
        doThrow(EstadoIncorrectoException.class).when(asignaturaService).verificarCorrelativasEstenAprobadas(2, alumno.getAsignaturasIds());

        assertThrows(EstadoIncorrectoException.class, () -> alumnoService.cambiarEstadoAsignatura(idAlumno, 2, asignaturaDto));

        verify(alumnoDao, times(1)).findById(1);
        verify(asignaturaService, times(1)).getAsignatura(2);
        verify(asignaturaService, times(0)).actualizarAsignatura(asignatura2);
    }

    @Test
    public void cambiarEstadoAsignaturaAprobadaNotaErroneaTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setNombre("Nombre");
        alumno.setApellido("Apellido");
        alumno.setDni(12345678);
        alumno.setAsignaturasIds(List.of(1,2));

        Materia materia1 = new Materia();
        materia1.setMateriaId(1);
        Materia materia2 = new Materia();
        materia2.setMateriaId(2);

        Asignatura asignatura1 = new Asignatura(materia1);
        asignatura1.setId(1);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura2.setId(2);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(5);
        asignaturaDto.setMateriaId(materia2.getMateriaId());

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(1)).thenReturn(asignatura1);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);
        doThrow(IllegalArgumentException.class).when(asignaturaService).verificarNotaCorrecta(asignaturaDto.getNota());

        assertThrows(IllegalArgumentException.class, () -> alumnoService.cambiarEstadoAsignatura(idAlumno, 2, asignaturaDto));

        verify(alumnoDao, times(1)).findById(1);
        verify(asignaturaService, times(1)).getAsignatura(2);
        verify(asignaturaService, times(0)).actualizarAsignatura(asignatura2);
    }

    /*
        Tanto el estado de CURSADO y NO_CURSADO testeo que cambie el estado y que se guarde ya que usan las mismas funciones que para APROBADO
     */

    @Test
    public void cambiarEstadoAsignaturaACursadoTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;
        int idAsignatura = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setDni(12345678);
        alumno.setNombre("Nombre");
        alumno.setApellido("apellido");
        alumno.setAsignaturasIds(List.of(1,2));

        Materia materia = new Materia();
        materia.setMateriaId(1);
        Materia materia2 = new Materia();
        materia.setMateriaId(2);

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(idAsignatura);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura.setId(2);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(1);
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.CURSADA);

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(idAsignatura)).thenReturn(asignatura);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);

        alumnoService.cambiarEstadoAsignatura(idAlumno, idAsignatura, asignaturaDto);

        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);

        verify(asignaturaService, times(1)).verificarCorrelativasEstenCursadas(idAsignatura, alumno.getAsignaturasIds());
        verify(asignaturaService, times(1)).actualizarAsignatura(asignatura);
    }

    @Test
    public void cambiarEstadoAsignaturaPerderCursadaTest() throws AlumnoNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        int idAlumno = 1;
        int idAsignatura = 1;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);
        alumno.setDni(12345678);
        alumno.setNombre("Nombre");
        alumno.setApellido("apellido");
        alumno.setAsignaturasIds(List.of(1,2));

        Materia materia = new Materia();
        materia.setMateriaId(1);
        Materia materia2 = new Materia();
        materia.setMateriaId(2);

        Asignatura asignatura = new Asignatura(materia);
        asignatura.setId(idAsignatura);
        Asignatura asignatura2 = new Asignatura(materia2);
        asignatura.setId(2);

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(1);
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.NO_CURSADA);

        when(alumnoDao.findById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignatura(idAsignatura)).thenReturn(asignatura);
        when(asignaturaService.getAsignatura(2)).thenReturn(asignatura2);

        alumnoService.cambiarEstadoAsignatura(idAlumno, idAsignatura, asignaturaDto);

        assertEquals(asignatura.getEstado(), EstadoAsignatura.NO_CURSADA);

        verify(asignaturaService, times(1)).verificarAsignaturasParaPerderCursada(idAsignatura, alumno.getAsignaturasIds());
        verify(asignaturaService, times(1)).actualizarAsignatura(asignatura);
    }
}


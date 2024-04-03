package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoAsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class AlumnoControllerTest {
    @InjectMocks
    AlumnoController alumnoController;

    @Mock
    AlumnoService alumnoService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(alumnoController).setControllerAdvice(new UtnResponseEntityExceptionHandler()).build();
    }

    @Test
    public void crearAlumnoTest() throws Exception {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("nombre");
        alumnoDto.setApellido("apellido");
        alumnoDto.setDni(12345678);
        alumnoDto.setIdMateriaDeAsignatura(List.of(1,2));

        when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(new Alumno());

        MvcResult result = mockMvc.perform(post("/alumno")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(alumnoDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Assertions.assertEquals(new Alumno(), mapper.readValue(result.getResponse().getContentAsString(), Alumno.class));
    }

    @Test
    public void creaAlumnoBadRequestTest() throws Exception {

        when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(new Alumno());
        MvcResult result = mockMvc.perform(post("/materia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nombre\" : \"nombre\",\n" +
                                "    \"apellido\" : \"apellido\", \n" +
                                "    \"dni\" : 12345678,\n" +
                                "    \"asignaturasIds\" : [1, 2] \n"+
                                "}")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void modificarAlumnoTest() throws Exception {
        int id = 1;
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("nombre");
        alumnoDto.setApellido("apellido");
        alumnoDto.setDni(12345678);
        alumnoDto.setIdMateriaDeAsignatura(List.of(1,2));

        doNothing().when(alumnoService).actualizarAlumno(id, alumnoDto);

        mockMvc.perform(put("/alumno/{idAlumno}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    public void modificarAlumnoBadRequestTest() throws Exception {
        int id = 1;


       doThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id)).when(alumnoService).actualizarAlumno(id, new AlumnoDto());

       mockMvc.perform(put("/alumno/{idAlumno}", id)
                        .contentType(MediaType.APPLICATION_JSON))
               /*.andExpectAll(
                       jsonPath("$.errorType").value("AlumnoNotFoundException"),
                       jsonPath("$.errorMessage").value("No se encontró un alumno con el id: 1")
               )*/
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void buscarAlumnoTest() throws Exception {
        int id = 1;

        when(alumnoService.buscarAlumno(anyInt())).thenReturn(new Alumno("nombre", "apellido", 12345678));

        mockMvc.perform(get("/alumno/{idAlumno}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    jsonPath("$.nombre").value("nombre"),
                    jsonPath("$.apellido").value("apellido"),
                    jsonPath("$.dni").value(12345678)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void buscarAlumnoBadRequestTest() throws Exception {
        int id = 1;
        when(alumnoService.buscarAlumno(id)).thenThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id));

        mockMvc.perform(get("/alumno/{idAlumno}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("AlumnoNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontró un alumno con el id: 1")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void cambiarEstadoAsignaturaTest() throws Exception {
        int id = 1;
        int idAsignatura = 1;

        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setMateriaId(1);
        asignaturaDto.setEstadoAsignatura(EstadoAsignatura.CURSADA);

        doNothing().when(alumnoService).cambiarEstadoAsignatura(id, idAsignatura, asignaturaDto);

        mockMvc.perform(put("/alumno/{idAlumno}/asignatura/{idAsignatura}", id, idAsignatura)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(asignaturaDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void canbiarEstadoAsignaturaBadRequestTest() throws Exception {
        int id = 1;
        int idAsignatura = 1;

        doThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id))
                .when(alumnoService).cambiarEstadoAsignatura(id, idAsignatura, new AsignaturaDto());

        mockMvc.perform(put("/alumno/{idAlumno}/asignatura/{idAsignatura}", id, idAsignatura))
                /*.andExpectAll(
                        jsonPath("$.errorType").value("AlumnoNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontró un alumno con el id: 1")
                )*/
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void getAllAsignaturasDeAlumnoTest() throws Exception {
        int id = 1;

        AlumnoAsignaturaDto alumnoAsignaturaDto = new AlumnoAsignaturaDto();
        alumnoAsignaturaDto.setId(1);
        alumnoAsignaturaDto.setNombre("nombre");
        alumnoAsignaturaDto.setApellido("apellido");
        alumnoAsignaturaDto.setDni(12345678);

        when(alumnoService.getAllAsignaturasDeAlumno(id)).thenReturn(alumnoAsignaturaDto);

        mockMvc.perform(get("/alumno/{idAlumno}/asignaturas", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoAsignaturaDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    jsonPath("$.id").value(1),
                    jsonPath("$.nombre").value("nombre"),
                    jsonPath("$.apellido").value("apellido"),
                    jsonPath("$.dni").value(12345678)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllAsignaturasDeAlumnoBadRequestTest() throws Exception {
        int id = 1;

        //copio el mensaje del AlumnoDao AlumnoNotFoundException para el control.
        when(alumnoService.getAllAsignaturasDeAlumno(id)).thenThrow(new AlumnoNotFoundException("No se encontró un alumno con el id: " + id));

        mockMvc.perform(get("/alumno/{idAlumno}/asignaturas", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("AlumnoNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontró un alumno con el id: 1")
                )
                .andExpect(status().is4xxClientError());
    }

}

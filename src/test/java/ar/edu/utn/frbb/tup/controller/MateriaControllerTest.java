package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;

import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class MateriaControllerTest {

    @InjectMocks
    MateriaController materiaController;

    @Mock
    MateriaService materiaService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(materiaController).setControllerAdvice(new UtnResponseEntityExceptionHandler()).build();
    }

    @Test
    public void crearMateriaTest() throws Exception {

        when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setNombre("Laboratorio II");
        materiaDto.setProfesorId(345);
        MvcResult result = mockMvc.perform(post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful())
                .andReturn();



        Assertions.assertEquals(new Materia(), mapper.readValue(result.getResponse().getContentAsString(), Materia.class));
    }

    @Test
    public void testCrearMateriaBadRequest() throws Exception {

        when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MvcResult result = mockMvc.perform(post("/materia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nombre\" : \"Laboratorio II\",\n" +
                                "    \"anio\" : \"segundo\", \n" +
                                "    \"cuatrimestre\" : 1,\n" +
                                "    \"profesorId\" : 2 \n"+
                                "}")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void modificarMateriaTest() throws Exception {
        doNothing().when(materiaService).modificarMateria(anyInt(), any(MateriaDto.class));

        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setNombre("Laboratorio II");
        materiaDto.setProfesorId(345);

        MvcResult result = mockMvc.perform(put("/materia/{idMateria}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(materiaService, times(1)).modificarMateria(1, materiaDto);
    }

    @Test
    public void modificarMateriaBadRequestTest() throws Exception {
        int id = 1;

        doThrow(new MateriaNotFoundException("No se encotro la materia con id: " + id))
                .when(materiaService).modificarMateria(anyInt(), any(MateriaDto.class));
        MvcResult result = mockMvc.perform(put("/materia/{idMateria}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nombre\" : \"Laboratorio II\",\n" +
                                "    \"anio\" : 1,\n" +
                                "    \"cuatrimestre\" : 1,\n" +
                                "    \"profesorId\" : 2 \n"+
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("MateriaNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encotro la materia con id: 1")
                )
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void eliminarMateriaTest() throws Exception {
        int id  = 1;

        doNothing().when(materiaService).eliminarMateria(id);

        MvcResult result = mockMvc.perform(delete("/materia/{idMateria}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(materiaService, times(1)).eliminarMateria(id);
    }

    @Test
    public void eliminarMateriaBadRequestTest() throws Exception {
        int idMateria = 1;

        doThrow(new MateriaNotFoundException("No se encotro la materia con id: " + idMateria)).when(materiaService).eliminarMateria(idMateria);

        MvcResult result = mockMvc.perform(put("/materia/{idMateria}",idMateria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                /*.andExpectAll(
                        jsonPath("$.errorType").value("MateriaNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encotro la materia con id: 1")
                )*/
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @Test
    public void getMateriaByIdTest() throws Exception {
        int id = 1;

        when(materiaService.getMateriaById(id)).thenReturn(new Materia());

        MvcResult result = mockMvc.perform(get("/materia/{idMateria}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(materiaService, times(1)).getMateriaById(id);
    }

    @Test
    public void getMateriaByIdBadRequestTest() throws Exception {
        int id = 1;

        when(materiaService.getMateriaById(id)).thenThrow(new MateriaNotFoundException("No se encontró la materia con id: " + id));

        mockMvc.perform(MockMvcRequestBuilders.get("/materia/{idMateria}",id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("MateriaNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontró la materia con id: 1")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getMateriaByNameTest() throws Exception {
        String nombre = "nombre";

        List<Materia> lista = new ArrayList<>();
        lista.add(new Materia());
        lista.add(new Materia());

        when(materiaService.getMateriaByName(nombre)).thenReturn(lista);

        MvcResult result = mockMvc.perform(get("/materia/materias").param("nombre", nombre)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(materiaService, times(1)).getMateriaByName(nombre);
    }

    @Test
    public void getMateriaByNameBadRequestTest() throws Exception {
        // Simular una excepción lanzada por el servicio

        doThrow(new MateriaNotFoundException("La materia invalido no existe")).when(materiaService).getMateriaByName(anyString());

        // Ejecutar la solicitud GET al endpoint del controlador
        mockMvc.perform(MockMvcRequestBuilders.get("/materia/materias?nombre=invalido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("MateriaNotFoundException"),
                        jsonPath("$.errorMessage").value("La materia invalido no existe")
                )
                .andExpect(status().is4xxClientError());

        // Verificar que el método del servicio se llamó una vez
        verify(materiaService, times(1)).getMateriaByName(anyString());
        verifyNoMoreInteractions(materiaService);
    }

    @Test
    public void getAllMateriasOrdenadas() throws Exception {
        List<Materia> materias = new ArrayList<>();

        Materia materia1 = new Materia("materia 1", 1, 1,new Profesor("profesor","Apellido", "titulo"));
        materia1.setMateriaId(1);
        materias.add(materia1);

        Materia materia2 = new Materia("materia 2", 1, 2,new Profesor("profesor","Apellido", "titulo"));
        materia2.setMateriaId(2);
        materias.add(materia2);

        when(materiaService.getAllMateriasOrdenadas("nombre_asc")).thenReturn(materias);

        mockMvc.perform(get("/materia/materias?orden=nombre_asc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materias)))
                .andExpect(status().is2xxSuccessful());
    }
}

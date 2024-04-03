package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class CarreraControllerTest {

    @InjectMocks
    CarreraController carreraController;

    @Mock
    CarreraService carreraService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(carreraController).setControllerAdvice(new UtnResponseEntityExceptionHandler()).build();
    }

    @Test
    public void crearCarreraTest() throws Exception {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Nombre");
        carreraDto.setDepartamento(1);
        carreraDto.setCantidadCuatrimestres(4);

        when(carreraService.crearCarrera(carreraDto)).thenReturn(new Carrera());

        mockMvc.perform(post("/carrera")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carreraDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllCarreraTest() throws Exception {
        Carrera carrera = new Carrera("carrera", 1, 1, 2);
        Map<Integer, Carrera> carreraMap = new HashMap<>();
        carreraMap.put(carrera.getCarreraId(), carrera);

        when(carreraService.getAll()).thenReturn(carreraMap);

        mockMvc.perform(get("/carrera/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void buscarCarreraPorIdTest() throws Exception {
        int id = 1;
        Carrera carrera = new Carrera("carrera", id, 1, 2);

        when(carreraService.buscarCarrera(id)).thenReturn(carrera);

        mockMvc.perform(get("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.nombre").value("carrera"),
                        jsonPath("$.carreraId").value(1),
                        jsonPath("$.departamento").value(1),
                        jsonPath("$.cantidadCuatrimestres").value(2)
                )
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void buscarCarreraPorIdBadRequestTest() throws Exception {
        int id = 1;

        when(carreraService.buscarCarrera(id)).thenThrow(new CarreraNotFoundException("No se encontro una carrera con id: " + id));

        mockMvc.perform(get("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("CarreraNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontro una carrera con id: 1")
                )
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void actualizarCarreraTest() throws Exception {
        int id = 1;

        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Nombre");
        carreraDto.setDepartamento(1);
        carreraDto.setCantidadCuatrimestres(4);

        doNothing().when(carreraService).actualizarCarrera(id, carreraDto);

        mockMvc.perform(put("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(carreraDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void actualizarCarreraBadRequestTest() throws Exception {
        int id = 1;

        doThrow(new CarreraNotFoundException("No se encontro una carrera con id: " + id)).when(carreraService).actualizarCarrera(id, new CarreraDto());

        mockMvc.perform(put("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON))
                /*
                .andExpectAll(
                        jsonPath("$.errorType").value("CarreraNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontro una carrera con id: 1")
                )*/
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void borrarCarreraTest() throws Exception {
        int id = 1;

        doNothing().when(carreraService).eliminarCarrera(id);

        mockMvc.perform(delete("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void borrarCarreraBadRequestTest() throws Exception {
        int id = 1 ;

        doThrow(new CarreraNotFoundException("No se encontro una carrera con id: " + id)).when(carreraService).eliminarCarrera(id);

        mockMvc.perform(delete("/carrera/{idCarrera}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.errorType").value("CarreraNotFoundException"),
                        jsonPath("$.errorMessage").value("No se encontro una carrera con id: 1")
                )
                .andExpect(status().is4xxClientError());

    }
}

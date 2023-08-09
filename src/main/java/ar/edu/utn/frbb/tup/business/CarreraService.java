package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;

import java.util.Map;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto carrera);
    Carrera buscarCarrera(int carreraId);
    Carrera actualizarCarrera(int id, CarreraDto carreraId);
    void eliminarCarrera(int id);

    Map<Integer, Carrera> getAll();
}

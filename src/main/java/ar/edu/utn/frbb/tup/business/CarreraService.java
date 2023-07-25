package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto carrera);
    Carrera buscarCarrera(int carreraId);
    Carrera actualizarCarrera(int carreraId);
}

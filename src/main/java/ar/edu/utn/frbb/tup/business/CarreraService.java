package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

import java.util.List;
import java.util.Map;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto carrera);

    Carrera buscarCarrera(int carreraId) throws CarreraNotFoundException;

    void actualizarCarrera(int id, CarreraDto carreraId) throws CarreraNotFoundException;

    void eliminarCarrera(int id) throws CarreraNotFoundException;

    Map<Integer, Carrera> getAll();

    void agregarMateriaEnCarrera(int carreraId, int materiaId) throws CarreraNotFoundException;
    void eliminarMateriaEnCarrera(int carreraId, int materiaId) throws CarreraNotFoundException;
    void actualizarMateriaEnCarrera(List<Integer> listaCarreraIds, int materiaId);
}
package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

import java.util.Map;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto carrera);

    Carrera buscarCarrera(int carreraId) throws CarreraNotFoundException;

    Carrera actualizarCarrera(int id, CarreraDto carreraId) throws CarreraNotFoundException;

    void eliminarCarrera(int id) throws CarreraNotFoundException;

    Map<Integer, Carrera> getAll();

    void agregarMateria(MateriaDto materia);
    void eliminarMateria(String materiaNombre);
    void actualizarMateria(MateriaDto materia);
}
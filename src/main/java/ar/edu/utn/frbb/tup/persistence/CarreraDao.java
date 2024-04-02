package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

import java.util.Map;

public interface CarreraDao {
    Carrera save(Carrera carrera);
    Carrera findById(int carreraId) throws CarreraNotFoundException;
    void update(Carrera carrera);
    void delete(int carreraId) throws CarreraNotFoundException;
    Map<Integer, Carrera> getAll();

}

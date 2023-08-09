package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;

import java.util.Map;

public interface CarreraDao {
    Carrera save(Carrera carrera);
    Carrera load(int carreraId);
    void actualizar(int id, Carrera carrera);
    void delete(int carreraId);

    Map<Integer, Carrera> getAll();
}

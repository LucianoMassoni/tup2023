package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

import java.util.Map;

public interface CarreraDao {
    Carrera save(Carrera carrera);
    Carrera load(int carreraId) throws CarreraNotFoundException;
    void actualizar(int id, Carrera carrera);
    void delete(int carreraId);

    Map<Integer, Carrera> getAll();

    void agregarMateria(MateriaDto materia);

    void eliminarMateria(String materiaNombre);

    void actualizarMateria(MateriaDto materia);
}

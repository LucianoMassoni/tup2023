package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.Map;

public interface MateriaDao {
    Materia save(Materia materia);

    Materia findById(int idMateria) throws MateriaNotFoundException;
    Map<Integer, Materia> getAllMaterias();

    void delete(int idMateria) throws MateriaNotFoundException;

    void modificar(Materia materia) throws MateriaNotFoundException;

}

package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.Map;

public interface MateriaDao {
    Materia save(Materia materia);

    Materia findById(int idMateria) throws MateriaNotFoundException;
    public Map<Integer, Materia> getAllMaterias();

    void delete(Materia materia);

    void modificar(Materia materia);

    Materia findByName(String nombreMateria) throws MateriaNotFoundException;
}

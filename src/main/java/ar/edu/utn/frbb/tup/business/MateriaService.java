package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.Map;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws IllegalArgumentException;

    Map<Integer, Materia> getAllMaterias();

    Materia getMateriaById(int idMateria) throws MateriaNotFoundException;

}

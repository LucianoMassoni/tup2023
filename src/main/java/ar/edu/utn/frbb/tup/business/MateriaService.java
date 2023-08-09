package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;
import java.util.Map;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws IllegalArgumentException;

    void eliminarMateria(int idMateria) throws MateriaNotFoundException;
    Map<Integer, Materia> getAllMaterias();

    Materia getMateriaById(int idMateria) throws MateriaNotFoundException;

    Materia  getMateriaByName(String nombreMateria) throws MateriaNotFoundException;

    void modificarMateria(int id, MateriaDto materiaDto) throws MateriaNotFoundException;

    List<Materia> getAllMateriasOrdenadas(String orden) throws MateriaNotFoundException;
}

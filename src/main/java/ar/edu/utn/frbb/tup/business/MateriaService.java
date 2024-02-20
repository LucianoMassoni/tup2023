package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws IllegalArgumentException, CarreraNotFoundException, MateriaNotFoundException;

    void eliminarMateria(int idMateria) throws MateriaNotFoundException, CarreraNotFoundException;

    Materia getMateriaById(int idMateria) throws MateriaNotFoundException;

    List<Materia>  getMateriaByName(String nombreMateria) throws MateriaNotFoundException;

    void modificarMateria(int id, MateriaDto materiaDto) throws MateriaNotFoundException, CarreraNotFoundException;

    List<Materia> getAllMateriasOrdenadas(String orden) throws MateriaNotFoundException;

}

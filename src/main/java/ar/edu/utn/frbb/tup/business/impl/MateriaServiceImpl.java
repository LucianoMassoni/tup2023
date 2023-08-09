package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao dao;
    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private CarreraService carreraService;

    //todo tendria que controlar si el profesor y la carrera existe.
    @Override
    public Materia crearMateria(MateriaDto materia) throws IllegalArgumentException, CarreraNotFoundException {
        Materia m = new Materia();
        m.setNombre(materia.getNombre());
        m.setAnio(materia.getAnio());
        m.setCuatrimestre(materia.getCuatrimestre());
        m.setProfesor(profesorService.buscarProfesor(materia.getProfesorId()));
        m.setCarrera(carreraService.buscarCarrera((int) materia.getCarreraId()));
        dao.save(m);
        carreraService.agregarMateria(m);
        /* te tira un error si el nombre de la materia tiene una "a"
        if (m.getNombre().contains("a")) {
            throw new IllegalArgumentException();
        }
         */

        return m;
    }

    @Override
    public void eliminarMateria(int idMateria) throws MateriaNotFoundException {
        Materia materia;
        materia = getMateriaById(idMateria);
        if (materia != null){
            dao.getAllMaterias().remove(idMateria);
            carreraService.eliminarMateria(idMateria);
        } else {
            throw new MateriaNotFoundException("La materia no se encontro");
        }
    }
    public void modificarMateria(int id, MateriaDto materiaDto) throws MateriaNotFoundException {
        Materia m;
        m = getMateriaById(id);
        if (m != null){
            m.setNombre(materiaDto.getNombre());
            m.setAnio(materiaDto.getAnio());
            m.setCuatrimestre(materiaDto.getCuatrimestre());
            m.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
            dao.modificar(m);
            carreraService.actualizarMateria(m);
        } else {
            throw new MateriaNotFoundException("la materia no existe");
        }
    }

    @Override
    public Map<Integer, Materia> getAllMaterias() {
        return dao.getAllMaterias();
    }

    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }

    public Materia getMateriaByName(String materiaNombre) throws MateriaNotFoundException {
        return dao.findByName(materiaNombre);
    }

    @Override
    public List<Materia> getAllMateriasOrdenadas(String orden) throws MateriaNotFoundException{
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());

        if (materias.isEmpty()){
            throw new MateriaNotFoundException("No se encontraron materias");
        }
        if (Objects.equals(orden, "nombre_asc")){
           materias.sort((m1, m2) -> m1.getNombre().toLowerCase().compareTo(m2.getNombre().toLowerCase()));
        } else if (orden.equals("nombre_desc")) {
            materias.sort((m1, m2) -> m2.getNombre().toLowerCase().compareTo(m1.getNombre().toLowerCase()));
        } else if (orden.equals("codigo_asc")){
            materias.sort((m1, m2) -> Integer.compare(m1.getMateriaId(), m2.getMateriaId()));
        } else if (orden.equals("codigo_desc")) {
            materias.sort((m1, m2) -> Integer.compare(m2.getMateriaId(), m1.getMateriaId()));
        } else {
            return null;
        }
        return materias;
    }
}

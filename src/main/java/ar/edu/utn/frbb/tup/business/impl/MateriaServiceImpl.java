package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
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

    @Override
    public Materia crearMateria(MateriaDto materia) throws IllegalArgumentException{
        Materia m = new Materia();
        m.setNombre(materia.getNombre());
        m.setAnio(materia.getAnio());
        m.setCuatrimestre(materia.getCuatrimestre());
        m.setProfesor(profesorService.buscarProfesor(materia.getProfesorId()));
        dao.save(m);
        /* te tira un error si el nombre de la materia tiene una a

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

    //todo esta no esta devolviendo nombres ordenados
    @Override
    public List<Materia> getAllMateriasOrdenadas(String orden){
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());

        if (Objects.equals(orden, "nombre_asc")){
            materias.sort(new Comparator<Materia>() {
                @Override
                public int compare(Materia m1, Materia m2) {
                    return m1.getNombre().compareTo(m2.getNombre());
                }
            });
            return materias;
        } else if (orden.equals("nombre_desc")) {
            materias.sort(new Comparator<Materia>() {
                @Override
                public int compare(Materia m1, Materia m2) {
                    return m2.getNombre().compareTo(m1.getNombre());
                }
            });
            return materias;
        } else if (orden.equals("codigo_asc")){
            materias.sort(new Comparator<Materia>() {
                @Override
                public int compare(Materia m1, Materia m2) {
                    return Integer.compare(m1.getMateriaId(), m2.getMateriaId());
                }
            });

            return materias;
        } else if (orden.equals("codigo_desc")) {
            materias.sort(new Comparator<Materia>() {
                @Override
                public int compare(Materia m1, Materia m2) {
                    return Integer.compare(m2.getMateriaId(), m1.getMateriaId());
                }
            });

            return materias;
        }
        return null;
    }
}

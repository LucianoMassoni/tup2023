package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        if (m.getNombre().contains("a")) {
            throw new IllegalArgumentException();
        }
        return m;
    }

    @Override
    public Map<Integer, Materia> getAllMaterias() {
        return dao.getAllMaterias();
    }

    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }
}

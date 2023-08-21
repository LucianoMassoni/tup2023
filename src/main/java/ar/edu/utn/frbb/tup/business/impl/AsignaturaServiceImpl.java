package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class AsignaturaServiceImpl implements AsignaturaService {
    @Autowired
    private AsignaturaDao asignaturaDao;
    @Autowired
    private MateriaService materiaService;

    @Override
    public Asignatura crearAsignatura(long materiaId) throws MateriaNotFoundException {
        Materia materia = materiaService.getMateriaById((int)materiaId);
        Asignatura a = new Asignatura(materia);
        asignaturaDao.save(a);
        return a;
    }

    public void eliminarAsignatura(long asignaturaId) throws AsignaturaNotFoundException {
        asignaturaDao.delete(asignaturaId);
    }
    @Override
    public Asignatura getAsignatura(int materiaId, long dni) {
        return null;
    }

    @Override
    public List<Asignatura> getAllAsignaturas() {
        return asignaturaDao.getAllAsignaturas();
    }

    @Override
    public void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException {
        asignaturaDao.actualizar(a);
    }


}

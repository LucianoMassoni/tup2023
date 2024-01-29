package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Component
public class AsignaturaServiceImpl implements AsignaturaService {
    @Autowired
    private AsignaturaDao asignaturaDao;
    @Autowired
    private MateriaService materiaService;

    @Override
    public Asignatura crearAsignatura(int materiaId) throws MateriaNotFoundException {
        Materia materia = materiaService.getMateriaById(materiaId);
        Asignatura a = new Asignatura(materia);
        asignaturaDao.save(a);
        return a;
    }

    public void eliminarAsignatura(int asignaturaId) throws AsignaturaNotFoundException {
        asignaturaDao.delete(asignaturaId);
    }

    @Override
    public Asignatura getAsignatura(int idAsignatura) throws AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturas = asignaturaDao.getAllAsignaturas().stream().toList();
        for (Asignatura asignatura:listaAsignaturas){
            if (Objects.equals(idAsignatura, asignatura.getId())){
                return asignatura;
            }
        }
        throw new AsignaturaNotFoundException("No se encontro una asignatura");
    }

    //Todo podria hacer un getAsignaturaByMateriaIdYAlumnoId??? deberia de tener dos service de materia y alumno?
    // de alumno no creo porque en alumno service ya tengo asignatura service.

    public List<Materia> getListaMateriasCorrelativasByIdAsignaturaYIdMateria(int idAsignatura, int idMateria) throws MateriaNotFoundException, AsignaturaNotFoundException {
        List<Materia> materiasCorrelativas = new ArrayList<>();
        Asignatura asignatura = getAsignatura(idAsignatura);
        for (MateriaInfoDto m:asignatura.getMateria().getCorrelatividades()){
            if (Objects.equals(m.getId(), idMateria)){
                materiasCorrelativas.add(materiaService.getMateriaById(idMateria));
            }
        }
        return materiasCorrelativas;
    }

    public List<Asignatura> getListaAsignaturasDeMateriasCorrelativas(int idAsignatura, int idMateria) throws MateriaNotFoundException, AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturasCorrelativas = new ArrayList<>();
        for (Asignatura asignatura:asignaturaDao.getAllAsignaturas()){
            for (Materia materia:getListaMateriasCorrelativasByIdAsignaturaYIdMateria(idAsignatura, idMateria)){
                if (Objects.equals(materia.getMateriaId(), asignatura.getMateria().getMateriaId())){
                    listaAsignaturasCorrelativas.add(asignatura);
                }
            }
        }
        return listaAsignaturasCorrelativas;
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

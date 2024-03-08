package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        Random r = new Random();
        asignaturaDao.save(a);
        return a;
    }

    public void eliminarAsignatura(int asignaturaId) throws AsignaturaNotFoundException {
        asignaturaDao.delete(asignaturaId);
    }

    @Override
    public Asignatura getAsignatura(int idAsignatura) throws AsignaturaNotFoundException {
        return asignaturaDao.findById(idAsignatura);
    }

    @Override
    public List<Asignatura> getAllAsignaturas() {
        return asignaturaDao.getAllAsignaturas().values().stream().toList();
    }

    @Override
    public void actualizarAsignatura(Asignatura a) throws AsignaturaNotFoundException {
        asignaturaDao.actualizar(a);
    }

    //----------------------------------------------------------------------------------------------------------------

    private List<Asignatura> getListaDeAsignaturasCorrelativas(Asignatura asignatura, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturasDelAlumno = new ArrayList<>();
        List<Asignatura> listaAsignaturasCorrelativas = new ArrayList<>();
        ;
        //Lleno la lista de asignaturas del alumno
        for (int asignaturaId : listaIdsAsignaturasDelAlumno) {
            listaAsignaturasDelAlumno.add(asignaturaDao.findById(asignaturaId));
        }

        // Recorro la lista de materiasCorrelativas de la asignatura en cuestion y me fijo si coincide el id con la materia de las
        // asignaturas del alumno. Lo pongo en listaAsignaturaCorrelativa.
        for (MateriaInfoDto materiaCorrelativa : asignatura.getMateria().getCorrelatividades()) {
            for (Asignatura asignaturaDeAlumno : listaAsignaturasDelAlumno) {
                if (asignaturaDeAlumno.getMateria().getMateriaId() == materiaCorrelativa.getId()) {
                    listaAsignaturasCorrelativas.add(asignaturaDeAlumno);
                }
            }
        }

        return listaAsignaturasCorrelativas;
    }

    private List<Asignatura> getListaDeAsignaturasALaQueEsCorrelativa(Asignatura asignatura, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException {
        List<Asignatura> listaAsignaturasDelAlumno = new ArrayList<>();
        List<Asignatura> listaAsignaturasCorrelativas = new ArrayList<>();

        //Lleno la lista de asignaturas del alumno
        for (int asignaturaId : listaIdsAsignaturasDelAlumno) {
            listaAsignaturasDelAlumno.add(asignaturaDao.findById(asignaturaId));
        }

        // Recorro la lista de materiasCorrelativas de la asignatura en cuestion y me fijo si coincide el id con la materia de las
        // asignaturas del alumno. Lo pongo en listaAsignaturaCorrelativas

        for (Asignatura asignaturaCorrelativa : listaAsignaturasDelAlumno) {
            for (MateriaInfoDto materiaInfoDto : asignaturaCorrelativa.getMateria().getCorrelatividades()) {
                if (materiaInfoDto.getId() == asignatura.getId()) {
                    listaAsignaturasCorrelativas.add(asignaturaCorrelativa);
                }
            }
        }

        return listaAsignaturasCorrelativas;
    }

    @Override
    public void verificarCorrelativasEstenAprobadas(int asignaturaId, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        Asignatura asignatura = asignaturaDao.findById(asignaturaId);
        List<Asignatura> listaAsignaturasCorrelativas = new ArrayList<>(getListaDeAsignaturasCorrelativas(asignatura, listaIdsAsignaturasDelAlumno));

        for (Asignatura asignaturaCorrelativa : listaAsignaturasCorrelativas) {
            if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                throw new EstadoIncorrectoException("La asignatura " + asignaturaCorrelativa.getNombreAsignatura() +
                        " con id: " + asignaturaCorrelativa.getId() + "No esta aprobada");
            }
        }
    }

    @Override
    public void verificarCorrelativasEstenCursadas(int asignaturaId, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        Asignatura asignatura = asignaturaDao.findById(asignaturaId);
        List<Asignatura> listaAsignaturasCorrelativas = new ArrayList<>(getListaDeAsignaturasCorrelativas(asignatura, listaIdsAsignaturasDelAlumno));

        for (Asignatura asignaturaCorrelativa : listaAsignaturasCorrelativas) {
            if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.NO_CURSADA)) {
                throw new EstadoIncorrectoException("La asignatura " + asignaturaCorrelativa.getNombreAsignatura() +
                        " con id: " + asignaturaCorrelativa.getId() + "No esta cursada");
            }
        }
    }

    @Override
    public void verificarAsignaturasParaPerderCursada(int asignaturaId, List<Integer> listaIdsAsignaturasDelAlumno) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        Asignatura asignatura = asignaturaDao.findById(asignaturaId);
        List<Asignatura> listaAsignaturasDeLasQueEsCorrelativa = new ArrayList<>(getListaDeAsignaturasALaQueEsCorrelativa(asignatura, listaIdsAsignaturasDelAlumno));

        if (asignatura.getEstado().equals(EstadoAsignatura.APROBADA))
            throw new EstadoIncorrectoException("No se puede perder la cursada si la asignatura está aprobada");

        for (Asignatura asignaturaCorrelativa : listaAsignaturasDeLasQueEsCorrelativa){
            if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA)){
                throw new EstadoIncorrectoException("No se puede perder la cursada si la que tiene como correlativa a esta está cursada!");
            }
        }
    }

    @Override
    public void verificarNotaCorrecta(int nota) {
        if (nota < 0)
            throw new IllegalArgumentException("La nota no puede ser un numero negativo");
        if (nota < 6)
            throw new IllegalArgumentException("La nota debe ser mayor o igual a 6 para aprobar");
        if (nota > 10)
            throw new IllegalArgumentException("La nota debe ser menor o igual a 10 para aprobar");
    }
}

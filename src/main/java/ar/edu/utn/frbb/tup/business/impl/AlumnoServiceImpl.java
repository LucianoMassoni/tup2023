package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Component
public class AlumnoServiceImpl implements AlumnoService {
    @Autowired
    private AlumnoDao alumnoDao;
    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public Alumno crearAlumno(AlumnoDto alumno) throws MateriaNotFoundException {
        Alumno a = new Alumno();
        a.setNombre(alumno.getNombre());
        a.setApellido(alumno.getApellido());
        a.setDni(alumno.getDni());
        Random random = new Random();
        a.setId(random.nextInt());
        if (alumno.getIdMateriasDeAsignatura() != null){
            for (Integer idMateria:alumno.getIdMateriasDeAsignatura()){
                a.addAsignatura(asignaturaService.crearAsignatura(idMateria));
            }
        }

        alumnoDao.saveAlumno(a);
        return a;
    }

    @Override
    public Alumno buscarAlumno(int id) throws AlumnoNotFoundException {
        return alumnoDao.findAlumno(id);
    }
    @Override
    public void eliminarAlumno(int alumnoId) throws AsignaturaNotFoundException {
        Alumno alumno = alumnoDao.loadAlumno(alumnoId);
        if (alumno.getAsignaturas() != null){
            for (Asignatura a: alumno.getAsignaturas()){
                asignaturaService.eliminarAsignatura(a.getId());
            }
        }
        alumnoDao.deleteAlumno(alumnoId);
    }


    //ToDo no actualiza los datos de las asignaturas
    // NO ANDA
    public Alumno actualizarAlumno(AlumnoDto alumnoDto) throws MateriaNotFoundException, AsignaturaNotFoundException, AlumnoNotFoundException {
        Alumno alumno = alumnoDao.loadAlumno(alumnoDto.getDni());
        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());

        if (alumnoDto.getIdMateriasDeAsignatura() != null) {
            List<Asignatura> nuevasAsignaturas = new ArrayList<>();

            for (Integer idMateria : alumnoDto.getIdMateriasDeAsignatura()) {
                // Si la asignatura no está en la lista actual del alumno, la creamos y la añadimos
                if (alumno.getAsignaturas().stream().noneMatch(asignatura -> Objects.equals(asignatura.getMateria().getMateriaId(), idMateria))) {
                    nuevasAsignaturas.add(asignaturaService.crearAsignatura(idMateria));
                }
            }
            alumno.getAsignaturas().addAll(nuevasAsignaturas);
        }


        List<Asignatura> asignaturasAEliminar = new ArrayList<>();

        for (Asignatura asignatura:alumno.getAsignaturas()){
            if (!(alumnoDto.getIdMateriasDeAsignatura().contains(asignatura.getMateria().getMateriaId()))){
                asignaturasAEliminar.add(asignatura);
            }
        }
        alumno.getAsignaturas().removeAll(asignaturasAEliminar);

        for (Asignatura asignatura : asignaturasAEliminar) {
            asignaturaService.eliminarAsignatura(asignatura.getId());
        }

        alumno = alumnoDao.actualizarAlumno(alumno);

        return alumno;
    }
    @Override
    public void aprobarAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AsignaturaNotFoundException {
        Asignatura a = asignaturaService.getAsignatura(idAsignatura);
        for (MateriaInfoDto m: a.getMateria().getCorrelatividades()) {
            Asignatura correlativa = asignaturaService.getAsignatura(idAsignatura);
            if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " debe estar aprobada para aprobar " + a.getNombreAsignatura());
            }
        }
        a.aprobarAsignatura(asignaturaDto.getNota());
        asignaturaService.actualizarAsignatura(a);
        Alumno alumno = alumnoDao.loadAlumno(idAlumno);
        alumno.actualizarAsignatura(a);
        alumnoDao.saveAlumno(alumno);
    }


    // Todo como le cambie la lista de correlativas a materia por materiaDtoInfo debo encontrar la forma obtener la asignatura con el id de la materia.
    @Override
    public Alumno aprobar(Alumno alumno, Asignatura asignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException, EstadoIncorrectoException {
        //Chequea si la asignatura que paso esta cursada para poder aprobar.
        if (asignatura.getEstado().equals(EstadoAsignatura.APROBADA)){
            throw new EstadoIncorrectoException("La asignatura " + asignatura.getNombreAsignatura() + " ya esta aprobada");
        } else if (asignatura.getEstado().equals(EstadoAsignatura.NO_CURSADA)){
            throw new EstadoIncorrectoException("La asignatura " + asignatura.getNombreAsignatura() + " debe estar cursada para poder aprobar");
        }

        //En caso de que esté cursada se toma la lista de asignaturas de materias correlativas para ver el estado de estas.
        if (asignaturaService.getListaAsignaturasDeMateriasCorrelativas(asignatura.getId(), asignatura.getMateria().getMateriaId()) == null){
            asignatura.aprobarAsignatura(asignaturaDto.getNota());
        } else {
            for (Asignatura asignaturaCorrelativa: asignaturaService.getListaAsignaturasDeMateriasCorrelativas(asignatura.getId(), asignatura.getMateria().getMateriaId())){
                if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.NO_CURSADA)){
                    throw new EstadoIncorrectoException("No cursó la asignatura: " + asignaturaCorrelativa.getNombreAsignatura());
                } else if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA)) {
                    throw new EstadoIncorrectoException("La asignatura " + asignaturaCorrelativa.getNombreAsignatura() + " no está aprobada");
                }
            }
        }
        asignatura.setEstado(asignaturaDto.getEstadoAsignatura());
        asignatura.setNota(asignaturaDto.getNota());
        asignaturaService.actualizarAsignatura(asignatura);
        //Aca deberia ver si debo actualizar alumno

        return alumno;
    }
    /*
    public void cursarAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto){

    }

     */

    public Alumno cambiarEstadoAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, MateriaNotFoundException, EstadoIncorrectoException {
        Alumno alumno = alumnoDao.findAlumno(idAlumno);
        Asignatura asignatura = asignaturaService.getAsignatura(idAsignatura);
        if (asignaturaDto.getEstadoAsignatura().equals(EstadoAsignatura.CURSADA)){
            return alumnoDao.findAlumno(idAlumno);
        } else if (asignaturaDto.getEstadoAsignatura().equals(EstadoAsignatura.APROBADA)){
            return aprobar(alumno, asignatura, asignaturaDto);
        }
        throw new EstadoIncorrectoException("si llegas a leer esto es de cambiarEstadoAsignatura");
    }
}

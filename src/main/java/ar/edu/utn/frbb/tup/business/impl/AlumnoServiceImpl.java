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
        a.setId(random.nextLong());
        if (alumno.getIdMateriasDeAsignatura() != null){
            for (Integer idMateria:alumno.getIdMateriasDeAsignatura()){
                a.addAsignatura(asignaturaService.crearAsignatura(idMateria));
            }
        }

        alumnoDao.saveAlumno(a);
        return a;
    }

    @Override
    public Alumno buscarAlumno(Long id) {
        return alumnoDao.findAlumno(id);
    }
    @Override
    public void eliminarAlumno(Long alumnoId) throws AsignaturaNotFoundException {
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
    public Alumno actualizarAlumno(AlumnoDto alumnoDto) throws MateriaNotFoundException, AsignaturaNotFoundException {
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
    public void aprobarAsignatura(int materiaId, int nota, long dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AsignaturaNotFoundException {
        Asignatura a = asignaturaService.getAsignatura(materiaId, dni);
        for (MateriaInfoDto m: a.getMateria().getCorrelatividades()) {
            Asignatura correlativa = asignaturaService.getAsignatura(m.getId(), dni);
            if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " debe estar aprobada para aprobar " + a.getNombreAsignatura());
            }
        }
        a.aprobarAsignatura(nota);
        asignaturaService.actualizarAsignatura(a);
        Alumno alumno = alumnoDao.loadAlumno(dni);
        alumno.actualizarAsignatura(a);
        alumnoDao.saveAlumno(alumno);
    }

    public Alumno cambiarEstadoAsignatura(long idAlumno, long idAsignatura, AsignaturaDto asignaturaDto){
        Alumno alumno = alumnoDao.loadAlumno(idAlumno);
        Asignatura asignatura = asignaturaService.getAsignatura(idAsignatura);
        if (asignaturaDto.getEstadoAsignatura().equals(EstadoAsignatura.CURSADA)){
            return cursarAsignatura(idAlumno, idAsignatura, asignaturaDto);
        } else if (asignaturaDto.getEstadoAsignatura().equals(EstadoAsignatura.APROBADA)){
            return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto);
        }

    }
}

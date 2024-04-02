package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoAsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {
    @Autowired
    private AlumnoDao alumnoDao;
    @Autowired
    private AsignaturaService asignaturaService;
    @Autowired
    private MateriaService materiaService;


    @Override
    public Alumno crearAlumno(AlumnoDto alumnoDto) throws MateriaNotFoundException {
        List<Asignatura> listaDeAsignaturas = new ArrayList<>();

        validarExistenciaDeMateriasIds(alumnoDto.getIdMateriasDeAsignatura());
        validarCorrectoDni(alumnoDto.getDni());
        validarUnicoDni(alumnoDto.getDni());

        Alumno alumno = new Alumno();
        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());
        if (alumnoDto.getIdMateriasDeAsignatura() != null){
            for (Integer idMateria:alumnoDto.getIdMateriasDeAsignatura()){
                listaDeAsignaturas.add(asignaturaService.crearAsignatura(idMateria));
            }
        }
        for (Asignatura asignatura:listaDeAsignaturas){
            alumno.getAsignaturasIds().add(asignatura.getId());
        }

        alumnoDao.save(alumno);
        return alumno;
    }

    @Override
    public Alumno buscarAlumno(int id) throws AlumnoNotFoundException {
        return alumnoDao.findById(id);
    }
    @Override
    public void eliminarAlumno(int alumnoId) throws AlumnoNotFoundException {
        alumnoDao.delete(alumnoId);
    }

    @Override
    public void actualizarAlumno(int alumnoId, AlumnoDto alumnoDto) throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        validarCorrectoDni(alumnoDto.getDni());
        validarUnicoDniEnActualizar(alumnoId, alumnoDto.getDni());
        validarExistenciaDeMateriasIds(alumnoDto.getIdMateriasDeAsignatura());

        Alumno alumno = alumnoDao.findById(alumnoId);
        alumno.setNombre(alumno.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());
        setAsignaturasEnActualizarAlumno(alumno, alumnoDto);

        alumnoDao.update(alumno);
    }

    @Override
    public AlumnoAsignaturaDto getAllAsignaturasDeAlumno(int alumnoId) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = alumnoDao.findById(alumnoId);
        List<Asignatura> listaAsignatuas = new ArrayList<>();
        for (int asignaturaId : alumno.getAsignaturasIds()){
            listaAsignatuas.add(asignaturaService.getAsignatura(asignaturaId));
        }
        AlumnoAsignaturaDto alumnoAsignaturaDto = new AlumnoAsignaturaDto();
        alumnoAsignaturaDto.setId(alumno.getId());
        alumnoAsignaturaDto.setNombre(alumno.getNombre());
        alumnoAsignaturaDto.setApellido(alumno.getApellido());
        alumnoAsignaturaDto.setDni(alumno.getDni());
        alumnoAsignaturaDto.setAsignaturas(listaAsignatuas);

        return alumnoAsignaturaDto;
    }

    /*
        ----------------------------------- Funciones para crear y actualizar ----------------------------------------
     */
    private void setAsignaturasEnActualizarAlumno(Alumno alumno, AlumnoDto alumnoDto) throws MateriaNotFoundException, AsignaturaNotFoundException {
        if (alumnoDto.getIdMateriasDeAsignatura().isEmpty()){
            alumno.setAsignaturasIds(new ArrayList<>());
            return;
        }
        List<Asignatura> nuevasAsignaturas = new ArrayList<>();
        List<Integer> idMateriasNuevasAsignaturas = new ArrayList<>();
        List<Integer> idsAsignaturas = new ArrayList<>();

        //agrego las que coinciden
        for (int asignaturaId : alumno.getAsignaturasIds()){
            Asignatura asignatura = asignaturaService.getAsignatura(asignaturaId);
            if (alumnoDto.getIdMateriasDeAsignatura().contains(asignatura.getMateria().getMateriaId())){
                nuevasAsignaturas.add(asignatura);
                idMateriasNuevasAsignaturas.add(asignatura.getMateria().getMateriaId());
            }
        }

        //agrego las de alumnoDto que no coinciden
        for (int materiaId : alumnoDto.getIdMateriasDeAsignatura()){
            if (!idMateriasNuevasAsignaturas.contains(materiaId)){
                nuevasAsignaturas.add(asignaturaService.crearAsignatura(materiaId));
            }
        }

        for (Asignatura asignatura : nuevasAsignaturas){
            idsAsignaturas.add(asignatura.getId());
        }
        //Reemplazo las asignaturas.
        alumno.setAsignaturasIds(idsAsignaturas);
    }


    private void validarExistenciaDeMateriasIds(List<Integer> materiasIds) throws MateriaNotFoundException {
        for (int materiaId:materiasIds){
            materiaService.getMateriaById(materiaId);
        }
    }

    private void validarCorrectoDni(int dni){
        if (dni < 0)
            throw new IllegalArgumentException("El dni no puede ser negativo");
        if (dni < 10000000 || dni > 99999999)
            throw new IllegalArgumentException("El dni debe tener 8 dígitos");
    }

    private void validarUnicoDni(int dni){
        for (Alumno alumno:alumnoDao.getAllAlunno().values()){
            if (alumno.getDni()==dni){
                throw new IllegalArgumentException("Ya existe un alumno con DNI: " + dni);
            }
        }
    }

    private void validarUnicoDniEnActualizar(int alumnoId, int dni){
        for (Alumno alumno:alumnoDao.getAllAlunno().values()){
            if (alumno.getDni() == dni && alumno.getId() != alumnoId){
                throw new IllegalArgumentException("Ya existe un alumno con DNI: " + dni);
            }
        }
    }

    /*
        ----------------------------------- Funciones de Asignatura desde Alumno ------------------------------------
     */

    @Override
    public void cambiarEstadoAsignatura(int alumnoId, int asignaturaId, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException,
            AsignaturaNotFoundException, EstadoIncorrectoException
    {
        Alumno alumno = alumnoDao.findById(alumnoId);

        verificarExistenciaAsignatura(asignaturaId);
        verificarAsignaturaCorrespondeAAlumno(asignaturaId, alumno);

        switch (asignaturaDto.getEstadoAsignatura()) {
            case APROBADA -> aprobarAsignatura(alumno, asignaturaId, asignaturaDto);
            case CURSADA -> cursarAsignatura(alumno, asignaturaId);
            case NO_CURSADA -> perderCursadaAsignatura(alumno, asignaturaId);
        }
    }


    private void aprobarAsignatura(Alumno alumno, int asignaturaId, AsignaturaDto asignaturaDto) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        asignaturaService.verificarAsignaturaEstaCursada(asignaturaId);
        asignaturaService.verificarCorrelativasEstenAprobadas(asignaturaId, alumno.getAsignaturasIds());

        asignaturaService.verificarNotaCorrecta(asignaturaDto.getNota());

        Asignatura asignatura = asignaturaService.getAsignatura(asignaturaId);
        asignatura.setEstado(asignaturaDto.getEstadoAsignatura());
        asignatura.setNota(asignaturaDto.getNota());

        asignaturaService.actualizarAsignatura(asignatura);
    }

    private void cursarAsignatura(Alumno alumno, int asignaturaId) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        asignaturaService.verificarAsignaturaEstaNoCursada(asignaturaId);
        asignaturaService.verificarCorrelativasEstenCursadas(asignaturaId, alumno.getAsignaturasIds());

        Asignatura asignatura = asignaturaService.getAsignatura(asignaturaId);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        asignaturaService.actualizarAsignatura(asignatura);
    }

    private void perderCursadaAsignatura(Alumno alumno, int asignaturaId) throws AsignaturaNotFoundException, EstadoIncorrectoException {
        asignaturaService.verificarAsignaturasParaPerderCursada(asignaturaId, alumno.getAsignaturasIds());

        Asignatura asignatura = asignaturaService.getAsignatura(asignaturaId);
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        asignaturaService.actualizarAsignatura(asignatura);
    }

    private void verificarExistenciaAsignatura(int asignaturaId) throws AsignaturaNotFoundException {
        asignaturaService.getAsignatura(asignaturaId);
    }

    private void verificarAsignaturaCorrespondeAAlumno(int asignaturaId, Alumno alumno) throws AsignaturaNotFoundException {
        if (!alumno.getAsignaturasIds().contains(asignaturaId)){
            throw new AsignaturaNotFoundException("El alumno no tiene una asignatura con id: " + asignaturaId);
        }
    }

}

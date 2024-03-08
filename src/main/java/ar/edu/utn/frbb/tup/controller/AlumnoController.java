package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Objects;

@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AsignaturaService asignaturaService;

    @PostMapping()
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) throws MateriaNotFoundException {
        return alumnoService.crearAlumno(alumnoDto);
    }

    @PutMapping("/{idAlumno}")
    public void modificarAlumno(@PathVariable int idAlumno, @RequestBody AlumnoDto alumnoDto) throws AlumnoNotFoundException, MateriaNotFoundException, AsignaturaNotFoundException {
        alumnoService.actualizarAlumno(idAlumno, alumnoDto);
    }

    @GetMapping("/{idAlumno}")
    public Alumno buscarAlumno(@PathVariable int idAlumno) throws AlumnoNotFoundException {
       return alumnoService.buscarAlumno(idAlumno);
    }

    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public void cambiarEstadoAsignatura(@PathVariable int idAlumno, @PathVariable int idAsignatura, @RequestBody AsignaturaDto asignaturaDto) throws AsignaturaNotFoundException, AlumnoNotFoundException, EstadoIncorrectoException {
         alumnoService.cambiarEstadoAsignatura(idAlumno, idAsignatura, asignaturaDto);
    }
    //Todo: Creo que no hay forma de ver si se cambió el estado de la asignatura
    // agregar un get "/{idAlumno}/asignatura/{idAsignatura}". o algo por el estilo???

}

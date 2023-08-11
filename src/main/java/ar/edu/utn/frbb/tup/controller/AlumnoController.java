package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
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

    @PostMapping("/")
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        return alumnoService.crearAlumno(alumnoDto);
    }

    @PutMapping("/{idAlumno}")
    public Alumno modificarAlumno(@RequestBody AlumnoDto alumnoDto){
        return alumnoService.actualizarAlumno(alumnoDto);
    }
    @GetMapping("/{idAlumno}")
    public Alumno buscarAlumno(@PathVariable Long idAlumno) {
       return alumnoService.buscarAlumno(idAlumno);
    }

    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public void cambiarEstadoAsignatura(@PathVariable long idAlumno, @PathVariable long idAsignatura, @RequestBody Asignatura a){
        Alumno alumno = alumnoService.buscarAlumno(idAlumno);
        if (!Objects.equals(a.getId(), idAsignatura)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Error. No se econtró una asignatura con id: " + idAsignatura);
        } else if (alumno == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Error. No se econtró un alumno con id: " + idAlumno);
        } else {
            asignaturaService.actualizarAsignatura(a);
        }
    }

}

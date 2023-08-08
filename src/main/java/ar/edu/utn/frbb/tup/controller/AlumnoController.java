package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @PostMapping("/")
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        return alumnoService.crearAlumno(alumnoDto);
    }

    @PutMapping("/{idAlumno}")
    public Alumno modificarAlumno(@RequestBody AlumnoDto alumnoDto){
        return alumnoService.actualizarAlumno(alumnoDto);
    }
    @GetMapping("/buscar")
    public Alumno buscarAlumno(@RequestParam String apellido) {

       return alumnoService.buscarAlumno(apellido);

    }

    @PostMapping
    public Alumno actualizarAlumno(@RequestParam long dni){
        return null;
    }
    //Funcion de prueba
    @GetMapping
    public Alumno cargarAlumno(@RequestParam long dni){
        return alumnoService.cargarAlumno(dni);
    }



}

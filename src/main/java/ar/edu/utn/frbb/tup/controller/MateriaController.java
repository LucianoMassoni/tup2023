package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto) throws CarreraNotFoundException, MateriaNotFoundException {
        return materiaService.crearMateria(materiaDto);
    }

    @PutMapping("/{idMateria}")
    public void modificarMateria(@PathVariable int idMateria, @RequestBody MateriaDto materia) throws MateriaNotFoundException, CarreraNotFoundException {
        materiaService.modificarMateria(idMateria, materia);
    }

    @DeleteMapping("/{idMateria}")
    public void eliminarMateria(@PathVariable int idMateria) throws MateriaNotFoundException {
        materiaService.eliminarMateria(idMateria);
    }

    @GetMapping("/{idMateria}")
    public Materia getMateriaById(@PathVariable Integer idMateria) throws MateriaNotFoundException {
        return materiaService.getMateriaById(idMateria);
    }

    @GetMapping()
    public Materia getMateriaByName(@RequestParam(name = "nombre") String unNombre) throws MateriaNotFoundException{
        return materiaService.getMateriaByName(unNombre);
    }

    //Esta anda pero la ruta debe ser materia/materias?orden={orden}
    @GetMapping("/materias")
    public List<Materia> getAllMateriasOrdenadas(@RequestParam(name = "orden") String orden) throws MateriaNotFoundException, ResponseStatusException {
        return materiaService.getAllMateriasOrdenadas(orden);
    }
}

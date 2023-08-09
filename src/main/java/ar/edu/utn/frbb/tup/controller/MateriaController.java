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

   /* @GetMapping
    public List<Materia> getMaterias() {
        Materia m = new Materia("labo 1", 2, 1, new Profesor("Lucho", "Salotto", "Lic"));
        Materia m1 = new Materia("labo 2", 2, 1, new Profesor("Juan", "Perez", "Lic"));

        return Arrays.asList(m1, m);
    }
*/
    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto) throws CarreraNotFoundException {
        return materiaService.crearMateria(materiaDto);
    }

    @PutMapping("/{idMateria}")
    public void modificarMateria(@PathVariable int idMateria, @RequestBody MateriaDto materia) throws MateriaNotFoundException {
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
    @GetMapping("/getAll")
    public Map<Integer, Materia> getAllMaterias(){
        return materiaService.getAllMaterias();
    }

    @GetMapping()
    public Materia getMateriaByName(@RequestParam(name = "nombre") String unNombre) throws MateriaNotFoundException{
        return materiaService.getMateriaByName(unNombre);
    }

    //Esta anda pero la ruta debe ser materia/materias?orden={orden}
    @GetMapping("/materias")
    public List<Materia> getAllMateriasOrdenadas(@RequestParam(name = "order") String order) throws MateriaNotFoundException, ResponseStatusException {
        if (materiaService.getAllMateriasOrdenadas(order) == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "ut");
        } else {
            return materiaService.getAllMateriasOrdenadas(order);
        }
    }
}
